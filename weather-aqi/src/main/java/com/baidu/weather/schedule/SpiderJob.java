package com.baidu.weather.schedule;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.spider.*;
import com.baidu.weather.spider.PM25INProcess;
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
        Spider.create(new PM25INProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .setUUID("aqi-spider")
                .addUrl("http://www.pm25.in/rank")
                .thread(2)
                .run();
    }
}
