package com.webmaster.end.Controller;

import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Entity.User;
import com.webmaster.end.Service.UserService;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户进行登录
     * @param card 传入的卡号
     * @param password 传入的密码
     * @return 返回对应的字符串
     */
    @PostMapping("login")
    public String login(String card,String password){
        int userId = userService.login(card, password);
        if(userId==-1)
            return "{\"result\":0}";
        else {
            User user = userService.getUserById(userId);
            if(user==null)
                return "{\"result\":0}";
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("resutl",1);
            //数据部分
            JSONObject temp=new JSONObject(true);
            temp.put("id",""+userId);
            temp.put("card",user.getCard());
            temp.put("name",user.getName());
            temp.put("cover",user.getCover());
            //加入
            jsonObject.put("data",temp);
            return jsonObject.toJSONString();
        }
    }

    /**
     * 用户进行注册
     * @return 返回对应的字符串
     */
    @PostMapping("register")
    public String register(String card, String name, String password, int sex, String email,
                           String phone, @RequestParam(value = "cover",required = false)MultipartFile file){
        User user=new User();
        //图片处理
        try{
            String coverPath="/home/image/default.png";
            if(file!=null) {
                coverPath = "/home/image/" + (new Date()).getTime() + file.getOriginalFilename();
                File image = new File(coverPath);
                file.transferTo(image);
            }
            user.setCover("http://123.56.3.135:8080"+coverPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置属性
        user.setCard(card);
        user.setName(name);
        user.setSex(sex);
        user.setEmail(email);
        user.setPhone(phone);
        user.setSignTime(MyDateUtil.getCurrentString());
        //注册返回
        int userId = -1;
        try {
            userId = userService.register(user,password);
            if(userId==-1)
                return "{\"result\":0}";
            else{
                JSONObject jsonObject = new JSONObject(true);
                jsonObject.put("resutl",1);
                //数据部分
                JSONObject temp=new JSONObject(true);
                temp.put("id",""+userId);
                temp.put("card",""+user.getCard());
                temp.put("name",""+user.getName());
                temp.put("cover",user.getCover());
                //加入
                jsonObject.put("data",temp);
                return jsonObject.toJSONString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"result\":0}";
        }
    }

    /**
     * 用户注销
     * @param id 用户的id
     * @return 返回对应的字符串
     */
    @PostMapping("delete")
    public String delete(int id){
        boolean result = false;
        try {
            result = userService.deleteUser(id);
            return "{\"result\":"+(result?1:0)+"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"result\":0}";
        }
    }
}
