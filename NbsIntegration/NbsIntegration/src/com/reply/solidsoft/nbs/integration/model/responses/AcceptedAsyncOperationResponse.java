/**
 * -----------------------------------------------------------------------------
 * File=AcceptedAsyncOperationResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Response returned by the national system when it accepts an asynchronous operation request.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.responses;

import java.time.Instant;

/**
 * Response returned by the national system when it accepts an asynchronous
 * operation request.
 */
public class AcceptedAsyncOperationResponse {

    /**
     * The estimated time of arrival of the results.
     */
    private Instant eta = Instant.MIN;

    /**
     * Gets the estimated time of arrival of the results.
     *
     * @return The estimated time of arrival of the results.
     */
    public final Instant getEta() {
        return eta;
    }

    /**
     * Sets the estimated time of arrival of the results.
     *
     * @param value The estimated time of arrival of the results.
     */
    public final void setEta(Instant value) {
        eta = value;
    }

    /**
     * The result expiry date and time.
     */
    private Instant expires = Instant.MIN;

    /**
     * Gets the result expiry date and time.
     *
     * @return The result expiry date and time.
     */
    public final Instant getExpires() {
        return expires;
    }

    /**
     * Sets the result expiry date and time.
     *
     * @param value The result expiry date and time.
     */
    public final void setExpires(Instant value) {
        expires = value;
    }

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
}
