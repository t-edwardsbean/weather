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
    public static List<City> aqiCities;

    public WeatherManager(ApplicationContext applicationContext) {
        WeatherManager.applicationContext = applicationContext;
    }

    public void init() {
        weatherDao = (WeatherDao) applicationContext.getBean("weatherDao");
        aqiCities = weatherDao.getUSAAQICities();
    }

    public void start() throws JMException {
        init();
        new SpiderJob().grabAQI();
    }


}
