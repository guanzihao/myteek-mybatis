package com.myteek.mybatis.page;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.FifoCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.mapping.CacheBuilder;

import java.util.Properties;

@Slf4j
public class SimpleCache<K, V> implements ICache<K, V> {

    private final Cache cache;

    /**
     * constructor
     * @param properties properties
     * @param prefix prefix
     */
    public SimpleCache(Properties properties, String prefix) {
        CacheBuilder cacheBuilder = new CacheBuilder("SQL_CACHE");
        String typeClass = properties.getProperty(prefix + ".typeClass");
        if (typeClass != null && !"".equals(typeClass)) {
            try {
                cacheBuilder.implementation((Class<? extends Cache>) Class.forName(typeClass));
            } catch (ClassNotFoundException e) {
                cacheBuilder.implementation(PerpetualCache.class);
            }
        } else {
            cacheBuilder.implementation(PerpetualCache.class);
        }
        String evictionClass = properties.getProperty(prefix + ".evictionClass");
        if (typeClass != null && !"".equals(evictionClass)) {
            try {
                cacheBuilder.addDecorator((Class<? extends Cache>) Class.forName(evictionClass));
            } catch (ClassNotFoundException e) {
                cacheBuilder.addDecorator(FifoCache.class);
            }
        } else {
            cacheBuilder.addDecorator(FifoCache.class);
        }
        String flushInterval = properties.getProperty(prefix + ".flushInterval");
        if (flushInterval != null && !"".equals(flushInterval)) {
            cacheBuilder.clearInterval(Long.valueOf(flushInterval));
        }
        cacheBuilder.properties(properties);
        cache = cacheBuilder.build();
    }

    @Override
    public V get(K key) {
        Object value = cache.getObject(key);
        try {
            if (value != null) {
                return (V) value;
            }
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.debug("get data error", e);
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        cache.putObject(key, value);
    }

}
