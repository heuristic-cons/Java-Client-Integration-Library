/**
 * -----------------------------------------------------------------------------
 * File=RecoveryRequestAck.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The acknowledgement of a recovery request.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.responses;

import com.reply.solidsoft.nbs.integration.model.HttpStatusCode;
import com.reply.solidsoft.nbs.integration.properties.Resources;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * The acknowledgement of a recovery request.
 */
public class RecoveryRequestAck {

    /**
     * Initializes a new instance of the RecoveryRequestAck class.
     *
     * @param operationCode The operation Code.
     * @param warning The warning text.
     * @param uprc The Unique Pack Return Code.
     * @param eta The estimated time of arrival.
     * @param expires The expires time and date.
     * @param location The location of the results resource.
     * @param statusCode The status Code.
     */
    public RecoveryRequestAck(String operationCode, String warning, String uprc, Instant eta, Instant expires, URI location, int statusCode) {
        this.operationCode = operationCode;
        this.warning = warning;
        this.uprc = uprc;
        this.eta = eta;
        this.expires = expires;
        this.location = location;
        this.statusCode = statusCode;
    }

    /**
     * Initializes a new instance of the RecoveryRequestAck class.
     *
     * @param validationResponse The validation response.
     */
    public RecoveryRequestAck(LocalValidationResponse validationResponse) {
        this.operationCode = String.valueOf(validationResponse.getOperationCode());
        this.warning = validationResponse.getWarning();
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
     * The estimated time of arrival of the results.
     */
    private Instant eta = null;

    /**
     * Gets the estimated time of arrival of the results.
     *
     * @return The estimated time of arrival of the results.
     */
    public final Instant getEta() {
        return eta;
    }

    /**
     * The result expiry date and time.
     */
    private Instant expires = null;

    /**
     * Gets the result expiry date and time.
     *
     * @return The result expiry date and time.
     */
    public final Instant getExpires() {
        return expires;
    }

    /**
     * The location header value.
     */
    private URI location;

    /**
     * Gets the location header value;
     *
     * @return The location header value;
     */
    public final URI getLocation() {
        return location;
    }

    /**
     * The operation code of the response.
     */
    private final String operationCode;

    /**
     * Gets the operation code of the response.
     *
     * @return The operation code of the response.
     */
    public final String getOperationCode() {
        return operationCode;
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
     * The reason for the pack status.
     */
    private final String warning;

    /**
     * Gets the reason for the pack status.
     *
     * @return The reason for the pack status.
     */
    public final String getWarning() {
        return warning;
    }

    /**
     * Evaluates the equality of this recovery request result with another
     * result.
     *
     * @param obj The recovery request result to be compared.
     * @return True, if the recovery request results are equal; otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return obj.getClass() == this.getClass() && this.equals((RecoveryRequestAck) obj);
    }

    /**
     * Returns a hash code this this recovery request result.
     *
     * @return A hash code this this bulk request result.
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
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
        String reasonTitle
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getWarning())
                ? ""
                : String.format("%1$s: ", Resources.getWarning());
        String reasonValue
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getWarning())
                ? ""
                : String.format("%1$s; ", this.getWarning().trim());
        String uprcTitle
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getUprc())
                ? ""
                : String.format("%1$s: ", Resources.getUprc());
        String uprcValue
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getUprc())
                ? ""
                : String.format("%1$s; ", this.getUprc());
        String etaTitle
                = this.getEta().equals(Instant.MIN)
                ? ""
                : String.format("%1$s: ", Resources.getEta());
        String etaValue
                = this.getEta().equals(Instant.MIN)
                ? ""
                : String.format("%1$s; ", this.getEta());
        String expiresTitle
                = this.getExpires().equals(Instant.MIN)
                ? ""
                : String.format("%1$s: ", Resources.getExpires());
        String expiresValue
                = this.getExpires().equals(Instant.MIN)
                ? ""
                : String.format("%1$s; ", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault()).format(this.getExpires()));
        String locationTitle
                = (this.getLocation() == null)
                ? ""
                : String.format("%1$s: ", Resources.getLocation());

        String absoluteUrl;
        try {
            absoluteUrl
                    = (this.getLocation() == null)
                    ? ""
                    : this.getLocation().toURL().toString();
        } catch (MalformedURLException | IllegalArgumentException ex) {
            absoluteUrl = this.getLocation().toString();
        }

        String locationValue
                = (this.getLocation() == null)
                ? ""
                : String.format("%1$s; ", absoluteUrl);
        return String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s%8$s%9$s%10$s%11$s%12$s", operationCodeTitle, operationCodeValue, reasonTitle, reasonValue, uprcTitle, uprcValue, etaTitle, etaValue, expiresTitle, expiresValue, locationTitle, locationValue).trim();
    }

    /**
     * Evaluates the equality of this recovery request result with another
     * result.
     *
     * @param other The recovery request result to be compared.
     * @return True, if the recovery request results are equal; otherwise false.
     */
    private boolean equals(RecoveryRequestAck other) {
        return this.toString().equals(other.toString());
    }
}
