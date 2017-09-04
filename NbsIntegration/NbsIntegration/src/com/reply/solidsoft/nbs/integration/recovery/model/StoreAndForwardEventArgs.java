/**
 * -----------------------------------------------------------------------------
 * File=StoreAndForwardEventArgs.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Event arguments for the Store and Forward event.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery.model;

import com.reply.solidsoft.nbs.integration.InterchangeException;

/**
 * Event arguments for the Store and Forward event.
 */
public class StoreAndForwardEventArgs extends com.reply.solidsoft.nbs.integration.extensions.events.EventArgs {

    /**
     * Initializes a new instance of the StoreAndForwardEventArgs class.
     *
     * @param exception The interchange exception that caused the event to be
     * raised.
     * @param message A message describing the reason the event was raised.
     * @param request The details of the deferred HTTP request.
     * @param mode The current store and forward mode.
     */
    public StoreAndForwardEventArgs(InterchangeException exception, String message, DeferredRequest request, StoreAndForwardMode mode) {
        this.exception = exception;
        this.message = message;
        this.request = request;
        this.mode = mode;
    }

    /**
     * The interchange exception that caused the event to be raised.
     */
    private final InterchangeException exception;

    /**
     * Gets the interchange exception that caused the event to be raised.
     *
     * @return The interchange exception that caused the event to be raised.
     */
    public final InterchangeException getException() {
        return exception;
    }

    /**
     * The message describing the reason the event was raised.
     */
    private final String message;

    /**
     * Gets the message describing the reason the event was raised.
     *
     * @return The message describing the reason the event was raised.
     */
    public final String getMessage() {
        return message;
    }

    /**
     * The details of the deferred HTTP request.
     */
    private final DeferredRequest request;

    /**
     * Gets the details of the deferred HTTP request.
     *
     * @return The details of the deferred HTTP request.
     */
    public final DeferredRequest getRequest() {
        return request;
    }

    /**
     * The current store and forward mode.
     */
    private final StoreAndForwardMode mode;

    /**
     * Gets the current store and forward mode.
     *
     * @return The current store and forward mode.
     */
    public final StoreAndForwardMode getMode() {
        return mode;
    }

    /**
     * A value indicating whether automatic deferral should be canceled.
     */
    private boolean cancel;

    /**
     * Gets a value indicating whether automatic deferral should be canceled.
     *
     * @return A value indicating whether automatic deferral should be canceled.
     */
    public final boolean getCancel() {
        return cancel;
    }

    /**
     * Sets a value indicating whether automatic deferral should be canceled.
     *
     * @param value A value indicating whether automatic deferral should be
     * canceled.
     */
    public final void setCancel(boolean value) {
        cancel = value;
    }
}
