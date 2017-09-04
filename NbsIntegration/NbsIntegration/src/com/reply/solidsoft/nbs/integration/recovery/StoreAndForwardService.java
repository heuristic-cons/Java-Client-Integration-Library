/**
 * -----------------------------------------------------------------------------
 * File=IStoreAndForwardService.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Base class for store and forward databases.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery;

import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import java.util.List;
import com.reply.solidsoft.nbs.integration.data.DataManagementService;
import com.reply.solidsoft.nbs.integration.data.model.DataTable;

/**
 * Base class for store and forward databases.
 */
public interface StoreAndForwardService extends DataManagementService {

    /**
     * Gets the requests table.
     *
     * @return The requests table.
     */
    public DataTable getRequests();

    /**
     * Gets the responses table.
     *
     * @return The responses table.
     */
    public DataTable getResponses();

    /**
     * Gets a value indicating whether the service is per API client instance,
     * or global.
     * <p>
     * This property supports models where each instance of a store &amp;
     * forward services is associated with a single instance of an API Client.
     * In this case, the integration library assumes that the store &amp;
     * forward service provides an API client-specific list of deferred records
     * to be forwarded on recovery.
     *
     * @return A value indicating whether the service is per API client
     * instance, or global.
     */
    public boolean getPerInstance();

    /**
     * Record the acknowledgement of receipt of a list of deferred requests by
     * the national system.
     *
     * @param requests The list of deferred requests.
     */
    public void acknowledgeRequest(List<DeferredRequest> requests);
}
