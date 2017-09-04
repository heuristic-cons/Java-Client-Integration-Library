/**
 * -----------------------------------------------------------------------------
 * File=RequestType.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Types of request to the National System.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.requests;

/**
 * Types of request to the National System.
 */
public enum RequestType {
    /**
     * API VERIFY.
     */
    VERIFY(11),
    /**
     * API SUPPLY.
     */
    SUPPLY(12),
    /**
     * API DECOMMISSION.
     */
    DECOMMISSION(13),
    /**
     * API REACTIVATE.
     */
    REACTIVATE(14),
    /**
     * API SUBMIT_BULK_REQUEST
     */
    SUBMIT_BULK_REQUEST(21),
    /**
     * API GET_BULK_RESULT.
     */
    GET_BULK_RESULT(22),
    /**
     * API SUBMIT_RECOVERY_REQUEST.
     */
    SUBMIT_RECOVERY_REQUEST(31),
    /**
     * API GET_RECOVERY_RESULT.
     */
    GET_RECOVERY_RESULT(32);

    /**
     * The number of bits used to represent an {@code int} value in two's
     * complement binary form.
     */
    public static final int SIZE = java.lang.Integer.SIZE;

    /**
     * The integer value.
     */
    private final int intValue;

    /**
     * A hash map of request type mappings.
     */
    private static volatile java.util.HashMap<Integer, RequestType> mappings;

    /**
     * Gets the dictionary of request type mappings.
     *
     * @return The dictionary of request type mappings.
     */
    private static java.util.HashMap<Integer, RequestType> getMappings() {
        java.util.HashMap<Integer, RequestType> requestTypeMappings;
        requestTypeMappings = RequestType.mappings;
        if (requestTypeMappings == null) {
            synchronized (RequestType.class) {
                requestTypeMappings = RequestType.mappings;
                if (requestTypeMappings == null) {
                    RequestType.mappings = requestTypeMappings = new java.util.HashMap<>();
                }
            }
        }
        return requestTypeMappings;
    }

    /**
     * Initializes a new instance of the RequestType enumeration.
     *
     * @param value
     */
    @SuppressWarnings("LeakingThisInConstructor")
    private RequestType(int value) {
        intValue = value;
        RequestType put;
        put = getMappings().put(value, this);
    }

    /**
     * Gets the value of the request type.
     *
     * @return The value of the request type.
     */
    public int getValue() {
        return intValue;
    }

    /**
     * Returns a request type for a give value.
     *
     * @param value The integer value of the request type.
     * @return A request type
     */
    public static RequestType forValue(int value) {
        return getMappings().get(value);
    }
}
