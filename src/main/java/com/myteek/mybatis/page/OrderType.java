package com.myteek.mybatis.page;

public enum OrderType {

    ASC("asc"),

    DESC("desc");

    private String type;

    OrderType(String type) {
        this.type = type;
    }

}
