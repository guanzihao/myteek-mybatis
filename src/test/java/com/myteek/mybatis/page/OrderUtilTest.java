package com.myteek.mybatis.page;

import org.junit.Assert;
import org.junit.Test;

public class OrderUtilTest {

    @Test
    public void testCamelToUnderscore() {
        Assert.assertEquals("abcd_efg", OrderUtil.camelToUnderscore("abcdEfg"));
    }

}
