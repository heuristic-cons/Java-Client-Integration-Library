/**
 * -----------------------------------------------------------------------------
 * File=LogEntryRequest.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents an API request, as represented in a log entry.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;

/**
 * An API request, as represented in a log entry.
 */
public class LogEntryRequest implements NbsIntegrationLogEntryRequest {

    /**
     * Initializes a new instance of the LogEntryRequest class.
     */
    public LogEntryRequest() {
    }

    /**
     * Initializes a new instance of the LogEntryRequest class.
     *
     * @param request The deferred request.
     */
    public LogEntryRequest(DeferredRequest request) {
        this.setBody(request.getBody());
        this.setIsManual(request.getIsManual());
        this.setLanguage(request.getLanguage());
        this.setRequestedState(request.getRequestedState());
        this.setUri(request.getUri());
        this.setVerb(request.getVerb());
    }

    /**
     * The content of the body.
     */
    private String body;

    /**
     * Gets the content of the body.
     *
     * @return The content of the body.
     */
    @Override
    public final String getBody() {
        return body;
    }

    /**
     * Sets the content of the body.
     *
     * @param value The content of the body.
     */
    @Override
    public final void setBody(String value) {
        body = value;
    }

    /**
     * The requested pack state.
     */
    private String requestedState;

    /**
     * Gets the requested pack state.
     *
     * @return The requested pack state.
     */
    @Override
    public final String getRequestedState() {
        return requestedState;
    }

    /**
     * Sets the requested pack state.
     *
     * @param value The requested pack state.
     */
    @Override
    public final void setRequestedState(String value) {
        requestedState = value;
    }

    /**
     * A value indicating whether the data entry is manual or non-manual.
     */
    private boolean isManual;

    /**
     * Gets a value indicating whether the data entry is manual or non-manual.
     *
     * @return A value indicating whether the data entry is manual or
     * non-manual.
     */
    @Override
    public final boolean getIsManual() {
        return isManual;
    }

    /**
     * Sets a value indicating whether the data entry is manual or non-manual.
     *
     * @param value a value indicating whether the data entry is manual or
     * non-manual.
     */
    @Override
    public final void setIsManual(boolean value) {
        isManual = value;
    }

    /**
     * The required language for response messages.
     */
    private String language;

    /**
     * Gets the required language for response messages.
     *
     * @return The required language for response messages.
     */
    @Override
    public final String getLanguage() {
        return language;
    }

    /**
     * Sets the required language for response messages.
     *
     * @param value The required language for response messages.
     */
    @Override
    public final void setLanguage(String value) {
        language = value;
    }

    /**
     * The URI for the requested resource.
     */
    private String uri;

    /**
     * Gets the URI for the requested resource.
     *
     * @return The URI for the requested resource.
     */
    @Override
    public final String getUri() {
        return uri;
    }

    /**
     * Sets the URI for the requested resource.
     *
     * @param value The URI for the requested resource.
     */
    @Override
    public final void setUri(String value) {
        uri = value;
    }

    /**
     * The type of HTTP verb.
     */
    private String verb;

    /**
     * Gets the type of HTTP verb.
     *
     * @return The type of HTTP verb.
     */
    @Override
    public final String getVerb() {
        return verb;
    }

    /**
     * Sets the type of HTTP verb.
     *
     * @param value The type of HTTP verb.
     */
    @Override
    public final void setVerb(String value) {
        verb = value;
    }
}
