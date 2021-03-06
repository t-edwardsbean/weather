package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.dao.WeatherDao;
import com.baidu.weather.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashMap;
import java.util.List;
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
            if ("/suggestion".equals(entry.getKey())) {
                List<Map<String, Object>> sugs = (List<Map<String, Object>>) entry.getValue();
                for (Map<String, Object> sug : sugs) {
                    try {
                        String suggestion = (String) sug.get("suggestion");
                        String info = (String) sug.get("info");
                        String type = (String) sug.get("type");
                        String typeName = (String) sug.get("typename");
                        if (suggestion == null || info == null) {
                            logger.info("该城市暂无该指数：{},{}", typeName, sug);
                            continue;
                        }
                        //sug_type字典表中是否有该指数类型
                        if (!WeatherManager.sugTypeDict.containsKey(type)) {
                            SugType sugType = new SugType(type, typeName);
                            weatherDao.insertSugType(sugType);
                            WeatherManager.sugTypeDict.put(type, sugType);
                        }
                        //sug_type_info字典表中是否有该指数类型的具体建议
                        SugTypeInfo key = new SugTypeInfo(type, suggestion);
                        SugTypeInfo sugTypeInfo = WeatherManager.sugTypeInfoDict.get(key);
                        if (sugTypeInfo != null) {
                            //有,直接将数据插入sug表
                            sug.put("sug_type_info_id", sugTypeInfo.getId());
                        } else {
                            //没有，补充现有字典
                            sugTypeInfo = new SugTypeInfo(type, suggestion, info);
                            int sugTypeInfoId = weatherDao.insertSugTypeInfo(sugTypeInfo);
                            sugTypeInfo.setId(sugTypeInfoId);
                            WeatherManager.sugTypeInfoDict.put(key, sugTypeInfo);
                            sug.put("sug_type_info_id", sugTypeInfoId);
                        }
                        weatherDao.insertSug(sug);
                    } catch (Exception e) {
                        logger.warn("插入指数异常，" + sug, e);
                    }
                }
            }
        }

    }
}
