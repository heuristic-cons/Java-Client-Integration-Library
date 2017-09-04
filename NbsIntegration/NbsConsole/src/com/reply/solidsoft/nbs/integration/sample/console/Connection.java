// --------------------------------------------------------------------------------------------------------------------
// <copyright file="Connection.cs" company="Solidsoft Reply Ltd.">
//   (c) 2017 Solidsoft Reply Ltd.
// </copyright>
// <summary>
//   The connection to the national system.
// </summary>
// --------------------------------------------------------------------------------------------------------------------
package com.reply.solidsoft.nbs.integration.sample.console;

import java.net.URL;
import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;

/**
 * The connection to the national system.
 */
public class Connection {

    /**
     * Configuration for API connection.
     */
    private static AppConfig APP_CONFIG;

    /**
     * Initializes static members of the Connection class.
     */
    static {
        try (JsonReader reader = new JsonReader(new FileReader("appsettings.json"))) {
            Gson serializer = new Gson();
            String s = reader.toString();
            APP_CONFIG = (AppConfig) serializer.fromJson(reader, AppConfig.class);
        }
        catch (IOException ex)
        {
            System.out.println(String.format("Ar error occurred: %1$s", ex.getMessage()));
        }
    }

    /**
     * Gets the base URL for the national system.
     *
     * @return The base URL for the national system.
     */
    public static String getBaseUrl() {
        if (null == APP_CONFIG || APP_CONFIG.getApiConnection() == null) {
            return "";
        }

        String baseUrl = APP_CONFIG.getApiConnection().getBaseUrl();
        return baseUrl.endsWith("/")
                ? baseUrl
                : baseUrl + "/";
    }

    /**
     * Sets the base URL for the national system.
     *
     * @param value The base URL for the national system.
     */
    public void setBaseUrl(String value) {
        if (null == APP_CONFIG || APP_CONFIG.getApiConnection() == null) {
            return;
        }

        APP_CONFIG.getApiConnection().setBaseUrl(value);

        // Config.Save(ConfigurationSaveMode.Modified);
    }

    /**
     * Gets the URL for the identity server system.
     * @return The URL for the identity server system.
     */
    public static String getIdentityServerUrl() {
        if (APP_CONFIG == null || APP_CONFIG.getApiConnection() == null)
        {
            return "";
        }
        
        return APP_CONFIG.getApiConnection().getIdentityServerUrl();
    }

    /**
     * Sets the URL for the identity server system.
     * @param value The URL for the identity server system.
     */
    public static void setIdentityServerUrl(String value) {
        if (APP_CONFIG == null || APP_CONFIG.getApiConnection() == null)
        {
            return;
        }
        
        APP_CONFIG.getApiConnection().setIdentityServerUrl(value);

        // Config.Save(ConfigurationSaveMode.Modified);
    }
}

