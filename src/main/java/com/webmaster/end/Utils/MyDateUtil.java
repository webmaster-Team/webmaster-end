package com.webmaster.end.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateUtil {
    public static String convertDateToStr(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }
}
