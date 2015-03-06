package com.baidu.weather.model;

import java.util.List;

/**
 * Created by edwardsbean on 15-1-14.
 */
public class Alarm {
    private String province;
    private String model;
    private String citycode;
    private String city_name;
    private String content;
    private String pubdate;
    private String last_update;
    private String alarm_color;
    private String alarm_type;
    private String title;
    private String type_code;
    private String level;
    private String code;
    private String source;
    private List<String> targets;

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getAlarm_color() {
        return alarm_color;
    }

    public void setAlarm_color(String alarm_color) {
        this.alarm_color = alarm_color;
    }

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "province='" + province + '\'' +
                ", model='" + model + '\'' +
                ", citycode='" + citycode + '\'' +
                ", city_name='" + city_name + '\'' +
                ", content='" + content + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", last_update='" + last_update + '\'' +
                ", alarm_color='" + alarm_color + '\'' +
                ", alarm_type='" + alarm_type + '\'' +
                ", title='" + title + '\'' +
                ", type_code='" + type_code + '\'' +
                ", level='" + level + '\'' +
                ", code='" + code + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
