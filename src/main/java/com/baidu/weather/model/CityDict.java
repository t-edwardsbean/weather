package com.baidu.weather.model;

import java.util.List;
import java.util.Map;

/**
 * 从配置文件加载字典 
 * Created by edwardsbean on 15-1-11.
 */
public class CityDict {
    private List<String> allCity;
    private List<String> aqiCity;
    private Map<String, String> specialCity;
    private Map<String, String> specialArea;

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
