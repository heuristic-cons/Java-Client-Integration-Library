/**
 * -----------------------------------------------------------------------------
 * File=PackIdentifier.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a medicine pack.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

import com.google.gson.annotations.Expose;

/**
 * Represents a medicine pack.
 */
public class PackIdentifier {

    /**
     * Initializes a new instance of the PackIdentifier class.
     */
    public PackIdentifier() {
    }

    /**
     * Initializes a new instance of the PackIdentifier class.
     *
     * @param productCodeScheme The product code scheme.
     * @param productCode The product code.
     * @param serialNumber The serial number.
     * @param batchId The batch identifier.
     * @param expiryDate The expiry date.
     */
    protected PackIdentifier(String productCodeScheme, String productCode, String serialNumber, String batchId, String expiryDate) {
        this.setProductCodeScheme(productCodeScheme);
        this.setProductCode(productCode);
        this.setSerialNumber(serialNumber);
        this.setBatchId(batchId);
        this.setExpiryDate(expiryDate);
    }

    /**
     * The schema of the product code (GTIN or PPN).
     */
    private String productCodeScheme = com.reply.solidsoft.nbs.integration.model.ProductCodeScheme.values()[0].toString().toLowerCase();

    /**
     * Gets the schema of the product code (GTIN or PPN).
     *
     * @return The schema of the product code (GTIN or PPN).
     */
    public final String getProductCodeScheme() {
        return productCodeScheme;
    }

    /**
     * Sets the schema of the product code (GTIN or PPN).
     *
     * @param value The schema of the product code (GTIN or PPN).
     */
    public final void setProductCodeScheme(String value) {
        productCodeScheme = value;
    }

    /**
     * The product code.
     */
    @Expose
    private String productCode;

    /**
     * Gets the product code.
     *
     * @return The product code.
     */
    public final String getProductCode() {
        return productCode;
    }

    /**
     * Sets the product code.
     *
     * @param value The product code.
     */
    public final void setProductCode(String value) {
        productCode = value;
    }

    /**
     * The pack serial number.
     */
    @Expose
    private String serialNumber;

    /**
     * Gets the pack serial number.
     *
     * @return The pack serial number.
     */
    public final String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the pack serial number.
     *
     * @param value The pack serial number.
     */
    public final void setSerialNumber(String value) {
        serialNumber = value;
    }

    /**
     * The batch (lot) identifier.
     */
    @Expose
    private String batchId;

    /**
     * Gets the batch (lot) identifier.
     *
     * @return The batch (lot) identifier.
     */
    public final String getBatchId() {
        return batchId;
    }

    /**
     * Sets the batch (lot) identifier.
     *
     * @param value The batch (lot) identifier.
     */
    public final void setBatchId(String value) {
        batchId = value;
    }

    /**
     * The expiry date.
     */
    @Expose
    private String expiryDate;

    /**
     * Gets the expiry date.
     *
     * @return The expiry date.
     */
    public final String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date.
     *
     * @param value The expiry date.
     */
    public final void setExpiryDate(String value) {
        expiryDate = value;
    }
}
