package com.webmaster.end.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MD5Util {

    /**
     * 传入字符串进行带盐值的加密
     * @param str 字符串
     * @param salt 盐值
     * @return 加密后的密码
     */
    public static String getMD5String(String str,String salt) {
        try {
            str=str+salt;
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用当前的时间作为盐值
     * @return 返回生成的盐值
     */
    public static String getSalt(){
        Date date=new Date();
        return date.getTime()+"";
    }
}