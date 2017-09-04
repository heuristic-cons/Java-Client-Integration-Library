/**
 * -----------------------------------------------------------------------------
 * File=ILogEntrySinglePackResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * The single pack response.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

import java.time.Instant;

/**
 * The single pack response.
 */
public interface NbsIntegrationLogEntrySinglePackResponse {

    /**
     * Gets the Unique Pack Return Code.
     *
     * @return The Unique Pack Return Code.
     */
    public String getUprc();

    /**
     * Sets the Unique Pack Return Code.
     *
     * @param value The Unique Pack Return Code.
     */
    public void setUprc(String value);

    /**
     * Gets the estimated time of arrival of a bulk result.
     *
     * @return The estimated time of arrival of a bulk result.
     */
    public Instant getEta();

    /**
     * Sets the estimated time of arrival of a bulk result.
     *
     * @param value The estimated time of arrival of a bulk result.
     */
    public void setEta(Instant value);

    /**
     * Gets the Information display text.
     *
     * @return The Information display text.
     */
    public String getInformation();

    /**
     * Sets the Information display text.
     *
     * @param value The Information display text.
     */
    public void setInformation(String value);

    /**
     * Gets the operation code.
     *
     * @return The operation code.
     */
    public int getOperationCode();

    /**
     * Sets the operation code.
     *
     * @param value The operation code.
     */
    public void setOperationCode(int value);

    /**
     * Gets the pack state.
     *
     * @return The pack state.
     */
    public String getState();

    /**
     * Sets the pack state.
     *
     * @param value The pack state.
     */
    public void setState(String value);

    /**
     * Gets the Warning display text.
     *
     * @return The Warning display text.
     */
    public String getWarning();

    /**
     * Sets the Warning display text.
     *
     * @param value The Warning display text.
     */
    public void setWarning(String value);
}
