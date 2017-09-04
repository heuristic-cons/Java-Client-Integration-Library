/**
 * -----------------------------------------------------------------------------
 * File=PackIdentifierDb.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a medicine pack.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice.model;

import com.reply.solidsoft.nbs.integration.model.PackIdentifier;

/**
 * Represents a medicine pack.
 */
public class PackIdentifierDb extends PackIdentifier {

    /**
     * Initializes a new instance of the PackIdentifierDb class.
     */
    public PackIdentifierDb() {
    }

    /**
     * Initializes a new instance of the PackIdentifierDb class.
     *
     * @param timeStamp The timestamp of the enclosing
     * RecoverySinglePackResponse instance.
     * @param packIdentifier The base pack identifier.
     */
    public PackIdentifierDb(long timeStamp, PackIdentifier packIdentifier) {
        super(packIdentifier.getProductCodeScheme(), packIdentifier.getProductCode(), packIdentifier.getSerialNumber(), packIdentifier.getBatchId(), packIdentifier.getExpiryDate());
        this.setTimeStamp(timeStamp);
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
     * Gets or sets the timestamp value.
     *
     * @param value The timestamp value.
     */
    public final void setTimeStamp(long value) {
        this.timeStamp = value;
    }
}
