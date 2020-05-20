package com.webmaster.end.Utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MyHttpClient {
    static CloseableHttpClient httpClient=HttpClients.createDefault();

    public static JSONObject post(String url, Map<String,Object> map){
        HttpPost post = new HttpPost(url);
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        post.addHeader("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        if(map!=null) {
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            for (entries.iterator(); iterator.hasNext(); ) {
                Map.Entry<String, Object> next = iterator.next();
                jsonObject.put(next.getKey(), next.getValue());
            }
        }
        try {
            if(!jsonObject.isEmpty()) {
                StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(), "UTF-8");
                stringEntity.setContentType("application/json");
                post.setEntity(stringEntity);
            }
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            System.out.println(s);
            JSONObject object = (JSONObject) JSONObject.parse(s);
            return object;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }
        return null;
    }

    public static JSONObject get(String url, Map<String,Object> map){
        if(map!=null) {
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            int cnt=0;
            for (entries.iterator(); iterator.hasNext(); ) {
                Map.Entry<String, Object> next = iterator.next();
                if(cnt==0)
                    url+=("?"+next.getKey()+"="+next.getValue());
                else
                    url+=("&"+next.getKey()+"="+next.getValue());
                cnt++;
            }
        }
        HttpGet get = new HttpGet(url);
        get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        get.addHeader("Content-Type", "application/x-www-form-urlencoded");
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(s);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
