package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.exception.WeatherException;
import com.baidu.weather.exception.WeatherParseException;
import com.baidu.weather.model.Future;
import com.baidu.weather.model.LocalDict;
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
                String today = TimeUtil.getChinseMd();
                if (null == date || date.isEmpty() || !date.split(" ")[0].equals(today)) {
                    log.info("抓取天气预测异常，数据源没有更新,city：{},更新时间：{}", cityId, date);
                    return;
                }
            }
            //只存7天数据
            for (int i = 0; i < futures.size() && i < 7; i++) {
                try {
                    Future result = new Future();
                    result.setCityId(cityId);
                    result.setCityName(WeatherManager.fullCity.get(cityId));
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
                        log.info("抓取天气预测异常，CityId:{},日期：{},字段缺失：week,date,源：{}", cityId, date);
                    }
                    //某天天气
                    Selectable weather = futures.get(i).$("tr:nth-child(3)");
                    //早上所有温度数据
                    String mor = weather.$("td:nth-child(1) span:nth-child(1)", "text").regex("-?\\d*~-?\\d*").toString();
                    //晚上所有温度数据
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
                        if (morTemp.length <= 0) {
                            log.warn("温度范围提取出错，CityId:{},日期：{},值：" + mor, cityId, date);
                        }
                        morWind = weather.$("td:nth-child(1) span:nth-child(3)", "text").toString().split(" ");
                        result.setIconCodeDay(morImgUrl.charAt(morImgUrl.length() - 5) + "");
                        try {
                            int weatherCode = Integer.parseInt(result.getIconCodeDay());
                            result.setWeatherDay(WeatherManager.weatherCode.get(weatherCode));
                        } catch (NumberFormatException e) {
                            throw new WeatherParseException("天气代码转换成天气出异常,CityId:" + cityId + ",日期：" + date);
                        }
                    } else {
                        result.setIconCodeDay("");
                    }
                    if (null != noon) {
                        noonTemp = noon.split("~");
                        if (noonTemp.length <= 0) {
                            log.warn("温度范围提取出错，CityId:{},日期：{},值：" + noon, cityId, date);
                        }
                        noonWind = weather.$("td:nth-child(2) span:nth-child(3)", "text").toString().split(" ");
                        result.setIconCodeNight(noonImgUrl.charAt(noonImgUrl.length() - 5) + "");
                        try {
                            int weatherCode = Integer.parseInt(result.getIconCodeNight());
                            result.setWeatherNight(WeatherManager.weatherCode.get(weatherCode));
                        } catch (NumberFormatException e) {
                            throw new WeatherParseException("天气代码转换成天气出异常,CityId:" + cityId + ",日期：" + date);
                        }
                    } else {
                        result.setIconCodeNight("");
                    }
                    //温度
                    List<Integer> temps = new ArrayList<>();
                    for (String temp : morTemp) {
                        temps.add(FilterUtil.returnIntWithRange("temp", temp, cityId, -90, 63));
                    }
                    for (String temp : noonTemp) {
                        temps.add(FilterUtil.returnIntWithRange("temp", temp, cityId, -90, 63));
                    }
                    Collections.sort(temps);
//                futureMaps.put("temp" + (i + 1), temps.get(0) + "/" + temps.get(temps.size() - 1));
                    //温度:temp_low,temp_high
                    if (temps.isEmpty()) {
                        result.setTempHigh("-1000");
                        result.setTempLow("-1000");
                        log.info("抓取天气预测异常，CityId:{},日期：{},字段缺失：temp_high,temp_low", cityId, date);
                    } else {
                        result.setTempLow(temps.get(0) + "");
                        result.setTempHigh(temps.get(temps.size() - 1) + "");
                    }
                    //天气
//                futureMaps.put("img_title" + (i * 2 + 1), morWind[0]);
//                futureMaps.put("img_title" + (i * 2 + 2), noonWind[0]);
                    if (morWind.length == 3) {
                        result.setWindDirectionDay(FilterUtil.returnWithEndOrEquals("wind_direction_day", morWind[1], "风", "无持续风向", cityId));
                        result.setWindScaleDay(FilterUtil.returnWithEndOrEquals("wind_scale_day", morWind[2], "级", "微风", cityId));
                    } else if (morWind.length == 2) {
                        result.setWindDirectionDay(FilterUtil.returnWithEndOrEquals("wind_direction_day", morWind[0], "风", "无持续风向", cityId));
                        result.setWindScaleDay(FilterUtil.returnWithEndOrEquals("wind_scale_day", morWind[1], "级", "微风", cityId));
                    } else {
                        result.setWindDirectionDay("");
                        result.setWindScaleDay("");
                        log.info("抓取天气预测异常，CityId:{},日期：" + date + ",字段缺失：weather_day,wind_direction_day,wind_speed_day,源：{}", cityId, morWind);
                    }

                    if (noonWind.length == 3) {
                        result.setWindDirectionNight(FilterUtil.returnWithEndOrEquals("wind_direction_night", noonWind[1], "风", "无持续风向", cityId));
                        result.setWindScaleNight(FilterUtil.returnWithEndOrEquals("wind_speed_night", noonWind[2], "级", "微风", cityId));
                    } else if (noonWind.length == 2) {
                        result.setWindDirectionNight(FilterUtil.returnWithEndOrEquals("wind_direction_night", noonWind[0], "风", "无持续风向", cityId));
                        result.setWindScaleNight(FilterUtil.returnWithEndOrEquals("wind_speed_night", noonWind[1], "级", "微风", cityId));
                    } else {
                        result.setWindDirectionNight("");
                        result.setWindScaleNight("");
                        log.info("抓取天气预测异常，CityId:{},日期：" + date + ",字段缺失：weather_night,wind_direction_night,wind_speed_night,源：{}", cityId, noonWind);
                    }

                    //天气转变
                    if (!result.getWeatherDay().equals(result.getWeatherNight())) {
                        //                    futureMaps.put("weather" + (i + 1), morWind[0] + "转" + noonWind[0]);
                        result.setWeather(result.getWeatherDay() + "转" + result.getWeatherNight());
                    } else {
                        //                    futureMaps.put("weather" + (i + 1), morWind[0]);
                        result.setWeather(result.getWeatherNight());
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
