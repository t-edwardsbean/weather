package com.baidu.weather.spider;

import com.baidu.weather.model.LocalDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 查找所有包含PM2.5数据的城市
 * Created by edwardsbean on 15-1-12.
 */
public class AQICity implements PageProcessor {
    public static final Logger log = LoggerFactory.getLogger(AQICity.class);
    private static ApplicationContext applicationContext;
    private Site site = Site.me()
            .addHeader("Referer", "http://m.weather.com.cn/mweather1d/101230501.shtml")
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String cityId = page.getUrl().regex("/([0-9]*)\\.").toString();
        if (!"非常抱歉，网页无法访问".equals(html.$("head > title", "text").toString())) {
            log.error(cityId);
        }
//        AQIDict city = (AQIDict) applicationContext.getBean("aqiDict");
//        if ("城区".equals(chengqu) && city.getAllCity().contains(cityName)) {
//            log.error(cityId);
//        }

    }

    @Override
    public Site getSite() {
        return site;
    }

//    public static void main(String[] args) {
//        applicationContext = new ClassPathXmlApplicationContext("dict.xml");
//        addUrl(Spider.create(new AQICity()))
//                .thread(2)
//                .run();
//    }
//
//    public static Spider addUrl(Spider spider) {
//        LocalDict localDict = (LocalDict) applicationContext.getBean("cityDict");
//        List<String> dict = localDict.getAllCity();
//        for (String cityId : dict) {
//            spider.addUrl("http://d1.weather.com.cn/aqi_mobile/" + cityId + ".html");
//        }
//        return spider;
//    }
}
