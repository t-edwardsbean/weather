package com.baidu.weather;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetAddress;
import java.util.Properties;

/**
 * Created by edwardsbean on 15-1-5.
 */
public class AQIMain {
    public static final Logger log = LoggerFactory.getLogger(AQIMain
            .class);

    public static void main(String[] args) {
        try {
            log.info("启动应用程序");
            String address = InetAddress.getLocalHost().getHostAddress();
            MDC.put("host", address);
            MDC.put("source", "pm25aqi-spider");
            Properties p = new Properties();
            p.load(AQIMain.class.getResourceAsStream("/log4j.xml"));
            PropertyConfigurator.configure(p);
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("dict.xml", "dao.xml");
            WeatherManager weatherManager = new WeatherManager(applicationContext);
            weatherManager.start();
        } catch (Exception e) {
            log.error("程序异常退出", e);
        }
    }
}
