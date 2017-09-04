/**
 * -----------------------------------------------------------------------------
 * File=UpdateDeferredRequestStatisticsHandler.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A delegate type for the 'Update Deferred Request Statistics' event.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration;

import com.reply.solidsoft.nbs.integration.recovery.model.UpdateDeferredRequestStatisticsEventArgs;

/**
 * A delegate type for the 'Update Deferred Request Statistics' event.
 */
@FunctionalInterface
public interface UpdateDeferredRequestStatisticsListener {

    /**
     * @param sender The event sender.
     * @param eventArgs The event arguments.
     *
     * Handles updates to the current statistics for currently deferred requests
     * and previously deferred requests that have been forwarded to the National
     * System and successfully processed.
     */
    void invoke(Object sender, UpdateDeferredRequestStatisticsEventArgs eventArgs);
}
