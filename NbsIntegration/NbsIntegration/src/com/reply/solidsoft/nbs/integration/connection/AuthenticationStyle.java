/**
 * -----------------------------------------------------------------------------
 * File=AuthenticationStyle.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 *  Enum for specifying the authentication style of a client
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.connection;

/**
 * Enum for specifying the authentication style of a client.
 */
public enum AuthenticationStyle {
    /**
     * HTTP basic authentication.
     */
    BASIC_AUTHENTICATION,
    /**
     * Post values in body.
     */
    //     post values in body
    POST_VALUES,
    /**
     * Custom authentication
     */
    CUSTOM;

    /**
     * Get the integer value corresponding to the enumerated value.
     *
     * @return The integer value corresponding to the enumerated value.
     */
    public int getValue() {
        return this.ordinal();
    }

    /**
     * Resolve an integer value to the corresponding enumerated value.
     *
     * @param value An integer value.
     * @return The enumerated value corresponding to the integer value.
     */
    public static AuthenticationStyle forValue(int value) {
        return values()[value];
    }
}
