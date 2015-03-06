package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.model.City;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ThinkpageCityDictTest {
    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("thinkpage-city-dict.xml", "dao.xml");
        WeatherManager weatherManager = new WeatherManager(applicationContext);
        weatherManager.init();
    }

    @Test
    public void testGetCityCode() throws Exception {
        for (City city : WeatherManager.cities) {
            String cityCode = WeatherManager.thinkpageCityDict.getCityCode(city);
            if (cityCode == null) {
                System.out.println("找不到该城市的thinkpage ID:" + city.getAreaname() + city.getCityname());
                continue;
            } else {
                System.out.println(city.getCityname() + " -> thinkpage ID:" + cityCode);
            }
        }
    }
}