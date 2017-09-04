/**
 * -----------------------------------------------------------------------------
 * File=BaseStoreAndForwardService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Base class for store and forward databases.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery;

import com.reply.solidsoft.nbs.integration.InterchangeException;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.reply.solidsoft.nbs.integration.data.model.DataTable;

/**
 * Base class for store and forward databases.
 */
public abstract class BaseStoreAndForwardService implements StoreAndForwardService {

    /**
     * The store and forward databases, as a collection of tables.
     */
    private Map<String, DataTable> tables;

    /**
     * Initializes a new instance of the BaseStoreAndForwardService class.
     *
     * @param name The name of the store and forward data management service.
     * @param requests The requests table.
     * @param responses The responses table.
     */
    protected BaseStoreAndForwardService(String name, DataTable requests, DataTable responses) {
        this.setName(name);

        if (requests != null) {
            this.Requests = requests;
        } else {
            throw new InterchangeException(Resources.getInterchangeException_NullRequestsTable(), new IllegalArgumentException(Resources.getInterchangeException_NullRequestsTable()));
        }

        if (responses != null) {
            this.Responses = responses;
        } else {
            throw new InterchangeException(Resources.getInterchangeException_NullResponsesTable(), new IllegalArgumentException(Resources.getInterchangeException_NullResponsesTable()));
        }

        this.tables = new HashMap<>();
        this.tables.put("requests", requests);
        this.tables.put("responses", responses);

        this.transactionLog = null;
    }

    /**
     * The requests table.
     */
    private final DataTable Requests;

    /**
     * Gets the requests table.
     *
     * @return The requests table.
     */
    @Override
    public final DataTable getRequests() {
        return Requests;
    }

    /**
     * The responses table.
     */
    private final DataTable Responses;

    /**
     * Gets the responses table.
     *
     * @return The responses table.
     */
    @Override
    public final DataTable getResponses() {
        return Responses;
    }

    /**
     * A value indicating whether the service is per API client instance, or
     * global.
     */
    private boolean PerInstance;

    /**
     * Gets a value indicating whether the service is per API client instance,
     * or global.
     *
     * @return A value indicating whether the service is per API client
     * instance, or global.
     */
    @Override
    public final boolean getPerInstance() {
        return PerInstance;
    }

    /**
     * Record the acknowledgement of receipt of a list of deferred requests by
     * the national system.
     *
     * @param requests The list of deferred requests.
     */
    @Override
    public abstract void acknowledgeRequest(List<DeferredRequest> requests);

    /**
     * Sets a value indicating whether the service is per API client instance,
     * or global.
     *
     * @param value a value indicating whether the service is per API client
     * instance, or global.
     */
    protected final void setPerInstance(boolean value) {
        PerInstance = value;
    }

    /**
     * The name of the store and forward data management service.
     */
    private String name;

    /**
     * Gets the name of the store and forward data management service.
     *
     * @return The name of the store and forward data management service.
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * Sets the name of the store and forward data management service.
     *
     * @param value The name of the store and forward data management service.
     */
    protected final void setName(String value) {
        name = value;
    }

    /**
     * Gets the store and forward data management service tables. The data
     * management service is represented as a cloned dictionary of tables.
     *
     * @return The store and forward data management service tables.
     */
    @Override
    public final Map<String, DataTable> getTables() {
        return new HashMap<>(this.tables);
    }

    /**
     * Sets the store and forward data management service tables.
     *
     * @param value The store and forward data management service tables.
     */
    public final void setTables(Map<String, DataTable> value) {
        this.tables = value;
    }

    /**
     * The transaction log table.
     */
    private final DataTable transactionLog;

    /**
     * Gets the transaction log table.
     *
     * @return The transaction log table.
     */
    @Override
    public DataTable getTransactionLog() {
        return transactionLog;
    }

    /**
     * Closes the store and forward data management service.
     */
    @Override
    public void close() {
    }
}
