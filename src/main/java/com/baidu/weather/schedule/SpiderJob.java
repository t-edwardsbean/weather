package com.baidu.weather.schedule;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.spider.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.MultiPagePipeline;

import java.util.List;

/**
 * Created by edwardsbean on 15-1-5.
 */
public class SpiderJob {

    public static final Logger log = LoggerFactory.getLogger(SpiderJob.class);

    public void grabWeather() {
        log.info("定时调度，抓取当天天气");
        addUrl(Spider.create(new WeatherProcess()))
                .addPipeline(new MultiPagePipeline())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .addPipeline(new HttpPipeline("http://172.17.150.115:8080", 6))
                .thread(10)
                .run();
    }

    public void grabSuggestion() {
        log.info("定时调度，抓取指数");
        Spider spider = Spider.create(new SuggestionProcess())
//                .addPipeline(new HttpPipeline("http://172.17.150.115:8080", 6))
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .thread(10);
        for (String cityId : WeatherManager.weatherDict) {
            spider.addUrl("http://www.weather.com.cn/weather1d/" + cityId + ".shtml");
        }
        spider.run();
    }

    public void grabAlarm() {
        log.info("定时调度，抓取告警");
        Spider.create(new AlarmProcess())
//                .addPipeline(new HttpPipeline("http://172.17.150.115:8080", 6))
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .addUrl("http://product.weather.com.cn/alarm/grepalarm.php?areaid=%5B0-9%5D%7B5%2C7%7D")
                .thread(5)
                .run();
    }

    public void grabFuture() {
        log.info("定时调度，抓取未来天气");
        Spider spider = Spider.create(new FutureProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
//                .addPipeline(new HttpPipeline("http://172.17.150.115:8080", 6))
                .thread(10);
        for (String cityId : WeatherManager.weatherDict) {
            spider.addUrl("http://m.weather.com.cn/mweather15d/" + cityId + ".shtml");
        }
        spider.run();
    }

    public void grabAQI() {
        log.info("定时调度，抓取AQI");
        Spider spider = Spider.create(new AQIProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .thread(5);
        for (String cityId : WeatherManager.aqiDict) {
            spider.addUrl("http://d1.weather.com.cn/aqi_mobile/" + cityId + ".html");
        }
        spider.run();
    }

    private Spider addUrl(Spider spider) {
        for (String cityId : WeatherManager.weatherDict) {
            spider.addUrl("http://m.weather.com.cn/mweather1d/" + cityId + ".shtml");
            spider.addUrl("http://d1.weather.com.cn/sk_2d/" + cityId + ".html");
        }
        return spider;
    }
}
