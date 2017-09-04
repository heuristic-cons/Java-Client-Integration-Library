/**
 * -----------------------------------------------------------------------------
 * File=InMemoryStoreAndForwardService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An in-memory data management service for store and forward requests and
 * responses.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery.inmemory;

import com.reply.solidsoft.nbs.integration.recovery.BaseStoreAndForwardService;
import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import java.util.List;
import com.reply.solidsoft.nbs.integration.data.model.DataTable;

/**
 * An in-memory data management service for store and forward requests and
 * responses.
 */
public class InMemoryStoreAndForwardService extends BaseStoreAndForwardService {

    /**
     * Initializes a new instance of the InMemoryStoreAndForwardService class.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public InMemoryStoreAndForwardService() {
        super("storeAndForward", new InMemoryRequests(), new InMemoryResponses());
        // Create the transaction log
        this.transactionLog = new InMemoryDataTable<>(this, "logEntries");
        this.setPerInstance(true);

        // Initialize the data management service tables
        ((InMemoryRequests) this.getRequests()).setDataManagementService(this);
        ((InMemoryResponses) this.getResponses()).setDataManagementService(this);
    }

    /**
     * The transaction log table table.
     */
    private final DataTable transactionLog;

    /**
     * Gets the transaction log table table.
     *
     * @return The transaction log table table.
     */
    @Override
    public DataTable getTransactionLog() {
        return transactionLog;
    }

    /**
     * Record the acknowledgement of receipt of a list of deferred requests by
     * the national system.
     *
     * @param requests The list of deferred requests.
     */
    @Override
    public void acknowledgeRequest(List<DeferredRequest> requests) {
        if (null == requests) {
            return;
        }

        requests.forEach((deferredRequest) -> {
            this.getRequests().getRecords()
                    .stream()
                    .filter(dr -> ((DeferredRequest) dr).getTimeStamp() == deferredRequest.getTimeStamp())
                    .forEach(dr -> ((DeferredRequest) dr).setAcknowledged(true));
        });
    }

    /**
     * Closes the service.
     */
    @Override
    public void close() {
        // ignore;
    }
}
