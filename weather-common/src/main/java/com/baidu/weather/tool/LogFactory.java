package com.baidu.weather.tool;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 日志工厂
 *
 * @author sean
 */
public class LogFactory {
    private static PropertyConfigurator pc = new PropertyConfigurator();
    private static Map<String, Logger> container = new HashMap<String, Logger>();

    public static synchronized Logger getLogger(String name, Class clazz) {
        String className = name + ":" + clazz.getSimpleName();
        Logger logger = container.get(className);
        if (logger == null) {
            try {
                logger = Logger.getLogger(className);

                Properties p = new Properties();
                p.load(LogFactory.class.getResourceAsStream("/log4j.properties"));
                p.setProperty("log4j.appender.file.File", "./logs/" + name + ".log");

                LoggerRepository lr1 = new Hierarchy(logger);
                pc.doConfigure(p, lr1);

                container.put(className, logger);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

}
