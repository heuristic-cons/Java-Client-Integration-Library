/**
 * -----------------------------------------------------------------------------
 * File=TransactionException.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An exception raised during transaction management.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.data;

/**
 * An exception raised during transaction management.
 */
public class TransactionException extends RuntimeException {

    /**
     * Initializes a new instance of the InterchangeException class.
     */
    public TransactionException() {
    }

    /**
     * Initializes a new instance of the InterchangeException class.
     *
     * @param message The error message.
     */
    public TransactionException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the InterchangeException class.
     *
     * @param message The error message,
     * @param innerException An inner exception.
     */
    public TransactionException(String message, RuntimeException innerException) {
        super(message, innerException);
    }
}
