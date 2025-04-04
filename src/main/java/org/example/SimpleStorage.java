package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleStorage {
    private static final Map<Long, Object> storage = new ConcurrentHashMap<>();

    public static void put(Long key, Object value) {
        storage.put(key, value);
    }

    public static Object get(Long key) {
        return storage.get(key);
    }

    public static void remove(Long key) {
        storage.remove(key);
    }

    public static boolean containsKey(Long key) {
        return storage.containsKey(key);
    }
}