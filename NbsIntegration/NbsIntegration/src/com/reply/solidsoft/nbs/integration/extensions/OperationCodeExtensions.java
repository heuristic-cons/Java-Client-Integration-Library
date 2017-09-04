/**
 * -----------------------------------------------------------------------------
 * File=OperationCodeExtensions.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Extension helpers for operational codes.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.extensions;

/**
 * Extension helpers for operational codes.
 */
public final class OperationCodeExtensions {

    /**
     * Tests if the response contains a warning.
     *
     * @param operationCode The operation code.
     * @return True, if the response contains a warning.
     */
    public static boolean hasWarning(int operationCode) {
        return validateCode(operationCode) && String.valueOf(operationCode).substring(3, 4).equals("2");
    }

    /**
     * Tests if the code represents an HTTP success (2xx).
     *
     * @param operationCode The operation code.
     * @return True, is an HTTP success; otherwise false.
     */
    public static boolean isSuccess(int operationCode) {
        return validateCode(operationCode) && String.valueOf(operationCode).startsWith("1");
    }

    /**
     * Validates an operation code.
     *
     * @param operationCode The operation code.
     * @return True, if valid; otherwise false.
     */
    private static boolean validateCode(int operationCode) {
        return String.valueOf(operationCode).length() == 8;
    }
}
