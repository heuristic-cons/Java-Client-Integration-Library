/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reply.solidsoft.nbs.integration.sample.console;

import com.reply.solidsoft.nbs.integration.ApiClient;
import com.reply.solidsoft.nbs.integration.configuration.Configuration;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ClientCredentialsConnectionIdentifier;
import com.reply.solidsoft.nbs.integration.model.DataEntryMode;
import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.reply.solidsoft.nbs.integration.model.requests.BulkRequest;
import com.reply.solidsoft.nbs.integration.model.responses.BulkRequestAck;
import com.reply.solidsoft.nbs.integration.model.responses.BulkRequestResults;
import com.reply.solidsoft.nbs.integration.offline.OffLineEventArgs;
import com.reply.solidsoft.nbs.integration.clientcredentials.model.ClientCredentials;
import com.reply.solidsoft.nbs.integration.sample.credentialsservice.SampleClientCredentialsService;
import com.reply.solidsoft.nbs.integration.sample.dataservice.SampleStoreAndForwardService;
import com.reply.solidsoft.nbs.integration.sample.loggingservice.SampleLoggingService;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.net.URI;
import java.net.URISyntaxException;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.reply.solidsoft.nbs.integration.model.RequestedPackState;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import com.reply.solidsoft.nbs.integration.clientcredentials.ClientCredentialsService;
import com.reply.solidsoft.nbs.integration.logging.LoggingService;
import static com.reply.solidsoft.nbs.integration.model.DataEntryMode.NON_MANUAL;
import static com.reply.solidsoft.nbs.integration.model.RequestedPackState.DESTROYED;
import static com.reply.solidsoft.nbs.integration.model.RequestedPackState.EXPORTED;
import static com.reply.solidsoft.nbs.integration.model.RequestedPackState.FREE_SAMPLE;
import static com.reply.solidsoft.nbs.integration.model.RequestedPackState.LOCKED;
import static com.reply.solidsoft.nbs.integration.model.RequestedPackState.SAMPLE;
import static com.reply.solidsoft.nbs.integration.model.RequestedPackState.STOLEN;
import com.reply.solidsoft.nbs.integration.recovery.StoreAndForwardService;
import static com.reply.solidsoft.nbs.integration.sample.console.Connection.getBaseUrl;
import static com.reply.solidsoft.nbs.integration.sample.console.Connection.getIdentityServerUrl;
import static java.lang.String.format;
import static java.lang.System.in;
import static java.lang.System.out;
import static java.lang.Thread.sleep;
import static java.time.ZoneId.of;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 *
 * @author ch.young
 */
public class NbsConsole {

    /**
     * The current client credentials identifier.
     */
    private static final ClientCredentialsConnectionIdentifier CLIENT_CREDENTIALS_IDENTIFIER = getClientCredentialsIdentifier();

    /**
     * The current client credentials service.
     */
    private static final ClientCredentialsService CLIENT_CREDENTIALS_SERVICE = new SampleClientCredentialsService();

    /**
     * The current store &amp; forward service.
     */
    private static final StoreAndForwardService STORE_AND_FORWARD_SERVICE = new SampleStoreAndForwardService();

    /**
     * The current logging service.
     */
    private static final LoggingService LOGGING_SERVICE = new SampleLoggingService();

    /** <summary>
     * The current requested Language.
     */
    @SuppressWarnings("FieldMayBeFinal")
    private static String language = "";

    /**
     * The current data entry mode.
     */
    @SuppressWarnings("FieldMayBeFinal")
    private static DataEntryMode dataEntryMode = NON_MANUAL;

    /**
     * The current client configuration.
     */
    private static Configuration configuration;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // The test code sets up the client credentails each time to 
        // ensure they are available.
        initializeClientCredentials();

        // Set and change the client configuraton here.
        configuration = new Configuration();
        configuration.setRecoveryPollInterval(10000);
        configuration.setConnectionIdentifier(CLIENT_CREDENTIALS_IDENTIFIER);
        configuration.setBaseUrl(getBaseUrl());
        configuration.setIdentityServerUrl(getIdentityServerUrl());
        configuration.setStoreAndForwardBulkRequests(true);
        //// configuration.setDetectRepeatedSinglePackRequests(true);
        //// configuration.setRepeatedSinglePackRequestsWindowInSeconds(20);

        ApiClient apiClient
                = new ApiClient(
                        CLIENT_CREDENTIALS_SERVICE,
                        configuration,
                        STORE_AND_FORWARD_SERVICE,
                        LOGGING_SERVICE);

        apiClient.setDataEntryMode(dataEntryMode);
        apiClient.setLanguage(language);

        // Switch on logging.  As a general principle, anything that is 
        // configurable through the constructor (using a Configuration 
        // instance) can be read and, as appropriate, set via client 
        // properties.
        apiClient.setIsLogging(true);

        // Add in an event handler to respond to off-line mode change notifications.
        apiClient.offLineModeChanged.addListener(NbsConsole::offlineModeChanged);

        int initialProcessedDeferredRecordsSize = apiClient.getStoredAndForwardResults().size();

        // Set the requested langage here.
        //// language = "sv-SE";
        // Set the data entry mode here.
        //// dataEntryMode = DataEntryMode.MANUAL;
        // Create an instance of the API client.
        // The client should always be disposed.   You can do this by explicitly calling Stop()
        // but in most cases, it is better to use 'try', as below.
        try {

            // Run some tests...
            out.println("=== Common Error Tests ===");
            commonErrorTests(apiClient);

            out.println("=== Verify Tests ===");
            verifyTests(apiClient);

            out.println("=== Supply Tests ===");
            supplyTests(apiClient);

            out.println("=== Decommission Tests ===");
            decommissionTests(apiClient);

            out.println("=== Reactivate Tests ===");
            reactivateTests(apiClient);

            out.println("=== Bulk BulkRequest Tests ===");
            bulkRequestTests();
        } catch (Throwable ex) {
            out.println(ex.toString());
        } finally {
            try {
                apiClient.close();
            } catch (IOException ex) {
                out.println(ex.toString());
            }
        }

        // Redundant, due to use of 'try' above.
        //// apiClient.Stop();
        // The instance of the API Client created above is no longer responsible for handling 
        // deferred requests - requests made while the client was off-line.  The helper method 
        // called below will create a second API CLient which will take responsibility
        // automatically.  The code loops and reports until all deferred requets have been 
        // processed.  If the National System is unavailable, this will loop forever!
        processDeferredRecords(initialProcessedDeferredRecordsSize);

        out.println("=== Log ===");

        // Print out all log entries.   This may get big over time.  The sample logging service
        // keeps all its log records in a single databae (db file).  Delete the file if you want 
        // to get rid of historic data.  A new log database will be created automatically.
        
        apiClient.getLogEntries().forEach((apiClientLogEntry) -> {
            out.println(format("%1$s: %2$s - %3$s",
                    apiClientLogEntry.getId(),
                    ofPattern("dd/MM/yyyy HH:mm:ss").withZone(of("UTC+0")).format(apiClientLogEntry.getTime()),
                    apiClientLogEntry.getMessage() == null ? "" : apiClientLogEntry.getMessage()));
        });

        out.println("Press and key to continue...");
        
        try
        {
            in.read();
        }
        catch (IOException ioEx){
        }
    }

    /**
     * Event handler. Handles offline change mode notifications.
     *
     * @param sender The API client
     * @param eventArgs The event arguments.
     */
    private static void offlineModeChanged(Object sender, OffLineEventArgs eventArgs) {
        String message = eventArgs.getIsOffLine()
                ? "The integration library is offline."
                : "The integration library is online";
        out.println(message);
    }

    /**
     * Initialize the client credentials for this test run.
     */
    private static void initializeClientCredentials() {
        CLIENT_CREDENTIALS_SERVICE.add(getCredentials());
    }

    /**
     * The common error tests.
     */
    private static void commonErrorTests(ApiClient apiClient) {
        // deserialize JSON directly from a file
        try (JsonReader reader = new JsonReader(new FileReader("CommonError.json"))) {
            Gson serializer = new Gson();
            Packs packs = (Packs) serializer.fromJson(reader, Packs.class);

            packs.forEach((pack) -> {
                out.println(apiClient.verify(pack));
            });
        } catch (IOException ex) {
            String e = ex.getMessage();
        }
    }

    /// <summary>
    /// The verify tests.
    /// </summary>
    private static void verifyTests(ApiClient apiClient) {
        // deserialize JSON directly from a file
        try (JsonReader reader = new JsonReader(new FileReader("Verify.json"))) {
            Gson serializer = new Gson();
            Packs packs = (Packs) serializer.fromJson(reader, Packs.class);

            packs.forEach((pack) -> {
                out.println(apiClient.verify(pack));
            });
        } catch (IOException ex) {

        }
    }

    /// <summary>
    /// The Supply tests.
    /// </summary>
    private static void supplyTests(ApiClient apiClient) {
        // deserialize JSON directly from a file
        try (JsonReader reader = new JsonReader(new FileReader("Supply.json"))) {
            Gson serializer = new Gson();
            Packs packs = (Packs) serializer.fromJson(reader, Packs.class);

            packs.forEach((pack) -> {
                out.println(apiClient.supply(pack));
            });
        } catch (IOException ex) {

        }
    }

    /// <summary>
    /// The Decommission tests.
    /// </summary>
    private static void decommissionTests(ApiClient apiClient) {
        // deserialize JSON directly from a file
        try (JsonReader reader = new JsonReader(new FileReader("Decommission.json"))) {
            Gson serializer = new Gson();
            PacksWithCommands packs = (PacksWithCommands) serializer.fromJson(reader, PacksWithCommands.class);

            packs.forEach((pack) -> {
                out.println(apiClient.decommission(pack, pack.getState()));
            });
        } catch (IOException ex) {

        }
    }

    /// <summary>
    /// The reactivate tests.
    /// </summary>
    private static void reactivateTests(ApiClient apiClient) {
        // deserialize JSON directly from a file
        try (JsonReader reader = new JsonReader(new FileReader("Reactivate.json"))) {
            Gson serializer = new Gson();
            Packs packs = (Packs) serializer.fromJson(reader, Packs.class);

            packs.forEach((pack) -> {
                out.println(apiClient.reactivate(pack));
            });
        } catch (IOException ex) {

        }
    }

    /*
     * The bulk request tests.
     */
    private static void bulkRequestTests() {
        List<Thread> threads = new ArrayList<>();

        BulkRequestThread bulkVerifyTest = new BulkRequestThread();
        threads.add(bulkVerifyTest);

        BulkRequestThread bulkSupplyTest = new BulkRequestThread();
        threads.add(bulkSupplyTest);

        BulkRequestThread bulkDecommissionDestroyedTest = new BulkRequestThread();
        threads.add(bulkDecommissionDestroyedTest);

        BulkRequestThread bulkDecommissionExportedTest = new BulkRequestThread();
        threads.add(bulkDecommissionExportedTest);

        BulkRequestThread bulkDecommissionFreeSampleTest = new BulkRequestThread();
        threads.add(bulkDecommissionFreeSampleTest);

        BulkRequestThread bulkDecommissionLockedTest = new BulkRequestThread();
        threads.add(bulkDecommissionLockedTest);

        BulkRequestThread bulkDecommissionSampleTest = new BulkRequestThread();
        threads.add(bulkDecommissionSampleTest);

        BulkRequestThread bulkDecommissionStolenTest = new BulkRequestThread();
        threads.add(bulkDecommissionStolenTest);

        BulkRequestThread bulkReactivateTest = new BulkRequestThread();
        threads.add(bulkReactivateTest);

        bulkVerifyTest.StartBulkVerifyTestPacks();
        bulkSupplyTest.StartBulkSupplyTestPacks();
        bulkDecommissionDestroyedTest.StartBulkDecommissionTestPacks(DESTROYED);
        bulkDecommissionExportedTest.StartBulkDecommissionTestPacks(EXPORTED);
        bulkDecommissionFreeSampleTest.StartBulkDecommissionTestPacks(FREE_SAMPLE);
        bulkDecommissionLockedTest.StartBulkDecommissionTestPacks(LOCKED);
        bulkDecommissionSampleTest.StartBulkDecommissionTestPacks(SAMPLE);
        bulkDecommissionStolenTest.StartBulkDecommissionTestPacks(STOLEN);
        bulkReactivateTest.StartBulkReactivateTestPacks();

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException ex) {
            // ignore;
        }
    }

    /**
     * Process deferred records (store &amp; forward).
     *
     * @param initialProcessedDeferredRecordsSize The initial number of
     * processed deferred records.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private static void processDeferredRecords(int initialProcessedDeferredRecordsSize) {
        try (ApiClient apiClientForDeferredRequests = new ApiClient(
                CLIENT_CREDENTIALS_SERVICE,
                configuration,
                STORE_AND_FORWARD_SERVICE)) {
            apiClientForDeferredRequests.setDataEntryMode(dataEntryMode);
            apiClientForDeferredRequests.setLanguage(language);
            apiClientForDeferredRequests.offLineModeChanged.addListener(NbsConsole::offlineModeChanged);

            if (apiClientForDeferredRequests.getDeferredRequests().isEmpty())
            {
                return;
            }
            
            while (apiClientForDeferredRequests.getDeferredRequests().size() > 0
                    || apiClientForDeferredRequests.getStoredAndForwardResults().size() <= initialProcessedDeferredRecordsSize) {
                out.println(format("Unsubmitted deferred requests: %1$s", apiClientForDeferredRequests.getDeferredRequests().size()));
                try {
                    sleep(10000);
                } catch (InterruptedException ex) {
                    // ignore;
                }
            }

            out.println(format("Unsubmitted deferred requests: %1$s", apiClientForDeferredRequests.getDeferredRequests().size()));
            out.println(format("Processed requests %1$s", apiClientForDeferredRequests.getStoredAndForwardResults().size()));

            apiClientForDeferredRequests.getStoredAndForwardResults().forEach((result) -> {
                out.println(result.toString());
            });
        } catch (IOException ex) {
            // ignore;
            out.println(ex.getMessage());
        } catch (Exception ex2) {
            // ignore;
            out.println(ex2.getMessage());
        }
    }

    /**
     * Perform a bulk request.
     *
     * @param getPacks Get the packs for the bulk request.
     */
    static void doBulkRequest(Supplier<PackIdentifier[]> getPacks) {
        performBulkRequest(() -> {
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.setPacks(getPacks.get());
            return bulkRequest;
        });
    }

    /**
     * Perform a bulk request.
     *
     * @param getPacks Get the packs for the bulk request.
     * @param state The requested pack state.
     */
    static void doBulkRequest(Supplier<PackIdentifier[]> getPacks, RequestedPackState state) {
        performBulkRequest(() -> {
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.setState(state.getValue());
            bulkRequest.setPacks(getPacks.get());
            return bulkRequest;
        });
    }

    /**
     * Perform the bulk request.
     *
     * @param getRequest Get the bulk request.
     */
    @SuppressWarnings("SleepWhileInLoop")
    private static void performBulkRequest(Supplier<BulkRequest> getRequest) {
        try (
                ApiClient apiClientBulkRequest = new ApiClient(
                        CLIENT_CREDENTIALS_SERVICE,
                        configuration,
                        STORE_AND_FORWARD_SERVICE)) {
            apiClientBulkRequest.setDataEntryMode(dataEntryMode);
            apiClientBulkRequest.setLanguage(language);
            apiClientBulkRequest.offLineModeChanged.addListener(NbsConsole::offlineModeChanged);

            boolean repeat = true;
            BulkRequestAck ack = apiClientBulkRequest.submitBulkRequest(getRequest.get());

            out.println(ack);

            if (ack.getIsAck()) {
                out.println(format("Delaying for %1$sms.", ack.getCurrentDelay()));
                try {
                    sleep(ack.getCurrentDelay());
                } catch (InterruptedException interrupedEx) {
                    // ignore;
                }
            }

            BulkRequestResults results = null;

            while (repeat) {
                if (ack.getLocation() == null) {
                    break;
                }

                results = apiClientBulkRequest.getBulkResult(new URI(ack.getLocation().toString()));

                if (results.getIsReady()) {
                    repeat = false;
                    continue;
                }

                if (results.getHasFailed()) {
                    out.println(format("Failed to communicate successfully with National System to obtain results.  %1$s", results.getWarning()));
                    repeat = false;
                    continue;
                }

                out.println(results);
                out.println(format("Delaying for %1$sms.", results.getCurrentDelay()));
                try {
                    sleep(results.getCurrentDelay());
                } catch (InterruptedException interrupedEx) {
                    // ignore;
                }
            }

            out.println(results);
        } catch (IOException | URISyntaxException ex) {
            // ignore;
        }
    }

    /**
     * Get test packs for a bulk-of-pack verify request.
     *
     * @return An array of pack identifiers.
     */
    static PackIdentifier[] bulkVerifyTestPacks() {
        // deserialize JSON directly from a file
        try (JsonReader reader = new JsonReader(new FileReader("Verify.json"))) {
            Gson serializer = new Gson();
            Packs packs = (Packs) serializer.fromJson(reader, Packs.class);
            return packs.toArray(new PackIdentifier[packs.getCount()]);
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Get test packs for a bulk-of-pack supply request.
     *
     * @return An array of pack identifiers.
     */
    static PackIdentifier[] bulkSupplyTestPacks() {
        // deserialize JSON directly from a file
        try (JsonReader reader = new JsonReader(new FileReader("Supply.json"))) {
            Gson serializer = new Gson();
            Packs packs = (Packs) serializer.fromJson(reader, Packs.class);
            return packs.toArray(new PackIdentifier[packs.getCount()]);
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Get test packs for a bulk-of-pack decommission request.
     *
     * @return An array of pack identifiers.
     */
    static PackIdentifier[] bulkDecommissionTestPacks() {
        // deserialize JSON directly from a file
        try (JsonReader reader = new JsonReader(new FileReader("Decommission.json"))) {
            Gson serializer = new Gson();
            Packs packs = (Packs) serializer.fromJson(reader, Packs.class);
            return packs.toArray(new PackIdentifier[packs.getCount()]);
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Get test packs for a bulk-of-pack reactivate request.
     *
     * @return An array of pack identifiers.
     */
    static PackIdentifier[] bulkReactivateTestPacks() {
        // deserialize JSON directly from a file
        try (JsonReader reader = new JsonReader(new FileReader("Reactivate.json"))) {
            Gson serializer = new Gson();
            Packs packs = (Packs) serializer.fromJson(reader, Packs.class);
            return packs.toArray(new PackIdentifier[packs.getCount()]);
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Get the required client credentials identifier.
     *
     * @return A client credentials identifier.
     */
    private static ClientCredentialsConnectionIdentifier getClientCredentialsIdentifier() {
        ClientCredentialsConnectionIdentifier ccIdentifier = new ClientCredentialsConnectionIdentifier();
        ccIdentifier.setOrganisation("Solidsoft Reply");
        ccIdentifier.setLocation("Honor Oak 1");
        ccIdentifier.setEquipment("demo machine");
        return ccIdentifier;
    }

    /**
     * Get a set of client credentials.
     *
     * @return A set of client credentials.
     */
    private static ClientCredentials getCredentials() {
        ClientCredentialsConnectionIdentifier clientCredentialsIdentifier = getClientCredentialsIdentifier();

        return new ClientCredentials(
                clientCredentialsIdentifier.getOrganisation(),
                clientCredentialsIdentifier.getLocation(),
                clientCredentialsIdentifier.getEquipment(),
                "demo-pharmacy",
                "itsasecret");

//                "bVf0VbkvxyhYRlWhm18qmw9G",
//                "iA6IIgHliAVtjXFw74yQGkxA");
    }
}
