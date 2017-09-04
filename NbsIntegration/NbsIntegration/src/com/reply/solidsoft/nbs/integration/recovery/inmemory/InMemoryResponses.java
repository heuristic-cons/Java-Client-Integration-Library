/**
 * -----------------------------------------------------------------------------
 * File=InMemoryResponses.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Implements an in-memory record store for responses.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery.inmemory;

import com.reply.solidsoft.nbs.integration.model.responses.RecoverySinglePackResponse;

/**
 * Implements an in-memory record store for responses.
 */
public class InMemoryResponses extends InMemoryDataTable<RecoverySinglePackResponse> {

    /**
     * Initializes a new instance of the InMemoryResponses class.
     */
    public InMemoryResponses() {
        super("requests");
    }

    /**
     * Initializes a new instance of the InMemoryResponses class.
     *
     * @param service The data management service.
     */
    public InMemoryResponses(InMemoryStoreAndForwardService service) {
        super(service, "requests");
    }
}
