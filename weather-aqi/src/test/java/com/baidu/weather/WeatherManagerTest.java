package com.baidu.weather;

import com.baidu.weather.spider.JdbcPipeline;
import com.baidu.weather.schedule.SpiderJob;
import com.baidu.weather.spider.AQIProcess;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.Spider;


/**
 * Created by edwardsbean on 15-1-14.
 */
public class WeatherManagerTest {
    ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("dict.xml", "dao.xml");
        WeatherManager weatherManager = new WeatherManager(applicationContext);
        weatherManager.start();
    }


    @Test
    public void testAQI() throws Exception {
//        Spider spider = Spider.create(new AQIProcess())
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .thread(10);
//        for (String cityId : WeatherManager.aqiDict) {
//            spider.addUrl("http://d1.weather.com.cn/aqi_mobile/" + cityId + ".html");
//        }
//        spider.run();
    }

    @Test
    public void testSomeAQI() throws Exception {
        Spider spider = Spider.create(new AQIProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .thread(10);
        spider.addUrl("http://d1.weather.com.cn/aqi_mobile/" + 101120104 + ".html");
        spider.addUrl("http://d1.weather.com.cn/aqi_mobile/" + 101210108 + ".html");
        spider.run();
    }

    @Test
    public void testPM25IN() throws Exception {
        new SpiderJob().grabAQI();
    }
}
