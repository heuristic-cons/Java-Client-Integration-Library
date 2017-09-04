/**
 * -----------------------------------------------------------------------------
 * File=LocalValidationResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The response from pack identifier validation.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.responses;

/**
 * The response from pack identifier validation.
 */
public class LocalValidationResponse {

    /**
     * The NBS operation code.
     */
    private int operationCode;

    /**
     * Gets the NBS operation code.
     *
     * @return The NBS operation code.
     */
    public final int getOperationCode() {
        return operationCode;
    }

    /**
     * Sets the NBS operation code.
     *
     * @param value The NBS operation code.
     */
    public final void setOperationCode(int value) {
        operationCode = value;
    }

    /**
     * The warning text.
     */
    private String warning;

    /**
     * Gets the warning text.
     *
     * @return The warning text.
     */
    public final String getWarning() {
        return warning;
    }

    /**
     * Sets the warning text.
     *
     * @param value The warning text.
     */
    public final void setWarning(String value) {
        warning = value;
    }
}
