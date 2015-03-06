package com.baidu.weather.spider;

import com.baidu.weather.dao.WeatherDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * Created by edwardsbean on 15-1-12.
 */
public class JdbcPipeline implements Pipeline {
    private static final Logger logger = LoggerFactory.getLogger(JdbcPipeline.class);
    private WeatherDao weatherDao;

    public JdbcPipeline(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if ("/aqiusa".equals(entry.getKey())) {
                try {
                    Map<String, Object> air = (Map<String, Object>) entry.getValue();
                    weatherDao.insertAir(air);
//                    String cityName = air.get("cityname").toString();
//                    String areaCode = WeatherManager.areaDict.get(cityName);
//                    if (areaCode != null) {
//                        List<String> cityCodes = WeatherManager.areaToCitycode.get(areaCode);
//                        for (String citycode : cityCodes) {
//                            Map<String, Object> child = new HashMap<>();
//                            child.put("citycode", citycode);
//                            child.put("cityname", WeatherManager.fullCity.get(new City(citycode)));
//                            child.put("aqi", air.get("aqi"));
//                            child.put("quality", air.get("quality"));
//                            child.put("color", air.get("color"));
//                            child.put("advice", air.get("advice"));
//                            child.put("pm25", air.get("pm25"));
//                            child.put("pm10", air.get("pm10"));
//                            child.put("so2", air.get("so2"));
//                            child.put("no2", air.get("no2"));
//                            child.put("o3", air.get("o3"));
//                            child.put("co", air.get("co"));
//                            child.put("pubdate", air.get("pubdate"));
//                            child.put("last_update", air.get("last_update"));
//                            child.put("source", air.get("source"));
//                            weatherDao.insertAir(child);
//                        }
//                    }
                } catch (Exception e) {
                    logger.warn("插入空气质量异常，" + entry.getValue(), e);
                }
            }
        }

    }
}
