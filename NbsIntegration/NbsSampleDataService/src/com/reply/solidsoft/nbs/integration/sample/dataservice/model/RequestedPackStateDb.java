/**
 * -----------------------------------------------------------------------------
 * File=RequestedPackStateDb.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An enumeration of pack states.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice.model;

import com.reply.solidsoft.nbs.integration.model.RequestedPackState;

/**
 * An enumeration of pack states.
 */
public class RequestedPackStateDb {

    private final RequestedPackState requestedPackState;

    /**
     * Initializes a new instance of the RequestedPackStateDb class.
     */
    public RequestedPackStateDb() {
        requestedPackState = RequestedPackState.ACTIVE;
    }

    /**
     * Initializes a new instance of the RequestedPackStateDb class.
     *
     * @param timeStamp The timestamp of the enclosing
     * RecoverySinglePackResponse instance.
     * @param requestedPackState The base requested pack state.
     */
    public RequestedPackStateDb(long timeStamp, RequestedPackState requestedPackState) {
        this.requestedPackState = requestedPackState;
        this.setTimeStamp(timeStamp);
    }

    /**
     * The timestamp value.
     */
    private long timeStamp;

    /**
     * Gets the timestamp value.
     *
     * @return The timestamp value.
     */
    public final long getTimeStamp() {
        return this.timeStamp;
    }

    /**
     * Sets the timestamp value.
     *
     * @param value The timestamp value.
     */
    public final void setTimeStamp(long value) {
        this.timeStamp = value;
    }

    /**
     * Gets the display name of the enumeration value.
     *
     * @return The display name of the enumeration value.
     */
    public final String getDisplayName() {
        return this.requestedPackState.getDisplayName();
    }

    /**
     * Gets the enumeration value.
     *
     * @return The enumeration value.
     */
    public final String getValue() {
        return this.requestedPackState.getValue();
    }
}
