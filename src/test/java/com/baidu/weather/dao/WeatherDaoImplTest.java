package com.baidu.weather.dao;

import com.baidu.weather.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:dao.xml")
public class WeatherDaoImplTest {
    @Autowired
    WeatherDao weatherDao;

    @Test
    public void testGetCityDict() throws Exception {
        Map<City, String> result = weatherDao.getCityDict();
        Assert.notEmpty(result);
    }

    @Test
    public void testInsertSuggestion() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("citycode", "101010100");
        params.put("cityname", "scy SB");
        params.put("typename", "穿衣指数");
        params.put("pubdate", "2015-01-19");
        params.put("sug_type_info_id","140912");
        params.put("last_update", "2015-01-19");
        params.put("type", "dressing");
        params.put("info", "lol_killed");
        params.put("source","1");
        params.put("suggestion", "lulululululu");
        weatherDao.insertSug(params);
    }

    @Test
    public void testInsertAlarms() throws Exception{
        Alarm a = new Alarm();
        a.setCitycode("101010100");
        a.setCity_name("北京");
        a.setTitle("告警啦");
        a.setContent("暴雨暴雪暴风雷电");
        a.setModel("2");
        a.setCode("0102");
        a.setSource("1");
        a.setPubdate("1990-04-08");
        a.setLast_update("1990-05-06");
        Alarm b = new Alarm();
        b.setCitycode("101010101");
        b.setCity_name("上海");
        b.setTitle("告警啦");
        b.setModel("2");
        b.setContent("上海大雨暴雨");
        b.setCode("0103");
        b.setSource("1");
        b.setPubdate("1990-04-08");
        b.setLast_update("1990-05-06");
        weatherDao.insertPublish(a);
        weatherDao.insertPublish(b);
    }

    public void testInsertAir() throws Exception{

    }

    @Test
    public void testInsertWeather() throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("citycode","101010100");
        map.put("cityname","testname");
        map.put("sunrise","7:26");
        map.put("sunset","16.59");
        map.put("text","晴");
        map.put("icon_code","d00");
        map.put("temperature","6");
        map.put("wind_direction","西北风");
        map.put("wind_speed","12km/mmm");
        map.put("wind_scale","111级");
        map.put("humidity","%66");
        map.put("pressure","1023");
        map.put("source","99");
        map.put("pubdate","2015-01-21 15:05:00");
        map.put("last_update","2015-01-21 15:26:00");
        weatherDao.insertWeather(map);
    }

    @Test
    public void testInsertFuture() throws Exception{
        Future fut = new Future();
        fut.setCityId("101021300");
        fut.setCityName("piking");
        fut.setWeek("星期三");
        fut.setDate("2015-12-09 00:00:00");
        fut.setWeather("暴雨");
        fut.setIconCodeDay("3");
        fut.setIconCodeNight("1");
        fut.setTempLow("1");
        fut.setTempHigh("10");
        fut.setWindDirectionDay("东南");
        fut.setWindDirectionNight("西北");
        fut.setWindSpeedDay("5");
        fut.setWindSpeedNight("7");
        fut.setWeatherDay("小雨");
        fut.setWeatherNight("中雨");
        fut.setPubDate("2010-01-01");
        fut.setLast_update("2010-01-01");
        fut.setSource("1");
        weatherDao.insertFuture(fut);
    }

    @Test
    public void testGetProvinceDict() throws Exception {
        Map<String, String> provinces = weatherDao.getProvinceDict();
        Assert.notEmpty(provinces);
    }

    @Test
    public void testGetAreaDict() throws Exception {
        Map<String, String> areaDict = weatherDao.getAreaDict();
        Assert.notEmpty(areaDict);
    }

    @Test
    public void testGetAreaToCitycodeDict() throws Exception {
        Map<String, List<String>> results = weatherDao.getAreaToCitycodeDict();
        Assert.notEmpty(results);
    }

    @Test
    public void testGetProToAreaDict() throws Exception {
        Map<String, List<String>> results = weatherDao.getProToAreaDict();
        Assert.notEmpty(results);
    }

    @Test
    public void testGetSugTypeDict() throws Exception {
        Map<String, SugType> dict = weatherDao.getSugTypeDict();
        Assert.notEmpty(dict);
    }

    @Test
    public void testGetSugTypeInfoDict() throws Exception {
        Map<SugTypeInfo, SugTypeInfo> dict = weatherDao.getSugTypeInfoDict();
        SugTypeInfo sugTypeInfo = new SugTypeInfo("dressing", "舒适");
        SugTypeInfo result = dict.get(sugTypeInfo);
        Assert.notNull(result);
    }

}