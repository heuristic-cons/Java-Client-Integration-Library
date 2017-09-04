/**
 * -----------------------------------------------------------------------------
 * File=ApiConnection.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The connection to the national system.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.connection;

/**
 * The connection to the national system.
 */
public class ApiConnection {

    /**
     * Initializes a new instance of the ApiConnection class.
     *
     * @param baseUrl The base address.
     * @param identityServerUrl The identity server address.
     */
    public ApiConnection(String baseUrl, String identityServerUrl) {
        this.setBaseUrl(baseUrl);
        this.setIdentityServerUrl(identityServerUrl);
    }

    /**
     * The base URL for the national system.
     */
    private String baseUrl;

    /**
     * Gets the base URL for the national system.
     *
     * @return The base URL for the national system.
     */
    public final String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets the base URL for the national system.
     *
     * @param value The base URL for the national system.
     */
    public final void setBaseUrl(String value) {
        baseUrl = value;
    }

    /**
     * The URL for the identity server system.
     */
    private String identityServerUri;

    /**
     * Gets the URL for the identity server system.
     *
     * @return The URL for the identity server system.
     */
    public final String getIdentityServerUrl() {
        return identityServerUri;
    }

    /**
     * Sets the URL for the identity server system.
     *
     * @param value The URL for the identity server system.
     */
    public final void setIdentityServerUrl(String value) {
        identityServerUri = value;
    }
}
