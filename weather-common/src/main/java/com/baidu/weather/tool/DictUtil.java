package com.baidu.weather.tool;

import com.baidu.weather.exception.WeatherException;
import com.baidu.weather.model.City;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by edwardsbean on 15-1-26.
 */
public class DictUtil {
    public static String getCityCode(String cityName, String areaName, List<City> cityDict) {
        for (City city : cityDict) {
            if (city.getCityname().equals(cityName) && (areaName.equals(city.getAreaname()) || areaName.equals(city.getProv()))) {
                return city.getCitycode();
            }
        }
        return null;
    }

    public static String getCityCode(String cityName, List<City> cityDict) {
        for (City city : cityDict) {
            if (city.getCityname().equals(cityName)) {
                return city.getCitycode();
            }
        }
        return null;
    }

    public static String getCityName(String cityNameEn, List<City> cityDict) {
        for (City city : cityDict) {
            if (city.getCitynameen().equals(cityNameEn)) {
                return city.getCityname();
            }
        }
        return null;
    }

    public static String getCityNameByCityCode(String cityCode, List<City> cityDict) {
        for (City city : cityDict) {
            if (city.getCitycode().equals(cityCode)) {
                return city.getCityname();
            }
        }
        return null;
    }
    

    public static City getCityByEn(String cityNameEn, List<City> cityDict) {
        for (City city : cityDict) {
            if (cityNameEn.equals(city.getCitynameen())) {
                return city;
            }
        }
        return null;
    }

    public static Map<String, String> getFullCity(List<City> cities) {
        Map<String, String> result = new HashMap<>();
        for (City city : cities) {
            result.put(city.getCitycode(), city.getCityname());
        }
        return result;
    }

    public static List<String> getWeatherDict(List<City> cities) {
        List<String> dict = new ArrayList<>();
        for (City city : cities) {
            dict.add(city.getCitycode());
        }
        return dict;
    }

    public static Map<String, String> getInverseCity(List<City> cities) {
        Map<String, String> result = new HashMap<>();
        for (City city : cities) {
            result.put(city.getCityname(), city.getCitycode());
        }
        return result;
    }

    public static List<String[]> getProxy() {
        HttpUtil httpUtil = new HttpUtil();
        try {
            List<String[]> result = new ArrayList<>();
            HttpResponse response = httpUtil.doGet("http://tool.sj.91.com/RedisHandler.ashx?act=get&key=Cache:CrawlIP:Data", null);
            String json = EntityUtils.toString(response.getEntity());
            Gson gson = new Gson();
            ProxyReturn proxyReturn = gson.fromJson(json, ProxyReturn.class);
            if (proxyReturn.getReturnCode() != 0) {
                throw new WeatherException("代理接口返回非0状态");
            } else {
                String[] proxies = proxyReturn.getReturnMsg().split(";");
                for (String proxy : proxies) {
                    String[] data = proxy.split(":");
                    if (data.length == 2) {
                        result.add(data);
                    }
                }
                return result;
            }
        } catch (Exception e) {
            throw new WeatherException("获取代理失败", e);
        }

    }

    private class ProxyReturn {
        private int ReturnCode;
        private String ReturnMsg;

        public int getReturnCode() {
            return ReturnCode;
        }

        public void setReturnCode(int returnCode) {
            ReturnCode = returnCode;
        }

        public String getReturnMsg() {
            return ReturnMsg;
        }

        public void setReturnMsg(String returnMsg) {
            ReturnMsg = returnMsg;
        }
    }
}
