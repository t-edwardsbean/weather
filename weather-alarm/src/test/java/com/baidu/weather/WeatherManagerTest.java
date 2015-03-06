package com.baidu.weather;

import com.baidu.weather.schedule.SpiderJob;
import com.baidu.weather.spider.AlarmProcess;
import com.baidu.weather.spider.HttpPipeline;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.Spider;

import java.util.Date;


/**
 * Created by edwardsbean on 15-1-14.
 */
public class WeatherManagerTest {
    ApplicationContext applicationContext;
    WeatherManager weatherManager;

    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("dict.xml", "dao.xml");
        weatherManager = new WeatherManager(applicationContext);
    }


    @Test
    public void testAlarm() throws Exception {
        weatherManager.start();
    }

    @Test
    public void testDeleteAlarm() throws Exception {
        new SpiderJob().deleteAlarm();
    }

    @Test
    public void testAlarmInfo() throws Exception {
        Spider.create(new AlarmProcess())
                .addUrl("http://www.weather.com.cn/data/alarm/1010702-20150302205758-1203.html?_=" + System.currentTimeMillis())
                .thread(1)
                .run();
    }
}
