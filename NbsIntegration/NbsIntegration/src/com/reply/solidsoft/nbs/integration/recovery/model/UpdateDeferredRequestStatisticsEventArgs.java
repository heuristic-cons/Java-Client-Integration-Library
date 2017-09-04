/**
 * -----------------------------------------------------------------------------
 * File=UpdateDeferredRequestStatisticsEventArgs.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Event arguments for the Store and Forward event.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery.model;

/**
 * Event arguments for the Store and Forward event.
 */
public class UpdateDeferredRequestStatisticsEventArgs extends com.reply.solidsoft.nbs.integration.extensions.events.EventArgs {

    /**
     * Initializes a new instance of the
     * UpdateDeferredRequestStatisticsEventArgs class.
     *
     * @param deferredRequests The number of deferred requests currently being
     * stored.
     * @param deferredResponses The number of previously deferred requests that
     * have been successfully forwarded to the National System and processed.
     */
    public UpdateDeferredRequestStatisticsEventArgs(int deferredRequests, int deferredResponses) {
        this.deferredRequests = deferredRequests;
        this.deferredResponses = deferredResponses;
    }

    /**
     * The message describing the reason the event was raised.
     */
    private final int deferredRequests;

    /**
     * Gets the message describing the reason the event was raised.
     *
     * @return The message describing the reason the event was raised.
     */
    public final int getDeferredRequests() {
        return deferredRequests;
    }

    /**
     * The details of the deferred HTTP request.
     */
    private final int deferredResponses;

    /**
     * Gets the details of the deferred HTTP request.
     * 
     * @return The details of the deferred HTTP request.
     */
    public final int getDeferredResponses() {
        return deferredResponses;
    }
}
