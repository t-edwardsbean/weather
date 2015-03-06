package com.baidu.weather;

import com.baidu.weather.dao.WeatherDao;
import com.baidu.weather.model.City;
import com.baidu.weather.schedule.SpiderJob;
import com.baidu.weather.spider.ThinkpageCityDict;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by edwardsbean on 15-3-2.
 */
public class WeatherManager {
    public static List<City> cities;
    public static WeatherDao weatherDao;
    public static ThinkpageCityDict thinkpageCityDict;
    public static ApplicationContext applicationContext;


    public WeatherManager(ApplicationContext applicationContext) {
        WeatherManager.applicationContext = applicationContext;
    }

    public void init() {
        weatherDao = (WeatherDao) applicationContext.getBean("weatherDao");
        thinkpageCityDict = (ThinkpageCityDict) applicationContext.getBean("thinkpageCityDict");
        cities = weatherDao.getCities();
    }

    public void start() {
        init();
        new SpiderJob().grabAll();
    }

}
