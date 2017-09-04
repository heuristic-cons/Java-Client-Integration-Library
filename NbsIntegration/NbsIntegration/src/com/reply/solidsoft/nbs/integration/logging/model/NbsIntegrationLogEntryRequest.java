/**
 * -----------------------------------------------------------------------------
 * File=ILogEntryRequest.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents an API request, as represented in a log entry.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

/**
 * Represents an API request, as represented in a log entry.
 */
public interface NbsIntegrationLogEntryRequest {

    /**
     * Gets the content of the body.
     *
     * @return The content of the body.
     */
    public String getBody();

    /**
     * Sets the content of the body.
     *
     * @param value The content of the body.
     */
    public void setBody(String value);

    /**
     * Gets the requested pack state.
     *
     * @return The requested pack state.
     */
    public String getRequestedState();

    /**
     * Sets the requested pack state.
     *
     * @param value The requested pack state.
     */
    public void setRequestedState(String value);

    /**
     * Gets a value indicating whether the data entry is manual or non-manual.
     *
     * @return A value indicating whether the data entry is manual or
     * non-manual.
     */
    public boolean getIsManual();

    /**
     * Sets a value indicating whether the data entry is manual or non-manual.
     *
     * @param value A value indicating whether the data entry is manual or
     * non-manual.
     */
    public void setIsManual(boolean value);

    /**
     * Gets the required language for response messages.
     *
     * @return The required language for response messages.
     */
    public String getLanguage();

    /**
     * Sets the required language for response messages.
     *
     * @param value The required language for response messages.
     */
    public void setLanguage(String value);

    /**
     * Gets the URI for the requested resource.
     *
     * @return The URI for the requested resource.
     */
    public String getUri();

    /**
     * Sets the URI for the requested resource.
     *
     * @param value The URI for the requested resource.
     */
    public void setUri(String value);

    /**
     * Gets the type of HTTP verb.
     *
     * @return The type of HTTP verb.
     */
    public String getVerb();

    /**
     * Sets the type of HTTP verb.
     *
     * @param value The type of HTTP verb.
     */
    public void setVerb(String value);
}
