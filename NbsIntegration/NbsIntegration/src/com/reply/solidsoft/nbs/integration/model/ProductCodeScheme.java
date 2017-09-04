/**
 * -----------------------------------------------------------------------------
 * File=ProductCodeScheme.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Product code schema type enumeration.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

/**
 * Product code schema type enumeration.
 */
public enum ProductCodeScheme {
    /**
     * GS1 scheme.
     */
    GS1,
    /**
     * IFA scheme.
     */
    IFA,
    /**
     * UNKNOWN schema
     */
    UNKNOWN;

    /**
     * The number of bits used to represent the product code scheme value in
     * two's complement binary form.
     */
    public static final int SIZE = java.lang.Integer.SIZE;

    /**
     * Gets the product code scheme value.
     *
     * @return The product code scheme value.
     */
    public int getValue() {
        return this.ordinal();
    }

    /**
     * Returns a product code scheme that corresponds to a value.
     *
     * @param value The product code scheme value.
     * @return A product code scheme.
     */
    public static ProductCodeScheme forValue(int value) {
        return values()[value];
    }
}
