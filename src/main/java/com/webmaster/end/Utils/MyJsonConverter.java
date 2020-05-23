package com.webmaster.end.Utils;

import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Dao.BookTypeDao;
import com.webmaster.end.Entity.Book;
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

    public static JSONObject convertSimpleBookToJson(Book book){
        JSONObject object = new JSONObject(true);
        object.put("id",book.getId()+"");
        object.put("name",book.getName());
        object.put("author",book.getAuthor());
        object.put("publisher",book.getPublisher());
        object.put("cover",book.getCover());
        object.put("state",book.getState()+"");
        return object;
    }

    public static JSONObject convertComplexBookToJson(Book book){
        JSONObject object = new JSONObject(true);
        object.put("id",book.getId()+"");
        object.put("name",book.getName());
        object.put("author",book.getAuthor());
        object.put("publisher",book.getPublisher());
        object.put("price",book.getPrice());
        object.put("version",book.getVersion());
        object.put("summary",book.getSummary());
        object.put("cover",book.getCover());
        object.put("place",book.getLibrary()+book.getLayer()+"层"+book.getEntail());
        object.put("state",book.getState()+"");
        object.put("entry_time",book.getEntryTime());
        return object;
    }
}
