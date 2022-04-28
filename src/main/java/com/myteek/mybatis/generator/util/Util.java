package com.myteek.mybatis.generator.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
public class Util {

    /**
     * convert lower field name
     * @param str source string
     * @return string
     */
    public static String convertLowerFieldName(String str) {
        if (str != null && str.length() != 0) {
            char firstChar = str.charAt(0);
            return Character.isLowerCase(firstChar) ? str : Character.toLowerCase(firstChar) + str.substring(1);
        }
        return str;
    }

    /**
     * convert field name
     * @param str source string
     * @return string
     */
    public static String convertFieldName(String str) {
        if (str != null && str.length() != 0) {
            char firstChar = str.charAt(0);
            return Character.isTitleCase(firstChar) ? str : Character.toTitleCase(firstChar) + str.substring(1);
        }
        return str;
    }

    public static String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static <T> String toJson(T object) {
        return JSON.toJSONString(object);
    }

    /**
     * strong to object
     * @param content content
     * @param className class name
     * @param <T> type
     * @return type
     */
    public static <T> T toObject(String content, Class<T> className) {
        if (Objects.isNull(content) || content.isEmpty()) {
            return null;
        }
        try {
            return JSON.parseObject(content, className);
        } catch (Exception e) {
            log.warn("parse json object error, content: {}, className: {}", content, className);
        }
        return null;
    }

    /**
     * string to array class
     * @param content content
     * @param className className
     * @param <T> type
     * @return type
     */
    public static <T> List<T> toArray(String content, Class<T> className) {
        if (Objects.isNull(content) || content.isEmpty()) {
            return null;
        }
        try {
            return JSON.parseArray(content, className);
        } catch (Exception e) {
            log.warn("parse json array error, content: {}, className: {}", content, className);
        }
        return null;
    }

}
