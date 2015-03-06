package com.baidu.weather.schedule;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.spider.*;
import com.baidu.weather.tool.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

/**
 * Created by edwardsbean on 15-1-5.
 */
public class SpiderJob {

    public static final Logger log = LoggerFactory.getLogger(SpiderJob.class);


    public void grabAlarm() {
        log.info("定时调度，抓取告警");
        deleteAlarm();
        Spider.create(new AlarmProcess())
                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .addUrl("http://product.weather.com.cn/alarm/grepalarm.php?areaid=%5B0-9%5D%7B5%2C7%7D")
                .thread(5)
                .run();
    }

    public void deleteAlarm() {
        String date = TimeUtil.getBeforeDate(1);
        WeatherManager.weatherDao.deleteAlarm(date);
    }
}
