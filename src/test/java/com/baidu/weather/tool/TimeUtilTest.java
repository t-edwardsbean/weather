package com.baidu.weather.tool;

import org.junit.Test;


public class TimeUtilTest {

    @Test
    public void testGetYYMMdd() throws Exception {
        System.out.println(TimeUtil.getYYMMdd());
    }

    @Test
    public void testGetYYMMddHHmm() throws Exception {
        System.out.println(TimeUtil.getYYMMddHHmm());
    }

    @Test
    public void testChineseFormat() throws Exception {
        System.out.println(TimeUtil.chineseFormat("01月5日"));
    }

    @Test
    public void testGetChinseMMdd() throws Exception {
        System.out.println(TimeUtil.getChinseMMdd());
    }

    @Test
    public void testGetTodayWeek() throws Exception {
        System.out.println(TimeUtil.getTodayWeek());
    }
}