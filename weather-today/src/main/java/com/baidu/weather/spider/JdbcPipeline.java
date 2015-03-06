package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.dao.WeatherDao;
import com.baidu.weather.model.Today;
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
            if ("/weather".equals(entry.getKey())) {
                Today today = (Today) entry.getValue();
                if (!today.isComplete()) {
                    /**
                     * 有可能因为部分数据超时丢失，导致聚合失败？
                     */
                    logger.warn("实时天气聚合丢失数据：{}", today);
                    continue;
                }
                Map<String, Object> args = today.getArgs();
                try {
                    if (!args.containsKey("citycode")) {
                        args.put("citycode", today.getPageKey());
                    }
                    if (!args.containsKey("text") && args.containsKey("icon_code")) {
                        args.put("text", WeatherManager.weatherCode.get(args.get("icon_code")));
                    }
                    if (!args.containsKey("cityname")) {
                        args.put("cityname", WeatherManager.fullCity.get(today.getPageKey()));
                    }
                    if (!args.containsKey("source")) {
                        args.put("source", "1");
                    }
                } catch (Exception e) {
                    logger.warn("MuiltiPipeline补充数据出错", e);
                }
                try {
                    weatherDao.insertWeather(today.getArgs());
                } catch (Exception e) {
                    logger.warn("插入实时天气异常," + today, e);
                }
            }

        }
    }
}
