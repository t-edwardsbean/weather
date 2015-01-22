package com.baidu.weather.model;

/**
 * Created by edwardsbean on 15-1-13.
 */
public class Future {
    String cityName;
    String cityId;
    String week;
    String date;
    String iconCodeDay;
    String iconCodeNight;
    String tempLow;
    String tempHigh;
    String windDirectionDay;
    String windDirectionNight;
    String windSpeedDay;
    String windSpeedNight;
    String weatherDay;
    String weatherNight;
    String last_update;
    String pubDate;
    String source;
    String weather;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeatherDay() {
        return weatherDay;
    }

    public void setWeatherDay(String weatherDay) {
        this.weatherDay = weatherDay;
    }

    public String getWeatherNight() {
        return weatherNight;
    }

    public void setWeatherNight(String weatherNight) {
        this.weatherNight = weatherNight;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIconCodeDay() {
        return iconCodeDay;
    }

    public void setIconCodeDay(String iconCodeDay) {
        this.iconCodeDay = iconCodeDay;
    }

    public String getIconCodeNight() {
        return iconCodeNight;
    }

    public void setIconCodeNight(String iconCodeNight) {
        this.iconCodeNight = iconCodeNight;
    }

    public String getTempLow() {
        return tempLow;
    }

    public void setTempLow(String tempLow) {
        this.tempLow = tempLow;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(String tempHigh) {
        this.tempHigh = tempHigh;
    }

    public String getWindDirectionDay() {
        return windDirectionDay;
    }

    public void setWindDirectionDay(String windDirectionDay) {
        this.windDirectionDay = windDirectionDay;
    }

    public String getWindDirectionNight() {
        return windDirectionNight;
    }

    public void setWindDirectionNight(String windDirectionNight) {
        this.windDirectionNight = windDirectionNight;
    }

    public String getWindSpeedDay() {
        return windSpeedDay;
    }

    public void setWindSpeedDay(String windSpeedDay) {
        this.windSpeedDay = windSpeedDay;
    }

    public String getWindSpeedNight() {
        return windSpeedNight;
    }

    public void setWindSpeedNight(String windSpeedNight) {
        this.windSpeedNight = windSpeedNight;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "Future{" +
                "cityName='" + cityName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", week='" + week + '\'' +
                ", date='" + date + '\'' +
                ", iconCodeDay='" + iconCodeDay + '\'' +
                ", iconCodeNight='" + iconCodeNight + '\'' +
                ", tempLow='" + tempLow + '\'' +
                ", tempHigh='" + tempHigh + '\'' +
                ", windDirectionDay='" + windDirectionDay + '\'' +
                ", windDirectionNight='" + windDirectionNight + '\'' +
                ", windSpeedDay='" + windSpeedDay + '\'' +
                ", windSpeedNight='" + windSpeedNight + '\'' +
                ", weatherDay='" + weatherDay + '\'' +
                ", weatherNight='" + weatherNight + '\'' +
                ", last_update='" + last_update + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", source='" + source + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }

    public void setSource(String source) {
        this.source = source;
    }
}
