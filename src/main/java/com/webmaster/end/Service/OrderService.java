package com.webmaster.end.Service;

import com.webmaster.end.Utils.MyDateUtil;
import com.webmaster.end.Utils.MyRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private MyRedisUtil redisUtil;
    /**
     * 从Redis中读取对应的数据存储到数据库中
     * @param key 对应的键
     * @return
     */
    public String putOrderToDB(String key){
        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                redisUtil.put("aaa","我真帅");
            }
        }, new Date(MyDateUtil.getCurrentTime()+1000*10));
        return "创建完毕";
    }

}
