package com.baidu.weather.model;

/**
 * 今日相关数据
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageToday {
    private String sunrise;
    private String sunset;
    private ThinkPageSuggestion suggestion;

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public ThinkPageSuggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(ThinkPageSuggestion suggestion) {
        this.suggestion = suggestion;
    }
}
