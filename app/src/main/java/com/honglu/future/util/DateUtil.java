package com.honglu.future.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期计算工具类
 */
public class DateUtil {

    //获取当前日期
    public static String getToday() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String result = formatter.format(curDate);
        return result;
    }

    //获取7天前的日期
    public static String getLastWeekDay() {
        Calendar c = Calendar.getInstance();
        int today = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, today - 7);
        int resultDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String result = formatter.format(c.getTime());
        return result;

    }

    //获取30天前的日期
    public static String getLast30Day() {
        Calendar c = Calendar.getInstance();
        int today = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, today - 30);
        int resultDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String result = formatter.format(c.getTime());
        return result;
    }

    //得到下一天
    public static String getNextDay(String dayFormated) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dayFormated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        String result = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return result;
    }

    //得到下一天
    public static String getNext30Day(String dayFormated) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dayFormated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 30);
        String result = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return result;
    }

    //通过时间戳得到时间
    public static String getDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        int i = Integer.parseInt(time);
        String result = sdf.format(new Date(i * 1000L));
        return result;

    }

    //将序列化时间转成毫秒值
    public static long dateToLong (String in) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(in);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    public static String dataToHHMM(String timeStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date data = dateFormat.parse(timeStr);
            SimpleDateFormat dateHHMM = new SimpleDateFormat("HH:mm");
            return dateHHMM.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    };

    public static boolean compareDate(String strarTime ,String endTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long mStrarTime = simpleDateFormat.parse(strarTime).getTime();
            long mEndTime = simpleDateFormat.parse(endTime).getTime();

            return mStrarTime <= mEndTime ? false : true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
