/**
 * -----------------------------------------------------------------------------
 * File=SinglePackResponseDb.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The single pack response.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice.model;

import com.reply.solidsoft.nbs.integration.model.responses.SinglePackResponse;

/**
 * The single pack response.
 */
public class SinglePackResponseDb extends SinglePackResponse {

    /**
     * Initializes a new instance of the SinglePackResponseDb class.
     */
    public SinglePackResponseDb() {
    }

    /**
     * Initializes a new instance of the SinglePackResponseDb class.
     *
     * @param timeStamp The timestamp of the enclosing
     * RecoverySinglePackResponse instance.
     * @param singlePackResponse The base single pack response.
     */
    public SinglePackResponseDb(long timeStamp, SinglePackResponse singlePackResponse) {
        this.setTimeStamp(timeStamp);
        this.setEta(singlePackResponse.getEta());
        this.setInformation(singlePackResponse.getInformation());
        this.setOperationCode(singlePackResponse.getOperationCode());
        this.setState(singlePackResponse.getState());
        this.setUprc(singlePackResponse.getUprc());
        this.setWarning(singlePackResponse.getWarning());
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
}
