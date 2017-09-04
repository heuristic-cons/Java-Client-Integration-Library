/**
 * -----------------------------------------------------------------------------
 * File=ApiResult.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The result of a verification request.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.responses;

import com.reply.solidsoft.nbs.integration.model.HttpStatusCode;
import com.reply.solidsoft.nbs.integration.model.ReportedPackState;
import com.reply.solidsoft.nbs.integration.model.requests.RequestType;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import java.net.URI;
import java.time.Instant;
import java.util.concurrent.Callable;
import org.apache.http.HttpResponse;

/**
 * The result of a verification request.
 */
public class ApiResult {

    /**
     * Initializes a new instance of the ApiResult class.
     */
    public ApiResult() {
    }

    /**
     * Initializes a new instance of the ApiResult class.
     *
     * @param state The reported pack state.
     * @param requestType The API request type.
     * @param response The HTTP response from the National System.
     * @param noContent Indicates whether there is no content in the response
     * returned fro the National System.
     */
    public ApiResult(ReportedPackState state, RequestType requestType, HttpResponse response, boolean noContent) {
        this(state, requestType, response, noContent, "");
    }

    /**
     * Initializes a new instance of the ApiResult class.
     *
     * @param state The reported pack state.
     * @param requestType The API request type.
     * @param response The HTTP response from the National System.
     */
    public ApiResult(ReportedPackState state, RequestType requestType, HttpResponse response) {
        this(state, requestType, response, false, "");
    }

    /**
     * Initializes a new instance of the ApiResult class.
     *
     * @param state The reported pack state.
     * @param requestType The API request type.
     */
    public ApiResult(ReportedPackState state, RequestType requestType) {
        this(state, requestType, null, false, "");
    }

    /**
     * Initializes a new instance of the ApiResult class.
     *
     * @param state The reported pack state.
     * @param requestType The API request type.
     * @param noContent Indicates whether there is no content in the response
     * returned fro the National System.
     * @param warningMessage Optional warningMessage to be included in warning.
     */
    public ApiResult(ReportedPackState state, RequestType requestType, boolean noContent, String warningMessage) {
        this(state, requestType, null, noContent, warningMessage);
    }

    /**
     * Initializes a new instance of the ApiResult class.
     *
     * @param state The reported pack state.
     * @param requestType The API request type.
     * @param response The HTTP response from the National System.
     * @param noContent Indicates whether there is no content in the response
     * returned fro the National System.
     * @param warningMessage Optional warningMessage to be included in warning.
     */
    public ApiResult(ReportedPackState state, RequestType requestType, HttpResponse response, boolean noContent, String warningMessage) {
        this.setState(state == null ? null : state.getValue());

        if (null == response) {
            this.setOperationCode(String.format("0%1$s20000", requestType.getValue()));
            this.setWarning(String.format("%1$s  %2$s", Resources.getApiClient_NoResponseWarning(), warningMessage).trim());
            return;
        }

        Callable<Integer> getHttpStatusId;
        getHttpStatusId = () -> response.getStatusLine().getStatusCode() == HttpStatusCode.SC_OK
                ? 1
                : response.getStatusLine().getStatusCode() == HttpStatusCode.SC_ACCEPTED
                ? 2
                : response.getStatusLine().getStatusCode() == HttpStatusCode.SC_FORBIDDEN
                ? 3
                : response.getStatusLine().getStatusCode() == HttpStatusCode.SC_NOT_FOUND
                ? 4
                : response.getStatusLine().getStatusCode() == HttpStatusCode.SC_CONFLICT
                ? 5
                : response.getStatusLine().getStatusCode() == HttpStatusCode.SC_UNPROCESSABLE_ENTITY
                ? 6
                : 0;

        this.setStatusCode(response.getStatusLine().getStatusCode());

        if (!noContent) {
            return;
        }

        try {
            this.setOperationCode(String.format("%1$s%2$s20000", getHttpStatusId.call(), requestType.getValue()));
        } catch (Exception ex) {
            this.setOperationCode(String.format("%1$s%2$s20000", HttpStatusCode.SC_NOTDEFINED, requestType.getValue()));

        }

        this.setWarning(String.format("%1$s  %2$s", Resources.getApiClient_NoContentWarning(), warningMessage).trim());
    }

    /**
     * Initializes a new instance of the ApiResult class.
     *
     * @param validationResponse The validation response.
     *
     */
    public ApiResult(LocalValidationResponse validationResponse) {
        this.setState(ReportedPackState.NONE.getValue());
        this.setOperationCode(String.valueOf(validationResponse.getOperationCode()));
        this.setWarning(validationResponse.getWarning());
    }

    /**
     * The Unique Pack Return Code.
     */
    private String alertId;

    /**
     * Gets the Unique Pack Return Code.
     *
     * @return The Unique Pack Return Code.
     */
    public final String getUprc() {
        return alertId;
    }

    /**
     * Sets the Unique Pack Return Code.
     *
     * @param value The Unique Pack Return Code.
     */
    public final void setUprc(String value) {
        alertId = value;
    }

    /**
     * The estimated time of arrival of the results.
     */
    private String eta = Instant.MIN.toString();

    /**
     * Gets the estimated time of arrival of the results.
     *
     * @return The estimated time of arrival of the results.
     */
    public final Instant getEta() {
        return Instant.parse(this.eta);
    }

    /**
     * Sets the estimated time of arrival of the results.
     *
     * @param value The estimated time of arrival of the results.
     */
    public final void setEta(Instant value) {
        eta = value.toString();
    }

    /**
     * The result expiry date and time.
     */
    private String expires = Instant.MIN.toString();

    /**
     * Gets the result expiry date and time.
     *
     * @return The result expiry date and time.
     */
    public final Instant getExpires() {
        return Instant.parse(expires);
    }

    /**
     * Sets the result expiry date and time.
     *
     * @param value The result expiry date and time.
     */
    public final void setExpires(Instant value) {
        expires = value.toString();
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
    public final URI getLocation() {
        return location;
    }

    /**
     * Sets the location header value.
     *
     * @param value The location header value.
     */
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
    public final String getInformation() {
        return information;
    }

    /**
     * Sets the reason for the pack status.
     *
     * @param value The reason for the pack status.
     */
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
    public final String getOperationCode() {
        return operationCode;
    }

    /**
     * Sets the operation code of the response.
     *
     * @param value The operation code of the response.
     */
    public final void setOperationCode(String value) {
        operationCode = value;
    }

    /**
     * The packs.
     */
    private BulkSinglePackResponse[] packs;

    /**
     * Gets the packs.
     *
     * @return The packs.
     */
    public final BulkSinglePackResponse[] getPacks() {
        return packs;
    }

    /**
     * Sets the packs.
     *
     * @param value The packs.
     */
    public final void setPacks(BulkSinglePackResponse[] value) {
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
    public final String getState() {
        return state;
    }

    /**
     * Sets the state of the pack.
     *
     * @param value The state of the pack.
     */
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
    public final int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the HTTP status code.
     *
     * @param value The HTTP status code.
     */
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
    public final String getWarning() {
        return warning;
    }

    /**
     * Sets the reason for the pack status.
     *
     * @param value The reason for the pack status.
     */
    public final void setWarning(String value) {
        warning = value;
    }

    /**
     * Returns a bulk request acknowledgment.
     *
     * @return A API result for the 'Not found' pack status.
     */
    public final BulkRequestAck bulkRequestAck() {
        return new BulkRequestAck(this.getOperationCode(), this.getWarning(), this.getUprc(), this.getEta(), this.getExpires(), this.getLocation(), this.getStatusCode());
    }

    /**
     * Returns a bulk request acknowledgment.
     *
     * @return A API result for the 'Not found' pack status.
     */
    public final BulkRequestResults bulkRequestResults() {
        return new BulkRequestResults(this.getOperationCode(), this.getInformation(), this.getWarning(), this.getPacks(), this.getEta(), this.getExpires(), this.getLocation(), this.getStatusCode());
    }

    /**
     * Evaluates the equality of this API result with another result.
     *
     * @param obj The API result to be compared.
     * @return True, if the verification results are equal; otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return obj.getClass() == this.getClass() && this.equals((ApiResult) obj);
    }

    /**
     * Returns a hash code this this API result.
     *
     * @return A hash code this this API result.
     */
    @Override
    public int hashCode() {
        return this.getState() != null ? this.getState().hashCode() : 0;
    }

    /**
     * Returns a bulk request acknowledgment.
     *
     * @return A API result for the 'Not found' pack status.
     */
    public final RecoveryRequestAck recoveryRequestAck() {
        return new RecoveryRequestAck(this.getOperationCode(), this.getWarning(), this.getUprc(), this.getEta(), this.getExpires(), this.getLocation(), this.getStatusCode());
    }

    /**
     * Returns a bulk request acknowledgment.
     *
     * @return A API result for the 'Not found' pack status.
     */
    public final RecoveryRequestResults recoveryRequestResults() {
        return new RecoveryRequestResults(this.getOperationCode(), this.getInformation(), this.getWarning(), this.getPacks(), this.getEta(), this.getExpires(), this.getLocation(), this.getStatusCode());
    }

    /**
     * Returns a single pack result.
     *
     * @return A API result for the 'Not found' pack status.
     */
    public final SinglePackResult singlePackResult() {
        return new SinglePackResult(this.getOperationCode(), this.getState(), this.getInformation(), this.getWarning(), this.getUprc(), this.getStatusCode());
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
                : String.format("%1$s:", Resources.getInformation());
        String reasonValue
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getInformation())
                ? (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getWarning())
                ? ""
                : String.format("%1$s: ", this.getWarning().trim()))
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

    /**
     * Evaluates the equality of this API result with another result.
     *
     * @param other The API result to be compared.
     * @return True, if the verification results are equal; otherwise false.
     */
    private boolean equals(ApiResult other) {
        return this.getState().equals(other.getState());
    }
}
