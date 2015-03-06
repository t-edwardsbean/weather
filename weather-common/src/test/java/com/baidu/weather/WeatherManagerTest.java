package com.baidu.weather;

import com.baidu.weather.model.City;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.MultiPagePipeline;

import java.util.Arrays;
import java.util.List;


/**
 * Created by edwardsbean on 15-1-14.
 */
public class WeatherManagerTest {
    ApplicationContext applicationContext;
//
//    @Before
//    public void setUp() throws Exception {
//        applicationContext = new ClassPathXmlApplicationContext("dict.xml", "dao.xml");
//        WeatherManager weatherManager = new WeatherManager(applicationContext);
//        weatherManager.start();
//    }
//
//    @Test
//    public void testSuggestion() throws Exception {
//        new SpiderJob().grabSuggestion();
//    }
//
//    @Test
//    public void testSomeSuggestion() throws Exception {
//        Spider spider = Spider.create(new SuggestionProcess())
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .thread(2);
//        List<String> cityIds = Arrays.asList("101270115");
////        List<String> cityIds = Arrays.asList("101010800", "101010300", "101010600", "101010100", "101270115", "101010500", "101010400", "101010200");
//        for (String cityId : cityIds) {
//            spider.addUrl("http://www.weather.com.cn/weather1d/" + cityId + ".shtml");
//        }
//        spider.run();
//    }
//
//    @Test
//    public void testJmxSuggestion() throws Exception {
//        Spider spider = Spider.create(new SuggestionProcess())
//                .setUUID("suggestion-spider")
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .thread(2);
//
//        for (String cityId : WeatherManager.weatherDict) {
//            spider.addUrl("http://www.weather.com.cn/weather1d/" + cityId + ".shtml");
//        }
//        Spider spider2 = Spider.create(new FutureProcess())
//                .setUUID("future-spider")
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .thread(2);
//        for (String cityId : WeatherManager.weatherDict) {
//            spider2.addUrl("http://m.weather.com.cn/mweather15d/" + cityId + ".shtml");
//        }
//        SpiderMonitor.instance().register(spider);
//        SpiderMonitor.instance().register(spider2);
//        spider.run();
//        spider2.run();
//    }
//
//    @Test
//    public void testAQI() throws Exception {
//        Spider spider = Spider.create(new AQIProcess())
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .thread(10);
//        for (String cityId : WeatherManager.aqiDict) {
//            spider.addUrl("http://d1.weather.com.cn/aqi_mobile/" + cityId + ".html");
//        }
//        spider.run();
//    }
//
//    @Test
//    public void testSomeAQI() throws Exception {
//        Spider spider = Spider.create(new AQIProcess())
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .thread(10);
//        spider.addUrl("http://d1.weather.com.cn/aqi_mobile/" + 101120104 + ".html");
//        spider.addUrl("http://d1.weather.com.cn/aqi_mobile/" + 101210108 + ".html");
//        spider.run();
//    }
//
//    @Test
//    public void testWeather() throws Exception {
//        new SpiderJob().grabWeather();
//    }
//
//    @Test
//    public void testSomeWeather() throws Exception {
//        Spider.create(new WeatherProcess())
//                .addUrl("http://m.weather.com.cn/mweather1d/" + 101010100 + ".shtml")
//                .addUrl("http://d1.weather.com.cn/sk_2d/" + 101010100 + ".html")
//                .addPipeline(new MultiPagePipeline())
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .thread(2)
//                .run();
//    }
//
//    @Test
//    public void testFuture() throws Exception {
//        new SpiderJob().grabFuture();
//    }
//
//    @Test
//    public void testSomeFuture() throws Exception {
//        Spider spider = Spider.create(new FutureProcess())
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .thread(10);
//        spider.addUrl("http://m.weather.com.cn/mweather15d/" + 101010700 + ".shtml");
//        spider.run();
//    }
//
//    @Test
//    public void testAlarm() throws Exception {
//        new SpiderJob().grabAlarm();
//
//    }
//
//    @Test
//    public void testFullDict() throws Exception {
//        String cityCode = "207700013";
//        String cityName = WeatherManager.fullCity.get(new City(cityCode));
//        Assert.assertNotNull(cityName);
//    }
//
//    @Test
//    public void testLossData() throws Exception {
//        String code = "101270115";
//        String cityName = "青白江";
//        String returnCityName = WeatherManager.fullCity.get(new City(code));
//        Assert.assertEquals(returnCityName, cityName);
//
//    }
//
//    @Test
//    public void testPM25INProcess() throws Exception {
//        Spider.create(new PM25INProcess())
//                .addUrl("http://www.pm25.in/rank")
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .thread(1)
//                .run();
//    }
}
