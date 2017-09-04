/**
 * -----------------------------------------------------------------------------
 * File=PackStateTransitionCommand.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A pack state transition command.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

/**
 * A pack state transition command.
 */
public class PackStateTransitionCommand implements java.io.Serializable {

    /**
     * The required pack state.
     */
    private String state;

    /**
     * Initializes a new instance of the PackStateTransitionCommand class.
     *
     * @param requestedPackState The required pack state.
     */
    public PackStateTransitionCommand(RequestedPackState requestedPackState) {
        this.state = requestedPackState.getValue();
    }

    /**
     * Initializes a new instance of the PackStateTransitionCommand class.
     *
     * @param packState The required pack state.
     */
    public PackStateTransitionCommand(String packState) {
        this.state = packState;
    }

    /**
     * Gets the required pack state.
     *
     * @return The required pack state.
     */
    public final String getState() {
        return (this.state == null) ? "" : this.state;
    }

    /**
     * Sets the required pack state.
     *
     * @param value The required pack state.
     */
    protected final void setState(String value) {
        this.state = value;
    }
}
