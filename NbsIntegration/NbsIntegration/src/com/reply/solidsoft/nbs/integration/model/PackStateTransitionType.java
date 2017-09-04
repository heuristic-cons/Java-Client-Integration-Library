/**
 * -----------------------------------------------------------------------------
 * File=PackStateTransitionType.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The pack state transition state.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

/**
 * The pack state transition state.
 */
public enum PackStateTransitionType {
    /**
     * The pack has been supplied.
     */
    SUPPLIED,
    /**
     * The pack is marked as stolen.
     */
    STOLEN,
    /**
     * The pack is marked as destroyed.
     */
    DESTROYED,
    /**
     * The pack is marked as a sample.
     */
    SAMPLE,
    /**
     * The pack is marked as a free sample.
     */
    FREE_SAMPLE,
    /**
     * The pack is locked for further investigation.
     */
    LOCKED,
    /**
     * The pack has been exported from the EU.
     */
    EXPORTED,
    /**
     * The pack is active.
     */
    ACTIVE;

    /**
     * The number of bits used to represent the pack state transition type in
     * two's complement binary form.
     */
    public static final int SIZE = java.lang.Integer.SIZE;

    /**
     * Gets the pack state transition type.
     *
     * @return The pack state transition type.
     */
    public int getValue() {
        return this.ordinal();
    }

    /**
     * Returns a pack state transition type that corresponds to a value.
     *
     * @param value The pack state transition type.
     * @return A pack state transition type.
     */
    public static PackStateTransitionType forValue(int value) {
        return values()[value];
    }
}
