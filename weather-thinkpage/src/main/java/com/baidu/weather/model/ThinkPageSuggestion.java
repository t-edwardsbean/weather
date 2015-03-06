package com.baidu.weather.model;

/**
 * 生活建议指数 **V1.0新增**
 * Created by edwardsbean on 14-11-5.
 */
public class ThinkPageSuggestion {
    
    private ThinkPageSuggestionInfo dressing;
    private ThinkPageSuggestionInfo uv;
    private ThinkPageSuggestionInfo car_washing;
    private ThinkPageSuggestionInfo travel;
    private ThinkPageSuggestionInfo flu;
    private ThinkPageSuggestionInfo sport;

    public ThinkPageSuggestionInfo getDressing() {
        return dressing;
    }

    public void setDressing(ThinkPageSuggestionInfo dressing) {
        this.dressing = dressing;
    }

    public ThinkPageSuggestionInfo getUv() {
        return uv;
    }

    public void setUv(ThinkPageSuggestionInfo uv) {
        this.uv = uv;
    }

    public ThinkPageSuggestionInfo getCar_washing() {
        return car_washing;
    }

    public void setCar_washing(ThinkPageSuggestionInfo car_washing) {
        this.car_washing = car_washing;
    }

    public ThinkPageSuggestionInfo getTravel() {
        return travel;
    }

    public void setTravel(ThinkPageSuggestionInfo travel) {
        this.travel = travel;
    }

    public ThinkPageSuggestionInfo getFlu() {
        return flu;
    }

    public void setFlu(ThinkPageSuggestionInfo flu) {
        this.flu = flu;
    }

    public ThinkPageSuggestionInfo getSport() {
        return sport;
    }

    public void setSport(ThinkPageSuggestionInfo sport) {
        this.sport = sport;
    }
}
