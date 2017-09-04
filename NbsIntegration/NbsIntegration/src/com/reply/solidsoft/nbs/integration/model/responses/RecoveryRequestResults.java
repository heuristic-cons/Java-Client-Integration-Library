/**
 * -----------------------------------------------------------------------------
 * File=RecoveryRequestResults.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The results of a recovery request request.
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
 * The results of a recovery request request.
 */
public class RecoveryRequestResults {

    /**
     * Initializes a new instance of the RecoveryRequestResults class.
     *
     * @param operationCode The operation Code.
     * @param information The information text.
     * @param warning The warning text.
     * @param packs The packs.
     * @param eta The estimated time of arrival.
     * @param expires The expires time and date.
     * @param location The location of the results resource.
     * @param statusCode The status Code.
     */
    public RecoveryRequestResults(String operationCode, String information, String warning, BulkSinglePackResponse[] packs, Instant eta, Instant expires, URI location, int statusCode) {
        this.operationCode = operationCode;
        this.information = information;
        this.warning = warning;
        this.packs = packs;
        this.eta = eta;
        this.expires = expires;
        this.location = location;
        this.statusCode = statusCode;
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
     * The reason for the pack status.
     */
    private final String information;

    /**
     * Gets the reason for the pack status.
     *
     * @return The reason for the pack status.
     */
    public final String getInformation() {
        return information;
    }

    /**
     * The location header value.
     */
    private final URI location;

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
     * The packs.
     */
    private final BulkSinglePackResponse[] packs;

    /**
     * Gets the packs.
     *
     * @return The packs.
     */
    public final BulkSinglePackResponse[] getPacks() {
        return packs;
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
     * Evaluates the equality of this recovery request results with another
     * result.
     *
     * @param obj The recovery request results to be compared.
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

        return obj.getClass() == this.getClass() && this.equals((RecoveryRequestResults) obj);
    }

    /**
     * Returns a hash code this this recovery request results.
     *
     * @return A hash code this this recovery request results.
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Returns a string representing the bulk results.
     *
     * @return A representation of the bulk results.
     */
    @Override
    public String toString() {
        String numberOfPackTitle
                = (this.getPacks() == null)
                ? ""
                : String.format("%1$s: ", Resources.getNoOfPacks());
        String numberOfPackValue
                = (this.getPacks() == null)
                ? ""
                : String.format("%1$s; ", this.getPacks().length);
        String operationCodeTitle
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getOperationCode())
                ? ""
                : String.format("%1$s: ", Resources.getOperationCode());
        String operationCodeValue
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getOperationCode())
                ? ""
                : String.format("%1$s; ", this.getOperationCode());
        String reasonTitle
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getInformation())
                ? (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getWarning())
                ? ""
                : String.format("%1$s; ", Resources.getWarning()))
                : String.format("%1$s; ", Resources.getInformation());
        String reasonValue
                = com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getInformation())
                ? (com.reply.solidsoft.nbs.integration.extensions.StringExtensions.isNullOrWhiteSpace(this.getWarning())
                ? ""
                : String.format("%1$s; ", this.getWarning().trim()))
                : String.format("%1$s; ", this.getInformation().trim());
        String etaTitle
                = this.getEta().equals(Instant.MIN)
                ? ""
                : String.format("%1$s: ", Resources.getEta());
        String etaValue
                = this.getEta().equals(Instant.MIN)
                ? ""
                : String.format("%1$s; ", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault()).format(this.getEta()));
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
                = this.getLocation() == null
                ? ""
                : String.format("%1$s; ", absoluteUrl);
        return String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s%8$s%9$s%10$s%11$s%12$s", numberOfPackTitle, numberOfPackValue, operationCodeTitle, operationCodeValue, reasonTitle, reasonValue, etaTitle, etaValue, expiresTitle, expiresValue, locationTitle, locationValue).trim();
    }

    /**
     * Evaluates the equality of this recovery request results with another bulk
     * results.
     *
     * @param other The recovery request results to be compared.
     * @return True, if the recovery request results are equal; otherwise false.
     */
    private boolean equals(RecoveryRequestResults other) {
        return this.toString().equals(other.toString());
    }
}
