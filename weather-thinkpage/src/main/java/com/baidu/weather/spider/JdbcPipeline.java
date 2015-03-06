package com.baidu.weather.spider;

import com.baidu.weather.WeatherManager;
import com.baidu.weather.dao.WeatherDao;
import com.baidu.weather.model.*;
import com.baidu.weather.tool.DictUtil;
import com.baidu.weather.tool.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.*;

/**
 * 实时天气和空气质量每小时更新。预报天气每天更新3次，更新时段为：8-9，12-13，19-20。
 * Created by edwardsbean on 15-3-3.
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
            if ("thinkpage".equals(entry.getKey())) {
                try {
                    ThinkPageWeather weather = (ThinkPageWeather) entry.getValue();
                    String cityName = DictUtil.getCityNameByCityCode(weather.getCity_id(), WeatherManager.cities);
                    String lastUpdate = TimeUtil.getYYMMddHHmm();
                    //实时天气
                    ThinkPageNow now = weather.getNow();
                    Map<String, Object> nowBean = new HashMap<>();
                    nowBean.put("citycode", weather.getCity_id());
                    nowBean.put("cityname", weather.getCity_name());
                    nowBean.put("sunrise", weather.getToday().getSunrise());
                    nowBean.put("sunset", weather.getToday().getSunset());
                    nowBean.put("tail", "");
                    nowBean.put("text", now.getText());
                    nowBean.put("icon_code", now.getCode());
                    nowBean.put("temperature", now.getTemperature());
                    nowBean.put("wind_direction", now.getWind_direction());
                    nowBean.put("wind_speed", now.getWind_speed() + "km/h");
                    nowBean.put("wind_scale", now.getWind_scale() + "级");
                    nowBean.put("humidity", now.getHumidity() + "%");
                    nowBean.put("pressure", now.getPressure());
                    nowBean.put("source", "3");
                    nowBean.put("pubdate", TimeUtil.parseUTC(weather.getLast_update()));
                    nowBean.put("last_update", lastUpdate);
                    weatherDao.insertWeather(nowBean);
                    //aqi
                    ThinkPageAirQuality air = now.getAir_quality();
                    Map<String, Object> airBean = new HashMap<>();
                    airBean.put("citycode", weather.getCity_id());
                    airBean.put("cityname", cityName);
                    airBean.put("aqi", air.getCity().getAqi());
                    airBean.put("quality", air.getCity().getQuality());
                    int num = Integer.parseInt(air.getCity().getAqi());
                    if (num > 0 && 50 > num) {
                        airBean.put("color", "#3bb64f");
                        airBean.put("advice", "空气很好，可以外出活动，呼吸新鲜空气，拥抱大自然！");
                    } else if (num >= 50 && 100 > num) {
                        airBean.put("color", "#ff9900");
                        airBean.put("advice", "空气好，可以外出活动，除极少数对污染物特别敏感的人群以外，对公众没有危害！");
                    } else if (num >= 100 && 150 > num) {
                        airBean.put("color", "#ff6000");
                        airBean.put("advice", "空气一般，老人、小孩及对污染物比较敏感的人群会感到些微不适！");
                    } else if (num >= 150 && 200 >= num) {
                        airBean.put("color", "#f61c1c");
                        airBean.put("advice", "空气较差，老人、小孩及对污染物比较敏感的人群会感到不适！");
                    } else if (num > 200 && 300 > num) {
                        airBean.put("color", "#bb002f");
                        airBean.put("advice", "空气差，适当减少外出活动，老人、小孩出门时需做好防范措施！");
                    } else if (num >= 300) {
                        airBean.put("color", "#7e0808");
                        airBean.put("advice", "空气很差，尽量不要外出活动!");
                    }
                    airBean.put("pm25", air.getCity().getPm25());
                    airBean.put("pm10", air.getCity().getPm10());
                    airBean.put("so2", air.getCity().getSo2());
                    airBean.put("no2", air.getCity().getNo2());
                    airBean.put("o3", air.getCity().getO3());
                    airBean.put("co", air.getCity().getCo());
                    airBean.put("pubdate", TimeUtil.parseUTC(air.getCity().getLast_update()));
                    airBean.put("last_update", lastUpdate);
                    airBean.put("source", "3");
                    weatherDao.insertAir(airBean);

                    //指数和预测需要根据时间判断，一天入库3次
                    Calendar CD = Calendar.getInstance();
                    int hour = CD.get(Calendar.HOUR_OF_DAY);
                    if (hour == 9 || hour == 13 || hour == 20) {
                        //指数
                        //TODO 指数字典需要整理映射
                        ThinkPageSuggestion suggestion = weather.getToday().getSuggestion();

                        //预测
                        List<ThinkPageFuture> futures = weather.getFuture();
                        for (ThinkPageFuture future : futures) {
                            Future futureBean = new Future();
                            futureBean.setCityId(weather.getCity_id());
                            futureBean.setCityName(weather.getCity_name());
                            String weatherAll = future.getText().replace("/", "转");
                            futureBean.setWeather(weatherAll);
                            futureBean.setDate(future.getDate());
                            futureBean.setIconCodeDay(future.getCode1());
                            futureBean.setIconCodeNight(future.getCode2());
                            futureBean.setLast_update(lastUpdate);
                            futureBean.setPubDate(lastUpdate);
                            futureBean.setSource("3");
                            futureBean.setTempHigh(future.getHigh());
                            futureBean.setTempLow(future.getLow());
                            futureBean.setWeek(future.getDay());
                            String[] weatherDayNight = future.getText().split("/");
                            if (weatherDayNight.length == 2) {
                                futureBean.setWeatherDay(weatherDayNight[0]);
                                futureBean.setWeatherNight(weatherDayNight[1]);
                            } else {
                                futureBean.setWeatherDay(weatherDayNight[0]);
                                futureBean.setWeatherNight(weatherDayNight[0]);
                            }
                            futureBean.setWindDirectionDay("无持续风向");
                            futureBean.setWindDirectionNight("无持续风向");
                            String wind = future.getWind();
                            if (wind.contains("级")) {
                                wind = wind.replace("微风", "");
                            }
                            futureBean.setWindScaleDay(wind);
                            futureBean.setWindScaleDay(wind);
                            weatherDao.insertFuture(futureBean);
                        }
                    }


                } catch (Exception e) {
                    logger.warn("插入空气质量异常，" + entry.getValue(), e);
                }
            }
        }

    }
}
