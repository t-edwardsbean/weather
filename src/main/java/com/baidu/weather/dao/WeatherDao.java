package com.baidu.weather.dao;

import com.baidu.weather.model.*;

import java.util.List;
import java.util.Map;

/**
 * Created by linhao01_91 on 2015/1/12.
 */
public interface WeatherDao {

    public Map<City, String> getCityDict();

    public void insertWeather(Map<String, Object> weather);

    public void insertAir(Map<String, Object> air);

    public void insertSug(Map<String, Object> suggestion);

    public void insertSugType(SugType sugType);

    public int insertSugTypeInfo(SugTypeInfo sugTypeInfo);

    public void insertFuture(Future future);

    public Map<String, String> getProvinceDict();

    public Map<String, String> getAreaDict();

    public void insertPublish(Alarm alarm);

    public Map<String, List<String>> getAreaToCitycodeDict();

    public Map<String, List<String>> getProToAreaDict();

    public Map<String, SugType> getSugTypeDict();

    public Map<SugTypeInfo, SugTypeInfo> getSugTypeInfoDict();

}
