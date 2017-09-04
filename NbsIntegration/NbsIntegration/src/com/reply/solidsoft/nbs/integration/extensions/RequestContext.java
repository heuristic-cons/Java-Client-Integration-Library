/**
 * -----------------------------------------------------------------------------
 * File=RequestContext.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Contextual data for HTTP requests.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.extensions;

import com.reply.solidsoft.nbs.integration.model.requests.RequestType;
import com.reply.solidsoft.nbs.integration.recovery.model.DeferredRequest;
import org.apache.http.HttpResponse;
import com.reply.solidsoft.nbs.integration.logging.model.NbsIntegrationLogEntry;

/**
 * Contextual data for HTTP requests.
 */
public class RequestContext {

    /**
     * The request type.
     */
    private RequestType requestType;

    /**
     * Gets the request type.
     *
     * @return The request type.
     */
    public RequestType getRequestType() {
        return this.requestType;
    }

    /**
     * Sets the request type.
     *
     * @param value The request type.
     */
    public void setRequestType(RequestType value) {
        this.requestType = value;
    }

    /**
     * The deferred request.
     */
    private DeferredRequest deferredRequest;

    /**
     * Gets the deferred request.
     *
     * @return The deferred request.
     */
    public DeferredRequest getDeferredRequest() {
        return this.deferredRequest;
    }

    /**
     * Sets the deferred request.
     *
     * @param value The deferred request.
     */
    public void setDeferredRequest(DeferredRequest value) {
        this.deferredRequest = value;
    }

    /**
     * The HTTP response.
     */
    private HttpResponse response;

    /**
     * Gets the HTTP response.
     *
     * @return The HTTP response.
     */
    public HttpResponse getResponse() {
        return this.response;
    }

    /**
     * Sets the HTTP response.
     *
     * @param value The HTTP response.
     */
    public void setResponse(HttpResponse value) {
        this.response = value;
    }

    /**
     * The log message.
     */
    private String message;

    /**
     * Gets the log message.
     *
     * @return The log message.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the log message.
     *
     * @param value The log message.
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * The log entry.
     */
    private NbsIntegrationLogEntry logEntry;

    /**
     * Gets the log entry.
     *
     * @return The log entry.
     */
    public NbsIntegrationLogEntry getLogEntry() {
        return this.logEntry;
    }

    /**
     * Sets the log entry.
     *
     * @param value The log entry.
     */
    public void setLogEntry(NbsIntegrationLogEntry value) {
        this.logEntry = value;
    }

    /**
     * The log entry.
     */
    private int remainingAttempts;

    /**
     * Gets the log entry.
     *
     * @return The log entry.
     */
    public int getRemainingAttempts() {
        return this.remainingAttempts;
    }

    /**
     * Sets the log entry.
     *
     * @param value The log entry.
     */
    public void setRemainingAttempts(int value) {
        this.remainingAttempts = value;
    }
}
