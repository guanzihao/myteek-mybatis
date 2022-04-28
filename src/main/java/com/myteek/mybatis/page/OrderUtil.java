package com.myteek.mybatis.page;

import java.util.LinkedHashMap;
import java.util.Map;

public class OrderUtil {

    /**
     * camel to underscore
     * @param source camel string
     * @return underscore string
     */
    public static String camelToUnderscore(String source) {
        StringBuilder sb = new StringBuilder();
        for (char item : source.toCharArray()) {
            if (Character.isUpperCase(item)) {
                sb.append("_").append(Character.toLowerCase(item));
            } else {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    /**
     * camel to underscore
     * @param page process page orders
     */
    public static void camelToUnderscore(Page page) {
        Map<String, OrderType> orderTypeMap = page.getOrders();
        LinkedHashMap<String, OrderType> newOrders = new LinkedHashMap<>();
        orderTypeMap.forEach((key, val) -> newOrders.put(camelToUnderscore(key), val));
        page.setOrders(newOrders);
    }

}
