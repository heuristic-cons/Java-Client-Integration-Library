// --------------------------------------------------------------------------------------------------------------------
// <copyright file="ApiConnection.cs" company="Solidsoft Reply Ltd.">
//   (c) 2017 Solidsoft Reply Ltd.
// </copyright>
// <summary>
//   Represents API connection configuration.
// </summary>
// --------------------------------------------------------------------------------------------------------------------
package com.reply.solidsoft.nbs.integration.sample.console;

/**
 * Represents API connection configuration.
 */
public class ApiConnection {

    /**
     * The base URL for the national system.
     */
    public String baseUrl;

    /**
     * Gets the base URL for the national system.
     *
     * @return The base URL for the national system.
     */
    public String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * Sets the base URL for the national system.
     *
     * @param value The base URL for the national system.
     */
    public void setBaseUrl(String value) {
        this.baseUrl = value;
    }

    /**
     * The URL for the identity server system.
     */
    public String identityServerUrl;

    /**
     * Gets the URL for the identity server system.
     *
     * @return The URL for the identity server system.
     */
    public String getIdentityServerUrl() {
        return this.identityServerUrl;
    }

    /**
     * Sets the URL for the identity server system.
     *
     * @param value The URL for the identity server system.
     */
    public void setIdentityServerUrl(String value) {
        this.identityServerUrl = value;
    }
}
