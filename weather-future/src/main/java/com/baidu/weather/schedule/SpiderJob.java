package com.baidu.weather.schedule;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.spider.FutureProcess;
import com.baidu.weather.spider.JdbcPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

/**
 * Created by edwardsbean on 15-1-5.
 */
public class SpiderJob {

    public static final Logger log = LoggerFactory.getLogger(SpiderJob.class);

    public void grabFuture() {
        log.info("定时调度，抓取未来天气");
        Spider spider = Spider.create(new FutureProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .thread(10);
        for (String cityId : WeatherManager.weatherDict) {
            spider.addUrl("http://m.weather.com.cn/mweather15d/" + cityId + ".shtml");
        }
        spider.run();
    }
}
