package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.tool.FilterUtil;
import com.baidu.weather.tool.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PM25.in网站的数据
 * Created by edwardsbean on 15-1-23.
 */
public class PM25INProcess implements PageProcessor {
    public static final Logger log = LoggerFactory.getLogger(PM25INProcess.class);
    private static final String PM = "http://www.pm25.in/.*";

    private Site site = Site.me()
            .addHeader("Referer", "http://www.pm25.in/putian")
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setCycleRetryTimes(3)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<Map<String, Object>> pms = new ArrayList<>();
        log.debug("Request PM25.in's AQI");
        String time = null;
        String timeElem = null;
        //PM2.5
        if (page.getUrl().regex(PM).match()) {
            try {
                timeElem = html.$("div.time > p", "text").toString();
                time = timeElem.split("：")[1];
            } catch (Exception e) {
                log.warn("PM25.in时间格式错误,源：{}", timeElem);
            }

            List<Selectable> pmNodes = html.$("body > div.container > div > div.table table tbody tr").nodes();
            for (Selectable node : pmNodes) {
                Map<String, Object> pm = new HashMap<>();
                String cityName = node.$("td:nth-child(2) a", "text").toString();
                //过滤掉不需要的地区
                if (WeatherManager.abandonCity.contains(cityName)) {
                    continue;
                }
                String aqi = node.$("td:nth-child(3)", "text").toString();
                String pm25 = node.$("td:nth-child(6)", "text").toString();
                String pm10 = node.$("td:nth-child(7)", "text").toString();
                String co = node.$("td:nth-child(8)", "text").toString();
                String no2 = node.$("td:nth-child(9)", "text").toString();
                String o3 = node.$("td:nth-child(10)", "text").toString();
                String so2 = node.$("td:nth-child(12)", "text").toString();
                //检查城市是否有特殊后缀
                String specialArea = WeatherManager.specialArea.get(cityName);
                if (specialArea != null) {
                    cityName = specialArea;
                } else if (cityName.endsWith("地区")) {
                    cityName = cityName.substring(0, cityName.length() - 2);
                } else if (cityName.endsWith("盟")) {
                    cityName = cityName.substring(0, cityName.length() - 1);
                }
                //检查城市是否改名
                String newName = WeatherManager.changeName.get(cityName);
                if (newName != null) {
                    cityName = newName;
                }
                pm.put("cityname", cityName);
                String areaCode = WeatherManager.areaDict.get(cityName);
                //这里可能会有个Bug，因为可能会有重名的城市，但是没有具体省的信息，没办法区分到底是哪个城市
                String cityCode = WeatherManager.inverseFullCity.get(cityName);
                if (null == areaCode) {
                    //TODO 海口在area中查不到，是否不是地区
//                    throw new WeatherException("查不到城市id:" + cityName);
                    if (cityCode != null) {
                        pm.put("citycode", cityCode);
                    } else {
                        log.info("查不到城市id:" + cityName);
                        continue;
                    }
                } else {
                    pm.put("areacode", areaCode);
                    pm.put("isArea", 1);
                }
                FilterUtil.putIntWithStart(pm, "pm25", pm25, cityName, 0);
                FilterUtil.putIntWithStart(pm, "pm10", pm10, cityName, 0);
                FilterUtil.putIntWithStart(pm, "so2", so2, cityName, 0);
                FilterUtil.putIntWithStart(pm, "no2", no2, cityName, 0);
                FilterUtil.putIntWithStart(pm, "o3", o3, cityName, 0);
                FilterUtil.putFloatWithStart(pm, "co", co, cityName, 0);
                int num = FilterUtil.putIntWithStart(pm, "aqi", aqi, cityName, 0);
                pm.put("pubdate", time);
                pm.put("last_update", TimeUtil.getYYMMddHHmm());
                pm.put("source", "1");
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
                pms.add(pm);
            }
            page.putField("/pm25", pms);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new PM25INProcess())
                .addUrl("http://www.pm25.in/rank")
                .addPipeline(new ConsolePipeline())
                .thread(2)
                .run();
    }
}
