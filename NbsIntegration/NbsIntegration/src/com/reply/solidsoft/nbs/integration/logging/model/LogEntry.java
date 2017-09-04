/**
 * -----------------------------------------------------------------------------
 * File=LogEntry.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A log entry record.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import java.time.Instant;

/**
 * A log entry record.
 */
public class LogEntry implements NbsIntegrationLogEntry {

    /**
     * Lock object for atomic reads and writes to the timestamp.
     */
    private static final Object TIMESTAMP_LOCK = new Object();

    /**
     * Timestamp counter.
     */
    private static long latestTimeStamp;

    /**
     * Timestamp value.
     */
    private long timeStamp;

    /**
     * Initializes a new instance of the LogEntry class.
     */
    public LogEntry() {
    }

    /**
     * Initializes a new instance of the LogEntry class.
     *
     * @param currentTimeStamp The current time stamp. Defaults to 0.
     */
    public LogEntry(long currentTimeStamp) {
        this.initialize(currentTimeStamp);
    }

    /**
     * The type of the log entry. May be information, warning or error.
     */
    private LogEntryType entryType = LogEntryType.values()[0];

    /**
     * Gets the type of the log entry. May be information, warning or error.
     *
     * @return The type of the log entry.
     */
    @Override
    public final LogEntryType getEntryType() {
        return entryType;
    }

    /**
     * Sets the type of the log entry. May be information, warning or error.
     *
     * @param value The type of the log entry. May be information, warning or
     * error.
     */
    @Override
    public final void setEntryType(LogEntryType value) {
        entryType = value;
    }

    /**
     * The log entry category.
     */
    private LogEntryCategory entryCategory = LogEntryCategory.values()[0];

    /**
     * Gets the log entry category.
     *
     * @return The log entry category.
     */
    @Override
    public final LogEntryCategory getEntryCategory() {
        return entryCategory;
    }

    /**
     * Sets the log entry category.
     *
     * @param value The log entry category.
     */
    @Override
    public final void setEntryCategory(LogEntryCategory value) {
        entryCategory = value;
    }

    /**
     * The pack identifier.
     */
    private NbsIntegrationLogEntryPackIdentifier pack;

    /**
     * Gets the pack identifier.
     *
     * @return The pack identifier.
     */
    @Override
    public final NbsIntegrationLogEntryPackIdentifier getPack() {
        return pack;
    }

    /**
     * Sets the pack identifier.
     *
     * @param value The pack identifier.
     */
    @Override
    public final void setPack(NbsIntegrationLogEntryPackIdentifier value) {
        pack = value;
    }

    /**
     * The date or time of the log entry.
     */
    private Instant time = Instant.MIN;

    /**
     * Sets the date or time of the log entry.
     *
     * @return The date or time of the log entry.
     */
    @Override
    public final Instant getTime() {
        return time;
    }

    /**
     * Sets the date or time of the log entry.
     *
     * @param value The date or time of the log entry.
     */
    @Override
    public final void setTime(Instant value) {
        time = value;
    }

    /**
     * The API request associated with the log entry.
     */
    private NbsIntegrationLogEntryRequest request;

    /**
     * Gets the API request associated with the log entry.
     *
     * @return The API request associated with the log entry.
     */
    @Override
    public final NbsIntegrationLogEntryRequest getRequest() {
        return request;
    }

    /**
     * Sets the API request associated with the log entry.
     *
     * @param value The API request associated with the log entry.
     */
    @Override
    public final void setRequest(NbsIntegrationLogEntryRequest value) {
        request = value;
    }

    /**
     * The API request associated with the log entry.
     */
    private NbsIntegrationLogEntryResponse response;

    /**
     * Gets the API request associated with the log entry.
     *
     * @return The API request associated with the log entry.
     */
    @Override
    public final NbsIntegrationLogEntryResponse getResponse() {
        return response;
    }

    /**
     * Sets the API request associated with the log entry.
     *
     * @param value The API request associated with the log entry.
     */
    @Override
    public final void setResponse(NbsIntegrationLogEntryResponse value) {
        response = value;
    }

    /**
     * The name of the current user.
     */
    private String user;

    /**
     * Gets the name of the current user.
     *
     * @return The name of the current user.
     */
    @Override
    public final String getUser() {
        return user;
    }

    /**
     * Sets the name of the current user.
     *
     * @param value The name of the current user.
     */
    @Override
    public final void setUser(String value) {
        user = value;
    }

    /**
     * The Unique pack Return Code.
     */
    private String uprc;

    /**
     * Gets the Unique pack Return Code.
     *
     * @return The Unique pack Return Code.
     */
    @Override
    public final String getUprc() {
        return uprc;
    }

    /**
     * Sets the Unique pack Return Code.
     *
     * @param value The Unique pack Return Code.
     */
    @Override
    public final void setUprc(String value) {
        uprc = value;
    }

    /**
     * The message for the log record.
     */
    private String message;

    /**
     * Gets the message for the log record.
     *
     * @return The message for the log record.
     */
    @Override
    public final String getMessage() {
        return message;
    }

    /**
     * Sets the message for the log record.
     *
     * @param value The message for the log record
     */
    @Override
    public final void setMessage(String value) {
        message = value;
    }

    /**
     * The severity of the log record.
     */
    private int severity;

    /**
     * Gets the severity of the log record.
     *
     * @return The severity of the log record.
     */
    @Override
    public final int getSeverity() {
        return severity;
    }

    /**
     * Sets the severity of the log record.
     *
     * @param value The severity of the log record.
     */
    @Override
    public final void setSeverity(int value) {
        severity = value;
    }

    /**
     * The unique identifier for this log record.
     */
    private String id;

    /**
     * Gets the unique identifier for this log record.
     *
     * @return The unique identifier for this log record.
     */
    @Override
    public final String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this log record.
     *
     * @param value The unique identifier for this log record.
     */
    @Override
    public final void setId(String value) {
        id = value;
    }

    /**
     * Gets the timestamp value for this deferred record.
     */
    @Override
    public final long getTimeStamp() {
        synchronized (TIMESTAMP_LOCK) {
            return this.timeStamp;
        }
    }

    /**
     * Sets the timestamp value for this deferred record.
     *
     * @param value The timestamp value for this deferred record.
     */
    public final void setTimeStamp(long value) {
        synchronized (TIMESTAMP_LOCK) {
            this.timeStamp = value;
        }
    }

    /**
     * Initializes a new instance of the LogEntry class.
     */
    private void initialize() {
        initialize(0);
    }

    /**
     * Initializes a new instance of the LogEntry class.
     *
     * @param currentTimeStamp The current time stamp. Defaults to 0.
     */
    private void initialize(long currentTimeStamp) {
        synchronized (TIMESTAMP_LOCK) {
            if (latestTimeStamp == Long.MAX_VALUE) {
                latestTimeStamp = 0;
            }

            if (currentTimeStamp > latestTimeStamp) {
                latestTimeStamp = currentTimeStamp;
            }

            latestTimeStamp++;
            this.setTimeStamp(latestTimeStamp);
        }
    }
}
