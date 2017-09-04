/**
 * -----------------------------------------------------------------------------
 * File=ILogEntryPackIdentifier.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a medicine pack.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

/**
 * Represents a medicine pack.
 */
public interface NbsIntegrationLogEntryPackIdentifier {

    /**
     * Gets the schema of the product code (GTIN or PPN).
     *
     * @return The schema of the product code (GTIN or PPN).
     */
    public String getProductCodeScheme();

    /**
     * Sets the schema of the product code (GTIN or PPN).
     *
     * @param value The schema of the product code (GTIN or PPN).
     */
    public void setProductCodeScheme(String value);

    /**
     * Gets the product code.
     *
     * @return The product code.
     */
    public String getProductCode();

    /**
     * Sets the product code.
     *
     * @param value The product code.
     */
    public void setProductCode(String value);

    /**
     * Gets the pack serial number.
     *
     * @return The pack serial number.
     */
    public String getSerialNumber();

    /**
     * Sets the pack serial number.
     *
     * @param value The pack serial number.
     */
    public void setSerialNumber(String value);

    /**
     * Gets the batch (lot) identifier.
     *
     * @return The batch (lot) identifier.
     */
    public String getBatchId();

    /**
     * Sets the batch (lot) identifier.
     *
     * @param value The batch (lot) identifier.
     */
    public void setBatchId(String value);

    /**
     * Gets the expiry date.
     *
     * @return The expiry date.
     */
    public String getExpiryDate();

    /**
     * Sets the expiry date.
     *
     * @param value The expiry date.
     */
    public void setExpiryDate(String value);
}
