package com.baidu.weather.tool;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by edwardsbean on 15-1-15.
 */
public class FilterUtil {
    public static final Logger log = LoggerFactory.getLogger(FilterUtil.class);

    public static void putOrElse(Map map, String key, String data) {
        if (StringUtils.isNumeric(data)) {
            map.put(key, data);
        } else
            map.put(key, -1);
    }

    public static void putOrElseFloat(Map map, String key, String data) {
        try {
            if (data == null || data.length() == 0) {
                map.put(key, -1);
                return;
            }
            Float.parseFloat(data);
        } catch (Exception e) {
            map.put(key, -1);
        }
        map.put(key, data);

    }

    public static void putIsDate(Map map, String key, String date, String flag) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            format.parse(date);
            map.put(key, date);
        } catch (Exception e) {
            map.put(key, "");
            log.warn("数据验证失败，字段:{},城市:{},值:" + date, key, flag);
        }
    }

    public static boolean putIsYYYYHHDate(Map map, String key, String date, String flag) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            format.parse(date);
            map.put(key, TimeUtil.getYYMMdd() + " " + date);
            return true;
        } catch (Exception e) {
            map.put(key, "");
            log.info("数据验证失败，字段:{},城市:{},值:" + date, key, flag);
            return false;
        }
    }


    public static int putIntWithRange(Map map, String key, String data, String cityName, int start, int end) {
        int num;
        try {
            num = Integer.parseInt(data);
            if (num > end) {
                data = end + "";
            }
            if (num < start) {
                throw new NumberFormatException();
            }
            map.put(key, data);
        } catch (NumberFormatException e) {
            log.warn("城市：{}，字段异常：{},值：" + data, cityName, key);
            map.put(key, -1);
            num = -1;
        }
        return num;
    }

    public static int returnIntWithRange(String key, String data, String cityName, int start, int end) {
        int num;
        try {
            num = Integer.parseInt(data);
            if (num > end) {
                data = end + "";
            }
            if (num < start) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            log.warn("城市：{}，字段异常：{},值：" + data, cityName, key);
            num = -1;
        }
        return num;
    }

    public static int putIntWithStart(Map map, String key, String data, String cityName, int start) {
        int num;
        try {
            if ("_".equals(data)) {
                log.info("城市：{}，字段暂无数据：{},值：" + data, cityName, key);
                map.put(key, -1);
                return -1;
            }
            num = Integer.parseInt(data);
            if (num < start) {
                throw new NumberFormatException();
            }
            map.put(key, data);
        } catch (NumberFormatException e) {
            log.warn("城市：{}，字段异常：{},值：" + data, cityName, key);
            map.put(key, -1);
            num = -1;
        }
        return num;
    }

    public static void putFloatWithRange(Map map, String key, String data, String cityName, float start, float end) {
        try {
            if (data == null || data.length() == 0) {
                throw new NumberFormatException();
            }
            float num = Float.parseFloat(data);
            if (num > end || num < start) {
                throw new NumberFormatException();
            }
            map.put(key, data);
        } catch (Exception e) {
            log.info("城市：{}，字段异常：{}，值：" + data, cityName, key);
            map.put(key, -1);
        }
    }

    public static void putFloatWithStart(Map map, String key, String data, String cityName, float start) {
        try {
            if ("_".equals(data)) {
                log.info("城市：{}，字段暂无数据：{},值：" + data, cityName, key);
                map.put(key, -1);
                return;
            }
            if (data == null || data.length() == 0) {
                throw new NumberFormatException();
            }
            float num = Float.parseFloat(data);
            if (num < start) {
                throw new NumberFormatException();
            }
            map.put(key, data);
        } catch (Exception e) {
            log.warn("城市：{}，字段异常：{}，值：" + data, cityName, key);
            map.put(key, -1);
        }
    }

    public static String returnIfContain(List<String> list, String key, String data, String cityName) {
        if (list.contains(data)) {
            return data;
        } else {
            log.info("城市：{}，字段异常：{}，值：" + data, cityName, key);
            return "";
        }
    }

    public static String returnWithEnd(String key, String data, String endWith, String cityName) {
        if (data.endsWith(endWith)) {
            return data;
        } else {
            log.info("城市：{}，字段异常：{}，值：" + data, cityName, key);
            return "";
        }
    }

    public static String returnWithEndOrEquals(String key, String data, String endWith, String equal, String cityName) {

        if (data.endsWith(endWith) || data.equals(equal)) {
            return data;
        } else {
            log.warn("城市：{}，字段异常：{}，值：" + data, cityName, key);
            return "";
        }
    }
}
