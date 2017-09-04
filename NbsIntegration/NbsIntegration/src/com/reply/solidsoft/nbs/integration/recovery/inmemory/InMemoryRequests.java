/**
 * -----------------------------------------------------------------------------
 * File=InMemoryRequests.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements a transactional file record store.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery.inmemory;

import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;

/**
 * Implements a transactional file record store.
 */
public class InMemoryRequests extends InMemoryDataTable<DeferredRequest> {

    /**
     * Initializes a new instance of the InMemoryRequests class.
     */
    public InMemoryRequests() {
        super("requests");
    }

    /**
     * Initializes a new instance of the InMemoryRequests class.
     *
     * @param service The data management service.
     */
    public InMemoryRequests(InMemoryStoreAndForwardService service) {
        super(service, "requests");
    }
}
