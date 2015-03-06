package com.baidu.weather.model;

import java.util.List;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageAirQuality {
    private ThinkPageCity city;
    private List<ThinkPageStation> stations;

    public List<ThinkPageStation> getStations() {
        return stations;
    }

    public void setStations(List<ThinkPageStation> stations) {
        this.stations = stations;
    }

    public ThinkPageCity getCity() {
        return city;
    }

    public void setCity(ThinkPageCity city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "ThinkPageAirQuality{" +
                "city=" + city +
                ", stations=" + stations +
                '}';
    }
}
