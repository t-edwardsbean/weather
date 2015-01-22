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
public class WeatherMain {
    public static final Logger log = LoggerFactory.getLogger(WeatherMain
            .class);
    
    public static void main(String[] args) {
        try {
            log.info("启动应用程序");
            String address = InetAddress.getLocalHost().getHostAddress();
            MDC.put("host", address);
            MDC.put("source", "weather-spider");
            Properties p = new Properties();
            p.load(WeatherMain.class.getResourceAsStream("/log4j.properties"));
            PropertyConfigurator.configure(p);
            System.setProperty("org.terracotta.quartz.skipUpdateCheck", "true");
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("quartz.xml", "dict.xml", "dao.xml");
            WeatherManager weatherManager = new WeatherManager(applicationContext);
            weatherManager.start();
        } catch (Exception e) {
            log.error("程序异常退出", e);
        }
    }
}
