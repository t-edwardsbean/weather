package com.baidu.weather;

import com.baidu.weather.schedule.SpiderJob;
import com.baidu.weather.spider.FutureProcess;
import com.baidu.weather.spider.JdbcPipeline;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.Spider;


/**
 * Created by edwardsbean on 15-1-14.
 */
public class FutureProcessTest {
    ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("dict.xml", "dao.xml");
        WeatherManager weatherManager = new WeatherManager(applicationContext);
        weatherManager.init();
    }

    @Test
    public void testFuture() throws Exception {
        new SpiderJob().grabFuture();
    }

    @Test
    public void testSomeFuture() throws Exception {
        Spider spider = Spider.create(new FutureProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .thread(2);
        spider.addUrl("http://m.weather.com.cn/mweather15d/" + 101230101 + ".shtml");
        spider.run();
    }

}
