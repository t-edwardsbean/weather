package com.baidu.weather.spider;

import com.baidu.weather.model.City;

import java.util.Map;

/**
 * Created by edwardsbean on 15-3-2.
 */
public class ThinkpageCityDict {
    private Map<String, String> cities;

    public Map<String, String> getCities() {
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }

    public String getCityCode(City city) {
        String cityCode = cities.get(city.getCityname());
        if (cityCode == null) {
            cityCode = cities.get(city.getAreaname() + city.getCityname());
        }
        return cityCode;
    }
}
