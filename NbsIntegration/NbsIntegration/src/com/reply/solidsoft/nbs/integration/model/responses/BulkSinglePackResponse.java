/**
 * -----------------------------------------------------------------------------
 * File=BulkSinglePackResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Verification response for a single pack within a bulk of packs.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.responses;

import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.google.gson.annotations.Expose;
import com.reply.solidsoft.nbs.integration.data.model.DataRecord;

/**
 * Verification response for a single pack within a bulk of packs.
 */
public class BulkSinglePackResponse implements DataRecord {

    /**
     * Lock object for atomic reads and writes to the timestamp.
     */
    @Expose(serialize = false, deserialize = false)
    private static final Object TIMESTAMP_LOCK = new Object();

    /**
     * Timestamp counter;
     */
    @Expose(serialize = false, deserialize = false)
    private static long latestTimeStamp;

    /**
     * The pack to be verified.
     */
    @Expose
    private PackIdentifier pack;

    /**
     * Gets the pack to be verified.
     *
     * @return The pack to be verified.
     */
    public final PackIdentifier getPack() {
        return pack;
    }

    /**
     * Sets the pack to be verified.
     *
     * @param value The pack to be verified.
     */
    public final void setPack(PackIdentifier value) {
        pack = value;
    }

    /**
     * The response for the verification of the single pack.
     */
    @Expose
    private SinglePackResponse result;

    /**
     * Gets the response for the verification of the single pack.
     *
     * @return The response for the verification of the single pack.
     */
    public final SinglePackResponse getResult() {
        return result;
    }

    /**
     * Sets the response for the verification of the single pack.
     *
     * @param value The response for the verification of the single pack.
     */
    public final void setResult(SinglePackResponse value) {
        result = value;
    }

    /**
     * The timestamp value for this response record.
     */
    @Expose(serialize = false, deserialize = false)
    private long timeStamp;

    /**
     * Gets the timestamp value for this response record.
     *
     * @return The timestamp value for this response record.
     */
    @Override
    public final long getTimeStamp() {
        synchronized (TIMESTAMP_LOCK) {
            return this.timeStamp;
        }
    }

    /**
     * Sets the timestamp value for this response record.
     *
     * @param value The timestamp value for this response record.
     */
    public final void setTimeStamp(long value) {
        synchronized (TIMESTAMP_LOCK) {
            this.timeStamp = value;
        }
    }

    /**
     * Initializes a new instance of the BulkSinglePackResponse class.
     */
    public final void initialize() {
        initialize(0);
    }

    /**
     * Initializes a new instance of the BulkSinglePackResponse class.
     *
     * @param currentTimeStamp The current time stamp. Defaults to 0.
     */
    public final void initialize(long currentTimeStamp) {
        synchronized (TIMESTAMP_LOCK) {
            if (latestTimeStamp == Long.MAX_VALUE) {
                latestTimeStamp = 0;
            }

            if (currentTimeStamp > latestTimeStamp) {
                latestTimeStamp = currentTimeStamp;
            }

            latestTimeStamp++;
            this.timeStamp = latestTimeStamp;
        }
    }
}
