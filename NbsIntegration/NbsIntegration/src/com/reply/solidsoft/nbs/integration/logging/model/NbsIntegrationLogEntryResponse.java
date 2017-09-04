/**
 * -----------------------------------------------------------------------------
 * File=ILogEntryResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents an API request, as represented in a log entry.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import java.time.Instant;
import java.net.URI;

/**
 * Represents an API request, as represented in a log entry.
 */
public interface NbsIntegrationLogEntryResponse {

    /**
     * Gets the Unique Pack Return Code.
     *
     * @return The Unique Pack Return Code.
     */
    public String getUprc();

    /**
     * Sets the Unique Pack Return Code.
     *
     * @param value The Unique Pack Return Code.
     */
    public void setUprc(String value);

    /**
     * Gets the estimated time of arrival of the results.
     *
     * @return The estimated time of arrival of the results.
     */
    public Instant getEta();

    /**
     * Sets the estimated time of arrival of the results.
     *
     * @param value The estimated time of arrival of the results.
     */
    public void setEta(Instant value);

    /**
     * Gets the result expiry date and time.
     *
     * @return The result expiry date and time.
     */
    public Instant getExpires();

    /**
     * Sets the result expiry date and time.
     *
     * @param value The result expiry date and time.
     */
    public void setExpires(Instant value);

    /**
     * Gets the location header value;
     *
     * @return The location header value;
     */
    public URI getLocation();

    /**
     * Sets the location header value;
     *
     * @param value The location header value;
     */
    public void setLocation(URI value);

    /**
     * Gets the reason for the pack status.
     *
     * @return The reason for the pack status.
     */
    public String getInformation();

    /**
     * Sets the reason for the pack status.
     *
     * @param value The reason for the pack status.
     */
    public void setInformation(String value);

    /**
     * Gets the operation code of the response.
     *
     * @return The operation code of the response.
     */
    public String getOperationCode();

    /**
     * Sets the operation code of the response.
     *
     * @param value The operation code of the response.
     */
    public void setOperationCode(String value);

    /**
     * Gets the packs.
     *
     * @return The packs.
     */
    public NbsIntegrationLogEntryBulkSinglePackResponse[] getPacks();

    /**
     * Sets the packs.
     *
     * @param value The packs.
     */
    public void setPacks(NbsIntegrationLogEntryBulkSinglePackResponse[] value);

    /**
     * Gets the state of the pack.
     *
     * @return The state of the pack.
     */
    public String getState();

    /**
     * Sets the state of the pack.
     *
     * @param value The state of the pack.
     */
    public void setState(String value);

    /**
     * Gets the HTTP status code;
     *
     * @return The HTTP status code;
     */
    public int getStatusCode();

    /**
     * Sets the HTTP status code;
     *
     * @param value The HTTP status code;
     */
    public void setStatusCode(int value);

    /**
     * Gets the reason for the pack status.
     *
     * @return The reason for the pack status.
     */
    public String getWarning();

    /**
     * Sets the reason for the pack status.
     *
     * @param value The reason for the pack status.
     */
    public void setWarning(String value);
}
