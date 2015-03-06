package com.baidu.weather.spider;

import com.alibaba.fastjson.JSON;
import com.baidu.weather.exception.WeatherException;
import com.baidu.weather.exception.WeatherParseException;
import com.baidu.weather.model.ThinkPageMsg;
import com.baidu.weather.model.ThinkPageWeather;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by edwardsbean on 15-3-2.
 */
public class AllProcess implements PageProcessor {
    public static final Logger log = LoggerFactory.getLogger(AllProcess.class);

    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setCycleRetryTimes(3)
            .setTimeOut(10000);

    @Override
    public void process(Page page) {
        String json = page.getHtml().toString();
        String cityCode = page.getUrl().regex("(cityCode=.*)").toString();
        String status = JsonPath.read(json, "$.status");
        if (!status.equals("OK")) {
            throw new WeatherException("获取数据错误：" + json);
        }
        ThinkPageMsg msg;
        try {
            msg = JSON.parseObject(json, ThinkPageMsg.class);
            //TODO 如果是支持一次取多个城市，则不能通过这种方式设置citycode
            msg.getWeather().get(0).setCity_id(cityCode);
        } catch (Exception e) {
            throw new WeatherParseException("转换Json数据出错，数据来源格式有问题:" + json);
        }
        for (ThinkPageWeather weather : msg.getWeather()) {
            page.putField("thinkpage",weather);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }
}
