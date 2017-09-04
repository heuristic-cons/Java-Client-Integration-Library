/**
 * -----------------------------------------------------------------------------
 * File=RequestTransactionLogEntry.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a log entry for requests during a transaction.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice.model;

import com.reply.solidsoft.nbs.integration.data.model.*;

/**
 * Represents a log entry for requests during a transaction.
 */
public class RequestTransactionLogEntry implements DataRecord {

    /**
     * Lock object for atomic reads and writes to the timestamp.
     */
    private static final Object GLOBAL_TIME_STAMP_LOCK = new Object();

    /**
     * Timestamp counter.
     */
    private static long globalTimeStamp;

    /**
     * Initializes a new instance of the RequestTransactionLogEntry class.
     */
    public RequestTransactionLogEntry() {
        this(0);
    }

    /**
     * Initializes a new instance of the RequestTransactionLogEntry class.
     *
     * @param currentTimeStamp The current time stamp. Defaults to 0.
     */
    public RequestTransactionLogEntry(long currentTimeStamp) {
        synchronized (GLOBAL_TIME_STAMP_LOCK) {
            if (globalTimeStamp == Long.MAX_VALUE) {
                globalTimeStamp = 0;
            }

            if (currentTimeStamp > globalTimeStamp) {
                globalTimeStamp = currentTimeStamp;
            }

            this.setTimeStamp(++globalTimeStamp);
        }
    }

    /**
     * The timestamp value for this deferred request log entry.
     */
    private long timeStamp;

    /**
     * Gets the timestamp value for this deferred request log entry.
     *
     * @return The timestamp value for this deferred request log entry.
     */
    @Override
    public final long getTimeStamp() {
        return this.timeStamp;
    }

    /**
     * Sets the timestamp value for this deferred request log entry.
     *
     * @param value The timestamp value for this deferred request log entry.
     */
    public final void setTimeStamp(long value) {
        this.timeStamp = value;
    }

    /**
     * The timestamp of the deferred request.
     */
    private long requestTimeStamp;

    /**
     * Gets or sets the timestamp of the deferred request.
     *
     * @return The timestamp of the deferred request.
     */
    public final long getRequestTimeStamp() {
        return this.requestTimeStamp;
    }

    /**
     * Sets the timestamp of the deferred request.
     *
     * @param value The timestamp of the deferred request.
     */
    public final void setRequestTimeStamp(long value) {
        this.requestTimeStamp = value;
    }
}
