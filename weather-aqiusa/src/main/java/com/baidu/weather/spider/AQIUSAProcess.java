package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.model.City;
import com.baidu.weather.tool.DictUtil;
import com.baidu.weather.tool.FilterUtil;
import com.baidu.weather.tool.TimeUtil;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.SimpleProxyPool;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 来自美国大使馆的AQI数据
 * Created by edwardsbean on 15-2-2.
 */
public class AQIUSAProcess implements PageProcessor {
    public static final Logger log = LoggerFactory.getLogger(AQIUSAProcess.class);
    private static final String ALL = "http://aqicn\\.org/city/all/cn/.*";
    private static final String AQI = "http://aqicn\\.org/city/.*";


    public Site site = Site.me()
            .addHeader("Referer", "http://aqicn.org")
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setCycleRetryTimes(3)
            .setHttpProxyPool(new SimpleProxyPool(DictUtil.getProxy()))
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        //提取所有城市
        Map<String, Object> pm = new HashMap<>();
        if (page.getUrl().regex(ALL).match()) {
            Selectable chineseCity = html.$("center > div:nth-child(41)");
            List<String> cities = chineseCity.regex("\\([\u4e00-\u9fa5]+\\)").all();
            for (String city : cities) {
                log.info(city.substring(1, city.length() - 1));
            }
        } else if (page.getUrl().regex(AQI).match()) {
            String cityNameEn = page.getUrl().regex("city/(.*)/cn.*").toString();
            String pubdate = html.$("table.api > tbody > tr > td:nth-child(3) > div:nth-child(2)").regex("([0-9]{1,2}:[0-9]{2})").toString();
            String aqi = html.$("table.api > tbody > tr > td:nth-child(1) > div", "text").toString();
            if ("-".equals(aqi)) {
                page.setSkip(true);
                log.info("该城市暂无数据：{}", cityNameEn);
                return;
            }
            String pm25 = html.$("#cur_pm25", "text").toString();
            String pm10 = html.$("#cur_pm10", "text").toString();
            String o3 = html.$("#cur_o3", "text").toString();
            String no2 = html.$("#cur_no2", "text").toString();
            String so2 = html.$("#cur_so2", "text").toString();
            String co = html.$("#cur_co", "text").toString();
            City city = DictUtil.getCityByEn(cityNameEn, WeatherManager.aqiCities);
            String cityName = city.getCityname();
            pm.put("cityname", city.getCityname());
            pm.put("citycode", city.getCitycode());
            FilterUtil.putIntWithRange(pm, "pm25", pm25, cityName, 0, 500);
            FilterUtil.putIntWithRange(pm, "pm10", pm10, cityName, 0, 500);
            FilterUtil.putIntWithStart(pm, "so2", so2, cityName, 0);
            FilterUtil.putIntWithStart(pm, "no2", no2, cityName, 0);
            FilterUtil.putIntWithStart(pm, "o3", o3, cityName, 0);
            FilterUtil.putFloatWithStart(pm, "co", co, cityName, 0);
            int num = FilterUtil.putIntWithRange(pm, "aqi", aqi, cityName, 0, 500);
            try {
                pm.put("pubdate", TimeUtil.parseMmm2YYYYMMddHHmmss(pubdate));
            } catch (Exception e) {
                log.warn("时间格式化错误，城市：{}", cityName);
                page.setSkip(true);
                return;
            }
            pm.put("last_update", TimeUtil.getYYMMddHHmm());
            pm.put("source", "2");
            if (num > 0 && 50 >= num) {
                pm.put("quality", "优");
                pm.put("color", "#3bb64f");
                pm.put("advice", "空气很好，可以外出活动，呼吸新鲜空气，拥抱大自然！");
            } else if (num > 50 && 100 >= num) {
                pm.put("quality", "良");
                pm.put("color", "#ff9900");
                pm.put("advice", "空气好，可以外出活动，除极少数对污染物特别敏感的人群以外，对公众没有危害！");
            } else if (num > 100 && 150 >= num) {
                pm.put("quality", "轻度污染");
                pm.put("color", "#ff6000");
                pm.put("advice", "空气一般，老人、小孩及对污染物比较敏感的人群会感到些微不适！");
            } else if (num > 150 && 200 >= num) {
                pm.put("quality", "中度污染");
                pm.put("color", "#f61c1c");
                pm.put("advice", "空气较差，老人、小孩及对污染物比较敏感的人群会感到不适！");
            } else if (num > 200 && 300 >= num) {
                pm.put("quality", "重度污染");
                pm.put("color", "#bb002f");
                pm.put("advice", "空气差，适当减少外出活动，老人、小孩出门时需做好防范措施！");
            } else if (num > 300 && num <= 500) {
                pm.put("quality", "严重污染");
                pm.put("color", "#7e0808");
                pm.put("advice", "空气很差，尽量不要外出活动!");
            } else {
                pm.put("quality", "");
                pm.put("color", "");
                pm.put("advice", "");
            }
            page.putField("/aqiusa", pm);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new AQIUSAProcess())
                .setUUID("aqiusa-spider")
//                .addUrl("http://aqicn.org/city/all/cn/#中国")
                .addUrl("http://aqicn.org/city/sanya/cn/")
                .addPipeline(new ConsolePipeline())
                .thread(1)
                .run();

    }
}
