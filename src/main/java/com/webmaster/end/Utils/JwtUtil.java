package com.webmaster.end.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    // 给定一个算法，如HmacSHA-256
    static Algorithm alg = Algorithm.HMAC256("Skye");

    //加密算法
    //key是服务器的key，param是用户信息，salty是为了浏览器在不同时间生成不同的Token
    public static String encode(Map<String,Object> param){
        // 1 签发Token
        Date currentTime = new Date();
        String token = JWT.create()
                .withIssuer("skye") // 发行者
                .withSubject("userid") // 用户身份标识,表明是id
                .withAudience("123.56.3.135") // 用户单位
                .withIssuedAt(currentTime) // 签发时间
                .withExpiresAt(new Date(currentTime.getTime() + 24*3600*1000L)) // 设置有效期，一天有效期
                .withJWTId("001") // 分配JWT的ID
                .withClaim("id", (int)param.get("id")) // 定义公共域信息
                .sign(alg);
        return token;
    }

    //解密算法
    public  static Map<String,Object>  decode(String token){
        Map<String,Object> result=new HashMap<>();

        JWTVerifier verifier = JWT.require(alg)//构建一个验证器
                .withIssuer("skye")
                .withAudience("123.56.3.135")
                .build();
        try{
            verifier.verify(token);
            result.put("isSuccess",true);//验证成功
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            result.put("isSuccess",false);//验证失败
        }

        //尝试解码
        try{
            //进行解码
            DecodedJWT originToken = JWT.decode(token);
            //添加发行者
            result.put("Issuser",originToken.getIssuer());
            //添加发行时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            result.put("tokenDate",sdf.format(originToken.getIssuedAt()));
            //添加发行的私有信息
            result.put("id",originToken.getClaim("id").asInt());
        } catch (JWTDecodeException e){
            e.printStackTrace();
        }
        return result;
    }
}
