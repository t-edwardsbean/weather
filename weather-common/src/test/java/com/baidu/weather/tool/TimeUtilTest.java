package com.baidu.weather.tool;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
        System.out.println(TimeUtil.getChinseMd());
    }

    @Test
    public void testGetTodayWeek() throws Exception {
        System.out.println(TimeUtil.getTodayWeek());
    }

    @Test
    public void testGetBeforeDay() throws Exception {
        System.out.println(TimeUtil.getBeforeDate(7));
    }

    @Test
    public void testParseMd2YYYYMMdd() throws Exception {
        System.out.println(TimeUtil.parseMmm2YYYYMMddHHmmss("9:00"));
    }

    @Test
    public void testParseUTC() throws Exception {
        System.out.println(TimeUtil.parseUTC("2014-01-26T12:52:57+08:00"));

    }
}