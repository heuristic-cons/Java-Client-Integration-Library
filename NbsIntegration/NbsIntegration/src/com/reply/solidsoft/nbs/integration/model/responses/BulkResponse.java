/**
 * -----------------------------------------------------------------------------
 * File=BulkResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The bulk response.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.responses;

/**
 * The bulk response.
 */
public class BulkResponse {

    /**
     * The operation code.
     */
    private int operationCode;

    /**
     * Gets the operation code.
     *
     * @return The operation code.
     */
    public final int getOperationCode() {
        return operationCode;
    }

    /**
     * Sets the operation code.
     *
     * @param value The operation code.
     */
    public final void setOperationCode(int value) {
        operationCode = value;
    }

    /**
     * The packs.
     */
    private BulkSinglePackResponse[] packs;

    /**
     * Gets the packs.
     *
     * @return The packs.
     */
    public final BulkSinglePackResponse[] getPacks() {
        return packs;
    }

    /**
     * Sets the packs.
     *
     * @param value The packs.
     */
    public final void setPacks(BulkSinglePackResponse[] value) {
        packs = value;
    }
}
