package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.model.City;
import com.baidu.weather.tool.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源更新时间：8:00,11:30
 * 抓取入库耗时:1小时左右
 * Created by edwardsbean on 15-1-11.
 */
public class SuggestionProcess implements PageProcessor {
    public static final Logger log = LoggerFactory.getLogger(SuggestionProcess.class);

    private static final String FLAG = "http://www\\.weather\\.com\\.cn/weather1d/.*";

    private Site site = Site.me()
            .addHeader("Referer", "http://m.weather.com.cn/mweather1d/101230501.shtml")
            .setRetryTimes(2)
            .setCycleRetryTimes(2)
            .setSleepTime(1000)
            .setRetrySleepTime(2000)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String cityId = page.getUrl().regex("/([0-9]*)\\.").toString();
        log.debug("Process Suggestion Request Content,City Id {}", cityId);
        //指数
        if (page.getUrl().regex(FLAG).match()) {
            List<Map<String, Object>> all = new ArrayList<>();
            String pubdate = html.$("#zs0 > p", "text").toString();

            Map<String, Object> dressing = new HashMap<>();
            dressing.put("citycode", cityId);
            dressing.put("cityname", WeatherManager.fullCity.get(cityId));
            dressing.put("last_update", TimeUtil.getYYMMddHHmm());
            dressing.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            dressing.put("type", "dressing");
            dressing.put("typename", "穿衣指数");
            dressing.put("source", "1");
            putOrElse(dressing, "suggestion", html.$("section.mask section.ct b", "text").toString());
            putOrElse(dressing, "info", html.$("section.mask section.ct aside", "text").toString());
            all.add(dressing);

//            flag.put("flu_brief", html.$("section.mask section.gm b", "text").toString());
//            flag.put("flu_details", html.$("section.mask section.gm aside", "text").toString());

            Map<String, Object> flu = new HashMap<>();
            flu.put("citycode", cityId);
            flu.put("cityname", WeatherManager.fullCity.get(cityId));
            flu.put("last_update", TimeUtil.getYYMMddHHmm());
            flu.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            flu.put("type", "flu");
            flu.put("typename", "感冒指数");
            flu.put("source", "1");
            putOrElse(flu, "suggestion", html.$("section.mask section.gm b", "text").toString());
            putOrElse(flu, "info", html.$("section.mask section.gm aside", "text").toString());
            all.add(flu);

//            flag.put("car_washing_brief", html.$("section.mask section.xc b", "text").toString());
//            flag.put("car_washing_details", html.$("section.mask section.xc aside", "text").toString());

            Map<String, Object> car = new HashMap<>();
            car.put("citycode", cityId);
            car.put("cityname", WeatherManager.fullCity.get(cityId));
            car.put("last_update", TimeUtil.getYYMMddHHmm());
            car.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            car.put("type", "car");
            car.put("typename", "洗车指数");
            car.put("source", "1");
            putOrElse(car, "info", html.$("section.mask section.xc aside", "text").toString());
            putOrElse(car, "suggestion", html.$("section.mask section.xc b", "text").toString());
            all.add(car);

            Map<String, Object> uv = new HashMap<>();
            uv.put("citycode", cityId);
            uv.put("cityname", WeatherManager.fullCity.get(cityId));
            uv.put("last_update", TimeUtil.getYYMMddHHmm());
            uv.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            uv.put("type", "uv");
            uv.put("typename", "紫外线指数");
            uv.put("source", "1");
            putOrElse(uv, "info", html.$("section.mask section.uv aside", "text").toString());
            putOrElse(uv, "suggestion", html.$("section.mask section.uv b", "text").toString());
            all.add(uv);

            Map<String, Object> glass = new HashMap<>();
            glass.put("citycode", cityId);
            glass.put("cityname", WeatherManager.fullCity.get(cityId));
            glass.put("last_update", TimeUtil.getYYMMddHHmm());
            glass.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            glass.put("type", "glass");
            glass.put("typename", "太阳镜指数");
            glass.put("source", "1");
            putOrElse(glass, "info", html.$("section.mask section.gl aside", "text").toString());
            putOrElse(glass, "suggestion", html.$("section.mask section.gl b", "text").toString());
            all.add(glass);

            Map<String, Object> travel = new HashMap<>();
            travel.put("citycode", cityId);
            travel.put("cityname", WeatherManager.fullCity.get(cityId));
            travel.put("last_update", TimeUtil.getYYMMddHHmm());
            travel.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            travel.put("type", "travel");
            travel.put("typename", "旅游指数");
            travel.put("source", "1");
            putOrElse(travel, "info", html.$("section.mask section.tr aside", "text").toString());
            putOrElse(travel, "suggestion", html.$("section.mask section.tr b", "text").toString());
            all.add(travel);

            Map<String, Object> hair = new HashMap<>();
            hair.put("citycode", cityId);
            hair.put("cityname", WeatherManager.fullCity.get(cityId));
            hair.put("last_update", TimeUtil.getYYMMddHHmm());
            hair.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            hair.put("type", "hair");
            hair.put("typename", "美发指数");
            hair.put("source", "1");
            putOrElse(hair, "info", html.$("section.mask section.mf aside", "text").toString());
            putOrElse(hair, "suggestion", html.$("section.mask section.mf b", "text").toString());
            all.add(hair);

            Map<String, Object> exercise = new HashMap<>();
            exercise.put("citycode", cityId);
            exercise.put("cityname", WeatherManager.fullCity.get(cityId));
            exercise.put("last_update", TimeUtil.getYYMMddHHmm());
            exercise.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            exercise.put("type", "exercise");
            exercise.put("typename", "晨练指数");
            exercise.put("source", "1");
            putOrElse(exercise, "info", html.$("section.mask section.cl aside", "text").toString());
            putOrElse(exercise, "suggestion", html.$("section.mask section.cl b", "text").toString());
            all.add(exercise);

            Map<String, Object> allergy = new HashMap<>();
            allergy.put("citycode", cityId);
            allergy.put("cityname", WeatherManager.fullCity.get(cityId));
            allergy.put("last_update", TimeUtil.getYYMMddHHmm());
            allergy.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            allergy.put("type", "allergy");
            allergy.put("typename", "过敏指数");
            allergy.put("source", "1");
            putOrElse(allergy, "info", html.$("section.mask section.ag aside", "text").toString());
            putOrElse(allergy, "suggestion", html.$("section.mask section.ag b", "text").toString());
            all.add(allergy);

            Map<String, Object> sport = new HashMap<>();
            sport.put("citycode", cityId);
            sport.put("cityname", WeatherManager.fullCity.get(cityId));
            sport.put("last_update", TimeUtil.getYYMMddHHmm());
            sport.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            sport.put("type", "sport");
            sport.put("typename", "运动指数");
            sport.put("source", "1");
            putOrElse(sport, "info", html.$("section.mask section.yd aside", "text").toString());
            putOrElse(sport, "suggestion", html.$("section.mask section.yd b", "text").toString());
            all.add(sport);

            Map<String, Object> umbrella = new HashMap<>();
            umbrella.put("citycode", cityId);
            umbrella.put("cityname", WeatherManager.fullCity.get(cityId));
            umbrella.put("last_update", TimeUtil.getYYMMddHHmm());
            umbrella.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            umbrella.put("type", "umbrella");
            umbrella.put("typename", "雨伞指数");
            umbrella.put("source", "1");
            putOrElse(umbrella, "info", html.$("section.mask section.ys aside", "text").toString());
            putOrElse(umbrella, "suggestion", html.$("section.mask section.ys b", "text").toString());
            all.add(umbrella);

            Map<String, Object> cosmetic = new HashMap<>();
            cosmetic.put("citycode", cityId);
            cosmetic.put("cityname", WeatherManager.fullCity.get(cityId));
            cosmetic.put("last_update", TimeUtil.getYYMMddHHmm());
            cosmetic.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            cosmetic.put("type", "cosmetic");
            cosmetic.put("typename", "化妆指数");
            cosmetic.put("source", "1");
            putOrElse(cosmetic, "info", html.$("section.mask section.pp aside", "text").toString());
            putOrElse(cosmetic, "suggestion", html.$("section.mask section.pp b", "text").toString());
            all.add(cosmetic);

            Map<String, Object> comfortable = new HashMap<>();
            comfortable.put("citycode", cityId);
            comfortable.put("cityname", WeatherManager.fullCity.get(cityId));
            comfortable.put("last_update", TimeUtil.getYYMMddHHmm());
            comfortable.put("pubdate", pubdate.substring(0, pubdate.length() - 2));
            comfortable.put("type", "comfortable");
            comfortable.put("typename", "舒适度指数");
            comfortable.put("source", "1");
            putOrElse(comfortable, "info", html.$("section.mask section.co aside", "text").toString());
            putOrElse(comfortable, "suggestion", html.$("section.mask section.co b", "text").toString());
            all.add(comfortable);
            page.putField("/suggestion", all);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new SuggestionProcess())
                .addUrl("http://www.weather.com.cn/weather1d/101230101.shtml")
                .thread(2)
                .run();
    }

    private void putOrElse(Map<String, Object> map, String key, String value) {
        if ("暂缺".equals(value)) {
            map.put(key, null);
        } else {
            map.put(key, StringUtils.trimToNull(value));
        }
    }
}
