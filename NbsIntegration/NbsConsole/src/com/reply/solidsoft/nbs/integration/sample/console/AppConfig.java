// --------------------------------------------------------------------------------------------------------------------
// <copyright file="AppConfig.cs" company="Solidsoft Reply Ltd.">
//   (c) 2017 Solidsoft Reply Ltd.
// </copyright>
// <summary>
//   Represents API connection configuration.
// </summary>
// --------------------------------------------------------------------------------------------------------------------

package com.reply.solidsoft.nbs.integration.sample.console;

/**
 *     The application configuration data.
 */
public class AppConfig
{
    /**
     * The API connection settings.
     */
    public ApiConnection apiConnection;
    
    /**
     * Gets the API connection settings.
     * @return The API connection settings.
     */
    public ApiConnection getApiConnection ()
    { 
        return this.apiConnection;
    }
    
    /**
     * Sets the API connection settings.
     * @param value The API connection settings.
     */
    public void setApiConnection (ApiConnection value)
    {
        this.apiConnection = value;
    }
}
