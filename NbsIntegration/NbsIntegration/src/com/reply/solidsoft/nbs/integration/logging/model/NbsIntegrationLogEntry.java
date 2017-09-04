/**
 * -----------------------------------------------------------------------------
 * File=ILogEntry.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A client for the NBS API.
 * The client uses a specific set of client credentials.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import java.time.Instant;
import com.reply.solidsoft.nbs.integration.data.model.DataRecord;

/**
 * Represents a log entry.
 */
public interface NbsIntegrationLogEntry extends DataRecord {

    /**
     * Gets the type of the log entry. May be information, warning or error.
     *
     * @return The type of the log entry. May be information, warning or error.
     */
    public LogEntryType getEntryType();

    /**
     * Sets the type of the log entry. May be information, warning or error.
     *
     * @param value The type of the log entry. May be information, warning or
     * error.
     */
    public void setEntryType(LogEntryType value);

    /**
     * Gets the category of the log entry.
     *
     * @return The category of the log entry.
     */
    public LogEntryCategory getEntryCategory();

    /**
     * Sets the category of the log entry.
     *
     * @param value The category of the log entry.
     */
    public void setEntryCategory(LogEntryCategory value);

    /**
     * Gets the pack identifier.
     *
     * @return The pack identifier.
     */
    public NbsIntegrationLogEntryPackIdentifier getPack();

    /**
     * Sets the pack identifier.
     *
     * @param value The pack identifier.
     */
    public void setPack(NbsIntegrationLogEntryPackIdentifier value);

    /**
     * Gets the date or time of the log entry.
     *
     * @return The date or time of the log entry.
     */
    public Instant getTime();

    /**
     * Sets the date or time of the log entry.
     *
     * @param value The date or time of the log entry.
     */
    public void setTime(Instant value);

    /**
     * Gets the API request associated with the log entry.
     *
     * @return The API request associated with the log entry.
     */
    public NbsIntegrationLogEntryRequest getRequest();

    /**
     * Sets the API request associated with the log entry.
     *
     * @param value The API request associated with the log entry.
     */
    public void setRequest(NbsIntegrationLogEntryRequest value);

    /**
     * Gets the API request associated with the log entry.
     *
     * @return The API request associated with the log entry.
     */
    public NbsIntegrationLogEntryResponse getResponse();

    /**
     * Sets the API request associated with the log entry.
     *
     * @param value The API request associated with the log entry.
     */
    public void setResponse(NbsIntegrationLogEntryResponse value);

    /**
     * Gets the name of the current user or claims principle.
     *
     * @return The name of the current user or claims principle.
     */
    public String getUser();

    /**
     * Sets the name of the current user or claims principle.
     *
     * @param value The name of the current user or claims principle.
     */
    public void setUser(String value);

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
     * Gets the message for the log record.
     *
     * @return The message for the log record.
     */
    public String getMessage();

    /**
     * Sets the message for the log record.
     *
     * @param value The message for the log record.
     */
    public void setMessage(String value);

    /**
     * Gets the severity of a log entry for an error state.
     *
     * @return The severity of a log entry for an error state.
     */
    public int getSeverity();

    /**
     * Sets the severity of a log entry for an error state.
     *
     * @param value The severity of a log entry for an error state.
     */
    public void setSeverity(int value);

    /**
     * Gets the unique identifier for this log record.
     *
     * @return The unique identifier for this log record.
     */
    public String getId();

    /**
     * Sets the unique identifier for this log record.
     *
     * @param value The unique identifier for this log record.
     */
    public void setId(String value);
}
