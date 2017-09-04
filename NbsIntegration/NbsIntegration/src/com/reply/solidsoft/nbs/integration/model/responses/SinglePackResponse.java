/**
 * -----------------------------------------------------------------------------
 * File=SinglePackResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The single pack response.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.responses;

import com.reply.solidsoft.nbs.integration.properties.Resources;
import java.time.Instant;

/**
 * The single pack response.
 */
public class SinglePackResponse {

    /**
     * The Unique Pack Return Code.
     */
    private String uprc;

    /**
     * Gets the Unique Pack Return Code.
     *
     * @return The Unique Pack Return Code.
     */
    public final String getUprc() {
        return uprc;
    }

    /**
     * Sets the Unique Pack Return Code.
     *
     * @param value The Unique Pack Return Code.
     */
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
    public final Instant getEta() {
        return eta;
    }

    /**
     * Sets the estimated time of arrival of a bulk result.
     *
     * @param value The estimated time of arrival of a bulk result.
     */
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
    public final String getInformation() {
        return information;
    }

    /**
     * Sets the information display text.
     *
     * @param value The information display text.
     */
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
    public final int getOperationCode() {
        return operationCode;
    }

    /**
     * Sets the operation code.
     *
     * @param value The operation code.
     */
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
    public final String getState() {
        return state;
    }

    /**
     * Sets the pack state.
     *
     * @param value The pack state.
     */
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
    public final String getWarning() {
        return warning;
    }

    /**
     * Sets the warning display text.
     *
     * @param value The warning display text.
     */
    public final void setWarning(String value) {
        warning = value;
    }

    /**
     * Returns a string representing the result.
     *
     * @return A representation of the result.
     */
    @Override
    public String toString() {
        String operationCodeTitle
                = (this.getOperationCode() == 0)
                ? ""
                : String.format("%1$s: ", Resources.getOperationCode());
        String operationCodeValue
                = (this.getOperationCode() == 0)
                ? ""
                : String.format("%1$s; ", this.getOperationCode());
        String packStateTitle
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getState())
                ? ""
                : String.format("%1$s: ", Resources.getPackState());
        String packStateValue
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getState())
                ? ""
                : String.format("%1$s; ", this.getState());
        String reasonTitle
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getInformation())
                ? (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getWarning())
                ? ""
                : String.format("%1$s: ", Resources.getWarning()))
                : String.format("%1$s: ", Resources.getInformation());
        String reasonValue
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getInformation())
                ? (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getWarning())
                ? ""
                : String.format("%1$s; ", this.getWarning().trim()))
                : String.format("%1$s; ", this.getInformation().trim());
        String uprcTitle
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getUprc())
                ? ""
                : String.format("%1$s: ", Resources.getUprc());
        String uprcValue
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getUprc())
                ? ""
                : String.format("%1$s; ", this.getUprc());
        return String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s%8$s", operationCodeTitle, operationCodeValue, packStateTitle, packStateValue, reasonTitle, reasonValue, uprcTitle, uprcValue).trim();
    }
}
