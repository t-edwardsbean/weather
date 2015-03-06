package com.baidu.weather;

import com.baidu.weather.dao.WeatherDao;
import com.baidu.weather.model.City;
import com.baidu.weather.model.LocalDict;
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
    //最完全的，包含了citycode,cityname,areacode,areaname,prov
    public static List<City> cities;
    public static List<String> weatherDict;
    //天气代码 -> 天气名称
    public static Map<Integer, String> weatherCode;

    public WeatherManager(ApplicationContext applicationContext) {
        WeatherManager.applicationContext = applicationContext;
    }

    public void init() {
        weatherDao = (WeatherDao) applicationContext.getBean("weatherDao");
        cities = weatherDao.getCities();
        fullCity = DictUtil.getFullCity(cities);
        weatherDict = DictUtil.getWeatherDict(cities);
        LocalDict localDict = (LocalDict) applicationContext.getBean("cityDict");
        weatherCode = localDict.getWeatherCode();
    }

    public void start() throws JMException {
        init();
        new SpiderJob().grabFuture();
    }


}
