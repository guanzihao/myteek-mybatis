package com.myteek.mybatis.generator.base;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class ConfigEnumItemTest {

    @Test
    public void testEnum() {
        ConfigEnumItem item = ConfigEnum.of(ConfigEnumItem.class, 1);
        Assert.assertEquals("WEB", item.toString());
    }

    @Test
    public void testToMap() {
        Map<Integer, String> map = ConfigEnum.toMap(ConfigEnumItem.class);
        Assert.assertEquals("IOS_STRING", map.get(2));
    }

}
