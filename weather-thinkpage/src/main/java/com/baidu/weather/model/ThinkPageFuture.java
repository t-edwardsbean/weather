package com.baidu.weather.model;

/**
 * Created by edwardsbean on 15-3-3.
 */
public class ThinkPageFuture {
    private String date;           //日期
    private String day; //星期
    private String text;    //天气情况文字 (白天和晚间通过斜线“/”分割。若白天晚间情况一致，则只保留一个值)
    private String code1;   //白天天气情况代码 (天气代码与天气图标对应说明)
    private String code2;   //晚间天气情况代码 (天气代码与天气图标对应说明)
    private String high;    //最高温度 (无数据时为"-")
    private String low; //最低温度 (无数据时为"-")
    private String cop; //降水概率
    private String wind;    //风向风力 **V1.0新增**

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getCop() {
        return cop;
    }

    public void setCop(String cop) {
        this.cop = cop;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
