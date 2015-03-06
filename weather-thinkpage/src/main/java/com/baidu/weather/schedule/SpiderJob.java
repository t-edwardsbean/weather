package com.baidu.weather.schedule;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.model.City;
import com.baidu.weather.spider.AllProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

/**
 * Created by edwardsbean on 15-3-2.
 */
public class SpiderJob {
    public static final Logger log = LoggerFactory.getLogger(SpiderJob.class);

    public void grabAll() {
        log.info("定时调度，抓取thinkpage数据");
        Spider spider = Spider.create(new AllProcess())
//                .addPipeline(new JdbcPipeline(WeatherManager.weatherDao))
                .setUUID("thinkpage-spider")
                .thread(15);
        for (City city : WeatherManager.cities) {
            String cityCode = WeatherManager.thinkpageCityDict.getCityCode(city);
            if (cityCode == null) {
                log.error("找不到该城市的thinkpage ID:{}", city);
                continue;
            }
            spider.addUrl("https://api.thinkpage.cn/v2/weather/all.json?city=" + cityCode + "&language=zh-chs&unit=c&aqi=city&key=&cityCode=" + city.getCitycode());
        }
        spider.run();

    }
}
