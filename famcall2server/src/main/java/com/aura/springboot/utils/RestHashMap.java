package com.aura.springboot.utils;

import java.util.HashMap;

public class RestHashMap<K, V> extends HashMap<K, V> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RestHashMap() {
    }

    public RestHashMap<K, V> p(K key, V value) {
        return this.putField(key, value);
    }

    public RestHashMap<K, V> putField(K key, V value) {
        super.put(key, value);
        return this;
    }

    public static RestHashMap<String, Object> n() {
        return newMap();
    }

    public static RestHashMap<String, Object> newMap() {
        RestHashMap<String, Object> m = new RestHashMap<String, Object>();
        return m;
    }
}