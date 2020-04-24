package com.webmaster.end.Controller;

import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Entity.User;
import com.webmaster.end.Service.UserService;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
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
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resutl",1);
            //数据部分
            JSONObject temp=new JSONObject();
            temp.put("id",""+userId);
            temp.put("card",""+user.getCard());
            temp.put("name",""+user.getName());
            //加入
            jsonObject.put("data",temp);
            return jsonObject.toJSONString();
        }
    }

    /**
     * 用户进行注册
     * @param user 用户的注册信息
     * @return 返回对应的字符串
     */
    @PostMapping("register")
    public String register(User user){
        user.setSignTime(MyDateUtil.getCurrentString());
        int userId = userService.register(user);
        if(userId==-1)
            return "{\"result\":0}";
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resutl",1);
            //数据部分
            JSONObject temp=new JSONObject();
            temp.put("id",""+userId);
            temp.put("card",""+user.getCard());
            temp.put("name",""+user.getName());
            //加入
            jsonObject.put("data",temp);
            return jsonObject.toJSONString();
        }
    }

    /**
     * 用户注销
     * @param id 用户的id
     * @return 返回对应的字符串
     */
    @PostMapping("register")
    public String delete(int id){
        boolean result = userService.deleteUser(id);
        return "{\"result\":"+(result?1:0)+"}";
    }
}
