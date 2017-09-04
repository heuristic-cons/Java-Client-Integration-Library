/**
 * -----------------------------------------------------------------------------
 * File=InterchangeException.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An exception raised during interchange with the national system.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration;

/**
 * An exception raised during interchange with the national system.
 */
public class InterchangeException extends RuntimeException {

    /**
     * Initializes a new instance of the InterchangeException class.
     */
    public InterchangeException() {
    }

    /**
     * Initializes a new instance of the InterchangeException class.
     *
     * @param message The error message.
     */
    public InterchangeException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the InterchangeException class.
     *
     * @param message The error message,
     * @param innerException An inner exception.
     */
    public InterchangeException(String message, Throwable innerException) {
        super(message, innerException);
    }
}
