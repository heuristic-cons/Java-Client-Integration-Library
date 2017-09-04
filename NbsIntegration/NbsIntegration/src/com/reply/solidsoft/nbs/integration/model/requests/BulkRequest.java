/**
 * -----------------------------------------------------------------------------
 * File=BulkRequest.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Bulk pack request class.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.requests;

import com.reply.solidsoft.nbs.integration.model.PackIdentifier;
import com.google.gson.annotations.Expose;

/**
 * Bulk pack request class.
 */
public class BulkRequest {

    /**
     * The required state.
     */
    @Expose
    private String state;

    /**
     * Gets the required state.
     *
     * @return The required state.
     */
    public final String getState() {
        return state;
    }

    /**
     * Sets required state.
     *
     * @param value The required state.
     */
    public final void setState(String value) {
        state = value;
    }

    /**
     * The maximum number of pack allowed in a bulk request.
     */
    @Expose(serialize = false, deserialize = false)
    private int maxBulkPackCount = 500000;

    /**
     * Gets the maximum number of pack allowed in a bulk request.
     *
     * @return The maximum number of pack allowed in a bulk request.
     */
    public final int getMaxBulkPackCount() {
        return maxBulkPackCount;
    }

    /**
     * Sets the maximum number of pack allowed in a bulk request.
     *
     * @param value The maximum number of pack allowed in a bulk request.
     */
    public final void setMaxBulkPackCount(int value) {
        maxBulkPackCount = value;
    }

    /**
     * The number of packs.
     */
    @Expose
    private int numberOfPacks;

    /**
     * Gets the number of packs.
     *
     * @return The number of packs.
     */
    public final int getNumberOfPacks() {
        numberOfPacks = this.getPacks() == null
                ? 0
                : this.getPacks().length;
        return numberOfPacks;
    }

    /**
     * The list of pack identifiers.
     */
    @Expose
    private PackIdentifier[] packs;

    /**
     * Gets the list of pack identifiers.
     *
     * @return The list of pack identifiers.
     */
    public final PackIdentifier[] getPacks() {
        return packs;
    }

    /**
     * Sets the list of pack identifiers.
     *
     * @param value The list of pack identifiers.
     */
    public final void setPacks(PackIdentifier[] value) {
        packs = value;
    }
}
