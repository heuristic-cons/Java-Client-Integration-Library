/**
 * -----------------------------------------------------------------------------
 * File=DataEntryMode.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An enumeration of data entry modes.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

/**
 * An enumeration of pack states.
 */
public enum DataEntryMode {
    MANUAL("Manual", "manual"),
    NON_MANUAL("NonManual", "non-manual");

    /**
     * The data entry mode value.
     */
    private final String value;

    /**
     * The data entry mode display name.
     */
    private final String displayName;

    /**
     * Initializes a new instance of the DataEntryMode class.
     */
    DataEntryMode() {
        this.value = "Active";
        this.displayName = "Active";
    }

    /**
     * Initializes a new instance of the DataEntryMode class.
     *
     * @param value The state name.
     */
    DataEntryMode(String value) {
        this.value = value;
        this.displayName = value;
    }

    /**
     * Initializes a new instance of the DataEntryMode class.
     *
     * @param value The state name.
     * @param displayName The display name for the state.
     */
    DataEntryMode(String value, String displayName) {
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
