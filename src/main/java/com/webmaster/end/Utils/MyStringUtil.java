package com.webmaster.end.Utils;

public class MyStringUtil {

    /**
     * 字符串去除对应的容易引起混淆的字符
     * @param str 原始字符串
     * @return 处理后的字符串
     */
    public static String toSimpleString(String str){
        return str.trim().replaceAll("[./ ,\\\\]","");
    }


}
