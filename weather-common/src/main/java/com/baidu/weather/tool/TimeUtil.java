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

    public static String parseMmm2YYYYMMddHHmmss(String date) throws ParseException {
        date = getYYMMdd() + " " + date;
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd H:mm");
        Date now = parse.parse(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(now);
    }
    
    public static String getYYMMddHHmm() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date());
    }

    public static String getBeforeDate(int day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        return format.format(calendar.getTime());
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

    public static String getChinseMd() {
        SimpleDateFormat format = new SimpleDateFormat("M月d日");
        return format.format(new Date());
    }
    
    public static String parseUTC(String origin) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date time = null;
        try {
            time = df.parse(origin);
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return newFormat.format(time);
        } catch (ParseException e) {
            return null;
        }
    }

}
