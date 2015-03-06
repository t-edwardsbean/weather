package com.baidu.weather.model;

import java.util.List;

/**
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageWeather {
    private String city_name;
    private String city_id;
    private String last_update;
    private ThinkPageNow now;
    private ThinkPageToday today;
    private List<ThinkPageFuture> future;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public ThinkPageNow getNow() {
        return now;
    }

    public void setNow(ThinkPageNow now) {
        this.now = now;
    }

    public ThinkPageToday getToday() {
        return today;
    }

    public void setToday(ThinkPageToday today) {
        this.today = today;
    }

    public List<ThinkPageFuture> getFuture() {
        return future;
    }

    public void setFuture(List<ThinkPageFuture> future) {
        this.future = future;
    }

    @Override
    public String toString() {
        return "ThinkPageWeather{" +
                "city_name='" + city_name + '\'' +
                ", city_id='" + city_id + '\'' +
                ", last_update='" + last_update + '\'' +
                ", now=" + now +
                ", today=" + today +
                ", future=" + future +
                '}';
    }
}
