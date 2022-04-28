package com.myteek.mybatis.generator.handler;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;

public class DateTypeHandlerTest {

    @Test
    public void testDateFormatter() {
        Assert.assertEquals(1674144000000L, Date.valueOf("2023-01-20").getTime());
    }

}
