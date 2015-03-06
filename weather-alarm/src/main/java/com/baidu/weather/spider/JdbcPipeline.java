package com.baidu.weather.spider;

import com.baidu.weather.dao.WeatherDao;
import com.baidu.weather.model.Alarm;
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
            if ("/alarm".equals(entry.getKey())) {
                Alarm alarm = (Alarm) entry.getValue();
                try {
                    weatherDao.insertPublish(alarm);
                } catch (Exception e) {
                    logger.warn("插入天气告警异常,请查看warning_type_info_id，是否有该字典" + alarm, e);
                }
            }
        }

    }
}
