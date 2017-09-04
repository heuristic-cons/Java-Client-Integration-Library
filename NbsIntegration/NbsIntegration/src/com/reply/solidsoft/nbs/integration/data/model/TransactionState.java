/**
 * -----------------------------------------------------------------------------
 * File=TransactionState.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Transaction states for a transaction manager.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.data.model;

/**
 * Transaction states for a transaction manager.
 */
public enum TransactionState {
    /**
     * Transaction has started.
     *
     * An ACTIVE transaction can transition to PARTIALLY_COMMITTED or FAILED.
     */
    ACTIVE,
    /**
     * Commit has been called, but not yet completed.
     *
     * A PARTIALLY_COMMITTED transaction can transition to COMMITTED or FAILED.
     */
    PARTIALLY_COMMITTED,
    /**
     * The transaction if fully committed.
     *
     * A COMMITTED transaction can transition to TERMINATED.
     */
    COMMITTED,
    /**
     * The transaction has failed and should be rolled back.
     *
     * A FAILED transaction can transition to ABORTED.
     */
    FAILED,
    /**
     * The transaction has been aborted and rolled back.
     *
     * An ABORTED transaction can transition to TERMINATED.
     */
    ABORTED,
    /**
     * The transaction has been terminated.
     */
    TERMINATED;

    /**
     * The number of bits used to represent a transaction state value in two's
     * complement binary form.
     */
    public static final int SIZE = java.lang.Integer.SIZE;

    /**
     * Gets the integer value of the transaction state.
     *
     * @return The integer value of the transaction state.
     */
    public int getValue() {
        return this.ordinal();
    }

    /**
     * Returns a transaction state corresponding to a given integer value.
     *
     * @param value The integer value.
     * @return A transaction state corresponding to a given integer value.
     */
    public static TransactionState forValue(int value) {
        return values()[value];
    }
}
