package com.baidu.weather.schedule;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.spider.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

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
                .thread(10)
                .run();
    }

    private Spider addUrl(Spider spider) {
        for (String cityId : WeatherManager.weatherDict) {
            spider.addUrl("http://m.weather.com.cn/mweather1d/" + cityId + ".shtml");
            spider.addUrl("http://d1.weather.com.cn/sk_2d/" + cityId + ".html");
        }
        return spider;
    }

}
