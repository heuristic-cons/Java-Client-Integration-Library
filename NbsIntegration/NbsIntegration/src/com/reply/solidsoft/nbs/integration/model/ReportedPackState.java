/**
 * -----------------------------------------------------------------------------
 * File=ReportedPackState.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An enumeration of verification states.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

public enum ReportedPackState {
    /**
     * The 'ACTIVE' pack state.
     */
    ACTIVE("Active", "Active"),
    /**
     * The 'DESTROYED' pack state.
     */
    DESTROYED("Destroyed", "Destroyed"),
    /**
     * The 'EXPORTED' pack state.
     */
    EXPORTED("Exported", "Exported"),
    /**
     * The 'Free SAMPLE' pack state.
     */
    FREE_SAMPLE("FreeSample", "Free Sample"),
    /**
     * The 'LOCKED' pack state.
     */
    LOCKED("Locked", "Locked"),
    /**
     * The 'SAMPLE' pack state.
     */
    SAMPLE("Sample", "Sample"),
    /**
     * The 'Checked-Out' pack state.
     */
    STOLEN("Stolen", "Stolen"),
    /**
     * The 'SUPPLIED' pack state.
     *
     * ACTIVE
     */
    SUPPLIED("Supplied", "Supplied"),
    /**
     * The 'Checked-Out' pack state.
     */
    CHECKED_OUT("CheckedOut", "Checked-Out"),
    /**
     * The 'EXPIRED' pack state.
     */
    EXPIRED("Expired", "Expired"),
    /**
     * The 'RECALLED' pack state.
     */
    RECALLED("Recalled", "Recalled"),
    /**
     * The 'WITHDRAWN' pack state.
     */
    WITHDRAWN("Withdrawn", "Withdrawn"),
    /**
     * The 'Not found' pack state.
     */
    NOT_FOUND("NotFound", "Not Found"),
    /**
     * The 'NONE' pack state.
     */
    NONE("", "None");

    private final String value;
    private final String displayName;

    /**
     * Initializes a new instance of the ReportedPackState class.
     */
    ReportedPackState() {
        this.value = "Get";
        this.displayName = "Get";
    }

    /**
     * Initializes a new instance of the ReportedPackState class.
     *
     * @param value The state name.
     */
    ReportedPackState(String value) {
        this.value = value;
        this.displayName = value;
    }

    /**
     * Initializes a new instance of the ReportedPackState class.
     *
     * @param value The state name.
     * @param displayName The display name for the state.
     */
    ReportedPackState(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the enumeration value.
     *
     * @return The display name of the enumeration value.
     */
    public final String getDisplayName() {
        return this.displayName;
    }

    /**
     * Gets the enumeration value.
     *
     * @return The enumeration value.
     */
    public final String getValue() {
        return this.value;
    }
}
