package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.exception.WeatherException;
import com.baidu.weather.model.City;
import com.baidu.weather.model.Future;
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

import java.util.*;

/**
 * 手机版15天页面上抓取，没有发布时间，但是据观察，一天更新2-3次。抓取一次34分钟
 * 数据源更新时间：7:30，11:30
 * Created by edwardsbean on 15-1-11.
 */
public class FutureProcess implements PageProcessor {
    public static final Logger log = LoggerFactory.getLogger(FutureProcess.class);
    private static final String FUTURE = "http://m\\.weather\\.com\\.cn/mweather15d/.*";

    private Site site = Site.me()
            .addHeader("Referer", "http://m.weather.com.cn/mweather1d/101230501.shtml")
            .setRetryTimes(3)
            .setCycleRetryTimes(3)
            .setSleepTime(500)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String cityId = page.getUrl().regex("/([0-9]*)\\.").toString();

        //未来7天,来自非手机版http://www.weather.com.cn/weather/101230501.shtml
//        if (page.getUrl().regex(FUTURE).match()) {
//            List<Selectable> futures = html.$("div.m.m3 ul.t.clearfix li.dn").nodes();
//            for (int i = 0; i < futures.size(); i++) {
//                String imgTitle = futures.get(i).$("p.wea", "text").toString();
//                if (imgTitle.indexOf("转") != -1) {
//                    String[] titles = imgTitle.split("转");
//                    page.putField("img_title" + (i + 1), titles[0]);
//                    page.putField("img_title" + (i + 2), titles[1]);
//                } else {
//                    page.putField("img_title" + (i + 1), imgTitle);
//                    page.putField("img_title" + (i + 2), imgTitle);
//                }
//                //天气转变，多云转晴
//                page.putField("weather" + (i + 1), imgTitle);
//                //温度,5°/10°
//                List<String> temp = futures.get(i).$("p.tem span", "text").all();
//                page.putField("temp" + (i + 1), temp.get(0) + '/' + temp.get(1));
//
//                //天气图标号码?
//                List<Selectable> imgs = futures.get(i).$("big").nodes();
//                for (int j = 0; j < imgs.size(); j++) {
//                    String[] bigs = imgs.get(j).$("big", "class").toString().split(" ");
//                    if (bigs.length > 1) {
//                        page.putField("img" + (i + j + 1), bigs[1]);
//                    }
//                }
//
//                //风向?
//                List<Selectable> fx = futures.get(i).$("p.win span").nodes();
//                for (int j = 0; j < fx.size(); j++) {
//                    page.putField("fx" + (i * 2 + j + 1), fx.get(j).$("span", "title"));
//                }
//
//                //风力
//                page.putField("fl" + (i * 2 + 1), futures.get(i).$("p.win i", "text"));
//                page.putField("fl" + (i * 2 + 2), futures.get(i).$("p.win i", "text"));
//
//            }
//        }

        //未来7天，来自手机版
        if (page.getUrl().regex(FUTURE).match()) {
//            Map<String, String> futureMaps = new HashMap<>();
            List<Future> results = new ArrayList<>();
            //页面没有详细的城市，区名
            //15天的天气
            List<Selectable> futures = html.$("div.yb15 table").nodes();
            //校验数据完整性
            if (futures.isEmpty() || futures.size() < 7) {
                log.warn("抓取天气预测异常，数据不全或者没有,cityId：{},源：{}", cityId, html);
                return;
            } else {
                //校验数据正确性，通过更新时间
                String date = futures.get(0).$("tr:nth-child(1) span:nth-child(2)", "text").toString();
                if (null == date || date.isEmpty() || !date.split(" ")[0].equals(TimeUtil.getChinseMMdd())) {
                    log.warn("抓取天气预测异常，数据源没有更新,源：{}", html);
                    return;
                }
            }
            //只存7天数据
            for (int i = 0; i < futures.size() && i < 7; i++) {
                try {
                    Future result = new Future();
                    result.setCityId(cityId);
                    result.setCityName(WeatherManager.fullCity.get(new City(cityId)));
                    String date = futures.get(i).$("tr:nth-child(1) span:nth-child(2)", "text").toString();
                    //日期
                    try {
                        String[] day = date.split(" ");
                        if ("今天".equals(day[1])) {
                            result.setWeek(TimeUtil.getTodayWeek());
                        } else {
                            result.setWeek(day[1]);
                        }
                        result.setDate(TimeUtil.chineseFormat(day[0]));
                    } catch (Exception e) {
                        log.warn("抓取天气预测异常，CityId:{},日期：{},字段缺失：week,date,源：{}", cityId, date);
                    }
                    //某天天气
                    Selectable weather = futures.get(i).$("tr:nth-child(3)");
                    //早上所有数据
                    String mor = weather.$("td:nth-child(1) span:nth-child(1)", "text").regex("-?\\d*~-?\\d*").toString();
                    //晚上所有数据
                    String noon = weather.$("td:nth-child(2) span:nth-child(1)", "text").regex("-?\\d*~-?\\d*").toString();
                    //早上温度范围
                    String[] morTemp = new String[0];
                    //晚上温度范围
                    String[] noonTemp = new String[0];
                    //早上风三元素
                    String[] morWind = new String[0];
                    //晚上风三元素
                    String[] noonWind = new String[0];
                    String noonImgUrl = weather.$("td:nth-child(2) img", "src").toString();
                    String morImgUrl = weather.$("td:nth-child(1) img", "src").toString();
                    if (null != mor) {
                        morTemp = mor.split("~");
                        morWind = weather.$("td:nth-child(1) span:nth-child(3)", "text").toString().split(" ");
                        result.setIconCodeDay(morImgUrl.charAt(morImgUrl.length() - 5) + "");
                    } else {
                        result.setIconCodeDay("");
                    }
                    if (null != noon) {
                        noonTemp = noon.split("~");
                        noonWind = weather.$("td:nth-child(2) span:nth-child(3)", "text").toString().split(" ");
                        result.setIconCodeNight(noonImgUrl.charAt(noonImgUrl.length() - 5) + "");
                    } else {
                        result.setIconCodeNight("");
                    }
                    //温度
                    List<Integer> temps = new ArrayList<>();
                    for (String temp : morTemp) {
                        temps.add(Integer.parseInt(temp));
                    }
                    for (String temp : noonTemp) {
                        temps.add(Integer.parseInt(temp));
                    }
                    Collections.sort(temps);
//                futureMaps.put("temp" + (i + 1), temps.get(0) + "/" + temps.get(temps.size() - 1));
                    //温度:temp_low,temp_high
                    if (temps.isEmpty()) {
                        result.setTempHigh("-1000");
                        result.setTempLow("-1000");
                        log.warn("抓取天气预测异常，CityId:{},日期：{},字段缺失：temp_high,temp_low", cityId, date);
                    } else {
                        result.setTempLow(temps.get(0) + "");
                        result.setTempHigh(temps.get(temps.size() - 1) + "");
                    }
                    //天气
//                futureMaps.put("img_title" + (i * 2 + 1), morWind[0]);
//                futureMaps.put("img_title" + (i * 2 + 2), noonWind[0]);
                    if (morWind.length == 3) {
                        result.setWeatherDay(morWind[0]);
                        result.setWindDirectionDay(morWind[1]);
                        result.setWindSpeedDay(morWind[2]);
                    } else {
                        result.setWeatherDay("");
                        result.setWindDirectionDay("");
                        result.setWindSpeedDay("");
                        log.warn("抓取天气预测异常，CityId:{},日期：" + date + ",字段缺失：weather_day,wind_direction_day,wind_speed_day,源：{}", cityId, morWind);
                    }

                    if (noonWind.length == 3) {
                        result.setWeatherNight(noonWind[0]);
                        result.setWindDirectionNight(noonWind[1]);
                        result.setWindSpeedNight(noonWind[2]);
                    } else {
                        result.setWeatherNight("");
                        result.setWindDirectionNight("");
                        result.setWindSpeedNight("");
                        log.warn("抓取天气预测异常，CityId:{},日期：" + date + ",字段缺失：weather_night,wind_direction_night,wind_speed_night,源：{}", cityId, noonWind);
                    }

                    //天气转变
                    if (morWind.length != 0 && !morWind[0].equals(noonWind[0])) {
                        //                    futureMaps.put("weather" + (i + 1), morWind[0] + "转" + noonWind[0]);
                        result.setWeather(morWind[0] + "转" + noonWind[0]);
                    } else {
                        //                    futureMaps.put("weather" + (i + 1), morWind[0]);
                        result.setWeather(noonWind[0]);
                    }


                    //天气图标号码
//                futureMaps.put("img" + (i * 2 + 1), morImgUrl.charAt(morImgUrl.length() - 5) + "");
//                futureMaps.put("img" + (i * 2 + 2), noonImgUrl.charAt(noonImgUrl.length() - 5) + "");

                    //风向
//                futureMaps.put("fx" + (i * 2 + 1), morWind[1]);
//                futureMaps.put("fx" + (i * 2 + 2), noonWind[1]);

                    //风力
//                futureMaps.put("fl" + (i * 2 + 1), morWind[2]);
//                futureMaps.put("fl" + (i * 2 + 2), noonWind[2]);
                    result.setLast_update(TimeUtil.getYYMMddHHmm());
                    result.setPubDate(TimeUtil.getYYMMddHHmm());
                    result.setSource("1");
                    results.add(result);
                } catch (Exception e) {
                    new WeatherException("抓取天气预测异常", e);
                }
            }
            page.putField("/future", results);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new FutureProcess())
                .addUrl("http://m.weather.com.cn/mweather15d/1010100.shtml")
                .addPipeline(new ConsolePipeline())
                .thread(2)
                .run();
    }
}
