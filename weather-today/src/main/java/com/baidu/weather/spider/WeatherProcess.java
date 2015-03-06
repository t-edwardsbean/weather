package com.baidu.weather.spider;

import com.baidu.weather.exception.WeatherException;
import com.baidu.weather.exception.WeatherParseException;
import com.baidu.weather.tool.FilterUtil;
import com.baidu.weather.tool.TimeUtil;
import com.baidu.weather.WeatherManager;
import com.baidu.weather.model.Today;
import org.apache.commons.lang3.StringUtils;
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
import us.codecraft.webmagic.selector.Selectable;

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
            .setSleepTime(1000)
            .setCycleRetryTimes(3)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String cityId = page.getUrl().regex("/([0-9]*)\\.").toString();
        log.debug("Process Weather Request Content,CityId:{}" + cityId);
        //日出日落
        if (page.getUrl().regex(SUN_INFO).match()) {
            Today today = new Today(cityId, "sun");
            Map<String, Object> args = new HashMap<>();
            args.put("cityname", WeatherManager.fullCity.get(cityId));
            args.put("citycode", cityId);
            args.put("source", "1");
            String sunRise = html.$("#layout > div.lie > p > span:nth-child(1)", "text").regex("([0-9]{1,2}:[0-9]{2})").toString();
            String sunSet = html.$("#layout > div.lie > p > span.rl", "text").regex("([0-9]{1,2}:[0-9]{2})").toString();
            String weatherCodeUrl = html.$("div.wyb > img", "src").toString();
            //尾号限行
            List<Selectable> limits = html.$("div.wl b", "text").nodes();
            String tail = "";
            for (Selectable limit : limits) {
                String tmp = StringUtils.trim(limit.toString());
                if (tmp.isEmpty()) {
                    continue;
                } else if (!StringUtils.isNumeric(tmp)) {
                    log.warn("尾号限行数值验证失败,值:{},城市:{}", limit, cityId);
                    continue;
                }
                if (!tail.isEmpty()) {
                    tail = tail + ",";
                }
                tail = tail + tmp;
            }
            args.put("tail", tail);

            Integer weatherCode;
            if (weatherCodeUrl != null) {
                String code = weatherCodeUrl.substring(weatherCodeUrl.length() - 6, weatherCodeUrl.length() - 4);
                try {
                    weatherCode = Integer.parseInt(code);
                    String weather = WeatherManager.weatherCode.get(weatherCode);
                    putOrElse(args, "text", weather, cityId);
                    putIntOrElse(args, "icon_code", code, cityId);
                } catch (NumberFormatException e) {
                    page.setSkip(true);
                    throw new WeatherParseException("天气代码转换成天气出异常,city:" + cityId + ",代码值：" + code);
                }
            }
            FilterUtil.putIsDate(args, "sunrise", sunRise, cityId);
            FilterUtil.putIsDate(args, "sunset", sunSet, cityId);
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
                    throw new WeatherException("数据源无数据或格式不对,cityId:" + cityId + ",源：" + html);
                }
                if ("暂无更新".equals(pubdate) || pubdate.isEmpty()) {
                    String cityName = WeatherManager.fullCity.get(cityId);
                    log.info("该城市暂无数据:{},{}", cityName, json);
                    page.setSkip(true);
                    return;
                }
                Boolean success = FilterUtil.putIsYYYYHHDate(args, "pubdate", pubdate, cityId);
                if (!success) {
                    log.info("数据验证失败源：" + html);
                    page.setSkip(true);
                    return;
                }
                
                Today today = new Today(cityId, "today");
                checkTemperatureIfNull(args, json);
                args.put("wind_direction", checkIfNullWithEnd("WD", json, "风"));
                args.put("wind_scale", checkIfNullWithEndOrEquals("WS", json, "级", "微风"));
                args.put("humidity", checkIfNullWithEnd("SD", json, "%"));
                args.put("last_update", TimeUtil.getYYMMddHHmm());
//                args.put("text", checkIfNull("weather", json));
//                args.put("icon_code", checkIfNull("weathercode", json));
                //非必要字段，不告警
                args.put("wind_speed", checkVarcharIfNullWithEnd("wse", json, "km/h").replace("&lt;", ""));
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
            log.info("抓取实时天气异常，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
            data = "";
        }
        return data;
    }

    private String checkIfNullWithRange(String key, String json, List<String> range) {
        String data = new JsonPathSelector("$." + key).select(json);
        String city = new JsonPathSelector("$.cityname").select(json);
        if (data == null || data.isEmpty() || "暂无实况".equals(data)) {
            log.info("抓取实时天气异常，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
            data = "";
        } else if (!range.contains(data)) {
            log.warn("字段校验异常，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
        }
        return data;
    }

    private String checkIfNullWithEnd(String key, String json, String end) {
        String data = new JsonPathSelector("$." + key).select(json);
        String city = new JsonPathSelector("$.cityname").select(json);
        if (data != null && data.isEmpty() || "暂无实况".equals(data)) {
            log.info("实时天气字段暂缺，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
        } else if (data == null) {
            log.warn("抓取实时天气异常,字段找不到，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
            data = "";
        } else if (!data.endsWith(end)) {
            log.warn("字段校验异常，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
        }
        return data;
    }

    private String checkIfNullWithEndOrEquals(String key, String json, String end, String equal) {
        String data = new JsonPathSelector("$." + key).select(json);
        String city = new JsonPathSelector("$.cityname").select(json);

        if (data != null && data.isEmpty() || "暂无实况".equals(data)) {
            log.info("实时天气字段暂缺，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
        } else if (data == null) {
            log.warn("抓取实时天气异常，字段找不到，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
            data = "";
        } else if (data.endsWith(end) || data.equals(equal)) {
            return data;
        } else {
            log.warn("字段校验异常，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
        }
        return data;
    }

    private String checkVarcharIfNullWithEnd(String key, String json, String end) {
        String data = new JsonPathSelector("$." + key).select(json);
        String city = new JsonPathSelector("$.cityname").select(json);
        if (data != null && data.isEmpty() || "暂无实况".equals(data)) {
            log.info("实时天气字段暂缺，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
        } else if (data == null) {
            log.warn("实时天气字段暂缺，字段找不到，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
            data = "";
        } else if (!data.endsWith(end)) {
            log.warn("字段校验异常，城市：" + city + ",字段：{},值：" + data + ",源：{}", key, json);
        }
        return data;
    }

    private void checkTemperatureIfNull(Map map, String json) {
        String data = new JsonPathSelector("$.temp").select(json);
        String city = new JsonPathSelector("$.cityname").select(json);
        if (data != null && data.isEmpty() || "暂无实况".equals(data)) {
            log.info("实时天气字段暂缺，城市：" + city + ",字段：temperature,值：" + data + ",源：{}", json);
        } else if (data == null) {
            log.warn("抓取实时天气异常，字段找不到，城市：" + city + ",字段：temperature，值:{},源：{}", data, json);
            data = "-1000";
        } else {
            FilterUtil.putIntWithRange(map, "temperature", data, city, -90, 63);
        }
    }

    private void putOrElse(Map<String, Object> args, String key, String data, String cityId) {
        if (data != null && data.isEmpty() || "暂无实况".equals(data)) {
            log.info("实时天气字段暂缺，城市：" + cityId + ",字段：{},值：" + data + ",源：{}", key, data);
        } else if (data == null) {
            log.warn("抓取实时天气异常，城市：{},字段：{}", cityId, key);
            args.put(key, "");
        } else {
            args.put(key, data);
        }
    }

    private void putIntOrElse(Map<String, Object> args, String key, String data, String cityId) {
        if (data != null && data.isEmpty() || "暂无实况".equals(data)) {
            log.warn("字段暂缺，城市：{},字段：{}", cityId, key);
        } else if (!StringUtils.isNumeric(data)) {
            log.warn("实时天气字段校验异常，城市：{},字段：{}，值：" + data, cityId, key);
            args.put(key, "");
        } else {
            args.put(key, data);
        }
    }

}

