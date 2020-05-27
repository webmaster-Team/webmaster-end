package com.webmaster.end.Utils;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    static Random random=new Random();

    /**
     * 生成一个随机的指定位数的数字字符串
     * @param digits 位数
     * @return 一个指定位数的随机数字字符串
     */
    public static String getRandomNumber(int digits){
        Double max=Math.pow(10,digits);
        int i = random.nextInt(max.intValue());
        return String.format("%0" + digits + "d", i);
    }

    /**
     * 生成一个随机的9999开头的学号，用于三方登录
     * @return 返回生成的随机学号
     */
    public static String getRandomCard(){
        String s = getRandomNumber(9);
        return "9999"+s;
    }

    /**
     * 获得一个UUID
     * @return 返回一个十六位的UUID
     */
    public static String getRandomUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
