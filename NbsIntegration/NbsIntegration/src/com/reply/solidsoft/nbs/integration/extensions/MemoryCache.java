/**
 * -----------------------------------------------------------------------------
 * File=MemoryCache.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * A memory cache manager.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.extensions;

import java.util.ArrayList;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

/**
 * A memory cache manager.
 *
 * @author crunchify.com
 * @param <K> The type of the key values.
 * @param <T> The type of the values stored in the memory cache.
 */
public class MemoryCache<K, T> {

    /**
     * The time that a cache entry is valid.
     */
    private final long timeToLive;

    /**
     * The internal cache map.
     */
    private final LRUMap cacheMap;

    /**
     * Represents a value within the memory cache.
     */
    protected class CacheObject {

        /**
         * The time at which the cache object was last accessed.
         */
        public long lastAccessed = System.currentTimeMillis();

        /**
         * The type of the value.
         */
        public T value;

        /**
         * Initializes a new instance of the CacheObject class.
         *
         * @param value The value to be cached.
         */
        protected CacheObject(T value) {
            this.value = value;
        }
    }

    /**
     * Initializes a new instance of the MemoryCache class.
     *
     * @param timeToLive The time that an entry will live in the cache.
     * @param timerInterval The interval at which the memory cache will be
     * cleaned u and old entries purged.
     * @param maxItems The maximium number of items allowed in the memory cache.
     */
    @SuppressWarnings({"CallToThreadStartDuringObjectConstruction", "SleepWhileInLoop"})
    public MemoryCache(long timeToLive, final long timerInterval, int maxItems) {
        this.timeToLive = timeToLive * 1000;

        cacheMap = new LRUMap(maxItems);

        if (timeToLive > 0 && timerInterval > 0) {

            Thread t;
            t = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(timerInterval * 1000);
                    } catch (InterruptedException ex) {
                    }
                    cleanup();
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    /**
     * Puts a new value into the memory cache.
     *
     * @param key The key value of the new entry.
     * @param value The value to be put into the memory cache.
     */
    public void put(K key, T value) {
        synchronized (cacheMap) {
            cacheMap.put(key, new CacheObject(value));
        }
    }

    /**
     * Gets a value from the memory cache.
     *
     * @param key The key value for the memory cache entry.
     * @return A value held in the memory cache.
     */
    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (cacheMap) {
            CacheObject c = (CacheObject) cacheMap.get(key);

            if (c == null) {
                return null;
            } else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    /**
     * Remove an entry form the memory cache.
     *
     * @param key The key value of the entry to be removed.
     */
    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    /**
     * Returns the current size of the memory cache.
     *
     * @return The size of the memory cache.
     */
    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }

    /**
     * Clean up the memory cache, removing old entries.
     */
    @SuppressWarnings({"unchecked", "CallToThreadYield"})
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey;

        synchronized (cacheMap) {
            MapIterator itr = cacheMap.mapIterator();

            deleteKey = new ArrayList<>((cacheMap.size() / 2) + 1);
            K key;
            CacheObject c;

            while (itr.hasNext()) {
                key = (K) itr.next();
                c = (CacheObject) itr.getValue();

                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }

        deleteKey.stream().map((key) -> {
            synchronized (cacheMap) {
                Object remove = cacheMap.remove(key);
            }
            return key;
        }).forEachOrdered((K _item) -> {
            Thread.yield();
        });
    }
}
