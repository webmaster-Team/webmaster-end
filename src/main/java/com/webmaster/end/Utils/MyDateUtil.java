package com.webmaster.end.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
