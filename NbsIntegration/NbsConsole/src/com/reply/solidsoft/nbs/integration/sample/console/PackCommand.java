// --------------------------------------------------------------------------------------------------------------------
// <copyright file="PackCommand.cs" company="Solidsoft Reply">
//   (c) 2017 Solidsoft Reply Ltd.
// </copyright>
// <summary>
// Represents a medicine pack.
// </summary>
// --------------------------------------------------------------------------------------------------------------------

package com.reply.solidsoft.nbs.integration.sample.console;

import com.reply.solidsoft.nbs.integration.model.PackIdentifier;

/**
 *     Represents a medicine pack.
 */
public class PackCommand extends PackIdentifier
{
    /**
     * The state.
     */
    private String state;
    
    /**
     * Gets the State.
     * 
     * @return The state.
     */
    public String getState()
    {
        return this.state;
    }
    
    /**
     * Gets the State.
     * 
     * @param value 
     */
    public void setState(String value)
    {
        this.state = value;
    }
}

