/**
 * -----------------------------------------------------------------------------
 * File=Resources.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Base class for store and forward databases.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.dataservice.properties;

import java.util.ResourceBundle;
import java.util.Locale;

/**
 * A strongly-typed resource class, for looking up localized strings, etc.
 */
public class Resources {

    /**
     * The locale for the resource bundle. Defaults to the current culture.
     */
    private static Locale LOCALE = Locale.getDefault();

    /**
     * The resource bundle which manages access to resources.
     */
    private static ResourceBundle STRING_RESOURCES;

    public Resources() {
    }

    /**
     * Returns the cached ResourceManager instance used by this class.
     *
     * @return The cached ResourceManager instance used by this class.
     */
    public static ResourceBundle getResourceManager() {
        if (STRING_RESOURCES == null) {
            ResourceBundle temp = ResourceBundle.getBundle("com.reply.solidsoft.nbs.integration.properties.IntegrationResources", LOCALE);
            STRING_RESOURCES = temp;
        }

        return STRING_RESOURCES;
    }

    /**
     * Overrides the current thread CurrentUICulture property for all resource
     * lookups using this strongly typed resource class.
     *
     * @return The culture being used by the resource bundle.
     */
    public static Locale getCulture() {
        return LOCALE;
    }

    public static void setCulture(Locale value) {
        LOCALE = value;
    }

    /**
     * Looks up a localized string similar to The data management service cannot
     * be null.
     * 
     * @return A localized string similar to The data management service cannot
     * be null.
     */
    public static String getDataTable_DataManagementServiceCannotBeNull() {
        return getResourceManager().getString("DataTable_DataManagementServiceCannotBeNull");
    }

    /**
     * Looks up a localized string similar to The data management service name
     * is invalid.
     * 
     * @return A localized string similar to The data management service name
     * is invalid.
     */
    public static String getDataTable_DataManagementServiceNameInvalid() {
        return getResourceManager().getString("DataTable_DataManagementServiceNameInvalid");
    }

    /**
     * Looks up a localized string similar to The table name is invalid.
     * 
     * @return A localized string similar to The table name is invalid.
     */
    public static String getDataTable_DataTableNameInvalid() {
        return getResourceManager().getString("DataTable_DataTableNameInvalid");
    }

    /**
     * Looks up a localized string similar to BulkRequest type must be {0}.
     * 
     * @return A localized string similar to BulkRequest type must be {0}.
     */
    public static String getDataTable_RequestTypeException() {
        return getResourceManager().getString("DataTable_RequestTypeException");
    }

    /**
     * Looks up a localized string similar to The transaction context must be
     * initialized over an instance of a resource manager.
     * 
     * @return A localized string similar to The transaction context must be
     * initialized over an instance of a resource manager.
     */
    public static String getTransactionManager_TContextInitializationError() {
        return getResourceManager().getString("TransactionManager_TContextInitializationError");
    }
}
