/**
 * -----------------------------------------------------------------------------
 * File=PropertyChangedEventArgs.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Provides data for the PropertyChanged event.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.model.extensions;

/**
 * Provides data for the PropertyChanged event.
 */
public class PropertyChangedEventArgs {

    private final String propertyName;

    /**
     * Initializes a new instance of the PropertyChangedEventArgs class.
     *
     * @param propertyName The name of the property.
     */
    public PropertyChangedEventArgs(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Gets the name of the property that changed.  * @return The name of the
     * property that changed.
     *
     * @return The name of the property that changed.  
     */
    public String getPropertyName() {
        return propertyName;
    }
}
