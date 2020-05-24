package com.webmaster.end.Entity;

import java.util.HashMap;
import java.util.Map;

public class ResultMap {
    public static Map<String,Object> getResultMap(Object state,String msg){
        HashMap<String, Object> map = new HashMap<>();
        map.put("state",state);
        map.put("msg",msg);
        return  map;
    }
}
