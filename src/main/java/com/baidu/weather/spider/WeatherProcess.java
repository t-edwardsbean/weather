package com.baidu.weather.spider;

import com.baidu.weather.exception.WeatherException;
import com.baidu.weather.model.City;
import com.baidu.weather.tool.FilterUtil;
import com.baidu.weather.tool.TimeUtil;
import com.baidu.weather.WeatherManager;
import com.baidu.weather.model.Today;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.MultiPagePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.*;

/**
 * 数据源更新时间不规则，为了更实时更新数据，抓取入库一次耗时6分左右
 * 调度爬虫，每15分钟
 * Created by edwardsbean on 15-1-5.
 */
public class WeatherProcess implements PageProcessor {
    public static final Logger log = LoggerFactory.getLogger(WeatherProcess.class);

    private static final String SUN_INFO = "http://m\\.weather\\.com\\.cn/mweather1d/.*";
    private static final String TODAY = "http://d1\\.weather\\.com\\.cn/sk_2d/.*";
    private Site site = Site.me()
            .addHeader("Referer", "http://m.weather.com.cn/mweather1d/101230501.shtml")
            .setRetryTimes(3)
            .setSleepTime(500)
            .setCycleRetryTimes(3)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String cityId = page.getUrl().regex("/([0-9]*)\\.").toString();
        log.debug("Process Weather Request Content,City {} ,CityId:" + cityId, html);
        //日出日落
        if (page.getUrl().regex(SUN_INFO).match()) {
            Today today = new Today(cityId, "sun");
            Map<String, Object> args = new HashMap<>();
            args.put("cityname", WeatherManager.fullCity.get(new City(cityId)));
            args.put("citycode", cityId);
            args.put("source", "1");
            String sunRise = html.$("#layout > div.lie > p > span:nth-child(1)", "text").regex("([0-9]{1,2}:[0-9]{2})").toString();
            String sunSet = html.$("#layout > div.lie > p > span.rl", "text").regex("([0-9]{1,2}:[0-9]{2})").toString();
            putOrElse(args, "sunrise", sunRise, cityId);
            putOrElse(args, "sunset", sunSet, cityId);
            today.setArgs(args);
            page.putField("/weather", today);
        }
        //当天天气
        if (page.getUrl().regex(TODAY).match()) {
            try {
                String json = html.regex("(\\{.*\\})").replace("&quot;", "\"").toString();
                Map<String, Object> args = new HashMap<>();
                String pubdate;
                try {
                    pubdate = new JsonPathSelector("$.time").select(json);
                } catch (Exception e) {
                    throw new WeatherException("数据源无数据,无法解析：" + html);
                }
                if ("暂无更新".equals(pubdate)) {
                    String cityName = WeatherManager.fullCity.get(new City(cityId));
                    throw new WeatherException("该城市暂无数据:" + cityName + json);
                }
                args.put("pubdate", TimeUtil.getYYMMdd() + " " + pubdate);
                Today today = new Today(cityId, "today");
                args.put("temperature", checkTemperatureIfNull(json));
                args.put("wind_direction", checkIfNull("WD", json));
                args.put("wind_scale", checkIfNull("WS", json));
                args.put("humidity", checkIfNull("SD", json));
                args.put("last_update", TimeUtil.getYYMMddHHmm());
                args.put("text", checkIfNull("weather", json));
                args.put("icon_code", checkIfNull("weathercode", json));
                //非必要字段，不告警
                args.put("wind_speed", checkVarcharIfNull("wse", json).replace("&lt;", ""));
                FilterUtil.putOrElse(args, "pressure", new JsonPathSelector("$.qy").select(json));
//            args.put("aqi", new JsonPathSelector("$.aqi").select(json));
                today.setArgs(args);
                //TODO 抓取时间,icon_code,气压
                page.putField("/weather", today);
            } catch (Exception e) {
                log.warn("爬取实时天气异常", e);
            }

        }


    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new WeatherProcess())
                .addUrl("http://d1.weather.com.cn/sk_2d/101042100.html")
                .addUrl("http://m.weather.com.cn/mweather1d/101042100.shtml")
//                .addUrl("http://d1.weather.com.cn/aqi_mobile/101230501.html")
                .addPipeline(new MultiPagePipeline())
                .addPipeline(new ConsolePipeline())
//                .addPipeline(new HttpPipeline("http://172.17.150.115:8080", 6))
                .thread(2)
                .run();
    }

    private String checkIfNull(String key, String json) {
        String data = new JsonPathSelector("$." + key).select(json);
        String city = new JsonPathSelector("$.cityname").select(json);
        if (data == null || data.isEmpty() || "暂无实况".equals(data)) {
            log.warn("抓取实时天气异常，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
            data = "";
        }
        return data;
    }

    private String checkVarcharIfNull(String key, String json) {
        String data = new JsonPathSelector("$." + key).select(json);
        String city = new JsonPathSelector("$.cityname").select(json);
        if (data == null || data.isEmpty() || "暂无实况".equals(data)) {
//            log.warn("抓取实时天气异常，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
            data = "";
        }
        return data;
    }
    
    private String checkTemperatureIfNull(String json) {
        String data = new JsonPathSelector("$.temp").select(json);
        String city = new JsonPathSelector("$.cityname").select(json);
        if (data == null || data.isEmpty() || "暂无实况".equals(data)) {
            log.warn("抓取实时天气异常，城市：" + city + ",字段：temperature，值:{},源：{}", data, json);
            data = "-1000";
        }
        return data;
    }

    private void putOrElse(Map<String, Object> args, String key, String data, String cityId) {
        if (null == data || data.isEmpty() || "暂无实况".equals(data)) {
            log.warn("抓取实时天气异常，城市：{},字段：{}", cityId, key);
            args.put(key, "");
        } else {
            args.put(key, data);
        }
    }
        
}

