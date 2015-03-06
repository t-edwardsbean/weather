package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.dao.WeatherDao;
import com.baidu.weather.model.City;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;


public class AQIUSAProcessTest {
    ApplicationContext applicationContext;
    WeatherDao weatherDao;
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("dict.xml", "dao.xml");
        weatherDao = (WeatherDao) applicationContext.getBean("weatherDao");
        jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate");
    }

    @Test
    public void testAQI() throws Exception {
        Spider spider = Spider.create(new AQIUSAProcess())
                .addPipeline(new ConsolePipeline())
                .setUUID("usaaqi-spider")
                .thread(5);

        for (City city : WeatherManager.aqiCities) {
            spider.addUrl("http://aqicn.org/city/" + city.getCitynameen() + "/cn/");
        }
        spider.run();
    }

    @Test
    public void testSomeAQI() throws Exception {
        new WeatherManager(applicationContext).init();
        Spider spider = Spider.create(new AQIUSAProcess())
                .addPipeline(new ConsolePipeline())
                .setUUID("usaaqi-spider")
                .thread(1);
        spider.addUrl("http://aqicn.org/city/" + "karamay" + "/cn/");
        spider.run();
    }

    @Test
    public void testInsertAQI() throws Exception {
        new WeatherManager(applicationContext).init();
        Spider spider = Spider.create(new AQIUSAProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .setUUID("usaaqi-spider")
                .thread(5);

        for (City city : WeatherManager.aqiCities) {
            spider.addUrl("http://aqicn.org/city/" + city.getCitynameen() + "/cn/");
        }
        spider.run();
    }


    @Test
    public void testProxy() throws Exception {
        new WeatherManager(applicationContext).start();
    }
}