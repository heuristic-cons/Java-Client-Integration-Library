/**
 * -----------------------------------------------------------------------------
 * File=InMemoryTransactionLogEntry.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a log entry for requests during a transaction.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery.inmemory;

import com.reply.solidsoft.nbs.integration.data.model.DataRecord;

/**
 * Represents a log entry for requests during a transaction.
 */
public class InMemoryTransactionLogEntry implements DataRecord {

    /**
     * Lock object for atomic reads and writes to the timestamp.
     */
    private static final Object TIMESTAMP_LOCK = new Object();

    /**
     * Timestamp counter;
     */
    private static long timeStamp;

    /**
     * Initializes a new instance of the InMemoryTransactionLogEntry class.
     */
    public InMemoryTransactionLogEntry() {
        this(0);
    }

    /**
     * Initializes a new instance of the InMemoryTransactionLogEntry class.
     *
     * @param currentTimeStamp The current time stamp. Defaults to 0.
     */
    public InMemoryTransactionLogEntry(long currentTimeStamp) {
        synchronized (TIMESTAMP_LOCK) {
            if (timeStamp == Long.MAX_VALUE) {
                timeStamp = 0;
            }

            if (currentTimeStamp > timeStamp) {
                timeStamp = currentTimeStamp;
            }

            timeStamp++;
        }
    }

    /**
     * Gets the timestamp value for this deferred request log entry.
     *
     * @return The timestamp value for this deferred request log entry.
     */
    @Override
    public final long getTimeStamp() {
        synchronized (TIMESTAMP_LOCK) {
            return timeStamp;
        }
    }

    /**
     * The timestamp of the deferred request.
     */
    private long requestTimeStamp;

    /**
     * Gets the timestamp of the deferred request.
     *
     * @return The timestamp of the deferred request.
     */
    public final long getRequestTimeStamp() {
        return requestTimeStamp;
    }

    /**
     * Sets the timestamp of the deferred request.
     *
     * @param value The timestamp of the deferred request.
     */
    public final void setRequestTimeStamp(long value) {
        requestTimeStamp = value;
    }
}
