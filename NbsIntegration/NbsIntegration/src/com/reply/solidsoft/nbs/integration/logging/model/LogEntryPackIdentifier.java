/**
 * -----------------------------------------------------------------------------
 * File=LogEntryPackIdentifier.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a medicine pack.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import com.reply.solidsoft.nbs.integration.model.PackIdentifier;

/**
 * Represents a medicine pack.
 */
public class LogEntryPackIdentifier implements NbsIntegrationLogEntryPackIdentifier {

    /**
     * Initializes a new instance of the LogEntryPackIdentifier class.
     */
    public LogEntryPackIdentifier() {
    }

    /**
     * Initializes a new instance of the LogEntryPackIdentifier class.
     *
     * @param pack The pack identifier.
     */
    public LogEntryPackIdentifier(PackIdentifier pack) {
        this.setProductCodeScheme(pack.getProductCodeScheme());
        this.setProductCode(pack.getProductCode());
        this.setSerialNumber(pack.getSerialNumber());
        this.setBatchId(pack.getBatchId());
        this.setExpiryDate(pack.getExpiryDate());
    }

    /**
     * The schema of the product code (GTIN or PPN).
     */
    private String productCodeScheme;

    /**
     * Gets the schema of the product code (GTIN or PPN).
     *
     * @return The schema of the product code (GTIN or PPN).
     */
    @Override
    public final String getProductCodeScheme() {
        return productCodeScheme;
    }

    /**
     * Sets the schema of the product code (GTIN or PPN).
     *
     * @param value The schema of the product code (GTIN or PPN).
     */
    @Override
    public final void setProductCodeScheme(String value) {
        productCodeScheme = value;
    }

    /**
     * The product code.
     */
    private String productCode;

    /**
     * Gets the product code.
     *
     * @return The product code.
     */
    @Override
    public final String getProductCode() {
        return productCode;
    }

    /**
     * Sets the product code.
     *
     * @param value The product code.
     */
    @Override
    public final void setProductCode(String value) {
        productCode = value;
    }

    /**
     * The pack serial number.
     */
    private String serialNumber;

    /**
     * Gets the pack serial number.
     *
     * @return The pack serial number.
     */
    @Override
    public final String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the pack serial number.
     *
     * @param value The pack serial number.
     */
    @Override
    public final void setSerialNumber(String value) {
        serialNumber = value;
    }

    /**
     * The batch (lot) identifier.
     */
    private String batchId;

    /**
     * Gets the batch (lot) identifier.
     *
     * @return The batch (lot) identifier.
     */
    @Override
    public final String getBatchId() {
        return batchId;
    }

    /**
     * Sets the batch (lot) identifier.
     *
     * @param value The batch (lot) identifier.
     */
    @Override
    public final void setBatchId(String value) {
        batchId = value;
    }

    /**
     * The expiry date.
     */
    private String expiryDate;

    /**
     * Gets the expiry date.
     *
     * @return The expiry date.
     */
    @Override
    public final String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date.
     *
     * @param value The expiry date.
     */
    @Override
    public final void setExpiryDate(String value) {
        expiryDate = value;
    }
}
