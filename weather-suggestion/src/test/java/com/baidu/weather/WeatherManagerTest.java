package com.baidu.weather;

import com.baidu.weather.model.City;
import com.baidu.weather.schedule.SpiderJob;
import com.baidu.weather.spider.JdbcPipeline;
import com.baidu.weather.spider.SuggestionProcess;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;

import java.util.Arrays;
import java.util.List;


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
    public void testSuggestion() throws Exception {
        new SpiderJob().grabSuggestion();
    }

    @Test
    public void testSomeSuggestion() throws Exception {
        Spider spider = Spider.create(new SuggestionProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .thread(2);
        List<String> cityIds = Arrays.asList("101010800");
//        List<String> cityIds = Arrays.asList("101010800", "101010300", "101010600", "101010100", "101270115", "101010500", "101010400", "101010200");
        for (String cityId : cityIds) {
            spider.addUrl("http://www.weather.com.cn/weather1d/" + cityId + ".shtml");
        }
        spider.run();
    }

    @Test
    public void testJmxSuggestion() throws Exception {
        Spider spider = Spider.create(new SuggestionProcess())
                .setUUID("suggestion-spider")
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .thread(2);

        for (City city : WeatherManager.cities) {
            spider.addUrl("http://www.weather.com.cn/weather1d/" + city.getCitycode() + ".shtml");
        }
        SpiderMonitor.instance().register(spider);
        spider.run();
    }

}
