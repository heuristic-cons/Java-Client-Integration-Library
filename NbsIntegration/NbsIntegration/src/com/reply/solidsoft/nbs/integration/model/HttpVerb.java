/**
 * -----------------------------------------------------------------------------
 * File=HttpVerb.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An enumeration of HTTP verbs.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

/**
 * An enumeration of HTTP verbs.
 */
public enum HttpVerb {

    /**
     * The 'GET' HTTP verb.
     */
    GET("Get", "get"),
    /**
     * The 'PATCH' HTTP verb.
     */
    PATCH("Patch", "patch"),
    /**
     * The 'POST' HTTP verb.
     */
    POST("Post", "post"),
    /**
     * The 'PUT' HTTP verb.
     */
    PUT("Put", "put");

    /**
     * The HTTP verb value.
     */
    private final String value;

    /**
     * The HTTP verb display name.
     */
    private final String displayName;

    /**
     * Initializes a new instance of the HttpVerb class.
     */
    HttpVerb() {
        this.value = "Get";
        this.displayName = "Get";
    }

    /**
     * Initializes a new instance of the HttpVerb class.
     *
     * @param value The state name.
     */
    HttpVerb(String value) {
        this.value = value;
        this.displayName = value;
    }

    /**
     * Initializes a new instance of the HttpVerb class.
     *
     * @param value The state name.
     * @param displayName The display name for the state.
     */
    HttpVerb(String value, String displayName) {
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
