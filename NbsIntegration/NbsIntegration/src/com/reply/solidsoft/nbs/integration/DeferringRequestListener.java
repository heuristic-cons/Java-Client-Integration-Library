/**
 * -----------------------------------------------------------------------------
 * File=DeferringRequestHandler.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A delegate type for the 'Deferred Request' event.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration;

import com.reply.solidsoft.nbs.integration.recovery.model.StoreAndForwardEventArgs;

/**
 * A delegate type for the 'Deferred Request' event.
 */
@FunctionalInterface
public interface DeferringRequestListener {

    /**
     * Invoke the deferred request listener.
     *
     * @param sender The event sender.
     * @param eventArgs The event arguments.
     *
     * If the library is in automatic store &amp; forward mode, the deferred
     * request is automatically stored in the provided store &amp; forward data
     * service, or in a default service, if no explicit service is provided. If
     * the library is in manual store &amp; forward mode, it is the
     * responsibility of the event handler to store and forward the deferred
     * request.
     */
    void invoke(Object sender, StoreAndForwardEventArgs eventArgs);
}
