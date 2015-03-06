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
    public static Map<String, String> fullCity;
    //城市中文名 -> code
    public static Map<String, String> inverseFullCity;
    //最完全的，包含了citycode,cityname,areacode,areaname,prov
    public static List<City> cities;
    //AQI,有些地区不需要，黄历天气和墨迹天气都查无此区的，过滤掉
    public static List<String> abandonCity;
    //特殊城市字典，自治县->映射成没有后缀
    public static Map<String, String> specialCity;
    //特殊城市字典，自治州->映射成没有后缀
    public static Map<String, String> specialArea;
    //有些城市改名了，旧城市名 -> 新城市名
    public static Map<String, String> changeName;
    //区域表，非最小单位，没有citycode,只有areacode。参见area表.name -> areacode
    public static Map<String, String> areaDict;
    //区域表与区表映射，areacode -> citycodes
    public static Map<String, List<String>> areaToCitycode;

    public WeatherManager(ApplicationContext applicationContext) {
        WeatherManager.applicationContext = applicationContext;
    }

    private void init() {
        weatherDao = (WeatherDao) applicationContext.getBean("weatherDao");
        cities = weatherDao.getCities();
        fullCity = DictUtil.getFullCity(cities);
        inverseFullCity = DictUtil.getInverseCity(cities);
        LocalDict localDict = (LocalDict) applicationContext.getBean("cityDict");
        abandonCity = localDict.getAbandonCity();
        specialCity = localDict.getSpecialCity();
        specialArea = localDict.getSpecialArea();
        changeName = localDict.getChangeName();
        areaDict = weatherDao.getAreaDict();
        areaToCitycode = weatherDao.getAreaToCitycodeDict();
    }

    public void start() throws JMException {
        init();
        new SpiderJob().grabAQI();
    }


}
