package com.baidu.weather.schedule;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.model.City;
import com.baidu.weather.spider.AQIUSAProcess;
import com.baidu.weather.spider.JdbcPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

/**
 * Created by edwardsbean on 15-1-5.
 */
public class SpiderJob {

    public static final Logger log = LoggerFactory.getLogger(SpiderJob.class);

    public void grabAQI() {
        log.info("定时调度，抓取AQI");
        Spider spider = Spider.create(new AQIUSAProcess())
//                .addPipeline(new ConsolePipeline())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .setUUID("usaaqi-spider")
                .thread(15);
        for (City city : WeatherManager.aqiCities) {
            spider.addUrl("http://aqicn.org/city/" + city.getCitynameen() + "/cn/");
        }
        spider.run();
    }
}
