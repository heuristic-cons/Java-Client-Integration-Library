/**
 * -----------------------------------------------------------------------------
 * File=IPackState.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Represents a pack state.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model;

/**
 * Represents a pack state.
 */
public interface PackState {

    /**
     * Gets the display name for the pack state.
     *
     * @return The display name for the pack state.
     */
    public String getDisplayName();

    /**
     * Gets the value of the pack state.
     *
     * @return The value of the pack state.
     */
    public String getValue();

    /**
     * Indicates whether the pack state is in an array of pack states.
     *
     * @param packStates The array of pack states.
     * @return True, if the pack state is in the array; otherwise false.
     */
    public boolean in(PackState... packStates);
}
