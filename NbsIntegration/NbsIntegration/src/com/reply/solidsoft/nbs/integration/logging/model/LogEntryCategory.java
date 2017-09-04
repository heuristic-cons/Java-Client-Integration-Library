/**
 * -----------------------------------------------------------------------------
 * File=LogEntryCategory.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Category of the log entry.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

/**
 * Category of the log entry.
 */
public enum LogEntryCategory {
    /**
     * The log record is associated with a single pack request.
     */
    SINGLE_PACK_REQUEST(0),
    /**
     * The log record is associated with a bulk pack request.
     */
    BULK_PACK_REQUEST(1),
    /**
     * The log record is associated with a bulk pack results request.
     */
    BULK_PACK_RESULTS_REQUEST(2),
    /**
     * The log record is associated with a recovery request.
     */
    RECOVERY_REQUEST(3),
    /**
     * The log record is associated with a recovery results request.
     */
    RECOVERY_RESULTS_REQUEST(4),
    /**
     * The log record is associated with a request for product data.
     */
    PRODUCT_DATA_REQUEST(5),
    /**
     * The log request is associated with a report request.
     */
    REPORT_REQUEST(6);

    public static final int SIZE = java.lang.Integer.SIZE;

    /**
     * The integer value corresponding to the log entry category.
     */
    private final int intValue;

    /**
     * A set of mappings between integer values and log entry categories.
     */
    private static volatile java.util.HashMap<Integer, LogEntryCategory> mappings;

    /**
     * Gets the set of mappings between integer values and log entry categories.
     *
     * @return A set of mappings between integer values and log entry
     * categories.
     */
    private static java.util.HashMap<Integer, LogEntryCategory> getMappings() {
        java.util.HashMap<Integer, LogEntryCategory> categoryMappings = LogEntryCategory.mappings;

        if (categoryMappings == null) {
            synchronized (LogEntryCategory.class) {
                categoryMappings = LogEntryCategory.mappings;
                if (categoryMappings == null) {
                    LogEntryCategory.mappings = categoryMappings = new java.util.HashMap<>();
                }
            }
        }
        return categoryMappings;
    }

    /**
     * Initializes a new instance of the LogEntryCategory enumeration.
     *
     * @param value an integer value for the log entry category.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    private LogEntryCategory(int value) {
        intValue = value;
        LogEntryCategory put;
        put = getMappings().put(value, this);
    }

    /**
     * Gets the integer value of this log entry category.
     *
     * @return The integer value of this log entry category.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * Returns a log entry category for the given integer value.
     *
     * @param value The integer value.
     * @return A log entry category for the given integer value.
     */
    public static LogEntryCategory forValue(int value) {
        return getMappings().get(value);
    }
}
