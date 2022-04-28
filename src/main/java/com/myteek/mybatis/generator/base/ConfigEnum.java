package com.myteek.mybatis.generator.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface ConfigEnum {

    String DEFAULT_VALUE_NAME = "value";
    String DEFAULT_LABEL_NAME = "label";

    /**
     * get enum value
     * @return Integer
     */
    default Integer getValue() {
        Exception exception = null;
        try {
            Field field = getClass().getDeclaredField(DEFAULT_VALUE_NAME);
            if (Objects.nonNull(field)) {
                field.setAccessible(true);
                return Integer.valueOf(field.get(this).toString());
            }
        } catch (NoSuchFieldException e) {
            exception = e;
        } catch (IllegalAccessException e) {
            exception = e;
        }
        throw new RuntimeException(exception);
    }

    /**
     * get enum label
     * @return String
     */
    default String getLabel() {
        Exception exception = null;
        try {
            Field field = getClass().getDeclaredField(DEFAULT_LABEL_NAME);
            if (Objects.nonNull(field)) {
                field.setAccessible(true);
                return field.get(this).toString();
            }
        } catch (NoSuchFieldException e) {
            exception = e;
        } catch (IllegalAccessException e) {
            exception = e;
        }
        throw new RuntimeException(exception);
    }

    /**
     * value of int value
     * @param enumClass enum class
     * @param value value
     * @return T
     */
    static <T extends ConfigEnum> T of(Class<T> enumClass, Integer value) {
        if (value != null) {
            for (T item : enumClass.getEnumConstants()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * convert to enum map
     * @param enumClass enum class
     * @param <T> type
     * @return Map&lt;Integer, String&gt;
     */
    static <T extends ConfigEnum> Map<Integer, String> toMap(Class<T> enumClass) {
        Map<Integer, String> ret = new HashMap<>(enumClass.getEnumConstants().length);
        for (T item : enumClass.getEnumConstants()) {
            ret.put(item.getValue(), item.getLabel());
        }
        return ret;
    }

}
