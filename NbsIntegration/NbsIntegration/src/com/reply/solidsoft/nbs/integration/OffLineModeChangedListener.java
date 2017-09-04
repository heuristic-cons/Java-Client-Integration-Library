/**
 * -----------------------------------------------------------------------------
 * File=OffLineModeChangedHandler.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A delegate type for the 'Off-Line Mode Changed' event.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration;

import com.reply.solidsoft.nbs.integration.offline.OffLineEventArgs;

/**
 * A delegate type for the 'Off-Line Mode Changed' event.
 */
@FunctionalInterface
public interface OffLineModeChangedListener {

    /**
     * @param sender The event sender.
     * @param eventArgs The event arguments.
     *
     * Handles changes to the library's off-line mode. the event is raised each
     * time the mode changes.
     */
    void invoke(Object sender, OffLineEventArgs eventArgs);
}
