package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.exception.WeatherException;
import com.baidu.weather.exception.WeatherParseException;
import com.baidu.weather.model.Alarm;
import com.baidu.weather.tool.DictUtil;
import com.baidu.weather.tool.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.SimpleProxyPool;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by edwardsbean on 15-1-11.
 */
public class AlarmProcess implements PageProcessor {
    private static final Logger log = LoggerFactory.getLogger(AlarmProcess.class);
    private static final String ALARM_LIST = "http://product\\.weather\\.com.cn/alarm/.*";
    private static final String ALARM_ITEM_URL = "http://www.weather.com.cn/data/alarm/";
    private static final String ALARM_ITEM = "http://www.weather.com.cn/data/alarm/.*";
    private static final String ALARM_ITEM_INFO_URL = "http://www.weather.com.cn/data/alarminfo/";
    Pattern pattern = Pattern.compile("(新疆|广西|西藏|宁夏|内蒙古)");
    Pattern areaPattern = Pattern.compile("(.*)(市|盟|州|地区)$");
    private Site site = Site.me()
            .addHeader("Referer", "http://m.weather.com.cn/mweather1d/101230501.shtml")
            .setRetryTimes(3)
            .setCycleRetryTimes(6)
            .setSleepTime(3000)
            .setRetrySleepTime(2000)
            .setTimeOut(10000)
//            .setHttpProxyPool(new SimpleProxyPool(DictUtil.getProxy()))
            .setCharset("UTF-8")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");


    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        //告警列表
        if (page.getUrl().regex(ALARM_LIST).match()) {
            try {
                String json = html.regex("(\\{.*\\})").replace("&quot;", "\"").toString();
                page.setSkip(true);
                List<String> list = new JsonPathSelector("$.data").selectList(json);
                for (String id : list) {
                    //告警id
                    String suffix = new JsonPathSelector("$.[1]").select(id);
                    //告警内容
                    page.addTargetRequest(ALARM_ITEM_URL + suffix + "?_=" + System.currentTimeMillis());
                    //防御指南
//                    page.addTargetRequest(ALARM_ITEM_INFO_URL + suffix.substring(suffix.length() - 9));
                }
            } catch (Exception e) {
                log.error("抓取天气告警列表失败,", e);
            }
        }
        //告警内容
        if (page.getUrl().regex(ALARM_ITEM).match()) {
            String json = null;
            try {
                json = html.regex("(\\{.*\\})").replace("&quot;", "\"").toString();
            } catch (Exception e) {
                throw new WeatherException("抓取天气告警失败，数据源可能为空，Url:" + page.getUrl());
            }
            try {
                Alarm alarm = new Alarm();
                String province = new JsonPathSelector("$.PROVINCE").select(json);
                if (province.equals("新疆维吾尔自治区")) {
                    System.out.println();
                }
                String city = new JsonPathSelector("$.CITY").select(json);
                if (WeatherManager.abandonCity.contains(city)) {
                    return;
                }
                String weather = new JsonPathSelector("$.SIGNALTYPE").select(json);
                String color = new JsonPathSelector("$.SIGNALLEVEL").select(json);
                String title = province + city + "气象台" + "发布" + weather + color + "预警";
                List<String> targets = new ArrayList<>();
                if (province.endsWith("市") || province.endsWith("省")) {
                    province = province.substring(0, province.length() - 1);
                } else {
                    Matcher matcher = pattern.matcher(province);
                    if (matcher.find()) {
                        province = matcher.group();
                    } else {
                        throw new WeatherParseException("预警信息中的province字段，没有匹配到对应的区域:" + json);
                    }
                }

                //范围性的告警，省或者直辖区
                if (city.isEmpty()) {
                    //找出该范围内所有城市
                    String provinceCode = WeatherManager.provinceDict.get(province);
                    List<String> areaCodes = WeatherManager.proToAreacode.get(provinceCode);
                    for (String areaCode : areaCodes) {
                        targets.addAll(WeatherManager.areaToCitycode.get(areaCode));
                    }
                    alarm.setTargets(targets);
                    alarm.setCitycode(provinceCode);
                    alarm.setModel("0");
                } else {
                    String cityCode;
                    Matcher m = areaPattern.matcher(city);
                    if (m.find()) {
                        // 范围性告警，从area表中查，查到说明是区域性地区
                        String specialArea = WeatherManager.specialArea.get(city);
                        if (specialArea != null) {
                            city = specialArea;
                        } else if (city.endsWith("地区")) {
                            city = city.substring(0, city.length() - 2);
                        } else {
                            city = city.substring(0, city.length() - 1);
                        }
                        alarm.setModel("1");
                        String areaCode = WeatherManager.areaDict.get(city);
                        if (areaCode == null) {
                            throw new WeatherException("区域表中无该城市:" + city);
                        }
                        List<String> cityCodes = WeatherManager.areaToCitycode.get(areaCode);
                        targets.addAll(cityCodes);
                        alarm.setTargets(targets);
                        alarm.setCitycode(areaCode);
                    } else if (city.endsWith("县") || city.endsWith("区")) {
                        //最小区域
                        String specialCity = WeatherManager.specialCity.get(city);
                        if (specialCity != null) {
                            city = specialCity;
                        } else if (city.endsWith("县") && city.length() == 2) {
                        } else {
                            city = city.substring(0, city.length() - 1);
                        }
                        //为了防止重名，需要通过省市定位
                        cityCode = DictUtil.getCityCode(city, province, WeatherManager.cities);
                        alarm.setModel("2");
                        targets.add(cityCode);
                        alarm.setTargets(targets);
                        alarm.setCitycode(cityCode);
                    } else {
                        throw new WeatherParseException("预警信息中的city字段，没有匹配到对应的区域:" + json);
                    }
                }

                alarm.setProvince(province);
                alarm.setCity_name(city);
                alarm.setContent(new JsonPathSelector("$.ISSUECONTENT").select(json));
                alarm.setPubdate(new JsonPathSelector("$.ISSUETIME").select(json));
                alarm.setLast_update(TimeUtil.getYYMMddHHmm());
                alarm.setAlarm_color(color);
                alarm.setAlarm_type(weather);
                alarm.setTitle(title);
                String code = page.getUrl().regex("-([0-9]*)\\.html").toString();
                alarm.setType_code(code.substring(0, 2));
                alarm.setLevel(code.substring(2));
                //            args.put("imgurl", "http://www.weather.com.cn/m2/i/about/alarmpic/" + code + ".gif");
                alarm.setCode(code);
                alarm.setSource("1");
                page.putField("/alarm", alarm);
            } catch (Exception e) {
                throw new WeatherParseException("解析预警信息链接:" + page.getUrl() + "，源:" + html, e);
            }
//            page.putField("alarm.item.code", page.getUrl().regex("-([0-9]*)\\.html"));

        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new AlarmProcess())
                .addUrl("http://product.weather.com.cn/alarm/grepalarm.php?areaid=%5B0-9%5D%7B5%2C7%7D")
                .addPipeline(new HttpPipeline("http://172.17.150.115:8080", 2))
                .thread(2)
                .run();
    }
}
