package com.baidu.weather.tool;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by edwardsbean on 15-1-15.
 */
public class FilterUtil {
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
}
