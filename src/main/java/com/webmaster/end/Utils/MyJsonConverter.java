package com.webmaster.end.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Dao.BookTypeDao;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.Order;
import com.webmaster.end.Entity.User;

import java.util.Map;


public class MyJsonConverter {
    /**
     * 用户信息转换
     * @param user 用户
     * @return JSONObject
     */
    public static JSONObject convertUserToJson(User user){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("id",""+user.getId());
        jsonObject.put("card",user.getCard());
        jsonObject.put("name",user.getName());
        jsonObject.put("cover",user.getCover());
        jsonObject.put("identity",user.getIdentity()==1?"教师":"学生");
        return jsonObject;
    }

    /**
     * 简单的书籍信息转换
     * @param book 书籍
     * @return JSONObject
     */
    public static JSONObject convertSimpleBookToJson(Book book){
        JSONObject object = new JSONObject(true);
        object.put("id",book.getId()+"");
        object.put("name",book.getName());
        object.put("author",book.getAuthor());
        object.put("publisher",book.getPublisher());
        object.put("cover",book.getCover());
        object.put("state",book.getState());
        return object;
    }

    /**
     * 复杂的书籍信息转换
     * @param book 书籍
     * @return JSONObject
     */
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
        object.put("place",book.getLibrary()+book.getLayer()+"层"+book.getOrigin()+book.getEntail());
        object.put("state",book.getState()+"");
        object.put("entry_time",book.getEntryTime());
        return object;
    }


    /**
     * 将简单的订单信息，生成SON
     * @param order 订单对象
     * @return JSONObject
     */
    public static JSONObject convertSimpleOrderToJson(Order order){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("id",order.getId()+"");
        jsonObject.put("userId",order.getUserId());
        jsonObject.put("createTime",order.getCreateTime());
        jsonObject.put("completeTime",order.getCompleteTime());
        jsonObject.put("state",order.getState());
        jsonObject.put("qrcode",order.getQrcode());
        JSONArray array = new JSONArray();
        for (Book book : order.getBooks()) {
            JSONObject temp = new JSONObject(true);
            temp.put("id",book.getId());
            temp.put("name",book.getName());
            temp.put("author",book.getAuthor());
            temp.put("publisher",book.getPublisher());
            temp.put("cover",book.getCover());
            array.add(temp);
        }
        jsonObject.put("books",array);
        return jsonObject;
    }

    /**
     * 将订单信息，生成复杂的json
     * @param order 订单对象
     * @return JSONObject
     */
    public static JSONObject convertComplexOrderToJson(Order order){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("id",order.getId()+"");
        jsonObject.put("userId",order.getUserId());
        jsonObject.put("createTime",order.getCreateTime());
        jsonObject.put("state",order.getState());
        JSONArray array = new JSONArray();
        for (Book book : order.getBooks()) {
            JSONObject temp = new JSONObject(true);
            temp.put("id",book.getId());
            temp.put("name",book.getName());
            temp.put("cover",book.getCover());
            array.add(temp);
        }
        jsonObject.put("books",array);
        return jsonObject;
    }


    /**
     * 将正确信息转换成JSON
     * @param map 正确信息对
     * @return JSONObject
     */
    public static JSONObject convertSuccessToJson(Map<String,Object> map){
        JSONObject object = new JSONObject();
        object.put("result",1);
        object.put("msg",(String)map.get("msg"));
        return object;
    }


    /**
     * 将错误信息转换成JSON
     * @param map 错误信息对
     * @return JSONObject
     */
    public static JSONObject convertErrorToJson(Map<String,Object> map){
        JSONObject object = new JSONObject();
        object.put("result",0);
        object.put("msg",(String)map.get("msg"));
        return object;
    }


    /**
     * 生成正确信息转换成JSON
     * @param success 错误信息
     * @return JSONObject
     */
    public static JSONObject createSuccessrToJson(String success){
        JSONObject object = new JSONObject();
        object.put("result",1);
        object.put("msg",success);
        return object;
    }


    /**
     * 生成错误信息转换成JSON
     * @param error 错误信息
     * @return JSONObject
     */
    public static JSONObject createErrorToJson(String error){
        JSONObject object = new JSONObject();
        object.put("result",0);
        object.put("msg",error);
        return object;
    }
}
