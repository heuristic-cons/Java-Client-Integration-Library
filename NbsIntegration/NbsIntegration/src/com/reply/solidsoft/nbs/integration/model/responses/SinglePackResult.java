/**
 * -----------------------------------------------------------------------------
 * File=SinglePackResult.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The result of a single pack request.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.responses;

import com.reply.solidsoft.nbs.integration.model.ReportedPackState;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import com.reply.solidsoft.nbs.integration.model.HttpStatusCode;
import com.reply.solidsoft.nbs.integration.model.extensions.ApiResponseExtensions;

/**
 * The result of a single pack request.
 */
public class SinglePackResult {

    /**
     * Initializes a new instance of the SinglePackResult class.
     *
     * @param operationCode The operation Code.
     * @param state The state.
     * @param information The information text.
     * @param warning The warning text.
     * @param uprc The Unique Pack Return Code.
     * @param statusCode The HTTP status Code.
     */
    public SinglePackResult(String operationCode, String state, String information, String warning, String uprc, int statusCode) {
        this.operationCode = operationCode;
        this.state = ApiResponseExtensions.convertToPackState(state);
        this.information = information;
        this.warning = warning;
        this.uprc = uprc;
        this.statusCode = statusCode;
    }

    /**
     * Initializes a new instance of the SinglePackResult class.
     *
     * @param state The pack state.
     */
    protected SinglePackResult(ReportedPackState state) {
        this.state = state;
    }

    /**
     * The reason for the pack status.
     */
    private String uprc;

    /**
     * Gets the reason for the pack status.
     *
     * @return The reason for the pack status.
     */
    public final String getUprc() {
        return uprc;
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
    public final String getInformation() {
        return information;
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
    public final String getOperationCode() {
        return operationCode;
    }

    /**
     * The reported state of the pack.
     */
    private final ReportedPackState state;

    /**
     * Gets the reported state of the pack.
     *
     * @return The reported state of the pack.
     */
    public final ReportedPackState getState() {
        return state;
    }

    /**
     * The HTTP status code.
     */
    private int statusCode = HttpStatusCode.SC_NOTDEFINED;

    /**
     * Gets the HTTP status code;
     *
     * @return The HTTP status code;
     */
    public final int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the reason for the pack status.
     */
    private String warning;

    public final String getWarning() {
        return warning;
    }

    /**
     * Evaluates the equality of this API result with another result.
     *
     * @param obj The API result to be compared.
     * @return True, if the API results are equal; otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return obj.getClass() == this.getClass() && this.equals((SinglePackResult) obj);
    }

    /**
     * Returns a hash code this this verify result.
     *
     * @return A hash code this this verify result.
     */
    @Override
    public int hashCode() {
        return this.getState() != null ? this.getState().getValue().hashCode() : 0;
    }

    /**
     * Returns a string representing the result.
     *
     * @return A representation of the result.
     */
    @Override
    public String toString() {
        String operationCodeTitle
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getOperationCode())
                ? ""
                : String.format("%1$s: ", Resources.getOperationCode());
        String operationCodeValue
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getOperationCode())
                ? "" : String.format("%1$s; ", this.getOperationCode());
        String packStateTitle
                = (this.getState() == ReportedPackState.NONE)
                ? ""
                : String.format("%1$s: ", Resources.getPackState());
        String packStateValue
                = (this.getState() == ReportedPackState.NONE)
                ? ""
                : String.format("%1$s; ", this.getState().getDisplayName());
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
                : String.format("%1$s: ", this.getWarning().trim()))
                : String.format("%1$s: ", this.getInformation().trim());
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

    /**
     * Evaluates the equality of this verify result with another result.
     *
     * @param other The verify result to be compared.
     * @return True, if the verify results are equal; otherwise false.
     */
    private boolean equals(SinglePackResult other) {
        return this.getState().equals(other.getState());
    }
}
