package com.baidu.weather;

import com.baidu.weather.schedule.SpiderJob;
import com.baidu.weather.spider.JdbcPipeline;
import com.baidu.weather.spider.WeatherProcess;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.MultiPagePipeline;


/**
 * Created by edwardsbean on 15-1-14.
 */
public class WeatherManagerTest {
    ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("dict.xml", "dao.xml");
        WeatherManager weatherManager = new WeatherManager(applicationContext);
        weatherManager.init();
    }


    @Test
    public void testWeather() throws Exception {
        new SpiderJob().grabWeather();
    }

    @Test
    public void testSomeWeather() throws Exception {
        Spider.create(new WeatherProcess())
                .addPipeline(new MultiPagePipeline())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .addUrl("http://m.weather.com.cn/mweather1d/101091103.shtml")
                .addUrl("http://d1.weather.com.cn/sk_2d/101091103.html")
                .thread(2)
                .run();
    }


}
