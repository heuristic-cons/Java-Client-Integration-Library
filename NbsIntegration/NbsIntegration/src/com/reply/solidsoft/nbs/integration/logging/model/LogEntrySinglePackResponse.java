/**
 * -----------------------------------------------------------------------------
 * File=LogEntrySinglePackResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The single pack response.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import com.reply.solidsoft.nbs.integration.model.responses.SinglePackResponse;
import java.time.Instant;

/**
 * The single pack response.
 */
public class LogEntrySinglePackResponse implements NbsIntegrationLogEntrySinglePackResponse {

    /**
     * Initializes a new instance of the LogEntrySinglePackResponse class.
     */
    public LogEntrySinglePackResponse() {
    }

    /**
     * Initializes a new instance of the LogEntrySinglePackResponse class.
     *
     * @param response The response.
     *
     */
    public LogEntrySinglePackResponse(SinglePackResponse response) {
        this.setState(response.getState());
        this.setEta(response.getEta());
        this.setInformation(response.getInformation());
        this.setOperationCode(response.getOperationCode());
        this.setUprc(response.getUprc());
        this.setWarning(response.getWarning());
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
     * The estimated time of arrival of a bulk result.
     */
    private Instant eta = null;

    /**
     * Gets the estimated time of arrival of a bulk result.
     *
     * @return The estimated time of arrival of a bulk result.
     */
    @Override
    public final Instant getEta() {
        return eta;
    }

    /**
     * Sets the estimated time of arrival of a bulk result.
     *
     * @param value The estimated time of arrival of a bulk result.
     */
    @Override
    public final void setEta(Instant value) {
        eta = value;
    }

    /**
     * The information display text.
     */
    private String information;

    /**
     * Gets the information display text.
     *
     * @return The information display text.
     */
    @Override
    public final String getInformation() {
        return information;
    }

    /**
     * Sets the information display text.
     *
     * @param value The information display text.
     */
    @Override
    public final void setInformation(String value) {
        information = value;
    }

    /**
     * The operation code.
     */
    private int operationCode;

    /**
     * Gets the operation code.
     *
     * @return The operation code.
     */
    @Override
    public final int getOperationCode() {
        return operationCode;
    }

    /**
     * Sets the operation code.
     *
     * @param value The operation code.
     */
    @Override
    public final void setOperationCode(int value) {
        operationCode = value;
    }

    /**
     * The pack state.
     */
    private String state;

    /**
     * Gets the pack state.
     *
     * @return The pack state.
     */
    @Override
    public final String getState() {
        return state;
    }

    /**
     * Sets the pack state.
     *
     * @param value The pack state.
     */
    @Override
    public final void setState(String value) {
        state = value;
    }

    /**
     * The warning display text.
     */
    private String warning;

    /**
     * Gets the warning display text.
     *
     * @return The warning display text.
     */
    @Override
    public final String getWarning() {
        return warning;
    }

    /**
     * Sets the warning display text.
     *
     * @param value The warning display text.
     */
    @Override
    public final void setWarning(String value) {
        warning = value;
    }
}
