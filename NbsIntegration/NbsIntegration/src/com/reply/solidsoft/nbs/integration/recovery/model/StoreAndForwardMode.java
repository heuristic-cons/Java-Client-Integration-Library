/**
 * -----------------------------------------------------------------------------
 * File=StoreAndForwardMode.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An enumeration of pack states.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.recovery.model;

public enum StoreAndForwardMode {
    /**
     * The store and forward mechanism is in automatic mode.
     */
    AUTOMATIC("Automatic", "automatic"),
    /**
     * The store and forward mechanism is in manual mode.
     */
    MANUAL("Manual", "manual"),
    /**
     * The store and forward mechanism is inactive.
     */
    NONE("None", "none");

    private final String value;
    private final String displayName;

    /**
     * Initializes a new instance of the StoreAndForwardMode class.
     */
    StoreAndForwardMode() {
        this.value = "Get";
        this.displayName = "Get";
    }

    /**
     * Initializes a new instance of the StoreAndForwardMode class.
     *
     * @param value The state name.
     */
    StoreAndForwardMode(String value) {
        this.value = value;
        this.displayName = value;
    }

    /**
     * Initializes a new instance of the StoreAndForwardMode class.
     *
     * @param value The state name.
     * @param displayName The display name for the state.
     */
    StoreAndForwardMode(String value, String displayName) {
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
