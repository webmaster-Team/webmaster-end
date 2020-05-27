package com.webmaster.end.Utils;

import com.webmaster.end.Entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MyRedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 放Redis里放对应的数据
     * @param key 需要放的键
     * @param value 需要放的值
     */
    public void put(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }


    /**
     * 取Redis里放对应的数据
     * @param key 需要放的键
     */
    public String get(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }


    /**
     * 删除指定的键值对
     * @param key 对应的键
     */
    public void remove(String key){
        redisTemplate.delete(key);
    }


    /**
     * 往Redis里存储对应的Order数据
     * @param order 订单
     */
    public void putOrder(Order order){
        //设置超时时间和值
        redisTemplate.opsForValue().set(order.getSerial(),order,200, TimeUnit.SECONDS);
    }


    /**
     * 从Redis中取出对应的Order
     * @param serial 订单的编号
     * @return 订单对象
     */
    public Order getOrder(String serial){
        return (Order) redisTemplate.opsForValue().get(serial);
    }



}