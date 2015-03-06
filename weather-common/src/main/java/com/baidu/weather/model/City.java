package com.baidu.weather.model;

/**
 * Created by edwardsbean on 15-1-15.
 */
public class City {
    private String citycode;
    private String areacode;
    private String cityname;
    private String areaname;
    private String prov;
    private String citynameen;

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public City(String citycode, String areacode) {
        this.citycode = citycode;
        this.areacode = areacode;
    }

    public City(String citycode, String areacode, String cityname, String areaname, String prov) {
        this.citycode = citycode;
        this.areacode = areacode;
        this.cityname = cityname;
        this.areaname = areaname;
        this.prov = prov;
    }

    public City(String citycode, String areacode, String cityname, String areaname, String prov, String citynameen) {
        this.citycode = citycode;
        this.areacode = areacode;
        this.cityname = cityname;
        this.areaname = areaname;
        this.prov = prov;
        this.citynameen = citynameen;
    }

    public String getCitynameen() {
        return citynameen;
    }

    public void setCitynameen(String citynameen) {
        this.citynameen = citynameen;
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
