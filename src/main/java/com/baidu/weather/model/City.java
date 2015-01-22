package com.baidu.weather.model;

/**
 * Created by edwardsbean on 15-1-15.
 */
public class City {
    private String citycode;
    private String areacode;
    private String cityname;

    public City(String citycode, String areacode) {
        this.citycode = citycode;
        this.areacode = areacode;
    }

    public City(String citycode) {
        this.citycode = citycode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getCityname() {
        return cityname;
    }

    @Override
    public int hashCode() {
        return citycode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (!citycode.equals(city.citycode)) return false;

        return true;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }
}
