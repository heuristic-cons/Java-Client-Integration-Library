/**
 * -----------------------------------------------------------------------------
 * File=PropertyChangedEventHandler.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents an event handler for PropertyChanged events.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.extensions;

/**
 * Represents an event handler for PropertyChanged events.
 */
@FunctionalInterface
public interface PropertyChangedEventHandler {

    /**
     * Invoke the event handler.
     *
     * @param sender The source of the event.
     * @param args The event arguments.
     */
    public void invoke(Object sender, PropertyChangedEventArgs args);
}
