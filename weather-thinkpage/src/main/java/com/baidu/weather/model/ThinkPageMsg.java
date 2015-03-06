package com.baidu.weather.model;


import java.util.List;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageMsg {
    private String status;
    private List<ThinkPageWeather> weather;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ThinkPageWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<ThinkPageWeather> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "ThinkPageMsg{" +
                "status='" + status + '\'' +
                ", weather=" + weather +
                '}';
    }
}
