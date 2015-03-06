package com.baidu.weather.exception;

/**
 * Created by edwardsbean on 15-1-16.
 */
public class WeatherException extends RuntimeException {
    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}
