/**
 * -----------------------------------------------------------------------------
 * File=DeferredRequest.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a deferred API request.  Deferred requests are used when deciding
 * if to store and forward a failed request.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery.model;

import com.reply.solidsoft.nbs.integration.data.model.DataRecord;

/**
 * Represents a deferred API request. Deferred requests are used when deciding
 * if to store and forward a failed request.
 */
public class DeferredRequest implements DataRecord {

    /**
     * Lock object for atomic reads and writes to the timestamp.
     */
    private static final Object TIMESTAMP_LOCK = new Object();

    /**
     * Timestamp counter.
     */
    private static long latestTimeStamp;

    /**
     * Timestamp value.
     */
    private long timeStamp;

    /**
     * Initializes a new instance of the DeferredRequest class.
     */
    public DeferredRequest() {
    }

    /**
     * Initializes a new instance of the DeferredRequest class.
     *
     * @param currentTimeStamp The current time stamp. Defaults to 0.
     */
    public DeferredRequest(long currentTimeStamp) {
        this.initialize(currentTimeStamp);
    }

    /**
     * A value indicating whether the deferred request has been received and
     * acknowledged by the national system.
     */
    private boolean acknowledged;

    /**
     * Gets a value indicating whether the deferred request has been received
     * and acknowledged by the national system.
     *
     * @return A value indicating whether the deferred request has been received
     * and acknowledged by the national system.
     */
    public boolean getAcknowledged() {
        return this.acknowledged;
    }

    /**
     * Sets a value indicating whether the deferred request has been received
     * and acknowledged by the national system.
     *
     * @param value A value indicating whether the deferred request has been
     * received and acknowledged by the national system.
     */
    public void setAcknowledged(boolean value) {
        this.acknowledged = value;
    }

    /**
     * The content of the body.
     */
    private String body;

    /**
     * Gets the content of the body.
     *
     * @return The content of the body.
     */
    public final String getBody() {
        return body;
    }

    /**
     * Sets the content of the body.
     *
     * @param value The content of the body.
     */
    public final void setBody(String value) {
        body = value;
    }

    /**
     * The requested pack state.
     */
    private String requestedState;

    /**
     * Gets the requested pack state.
     *
     * @return The requested pack state.
     */
    public final String getRequestedState() {
        return requestedState;
    }

    /**
     * Sets the requested pack state.
     *
     * @param value The requested pack state.
     */
    public final void setRequestedState(String value) {
        requestedState = value;
    }

    /**
     * A value indicating whether the data entry is manual or non-manual.
     */
    private boolean isManual;

    /**
     * Gets a value indicating whether the data entry is manual or non-manual.
     *
     * @return A value indicating whether the data entry is manual or
     * non-manual.
     */
    public final boolean getIsManual() {
        return isManual;
    }

    /**
     * Sets a value indicating whether the data entry is manual or non-manual.
     *
     * @param value A value indicating whether the data entry is manual or
     * non-manual.
     */
    public final void setIsManual(boolean value) {
        isManual = value;
    }

    /**
     * The required language for response messages.
     */
    private String language;

    /**
     * Gets the required language for response messages.
     *
     * @return The required language for response messages.
     */
    public final String getLanguage() {
        return language;
    }

    /**
     * Sets the required language for response messages.
     *
     * @param value The required language for response messages.
     */
    public final void setLanguage(String value) {
        language = value;
    }

    /**
     * Gets the timestamp value for this deferred record.
     *
     * @return The timestamp value for this deferred record.
     */
    @Override
    public final long getTimeStamp() {
        synchronized (TIMESTAMP_LOCK) {
            return this.timeStamp;
        }
    }

    /**
     * Sets the timestamp value for this deferred record.
     *
     * @param value The timestamp value for this deferred record.
     */
    public final void setTimeStamp(long value) {
        synchronized (TIMESTAMP_LOCK) {
            this.timeStamp = value;
        }
    }

    /**
     * The URI for the resource.
     */
    private String uri;

    /**
     * Gets the URI for the resource.
     *
     * @return The URI for the resource.
     */
    public final String getUri() {
        return uri;
    }

    /**
     * Sets the URI for the resource.
     *
     * @param value The URI for the resource.
     */
    public final void setUri(String value) {
        uri = value;
    }

    /**
     * The Type of HTTP verb.
     */
    private String verb;

    /**
     * Gets the type of HTTP verb.
     *
     * @return The type of HTTP verb.
     */
    public final String getVerb() {
        return verb;
    }

    /**
     * Sets the type of HTTP verb.
     *
     * @param value The type of HTTP verb.
     */
    public final void setVerb(String value) {
        verb = value;
    }

    /**
     * Initializes a new instance of the DeferredRequest class.
     *
     * @param currentTimeStamp The current time stamp.
     */
    private void initialize(long currentTimeStamp) {
        synchronized (TIMESTAMP_LOCK) {
            if (latestTimeStamp == Long.MAX_VALUE) {
                latestTimeStamp = 0;
            }

            if (currentTimeStamp > latestTimeStamp) {
                latestTimeStamp = currentTimeStamp;
            }

            latestTimeStamp++;
            this.setTimeStamp(latestTimeStamp);
        }
    }
}
