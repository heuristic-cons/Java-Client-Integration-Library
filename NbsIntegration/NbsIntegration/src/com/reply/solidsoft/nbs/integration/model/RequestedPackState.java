/**
 * -----------------------------------------------------------------------------
 * File=RequestedPackState.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An enumeration of pack states.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

import java.util.HashMap;
import java.util.Map;

public enum RequestedPackState {
    /**
     * The 'Active' pack state.
     */
    ACTIVE("Active", "Active"),
    /**
     * The 'Destroyed' pack state.
     */
    DESTROYED("Destroyed", "Destroyed"),
    /**
     * The 'Exported' pack state.
     */
    EXPORTED("Exported", "Exported"),
    /**
     * The 'Free Sample' pack state.
     */
    FREE_SAMPLE("FreeSample", "Free Sample"),
    /**
     * The 'Locked' pack state.
     */
    LOCKED("Locked", "Locked"),
    /**
     * The 'Sample' pack state.
     */
    SAMPLE("Sample", "Sample"),
    /**
     * The 'Stolen' pack state.
     */
    STOLEN("Stolen", "Stolen"),
    /**
     * The 'Supplied' pack state.
     *
     * ACTIVE
     */
    SUPPLIED("Supplied", "Supplied");

    private static final Map<String, RequestedPackState> lookup = new HashMap();

    static {
        //Create reverse lookup hash map 
        for (RequestedPackState state : RequestedPackState.values()) {
            lookup.put(state.getValue(), state);
        }
    }

    /**
     * The enumeration value.
     */
    private final String value;

    /**
     * The enumeration display name.
     */
    private final String displayName;

    /**
     * Initializes a new instance of the RequestedPackState class.
     */
    RequestedPackState() {
        this.value = "Get";
        this.displayName = "Get";
    }

    /**
     * Initializes a new instance of the RequestedPackState class.
     *
     * @param value The state name.
     */
    RequestedPackState(String value) {
        this.value = value;
        this.displayName = value;
    }

    /**
     * Initializes a new instance of the RequestedPackState class.
     *
     * @param value The state name.
     * @param displayName The display name for the state.
     */
    RequestedPackState(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    /**
     * Gets a requested state pack based on its value.
     *
     * @param value The string value.
     * @return A requested state pack.
     */
    public static RequestedPackState get(String value) {
        //the reverse lookup by simply getting 
        //the value from the lookup HsahMap. 
        return lookup.get(value);
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
