package com.baidu.weather.model;

/**
 * Created by edwardsbean on 15-3-3.
 */
public class ThinkPageNow {
    private String text;//天气情况文字
    private String code;//天气情况代码 (天气代码与天气图标对应说明)
    private String temperature;//当前实时温度
    private String feels_like;//当前实时体感温度
    private String wind_direction;//风向
    private String wind_speed;     //风速。单位：km/h
    private String wind_scale;//风力等级。根据风速计算的风力等级，参考百度百科定义：风力等级。**V1.0新增**
    private String humidity;//湿度。单位：百分比%
    private String visibility;       //能见度。单位：公里km
    private String pressure;      //气压。单位：百帕hPa
    private String pressure_rising;   //气压变化。0或steady为稳定，1或rising为升高，2或falling为降低

    private ThinkPageAirQuality air_quality;

    public ThinkPageAirQuality getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(ThinkPageAirQuality air_quality) {
        this.air_quality = air_quality;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(String feels_like) {
        this.feels_like = feels_like;
    }

    public String getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(String wind_direction) {
        this.wind_direction = wind_direction;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getWind_scale() {
        return wind_scale;
    }

    public void setWind_scale(String wind_scale) {
        this.wind_scale = wind_scale;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPressure_rising() {
        return pressure_rising;
    }

    public void setPressure_rising(String pressure_rising) {
        this.pressure_rising = pressure_rising;
    }

    @Override
    public String toString() {
        return "ThinkPageNow{" +
                "text='" + text + '\'' +
                ", code='" + code + '\'' +
                ", temperature='" + temperature + '\'' +
                ", feels_like='" + feels_like + '\'' +
                ", wind_direction='" + wind_direction + '\'' +
                ", wind_speed='" + wind_speed + '\'' +
                ", wind_scale='" + wind_scale + '\'' +
                ", humidity='" + humidity + '\'' +
                ", visibility='" + visibility + '\'' +
                ", pressure='" + pressure + '\'' +
                ", pressure_rising='" + pressure_rising + '\'' +
                ", air_quality=" + air_quality +
                '}';
    }
}
