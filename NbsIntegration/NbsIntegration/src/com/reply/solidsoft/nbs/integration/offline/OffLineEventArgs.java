/**
 * -----------------------------------------------------------------------------
 * File=OffLineEventArgs.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Event arguments for the Token Expired event.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.offline;

/**
 * Event arguments for toggling off-line mode.
 */
public class OffLineEventArgs extends com.reply.solidsoft.nbs.integration.extensions.events.EventArgs {

    /**
     * Initializes a new instance of the OffLineEventArgs class.
     *
     * @param isOffline A value indicating whether the integration library is
     * off-line.
     *
     */
    public OffLineEventArgs(boolean isOffline) {
        this.isOffLine = isOffline;
    }

    /**
     * A value indicating whether the integration library is off-line.
     */
    private final boolean isOffLine;

    /**
     * Gets a value indicating whether the integration library is off-line.
     *
     * @return A value indicating whether the integration library is off-line.
     */
    public final boolean getIsOffLine() {
        return isOffLine;
    }
}
