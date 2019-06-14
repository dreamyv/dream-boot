package com.dream.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存工具
 */
public class CacheUtil {

    private static Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    private static Map<String, Map<Object, Object>> caches = new HashMap<String, Map<Object, Object>>();

    /**
     * 添加缓存
     */
    public static <K,V> void put(String type, K key,  V v) {
        Map<Object, Object> cache = null;
        if(!caches.containsKey(type)) {
            cache = new HashMap<>();
            caches.put(type, cache);
        } else {
            cache = caches.get(type);
        }
        cache.put(key, v);
    }

    /**
     * 清空缓存
     */
    public static void cleanCaches(){
        caches.clear();
    }

    /**
     * 根据K获取V
     */
    public static <K, V> V get(String type, K key) {
        if(!caches.containsKey(type)) {
            return null;
        }
        return (V)caches.get(type).get(key);
    }

    /**
     * 判读type下是否有键
     */
    public static <K> boolean containsKey(String type, K key) {
        if(!caches.containsKey(type)) {
            return false;
        }
        return caches.get(type).containsKey(key);
    }

    /**
     * 删除对应KEY
     */
    public static <K, V> void delete(K type, V key) {
        if(!caches.containsKey(type)) {
            return;
        }
        if(!caches.get(type).containsKey(key)) {
            return;
        }
        caches.get(type).remove(key);
    }
}
