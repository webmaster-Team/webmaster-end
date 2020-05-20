package com.webmaster.end.Utils;

import java.util.Random;

public class RandomUtil {
    static Random random=new Random();
    public static String getRandomNumber(int digits){
        Double max=Math.pow(10,digits);
        int i = random.nextInt(max.intValue());
        return String.format("%0" + digits + "d", i);
    }
    public static String getRandomCard(){
        String s = getRandomNumber(9);
        return "9999"+s;
    }
}
