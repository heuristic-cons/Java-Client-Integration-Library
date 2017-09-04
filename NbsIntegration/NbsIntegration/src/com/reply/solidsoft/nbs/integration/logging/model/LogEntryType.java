/**
 * -----------------------------------------------------------------------------
 * File=LogEntryType.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Entries can be information, warnings, or errors.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

/**
 * Type of log entry. Entries can be information, warnings, or errors.
 */
public enum LogEntryType {
    /**
     * The log record represents information about an event or state.
     */
    INFORMATION(0),
    /**
     * The log record represents a warning concerning some event or state.
     */
    WARNING(1),
    /**
     * The log record represents an error or exception and the resulting state.
     */
    ERROR(2);

    /**
     * The number of bits used to represent an log entry type value in two's
     * complement binary form.
     */
    public static final int SIZE = java.lang.Integer.SIZE;

    /**
     * The integer value of the log entry type.
     */
    private final int intValue;

    /**
     * A set of mappings from integer values to log entry types.
     */
    private static volatile java.util.HashMap<Integer, LogEntryType> mappings;

    /**
     * Gets a set of mappings from integer values to log entry types.
     *
     * @return A set of mappings from integer values to log entry types.
     */
    private static java.util.HashMap<Integer, LogEntryType> getMappings() {
        java.util.HashMap<Integer, LogEntryType> logEntryTypeMappings = LogEntryType.mappings;

        if (logEntryTypeMappings == null) {
            synchronized (LogEntryType.class) {
                logEntryTypeMappings = LogEntryType.mappings;
                if (logEntryTypeMappings == null) {
                    LogEntryType.mappings = logEntryTypeMappings = new java.util.HashMap<>();
                }
            }
        }
        return logEntryTypeMappings;
    }

    /**
     * Initializes a new instance of the LogEntryType enumeration.
     *
     * @param value The integer value of a log entry type.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    private LogEntryType(int value) {
        intValue = value;
        LogEntryType put;
        put = getMappings().put(value, this);
    }

    /**
     * Gets the integer value of a log entry type.
     *
     * @return The integer value of a log entry type.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * Returns a log entry type corresponding to an integer value.
     *
     * @param value An integer value.
     * @return A log entry type corresponding to an integer value.
     */
    public static LogEntryType forValue(int value) {
        return getMappings().get(value);
    }
}
