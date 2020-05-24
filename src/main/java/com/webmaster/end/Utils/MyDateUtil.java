package com.webmaster.end.Utils;

import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.Rental;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyDateUtil {

    /**
     * 将对应的Date转换成数据库的字符串样式
     * @param date 日期
     * @return 转换后的字符串
     */
    public static String convertDateToStr(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    /**
     * 将字符串转化成日期
     * @param time 字符串
     * @return 日期对象
     */
    public static Date convertStrToDate(String time){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        try {
            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按照格式返回当前时间的字符串
     * @return
     */
    public static String getCurrentString(){
        return convertDateToStr(new Date());
    }

    /**
     * 返回当前Long的当前时间
     * @return
     */
    public static long getCurrentTime(){
        return new Date().getTime();
    }

    /**
     * 比较第一个时间是不是更早
     *
     * @param firstDate 第一个
     * @param secondDate 第二个时间
     * @return 第一个时间是否更早
     */
    public static boolean isFirstDatePrevious(String firstDate, String secondDate){
        Date date1 = convertStrToDate(firstDate);
        Date date2 = convertStrToDate(secondDate);
        return date1.compareTo(date2)<0;
    }

    /**
     * 计算距离归还
     * @param rental 流水
     * @return 计算距离天数
     */
    public static int reckonDateDistance(Rental rental){
        Date borrowDate = convertStrToDate(rental.getBorrowTime());
        Date currenDate = new Date();
        long returnTime=borrowDate.getTime()+(((long)rental.getDuration())*24*3600*1000);
        long currentTime = currenDate.getTime();
        return (int) ((returnTime-currentTime)/(1000*60*60*24));
    }
}
