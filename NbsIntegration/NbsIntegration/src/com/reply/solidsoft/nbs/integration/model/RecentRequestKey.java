/**
 * -----------------------------------------------------------------------------
 * File=RecentRequestKey.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Cache key for a recent response.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

/**
 * Cache key for a recent request.
 */
public final class RecentRequestKey {

    /**
     * Initializes a new instance of RecentRequestKey.
     */
    public RecentRequestKey() {
    }

    /**
     * Initializes a new instance of RecentRequestKey.
     *
     * @param pack The product pack identifier.
     * @param requestedPackState The requested pack state.
     * @param dataEntryMode The data entry mode.
     * @param language The requested language.
     */
    public RecentRequestKey(PackIdentifier pack, RequestedPackState requestedPackState, DataEntryMode dataEntryMode, String language) {
        this();
        this.setProductCodeScheme(pack.getProductCodeScheme());
        this.setRequestedPackState(requestedPackState.getValue());
        String tempVar = pack.getProductCode();
        this.setProductCode((tempVar != null) ? tempVar : "");
        String tempVar2 = pack.getSerialNumber();
        this.setSerialNumber((tempVar2 != null) ? tempVar2 : "");
        String tempVar3 = pack.getBatchId();
        this.setBatchId((tempVar3 != null) ? tempVar3 : "");
        String tempVar4 = pack.getExpiryDate();
        this.setExpiryDate((tempVar4 != null) ? tempVar4 : "");
        this.setDataEntryMode(dataEntryMode.getValue());
        this.setLanguage((language != null) ? language : "");
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
    public String getProductCodeScheme() {
        return productCodeScheme;
    }

    /**
     * Sets the schema of the product code (GTIN or PPN).
     *
     * @param value The schema of the product code (GTIN or PPN).
     */
    public void setProductCodeScheme(String value) {
        productCodeScheme = value;
    }

    /**
     * The requested language.
     */
    private String requestedPackState;

    /**
     * Gets the requested language.
     *
     * @return The requested language.
     */
    public String getRequestedPackState() {
        return requestedPackState;
    }

    /**
     * Sets the requested language.
     *
     * @param value The requested language.
     */
    public void setRequestedPackState(String value) {
        requestedPackState = value;
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
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the product code.
     *
     * @param value The product code.
     */
    public void setProductCode(String value) {
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
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the pack serial number.
     *
     * @param value The pack serial number.
     */
    public void setSerialNumber(String value) {
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
    public String getBatchId() {
        return batchId;
    }

    /**
     * Sets the batch (lot) identifier.
     *
     * @param value The batch (lot) identifier.
     */
    public void setBatchId(String value) {
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
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date.
     *
     * @param value The expiry date.
     */
    public void setExpiryDate(String value) {
        expiryDate = value;
    }

    /**
     * The data entry mode.
     */
    private String dataEntryMode;

    /**
     * Gets the data entry mode.
     *
     * @return The data entry mode.
     */
    public String getDataEntryMode() {
        return dataEntryMode;
    }

    /**
     * Sets the data entry mode.
     *
     * @param value The data entry mode.
     */
    public void setDataEntryMode(String value) {
        dataEntryMode = value;
    }

    /**
     * The requested language.
     */
    private String language;

    /**
     * Gets the requested language.
     *
     * @return The requested language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the requested language.
     *
     * @param value The requested language.
     */
    public void setLanguage(String value) {
        language = value;
    }

    /**
     * Equality operator.
     *
     * @param key1 The first key value.
     * @param key2 The second key value.
     * @return True, if the keys are equal; otherwise false;
     */
    public static boolean opEquality(RecentRequestKey key1, RecentRequestKey key2) {
        return key1.equals(key2.clone());
    }

    /**
     * Inequality operator.
     *
     * @param key1 The first key value.
     * @param key2 The second key value.
     * @return True, if the keys are not equal; otherwise false;
     */
    public static boolean opInequality(RecentRequestKey key1, RecentRequestKey key2) {
        return !key1.equals(key2.clone());
    }

    /**
     * Tests the equality of a RecentRequestKey value with this key.
     *
     * @param other The RecentRequestKey value to be tested
     * @return True, if the keys are equal; otherwise false.
     */
    public boolean equals(RecentRequestKey other) {
        if (other == null) {
            return false;
        }

        return other.getProductCodeScheme().equals(this.getProductCodeScheme()) && other.getRequestedPackState().equals(this.getRequestedPackState()) && other.getProductCode().equals(this.getProductCode()) && other.getSerialNumber().equals(this.getSerialNumber()) && other.getBatchId().equals(this.getBatchId()) && other.getExpiryDate().equals(this.getExpiryDate()) && other.getDataEntryMode().equals(this.getDataEntryMode()) && other.getLanguage().equals(this.getLanguage());
    }

    /**
     * Tests the equality of an object with this key.
     *
     * @param obj The object to be tested. Must be a RecentRequestKey value.
     * @return True, if the keys are equal; otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        RecentRequestKey objectToCompareWith = (RecentRequestKey) obj;

        return objectToCompareWith.getProductCodeScheme().equals(this.getProductCodeScheme()) && objectToCompareWith.getRequestedPackState().equals(this.getRequestedPackState()) && objectToCompareWith.getProductCode().equals(this.getProductCode()) && objectToCompareWith.getSerialNumber().equals(this.getSerialNumber()) && objectToCompareWith.getBatchId().equals(this.getBatchId()) && objectToCompareWith.getExpiryDate().equals(this.getExpiryDate()) && objectToCompareWith.getDataEntryMode().equals(this.getDataEntryMode()) && objectToCompareWith.getLanguage().equals(this.getLanguage());
    }

    /**
     * Get a hash code for this key value.
     *
     * @return A hash code for this key value.
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Returns a string representation of the value.
     *
     * @return A string representation of the value.
     */
    @Override
    public String toString() {
        return String.format(
                "%1$s:%2$s:%3$s:%4$s:%5$s:%6$s:%7$s:%8$s",
                this.getProductCodeScheme(),
                this.getRequestedPackState(),
                this.getProductCode(),
                this.getSerialNumber(),
                this.getBatchId(),
                this.getExpiryDate(),
                this.getDataEntryMode(),
                this.getLanguage()).trim();
    }

    /**
     * Returns a cloned copy of the request key instance.
     *
     * @return A cloned copy of the request key instance.
     */
    @Override
    public RecentRequestKey clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.toString();
        }
        RecentRequestKey varCopy = new RecentRequestKey();

        varCopy.productCodeScheme = this.productCodeScheme;
        varCopy.requestedPackState = this.requestedPackState;
        varCopy.productCode = this.productCode;
        varCopy.serialNumber = this.serialNumber;
        varCopy.batchId = this.batchId;
        varCopy.expiryDate = this.expiryDate;
        varCopy.dataEntryMode = this.dataEntryMode;
        varCopy.language = this.language;

        return varCopy;
    }
}
