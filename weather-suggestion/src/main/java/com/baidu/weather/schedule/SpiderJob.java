package com.baidu.weather.schedule;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.spider.JdbcPipeline;
import com.baidu.weather.spider.SuggestionProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

/**
 * Created by edwardsbean on 15-1-5.
 */
public class SpiderJob {

    public static final Logger log = LoggerFactory.getLogger(SpiderJob.class);


    public void grabSuggestion() {
        log.info("定时调度，抓取指数");
        Spider spider = Spider.create(new SuggestionProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .thread(10);
        for (String cityId : WeatherManager.weatherDict) {
            spider.addUrl("http://www.weather.com.cn/weather1d/" + cityId + ".shtml");
        }
        spider.run();
    }
}
