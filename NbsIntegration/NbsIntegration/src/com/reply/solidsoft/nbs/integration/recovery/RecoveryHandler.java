/**
 * -----------------------------------------------------------------------------
 * File=RecoveryHandler.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Handles recovery using a store and forward mechanism.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery;

import com.reply.solidsoft.nbs.integration.data.TransactionManager;
import com.reply.solidsoft.nbs.integration.model.HttpStatusCode;
import com.reply.solidsoft.nbs.integration.model.HttpVerb;
import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.reply.solidsoft.nbs.integration.model.PackStateTransitionType;
import com.reply.solidsoft.nbs.integration.model.ProductCodeScheme;
import com.reply.solidsoft.nbs.integration.model.requests.PackCommand;
import com.reply.solidsoft.nbs.integration.model.requests.RecoveryRequest;
import com.reply.solidsoft.nbs.integration.model.responses.BulkSinglePackResponse;
import com.reply.solidsoft.nbs.integration.model.responses.RecoveryRequestAck;
import com.reply.solidsoft.nbs.integration.model.responses.RecoveryRequestResults;
import com.reply.solidsoft.nbs.integration.model.responses.RecoverySinglePackResponse;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import com.reply.solidsoft.nbs.integration.recovery.model.UpdateDeferredRequestStatisticsEventArgs;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * Handles recovery using a store and forward mechanism.
 */
public class RecoveryHandler extends Thread {

    /**
     * The context for recovery.
     */
    private final RecoveryHandlerContext context;

    /**
     * Initializes a new instance of the RecoveryHandler class.
     *
     * @param context The context for recovery.
     */
    public RecoveryHandler(RecoveryHandlerContext context) {
        this.context = context;
    }

    /**
     * Start the thread.
     */
    @Override
    public void run() {
        this.pollForRecoveryTask();
    }

    /**
     * Process recovery requests asynchronously.
     */
    @SuppressWarnings("SleepWhileInLoop")
    public final void pollForRecoveryTask() {
        while (!this.context.getIsCancellationRequested()) {
            // Take responsibility for polling for recovery if no other instance is.
            if (this.context.getStoreAndForwardService().getPerInstance()) {
                this.context.setIsResponsibleForRecovery(true);
            } else {
                synchronized (RecoveryHandlerContext.POLLING_FOR_RECOVERY_LOCK) {
                    if (!RecoveryHandlerContext.CLIENT_POLLING_STATUS.keySet().contains(this.context.getStoreAndForwardService().getName())) {
                        RecoveryHandlerContext.CLIENT_POLLING_STATUS.put(this.context.getStoreAndForwardService().getName(), true);
                        this.context.setIsResponsibleForRecovery(true);
                    } else if (!RecoveryHandlerContext.CLIENT_POLLING_STATUS.get(this.context.getStoreAndForwardService().getName())) {
                        RecoveryHandlerContext.CLIENT_POLLING_STATUS.put(this.context.getStoreAndForwardService().getName(), true);
                        this.context.setIsResponsibleForRecovery(true);
                    }
                }
            }

            // If not responsible for polling, loop.
            if (!this.context.getIsResponsibleForRecovery()) {
                try {
                    Thread.sleep(this.context.getRecoveryPollInterval());
                } catch (InterruptedException interuptedEx) {
                    // ignore;
                }

                continue;
            }

            int nextPollinterval = this.context.getRecoveryPollInterval();

            if (this.context.getRequestTable() == null) {
                // Wait for full initialization.
                try {
                    Thread.sleep(this.context.getRecoveryPollInterval());
                } catch (InterruptedException interuptedEx) {
                    // ignore;
                }

                continue;
            }

            // Get the non-acknowledged deferred requests in timestamp order.
            List<DeferredRequest> deferredRequests = this.context.getDeferredRequests()
                    .stream()
                    .filter(deferredRequest -> !deferredRequest.getAcknowledged())
                    .sorted((DeferredRequest dr1, DeferredRequest dr2) -> {
                        if (dr1.getTimeStamp() == dr2.getTimeStamp()) {
                            return 0;
                        } else if (dr1.getTimeStamp() < dr2.getTimeStamp()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }).collect(Collectors.toList());

            // Take the number of commands, up to the maximum allowed in a bulk request.
            List<PackCommand> packCommands = deferredRequests
                    .stream()
                    .map(request -> convertToPackCommand(request))
                    .collect(Collectors.toList());
            List<PackCommand> commands;
            commands = packCommands
                    .stream()
                    .limit(packCommands.size() > this.context.getMaxBulkPackCount()
                            ? this.context.getMaxBulkPackCount()
                            : packCommands.size())
                    .collect(Collectors.toList());

            // Output the current recovery statistics.
            this.context.getUpdateDeferredRequestStatistics().listeners().forEach((listener) -> {
                listener.invoke(
                        this,
                        new UpdateDeferredRequestStatisticsEventArgs(
                                this.context.getRequestTable().getCount(),
                                this.context.getResponseTable().getCount()));
            });

            if (commands.isEmpty()) {
                try {
                    Thread.sleep(this.context.getRecoveryPollInterval());
                } catch (InterruptedException interuptedEx) {
                    // ignore;
                }

                continue;
            }

            RecoveryRequestAck response = null;
            try {
                RecoveryRequest recoveryRequest = new RecoveryRequest();
                recoveryRequest.setNumberOfPacks(commands.size());
                recoveryRequest.setPacks(commands.toArray(new PackCommand[0]));
                response = this.context.getSubmitRecoveryRequest().invoke(recoveryRequest, this.context.getLanguage());
            } catch (RuntimeException e) {
                // National system is still not available
                this.context.getLogOnError().invoke(Resources.getLogging_RecoveryRequestFailed(), null);
            }

            if (response != null) {
                URI resultsLocation = response.getLocation();

                switch (response.getStatusCode()) {
                    case HttpStatusCode.SC_ACCEPTED:
                        // Mark each deferred request as acknowledged.
                        this.context.getStoreAndForwardService().acknowledgeRequest(deferredRequests);

                        // Process the response to the request.
                        this.processRecoveryResponse(resultsLocation, this.calculateBackOff(response.getEta(), nextPollinterval), commands);
                        break;
                    case 429:
                        // National System is throttling.  Back off for the requested time, or normal poll interval,
                        // whichever is longer. Calculate the back-off interval
                        nextPollinterval = this.calculateBackOff(response.getEta(), nextPollinterval);

                        break;
                }

                // a response was obtained from the 
            }

            try {
                Thread.sleep(nextPollinterval);
            } catch (InterruptedException interuptedEx) {
                // ignore;
            }
        }
    }

    /**
     * Converts a deferred request to a pack command.
     *
     * @param request The deferred request.
     * @return A pack command.
     */
    private static PackCommand convertToPackCommand(DeferredRequest request) {
        if (!(request.getVerb().equalsIgnoreCase(HttpVerb.PATCH.getValue()))) {
            return new PackCommand(null, convertToPack(request));
        }

        PackStateTransitionType transition = null;

        try {
            PackStateTransitionType transitionParsed = PackStateTransitionType.valueOf(request.getRequestedState());
            transition = transitionParsed;
        } catch (IllegalArgumentException | NullPointerException ex) {
            // ignore;
        }

        return new PackCommand(transition, convertToPack(request));
    }

    /**
     * Converts a deferred request to a pack.
     *
     * @param request The deferred request.
     * @return A pack requestedState.
     */
    private static PackIdentifier convertToPack(DeferredRequest request) {
        if (request == null) {
            return new PackIdentifier();
        }

        String[] uriParts = request.getUri() == null
                ? null
                : request.getUri().replace("\\", "/").split("[/]", -1);

        if (uriParts == null || uriParts.length == 0) {
            return new PackIdentifier();
        }

        int packIndex = Arrays.asList(uriParts).lastIndexOf("pack");

        if (packIndex < 2 || uriParts.length < packIndex + 2) {
            return new PackIdentifier();
        }

        String scheme = uriParts[packIndex - 2].toUpperCase();

        ProductCodeScheme productCodeScheme;

        try {
            productCodeScheme = ProductCodeScheme.valueOf(scheme);
        } catch (IllegalArgumentException | NullPointerException ex) {
            productCodeScheme = ProductCodeScheme.UNKNOWN;
        }

        String[] lastPartWithParams = uriParts[uriParts.length - 1] == null
                ? null
                : (uriParts[uriParts.length - 1].split("[?]", -1));
        String serialNumber = uriParts.length > packIndex + 2
                ? uriParts[packIndex + 1]
                : lastPartWithParams == null ? null : lastPartWithParams[0];
        boolean isManual = lastPartWithParams == null
                ? false
                : lastPartWithParams.length <= 1;

        String batchId = "";
        String expiryDate = "";

        if (!isManual) {
            if (lastPartWithParams != null) {
                List<NameValuePair> parsedQueryString = URLEncodedUtils.parse(lastPartWithParams[1], Charset.forName("UTF-8"));

                for (NameValuePair nameValuePair : parsedQueryString) {
                    if ("batch".equals(nameValuePair.getName())) {
                        batchId = nameValuePair.getValue();
                    } else if ("expiry".equals(nameValuePair.getName())) {
                        expiryDate = nameValuePair.getValue();
                    }
                }
            }
        }

        PackIdentifier packIdentifier = new PackIdentifier();
        packIdentifier.setProductCodeScheme(productCodeScheme.toString().toLowerCase());
        packIdentifier.setProductCode(uriParts[packIndex - 1]);
        packIdentifier.setSerialNumber(serialNumber);
        packIdentifier.setBatchId(batchId);
        packIdentifier.setExpiryDate(expiryDate);
        return packIdentifier;
    }

    /**
     * Calculate the back-off period.
     *
     * @param eta the estimated time of arrival.
     * @param nextPollinterval The current interval to the next poll.
     * @return The back-off period.
     */
    private int calculateBackOff(Instant eta, int nextPollinterval) {
        Instant utcNow = Instant.now();

        if (!(eta.compareTo(utcNow) > 0)) {
            return this.context.getRecoveryPollInterval();
        }

        Duration duration = Duration.between(utcNow, eta);
        int durationTotalMilliseconds = (int) duration.toMillis();

        if (durationTotalMilliseconds > nextPollinterval) {
            nextPollinterval = durationTotalMilliseconds;
        }

        return nextPollinterval;
    }

    /**
     * Poll for recovery results.
     *
     * @param uri The URI location of the results.
     * @param nextPollInterval The next poll interval.
     * @param commands The list of commands.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void processRecoveryResponse(URI uri, int nextPollInterval, Collection commands) {
        while (!this.context.getIsCancellationRequested()) {
            // Create the recovery pack commands
            if (commands.isEmpty()) {
                try {
                    Thread.sleep(nextPollInterval);
                } catch (InterruptedException interuptedEx) {
                    // ignore;
                }

                continue;
            }

            RecoveryRequestResults response = null;
            nextPollInterval = this.context.getRecoveryPollInterval();

            try {
                response = this.context.getGetRecoveryResult().invoke(uri, this.context.getLanguage());
            } catch (RuntimeException e) {
                // National system is still not available
                this.context.getLogOnError().invoke(Resources.getLogging_RecoveryResultsRequestFailed(), null);
            }

            if (response != null) {
                switch (response.getStatusCode()) {
                    case 0:
                        return;
                    case HttpStatusCode.SC_OK:
                        try {
                            try (TransactionManager transaction1 = TransactionManager.newTransaction(this.context.getResponseTable());
                                    TransactionManager transaction2 = TransactionManager.newTransaction(this.context.getRequestTable())) {
                                try {
                                    long timeStamp = this.context.getResponseTable().getCurrentTimestamp();

                                    // Get the original requests.  The code assumes everything is 
                                    // maintained in strict order.
                                    List<DeferredRequest> requestsForRemoval;
                                    requestsForRemoval = this.context.getRequestTable()
                                            .getRecords()
                                            .stream()
                                            .sorted((DeferredRequest dr1, DeferredRequest dr2) -> {
                                                if (dr1.getTimeStamp() == dr2.getTimeStamp()) {
                                                    return 0;
                                                } else if (dr1.getTimeStamp() < dr2.getTimeStamp()) {
                                                    return -1;
                                                } else {
                                                    return 1;
                                                }
                                            })
                                            .limit(response.getPacks().length)
                                            .collect(Collectors.toList());

                                    int index = 0;

                                    // Process response
                                    for (BulkSinglePackResponse pack : response.getPacks()) {
                                        pack.initialize(timeStamp++);
                                        DeferredRequest request = requestsForRemoval.get(index++);
                                        RecoverySinglePackResponse resultResponse = new RecoverySinglePackResponse(request, pack);

                                        // Store results
                                        this.context.getResponseTable().add(resultResponse);
                                    }

                                    // Remove original requests
                                    requestsForRemoval.forEach((request) -> {
                                        this.context.getRequestTable().remove(request);
                                    });

                                    // Invoke each event listener.
                                    this.context.getUpdateDeferredRequestStatistics().listeners().forEach((listener) -> {
                                        listener.invoke(
                                                this,
                                                new UpdateDeferredRequestStatisticsEventArgs(
                                                        this.context.getRequestTable().getCount(),
                                                        this.context.getResponseTable().getCount()));
                                    });

                                } catch (java.lang.Exception e2) {
                                    // Basic co-ordination of transactions.  The transaction manager does not
                                    // support 2pc.  This simple implementation treates each table as a separate
                                    // resource manager.  This code should be improved to implement resource
                                    // management at the data management service level.
                                    transaction1.rollback();
                                    transaction2.rollback();

                                    // If we reach here, this means that we received results to a previous request 
                                    // but failed to process the records in the tore & forward data store.   
                                    // There is no very good answer to this
                                    throw e2;

                                }
                            }

                            return;
                        } catch (IOException ioEx) {
                            // ignore;
                        }
                    case HttpStatusCode.SC_CONFLICT:
                        nextPollInterval = this.calculateBackOff(response.getEta(), nextPollInterval);
                        break;
                    case HttpStatusCode.SC_NOT_FOUND:
                    case HttpStatusCode.SC_UNAUTHORIZED:
                        return;
                }
            }

            try {
                Thread.sleep(nextPollInterval);
            } catch (InterruptedException interuptedEx) {
                // ignore;
            }
        }
    }
}
