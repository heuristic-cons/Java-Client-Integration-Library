/**
 * -----------------------------------------------------------------------------
 * File=LogEntryResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents an API request, as represented in a log entry.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import com.reply.solidsoft.nbs.integration.model.HttpStatusCode;
import com.reply.solidsoft.nbs.integration.model.responses.ApiResult;
import java.net.URI;
import java.time.Instant;

/**
 * An API request, as represented in a log entry.
 */
public class LogEntryResponse implements NbsIntegrationLogEntryResponse {

    /**
     * Initializes a new instance of the LogEntryResponse class.
     */
    public LogEntryResponse() {
    }

    /**
     * Initializes a new instance of the LogEntryResponse class.
     *
     * @param result The API result.
     */
    public LogEntryResponse(ApiResult result) {
        this.setStatusCode(result.getStatusCode());
        this.setUprc(result.getUprc());
        this.setEta(result.getEta());
        this.setExpires(result.getExpires());
        this.setInformation(result.getInformation());
        this.setLocation(result.getLocation());
        this.setOperationCode(result.getOperationCode());

        if (result.getPacks() != null) {
            this.setPacks(new NbsIntegrationLogEntryBulkSinglePackResponse[result.getPacks().length]);

            for (int idx = 0; idx < result.getPacks().length; idx++) {
                this.getPacks()[idx] = new LogEntryBulkSinglePackResponse(result.getPacks()[idx]);
            }
        }

        this.setState(result.getState());
        this.setWarning(result.getWarning());
    }

    /**
     * The Unique Pack Return Code.
     */
    private String uprc;

    /**
     * Gets the Unique Pack Return Code.
     *
     * @return The Unique Pack Return Code.
     */
    @Override
    public final String getUprc() {
        return uprc;
    }

    /**
     * Sets the Unique Pack Return Code.
     *
     * @param value The Unique Pack Return Code.
     */
    @Override
    public final void setUprc(String value) {
        uprc = value;
    }

    /**
     * The estimated time of arrival of the results.
     */
    private Instant eta = Instant.MIN;

    /**
     * Gets the estimated time of arrival of the results.
     *
     * @return The estimated time of arrival of the results.
     */
    @Override
    public final Instant getEta() {
        return eta;
    }

    /**
     * Sets the estimated time of arrival of the results.
     *
     * @param value The estimated time of arrival of the results.
     */
    @Override
    public final void setEta(Instant value) {
        eta = value;
    }

    /**
     * The result expiry date and time.
     */
    private Instant expires = Instant.MIN;

    /**
     * Gets the result expiry date and time.
     *
     * @return The result expiry date and time.
     */
    @Override
    public final Instant getExpires() {
        return expires;
    }

    /**
     * Sets the result expiry date and time.
     *
     * @param value The result expiry date and time.
     */
    @Override
    public final void setExpires(Instant value) {
        expires = value;
    }

    /**
     * The location header value.
     */
    private URI location;

    /**
     * Gets the location header value.
     *
     * @return The location header value.
     */
    @Override
    public final URI getLocation() {
        return location;
    }

    /**
     * Sets the location header value.
     *
     * @param value The location header value.
     */
    @Override
    public final void setLocation(URI value) {
        location = value;
    }

    /**
     * The reason for the pack status.
     */
    private String information;

    /**
     * Gets the reason for the pack status.
     *
     * @return The reason for the pack status.
     */
    @Override
    public final String getInformation() {
        return information;
    }

    /**
     * Sets the reason for the pack status.
     *
     * @param value The reason for the pack status.
     */
    @Override
    public final void setInformation(String value) {
        information = value;
    }

    /**
     * The operation code of the response.
     */
    private String operationCode;

    /**
     * Gets the operation code of the response.
     *
     * @return The operation code of the response.
     */
    @Override
    public final String getOperationCode() {
        return operationCode;
    }

    /**
     * Sets the operation code of the response.
     *
     * @param value The operation code of the response.
     */
    @Override
    public final void setOperationCode(String value) {
        operationCode = value;
    }

    /**
     * The packs.
     */
    private NbsIntegrationLogEntryBulkSinglePackResponse[] packs;

    /**
     * Gets the packs.
     *
     * @return The packs.
     */
    @Override
    public final NbsIntegrationLogEntryBulkSinglePackResponse[] getPacks() {
        return packs;
    }

    /**
     * Sets the packs.
     *
     * @param value The packs.
     */
    @Override
    public final void setPacks(NbsIntegrationLogEntryBulkSinglePackResponse[] value) {
        packs = value;
    }

    /**
     * The state of the pack.
     */
    private String state;

    /**
     * Gets the state of the pack.
     *
     * @return The state of the pack.
     */
    @Override
    public final String getState() {
        return state;
    }

    /**
     * Sets the state of the pack.
     *
     * @param value The state of the pack.
     */
    @Override
    public final void setState(String value) {
        state = value;
    }

    /**
     * The HTTP status code.
     */
    private int statusCode = HttpStatusCode.SC_NOTDEFINED;

    /**
     * Gets the HTTP status code.
     *
     * @return The HTTP status code.
     */
    @Override
    public final int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the HTTP status code;
     *
     * @param value The HTTP status code.
     */
    @Override
    public final void setStatusCode(int value) {
        statusCode = value;
    }

    /**
     * The reason for the pack status.
     */
    private String warning;

    /**
     * Gets the reason for the pack status.
     *
     * @return The reason for the pack status.
     */
    @Override
    public final String getWarning() {
        return warning;
    }

    /**
     * Sets the reason for the pack status.
     *
     * @param value The reason for the pack status.
     */
    @Override
    public final void setWarning(String value) {
        warning = value;
    }
}
