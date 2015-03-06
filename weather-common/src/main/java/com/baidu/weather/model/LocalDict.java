package com.baidu.weather.model;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 从配置文件加载字典
 * Created by edwardsbean on 15-1-11.
 */
public class LocalDict {
    private List<String> allCity;
    private List<String> aqiCity;
    private Map<String, String> specialCity;
    private Map<String, String> specialArea;
    private Map<String, String> changeName;
    private Map<Integer, String> weatherCode;
    private List<String> abandonCity;
    public static List<String> windDirection = Arrays.asList("东风", "南风", "西风", "北风", "东南风", "西南风", "西北风", "东北风", "北东北风", "东东北风", "东东南风", "南东南风", "南西南风", "西西南风", "西西北风", "北西北风", "无持续风向");

    public List<String> getAbandonCity() {
        return abandonCity;
    }

    public void setAbandonCity(List<String> abandonCity) {
        this.abandonCity = abandonCity;
    }

    public Map<String, String> getChangeName() {
        return changeName;
    }

    public void setChangeName(Map<String, String> changeName) {
        this.changeName = changeName;
    }

    public Map<Integer, String> getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(Map<Integer, String> weatherCode) {
        this.weatherCode = weatherCode;
    }

    public Map<String, String> getSpecialArea() {
        return specialArea;
    }

    public void setSpecialArea(Map<String, String> specialArea) {
        this.specialArea = specialArea;
    }

    public Map<String, String> getSpecialCity() {
        return specialCity;
    }

    public void setSpecialCity(Map<String, String> specialCity) {
        this.specialCity = specialCity;
    }

    public List<String> getAqiCity() {
        return aqiCity;
    }

    public void setAqiCity(List<String> aqiCity) {
        this.aqiCity = aqiCity;
    }

    public List<String> getAllCity() {
        return allCity;
    }

    public void setAllCity(List<String> allCity) {
        this.allCity = allCity;
    }
}
