package com.baidu.weather.tool;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DictUtilTest {
    ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
//        applicationContext = new ClassPathXmlApplicationContext("dict.xml", "dao.xml");
//        WeatherManager weatherManager = new WeatherManager(applicationContext);
//        weatherManager.start();
    }

    @Test
    public void testGetCityCode() throws Exception {
//        String cityCode = DictUtil.getCityCode("通州", "北京", WeatherManager.);
//        System.out.println(cityCode);
    }

    @Test
    public void testGetProxy() throws Exception {
        System.out.println(DictUtil.getProxy());
    }
}