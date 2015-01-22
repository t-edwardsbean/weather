package com.baidu.weather;

import com.baidu.weather.dao.WeatherDao;
import com.baidu.weather.model.City;
import com.baidu.weather.model.CityDict;
import com.baidu.weather.model.SugType;
import com.baidu.weather.model.SugTypeInfo;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

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
    public static Map<City, String> fullCity;
    //城市中文名 -> code
    public static Map<String, City> inverseFullCity;
    //中国天气网，所有有天气的区代码
    public static List<String> weatherDict;
    //中国天气网，所有有AQI的区代码
    public static List<String> aqiDict;
    //省市自治区
    public static Map<String, String> provinceDict;
    //区域表，非最小单位，没有citycode,只有areacode。参见area表.name -> areacode
    public static Map<String, String> areaDict;
    //区域表与区表映射
    public static Map<String, List<String>> areaToCitycode;
    //省表与区域表映射
    public static Map<String, List<String>> proToAreacode;
    //特殊城市字典，自治县->citycode
    public static Map<String, String> specialCity;
    //特殊城市字典，自治州->areacode
    public static Map<String, String> specialArea;
    //type -> SugType
    public static Map<String, SugType> sugTypeDict;
    //type+suggestion -> SugTypeInfo(包含sug_type_info_id)
    public static Map<SugTypeInfo, SugTypeInfo> sugTypeInfoDict;


    
    public WeatherManager(ApplicationContext applicationContext) {
        WeatherManager.applicationContext = applicationContext;
    }

    private void init() {
        weatherDao = (WeatherDao) applicationContext.getBean("weatherDao");
        CityDict cityDict = (CityDict) applicationContext.getBean("cityDict");
        weatherDict = cityDict.getAllCity();
        provinceDict = weatherDao.getProvinceDict();
        aqiDict = cityDict.getAqiCity();
        specialCity = cityDict.getSpecialCity();
        specialArea = cityDict.getSpecialArea();
        fullCity = weatherDao.getCityDict();
        inverseFullCity = MapUtils.invertMap(fullCity);
        areaDict = weatherDao.getAreaDict();
        areaToCitycode = weatherDao.getAreaToCitycodeDict();
        proToAreacode = weatherDao.getProToAreaDict();
        sugTypeDict = weatherDao.getSugTypeDict();
        sugTypeInfoDict = weatherDao.getSugTypeInfoDict();

    }

    public void start() {
        init();
    }
}
