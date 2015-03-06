package com.baidu.weather;

import com.baidu.weather.dao.WeatherDao;
import com.baidu.weather.model.City;
import com.baidu.weather.model.SugType;
import com.baidu.weather.model.SugTypeInfo;
import com.baidu.weather.schedule.SpiderJob;
import com.baidu.weather.tool.DictUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.management.JMException;
import java.util.List;
import java.util.Map;

/**
 * Created by edwardsbean on 15-1-14.
 */
public class WeatherManager {
    public static final Logger log = LoggerFactory.getLogger(WeatherManager
            .class);
    public static ApplicationContext applicationContext;
    public static WeatherDao weatherDao;
    //code -> 城市中文名
    public static Map<String, String> fullCity;
    public static List<City> cities;
    public static List<String> weatherDict;
    //type -> SugType
    public static Map<String, SugType> sugTypeDict;
    //type+suggestion -> SugTypeInfo(包含sug_type_info_id)
    public static Map<SugTypeInfo, SugTypeInfo> sugTypeInfoDict;

    public WeatherManager(ApplicationContext applicationContext) {
        WeatherManager.applicationContext = applicationContext;
    }

    public void init() {
        weatherDao = (WeatherDao) applicationContext.getBean("weatherDao");
        cities = weatherDao.getCities();
        fullCity = DictUtil.getFullCity(cities);
        weatherDict = DictUtil.getWeatherDict(cities);
        sugTypeDict = weatherDao.getSugTypeDict();
        sugTypeInfoDict = weatherDao.getSugTypeInfoDict();
    }

    public void start() throws JMException {
        init();
        new SpiderJob().grabSuggestion();
    }


}
