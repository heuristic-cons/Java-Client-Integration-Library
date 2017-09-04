/**
 * -----------------------------------------------------------------------------
 * File=ILogEntryBulkSinglePackResponse.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Verification response for a single pack within a bulk of packs.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.logging.model;

/**
 * Verification response for a single pack within a bulk of packs.
 */
public interface NbsIntegrationLogEntryBulkSinglePackResponse {

    /**
     * Gets the pack to be verified.
     *
     * @return The pack to be verified.
     */
    public NbsIntegrationLogEntryPackIdentifier getPack();

    /**
     * Sets the pack to be verified.
     *
     * @param value The pack to be verified.
     */
    public void setPack(NbsIntegrationLogEntryPackIdentifier value);

    /**
     * Gets the response for the verification of the single pack.
     *
     * @return The response for the verification of the single pack.
     */
    public NbsIntegrationLogEntrySinglePackResponse getResult();

    /**
     * Sets the response for the verification of the single pack.
     *
     * @param value The response for the verification of the single pack.
     */
    public void setResult(NbsIntegrationLogEntrySinglePackResponse value);
}
