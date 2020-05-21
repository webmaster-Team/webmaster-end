package com.webmaster.end.Utils;

import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Entity.User;

public class MyJsonConverter {
    public static JSONObject convertUserToJson(User user){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("id",""+user.getId());
        jsonObject.put("card",user.getCard());
        jsonObject.put("name",user.getName());
        jsonObject.put("cover",user.getCover());
        jsonObject.put("identity",user.getIdentity()==1?"教师":"学生");
        return jsonObject;
    }
}
