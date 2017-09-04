/**
 * -----------------------------------------------------------------------------
 * File=InstantDb.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents an instantaneous point on the time-line.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.loggingservice.model;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;

/**
 * Represents an instantaneous point on the time-line.
 */
public class InstantDb implements TemporalAccessor {

    /**
     * The instant that is represented by this record.
     */
    private Instant instant;

    /**
     * Initializes a new instance of the InstantDb class.
     */
    public InstantDb() {
    }

    /**
     * Initializes a new instance of the InstantDb class.
     *
     * @param timeStamp The timestamp of the log entry..
     * @param instant The instant that will be stored in this record.
     */
    public InstantDb(long timeStamp, Instant instant) {
        this.instant = instant;
        this.nano = instant.getNano();
        this.epochSecond = instant.getEpochSecond();
        this.timeStamp = timeStamp;
    }

    /**
     * Checks if the specified field is supported.
     *
     * @param field the field to check, null returns false
     * @return true if this date-time can be queried for the field, false if not
     */
    @Override
    public boolean isSupported(TemporalField field) {
        return (field == ChronoField.INSTANT_SECONDS || field == ChronoField.NANO_OF_SECOND);
    }

    /**
     * Gets the value of the specified field as a {@code long}.
     *
     * @param field the field to get, not null
     * @return the value for the field
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public long getLong(TemporalField field) {
        if (field == ChronoField.INSTANT_SECONDS)
        {
            return this.epochSecond;
        }
        else if (field == ChronoField.NANO_OF_SECOND)
        {
            return this.nano;
        }
        
        return 0;
    }

    /**
     * The number of nanoseconds, later along the time-line, from the start of
     * the second.
     */
    private int nano;

    /**
     * Gets the number of nanoseconds, later along the time-line, from the start
     * of the second.
     *
     * @return The nanoseconds within the second, always positive, never exceeds
     * 999,999,999.
     */
    public final int getNano() {
        return this.nano;
    }

    /**
     * Sets the number of nanoseconds, later along the time-line, from the start
     * of the second.
     *
     * @param value The nanoseconds within the second, always positive, never
     * exceeds 999,999,999.
     */
    public final void setNano(int value) {
        this.nano = value;
        this.instant = Instant.from(this);
    }

    /**
     * The number of seconds from the Java epoch of 1970-01-01T00:00:00Z.
     */
    private long epochSecond;

    /**
     * Gets the number of seconds from the Java epoch of 1970-01-01T00:00:00Z.
     *
     * @return The seconds from the epoch of 1970-01-01T00:00:00Z
     */
    public final long getEpochSecond() {
        return this.epochSecond;
    }

    /**
     * Sets the number of seconds from the Java epoch of 1970-01-01T00:00:00Z.
     *
     * @param value The seconds from the epoch of 1970-01-01T00:00:00Z
     */
    public final void setEpochSecond(long value) {
        this.epochSecond = value;
        this.instant = Instant.from(this);
    }

    /**
     * The timestamp value.
     */
    private long timeStamp;

    /**
     * Gets the timestamp value.
     *
     * @return The timestamp value.
     */
    public final long getTimeStamp() {
        return this.timeStamp;
    }

    /**
     * Sets the timestamp value.
     *
     * @param value The timestamp value.
     */
    public final void setTimeStamp(long value) {
        this.timeStamp = value;
    }
    
    /**
     * Gets the instant represented by this record.
     * 
     * @return The instant represented by this record.
     */
    public Instant getInstant()
    {
        this.instant = Instant.from(this);
        return this.instant;
    }
}
