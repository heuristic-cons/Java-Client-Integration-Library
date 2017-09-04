/**
 * -----------------------------------------------------------------------------
 * File=Event.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * An event to which external listeners can be bound. This class supports
 * binding multiple listeners to the event.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.extensions.events;

/**
 * An event to which external listeners can be bound. This class supports
 * binding multiple listeners to the event.
 *
 * @param <T> The type of listener that can be bound to the event.
 */
public final class Event<T> {

    /**
     * A dictionary of named listeners bound to the event.
     */
    private final java.util.Map<String, T> namedListeners = new java.util.HashMap<>();

    /**
     * A list of anonymous listeners bound to the event.
     */
    private final java.util.List<T> anonymousListeners = new java.util.ArrayList<>();

    /**
     * Binds a named listener to the event.
     *
     * @param listenerName The name of the listener.
     * @param listenerMethod The listener method.
     */
    public void addListener(String listenerName, T listenerMethod) {
        if (!namedListeners.containsKey(listenerName)) {
            namedListeners.put(listenerName, listenerMethod);
        }
    }

    /**
     * Un-binds a named listener from the event.
     *
     * @param listenerName The name of the listener.
     */
    public void removeListener(String listenerName) {
        if (namedListeners.containsKey(listenerName)) {
            namedListeners.remove(listenerName);
        }
    }

    /**
     * Binds a listener to the event. The listener is anonymous and cannot be
     * unbound from the event.
     *
     * @param listenerMethod The listener method.
     */
    public void addListener(T listenerMethod) {
        anonymousListeners.add(listenerMethod);
    }

    /**
     * Returns the list of all listeners currently bound to the event.
     *
     * @return The list of all listeners currently bound to the event.
     */
    public java.util.List<T> listeners() {
        java.util.List<T> allListeners = new java.util.ArrayList<>();
        allListeners.addAll(namedListeners.values());
        allListeners.addAll(anonymousListeners);
        return allListeners;
    }
}
