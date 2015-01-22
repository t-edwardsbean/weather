package com.baidu.weather.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by edwardsbean on 15-1-12.
 */
public class TimeUtil {
    public static final String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static String format(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        String result = "";
        try {
            Date date = format.parse(time);
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            result = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getYYMMdd() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public static String getYYMMddHHmm() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date());
    }

    public static String chineseFormat(String chineseTime) {
        Calendar a = Calendar.getInstance();
        return a.get(Calendar.YEAR) + "-" + chineseTime.replace("月", "-").replace("日", "");
    }

    public static String getTodayWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getChinseMMdd() {
        SimpleDateFormat format = new SimpleDateFormat("M月dd日");
        return format.format(new Date());
    }
}
