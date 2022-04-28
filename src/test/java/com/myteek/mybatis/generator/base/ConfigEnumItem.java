package com.myteek.mybatis.generator.base;

public enum ConfigEnumItem implements ConfigEnum {

    WEB(1, "WEB_STRING"),

    IOS(2, "IOS_STRING"),

    ANDROID(3, "ANDROID_STRING");

    private Integer value;

    private String label;

    ConfigEnumItem(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
