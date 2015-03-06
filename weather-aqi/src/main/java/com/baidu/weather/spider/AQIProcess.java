package com.baidu.weather.spider;

import com.baidu.weather.exception.WeatherException;
import com.baidu.weather.tool.TimeUtil;
import com.baidu.weather.WeatherManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.HashMap;
import java.util.Map;

/**
 * 中国天气网的PM2.5数据
 * Created by edwardsbean on 15-1-11.
 */
public class AQIProcess implements PageProcessor {
    public static final Logger log = LoggerFactory.getLogger(AQIProcess.class);
    private static final String PM = "http://d1.weather.com.cn/aqi_mobile/.*";

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
        log.debug("Request City {}", cityId);

        //PM2.5
        if (page.getUrl().regex(PM).match()) {
            Map<String, Object> pm = new HashMap<>();
            pm.put("citycode", cityId);
            String cityName = WeatherManager.fullCity.get(cityId);
            pm.put("cityname", cityName);
            String json = null;
            try {
                //页面没有详细的城市，区名
                json = html.regex("(\\{.*\\})").replace("&quot;", "\"").toString();
                String aqi = new JsonPathSelector("$.p.p2").select(json);
                if (StringUtils.isEmpty(aqi)) {
                    pm.put("aqi", "-1");
                    pm.put("quality", "");
                    pm.put("color", "");
                    pm.put("advice", "");
                } else {
                    pm.put("aqi", aqi);
                    int num = Integer.parseInt(aqi);
                    if (num > 0 && 50 > num) {
                        pm.put("quality", "优");
                        pm.put("color", "#3bb64f");
                        pm.put("advice", "空气很好，可以外出活动，呼吸新鲜空气，拥抱大自然！");
                    } else if (num >= 50 && 100 > num) {
                        pm.put("quality", "良");
                        pm.put("color", "#ff9900");
                        pm.put("advice", "空气好，可以外出活动，除极少数对污染物特别敏感的人群以外，对公众没有危害！");
                    } else if (num >= 100 && 150 > num) {
                        pm.put("quality", "轻度污染");
                        pm.put("color", "#ff6000");
                        pm.put("advice", "空气一般，老人、小孩及对污染物比较敏感的人群会感到些微不适！");
                    } else if (num >= 150 && 200 >= num) {
                        pm.put("quality", "中度污染");
                        pm.put("color", "#f61c1c");
                        pm.put("advice", "空气较差，老人、小孩及对污染物比较敏感的人群会感到不适！");
                    } else if (num > 200 && 300 > num) {
                        pm.put("quality", "重度污染");
                        pm.put("color", "#bb002f");
                        pm.put("advice", "空气差，适当减少外出活动，老人、小孩出门时需做好防范措施！");
                    } else if (num >= 300) {
                        pm.put("quality", "严重污染");
                        pm.put("color", "#7e0808");
                        pm.put("advice", "空气很差，尽量不要外出活动!");
                    }
                }
                putOrElse(pm, "pm25", new JsonPathSelector("$.p.p1").select(json));
                putOrElse(pm, "pm10", new JsonPathSelector("$.p.p5").select(json));
                putOrElse(pm, "so2", new JsonPathSelector("$.p.p6").select(json));
                putOrElse(pm, "no2", new JsonPathSelector("$.p.p3").select(json));
                putOrElse(pm, "o3", new JsonPathSelector("$.p.p4").select(json));
                putOrElseFloat(pm, "co", new JsonPathSelector("$.p.p7").select(json));
                pm.put("pubdate", TimeUtil.format(new JsonPathSelector("$.p.p9").select(json)));
                pm.put("last_update", TimeUtil.getYYMMddHHmm());
                pm.put("source", "1");
//            pm.put("others", "暂无");
                page.putField("/air", pm);
            } catch (Exception e) {
                throw new WeatherException("抓取空气质量异常：" + cityName + "," + json, e);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    private void putOrElse(Map map, String key, String data) {
        if (StringUtils.isNumeric(data)) {
            map.put(key, data);
        } else
            map.put(key, -1);
    }

    private void putOrElseFloat(Map map, String key, String data) {
        try {
            if (data == null || data.length() == 0) {
                map.put(key, -1);
                return;
            }
            Float.parseFloat(data);
            map.put(key, data);
        } catch (Exception e) {
            map.put(key, -1);
        }
    }

    public static void main(String[] args) {
        Spider.create(new AQIProcess())
                .addUrl("http://d1.weather.com.cn/aqi_mobile/101202.html")
                .addPipeline(new ConsolePipeline())
                .thread(2)
                .run();
    }
}
