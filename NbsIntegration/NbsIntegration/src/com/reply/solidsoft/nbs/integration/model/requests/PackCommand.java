/**
 * -----------------------------------------------------------------------------
 * File=PackCommand.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A pack transition command for packs of medicinal product passed during recovery.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.requests;

import com.reply.solidsoft.nbs.integration.model.PackStateTransitionType;
import com.reply.solidsoft.nbs.integration.model.PackIdentifier;

/**
 * A pack transition command for packs of medicinal product passed during
 * recovery.
 */
public class PackCommand extends PackIdentifier {

    /**
     * Initializes a new instance of the PackCommand class.
     *
     * @param packIdentifier The pack identifier.
     */
    public PackCommand(PackIdentifier packIdentifier) {
        this.setProductCodeScheme(packIdentifier.getProductCodeScheme());
        this.setProductCode(packIdentifier.getProductCode());
        this.setSerialNumber(packIdentifier.getSerialNumber());
        this.setBatchId(packIdentifier.getBatchId());
        this.setExpiryDate(packIdentifier.getExpiryDate());
    }

    /**
     * Initializes a new instance of the PackCommand class.
     *
     * @param transitionType The transition type to which the pack should be
     * transitioned.
     * @param productCodeScheme The product code scheme.
     * @param productCode The product code.
     * @param serialNumber The serial number.
     * @param batchId The batch identifier.
     * @param expiryDate The expiry date.
     */
    public PackCommand(PackStateTransitionType transitionType, String productCodeScheme, String productCode, String serialNumber, String batchId, String expiryDate) {
        super(productCodeScheme, productCode, serialNumber, batchId, expiryDate);
        this.setTransitionType(transitionType);
    }

    /**
     * Initializes a new instance of the PackCommand class.
     *
     * @param transitionType The transition type to which the pack should be
     * transitioned.
     * @param pack The pack.
     */
    public PackCommand(PackStateTransitionType transitionType, PackIdentifier pack) {
        super(pack.getProductCodeScheme(), pack.getProductCode(), pack.getSerialNumber(), pack.getBatchId(), pack.getExpiryDate());
        this.setTransitionType(transitionType);
        this.setProductCodeScheme(super.getProductCodeScheme().toLowerCase());
    }

    /**
     * The transition type to which the pack should be transitioned.
     */
    private PackStateTransitionType transitionType = null;

    /**
     * Gets the transition type to which the pack should be transitioned.
     *
     * @return The transition type to which the pack should be transitioned.
     */
    public final PackStateTransitionType getTransitionType() {
        return transitionType;
    }

    /**
     * Sets the transition type to which the pack should be transitioned.
     *
     * @param value The transition type to which the pack should be
     * transitioned.
     */
    public final void setTransitionType(PackStateTransitionType value) {
        transitionType = value;
    }
}
