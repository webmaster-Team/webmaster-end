package com.webmaster.end.Controller;

import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Entity.User;
import com.webmaster.end.Service.UserService;
import com.webmaster.end.Utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MyMail myMail;
    @Autowired
    private ImageUtil imageUtil;

    @CrossOrigin
    @LoginAccess
    @PostMapping("getUserData")
    public String getUserData(HttpSession session){
        int userId = (int)session.getAttribute("userId");
        User user = userService.getUserById(userId);
        JSONObject result = new JSONObject(true);
        if(user!=null){
            result.put("result",1);
            JSONObject temp = MyJsonConverter.convertUserToJson(user);
            temp.put("borrow",userService.getBorrowBooksByUserId(userId));
            result.put("data",temp);
            return result.toJSONString();
        }
        else
            return "{\"result\":0}";
    }
    /**
     * 用户进行登录
     * @param map 数据map
     * @return 返回对应的字符串
     */
    @CrossOrigin
    @PostMapping("login")
    public String login(@RequestBody Map<String,String> map, HttpSession session, HttpServletResponse response){
        String card = map.get("card");
        String password=map.get("password");
        int userId = userService.login(card, password);
        if(userId==-1)
            return "{\"result\":0}";
        else {
            User user = userService.getUserById(userId);
            if(user==null)
                return "{\"result\":0}";
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("result",1);
            //生成Token
            Map<String,Object> param=new HashMap<>(){
                {put("id",user.getId());}
            };
            String token=JwtUtil.encode(param);
            //加入
            jsonObject.put("token",token);
            return jsonObject.toJSONString();
        }
    }

    /**
     * 用户进行注册
     * @return 返回对应的字符串
     */
    @CrossOrigin
    @PostMapping("register")
    public String register(@RequestBody Map<String,Object> map, HttpSession session, HttpServletResponse response){
        String card=(String)map.get("card");
        String name=(String)map.get("name");
        String password=(String)map.get("password");
        int sex=(int)map.get("sex");
        String email=(String)map.get("email");
        String phone=(String)map.get("phone");
        String coverPath=(String)map.get("cover");
        int identity=0;
        if(map.get("identity")==null)
            identity=(int)map.get("identity");
        if(StringUtils.isEmpty(coverPath))
            coverPath="http://123.56.3.135:8081/image/default.png";
        User user=new User(card,name,sex,email,phone,coverPath,MyDateUtil.getCurrentString(),identity);
        //注册返回
        int userId = -1;
        try {
            userId = userService.register(user,password);
            if(userId==-1)
                return "{\"result\":0}";
            else
                return "{\"result\":1}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"result\":0}";
        }
    }

    /**
     * 用户删除
     * @param map 用户的数据列表，包含Id
     * @return 返回对应的字符串
     */
    @LoginAccess
    @CrossOrigin
    @PostMapping("delete")
    public String delete(@RequestBody Map<String,Integer> map){
        int id=map.get("id");
        boolean result = false;
        try {
            result = userService.deleteUser(id);
            return "{\"result\":"+(result?1:0)+"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"result\":0}";
        }
    }

    /**
     * 用户注销
     * @param map 用户的数据列表，包含Id
     * @return 返回对应的字符串
     */
    @Deprecated
    @LoginAccess
    @CrossOrigin
    @PostMapping("logout")
    public String logout(@RequestBody Map<String,Integer> map,HttpSession session){
        try {
            int id=map.get("id");
            session.removeAttribute("user");
            return "{\"result\":1}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"result\":0}";
        }
    }


    /**
     * 上传图片
     * @param file
     * @return
     */
    @CrossOrigin(origins = {"http://39.170.26.135:3000"}, maxAge = 3600)
    @PostMapping("upload")
    @ResponseBody
    public String upload(@RequestParam(value="portrait") MultipartFile file) {
        JSONObject result=new JSONObject(true);
        try{
            if(file!=null) {
                String coverPath = "/home/image/" + (new Date()).getTime() + file.getOriginalFilename();
                File image = new File(coverPath);
                file.transferTo(image);
                result.put("result",1);
                result.put("url","http://123.56.3.135:8081"+coverPath.substring(5));
                return result.toJSONString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("result",0);
        return result.toJSONString();
    }

    /**
     * 微博三方登录接口
     * @param code
     * @return 对应的用户数据
     */
    @CrossOrigin
    @GetMapping("weiboThreeLogin")
    public void threeLogin(String code,HttpServletResponse response){
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String app_id="323275235";
        String app_secret="1409df204947454234cf764953ac6549";
        String redirect_uri="http://127.0.0.1:8080/api/user/weiboThreeLogin";
        JSONObject jsonObject=MyHttpClient.post("https://api.weibo.com/oauth2/access_token?client_id="
                +app_id+
                "&client_secret="+app_secret+
                "&grant_type=authorization_code"+
                "&redirect_uri="+redirect_uri+
                "&code="+code,null);
        String access_token = (String)jsonObject.get("access_token");
        String uid = (String)jsonObject.get("uid");
        HashMap<String, Object> map = new HashMap<>(){
            {put("access_token",access_token);put("uid",uid);}
        };
        JSONObject data=(JSONObject)MyHttpClient.get("https://api.weibo.com/2/users/show.json",map);
        //判断用户是否存在
        String name=(String)data.get("name");
        int userId = userService.getUserIdByName(name);
        //不存在
        if(userId==-1) {
            User user = new User();
            user.setCard(RandomUtil.getRandomCard());
            user.setName((String) data.get("name"));
            user.setSex(((String) data.get("gender")).equals("m") ? 1 : 0);
            user.setEmail("无");
            user.setCover((String) data.get("profile_image_url"));
            user.setSignTime(MyDateUtil.getCurrentString());
            user.setIdentity(0);
            try {
                //默认密码是111111
                userId = userService.register(user, "111111");
                //注册失败
                if(userId==-1)
                    response.sendRedirect("http://127.0.0.1:80/error.html");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //登录或者是注册完成之后返回数据
        //获得用户
        User user = userService.getUserById(userId);
        //生成token
        Map<String,Object> param=new HashMap<>(){
            {put("id",user.getId());}
        };
        String token=JwtUtil.encode(param);
        try {
            response.sendRedirect("http://127.0.0.1:80/success.html?token="+access_token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送验证码
     * @param map 卡号
     * @param session
     * @return 返回是否成功
     */
    @CrossOrigin
    @PostMapping("sendVerificationCode")
    public String sendVerificationCode(@RequestBody Map<String,String> map,HttpSession session){
        String card=(String)map.get("card");
        User user = userService.getUserByCard(card);
        if(user!=null){
            Map<String, Object> data = myMail.sendVerificationCode(user.getEmail());
            String veriCode = (String)data.get("veriCode");
            long sendTime=(long)data.get("sendTime");
            session.setAttribute("veriCode",veriCode);
            session.setAttribute("sendTime",sendTime);
            return "{\"result\":1}";
        }
        else
            return "{\"result\":0}";
    }

    /**
     * 检验对应的验证码
     * @param map 验证码
     * @param session
     * @return 是否成功
     */
    @CrossOrigin
    @PostMapping("checkVerificationCode")
    public String checkVerificationCode(@RequestBody Map<String,String> map,HttpSession session){
        String veriCode = map.get("veriCode");
        long currentTime=MyDateUtil.getCurrentTime();
        String trueVeriCode = (String)session.getAttribute("veriCode");
        long sendTime=(long)session.getAttribute("sendTime");
        if((currentTime-sendTime)/1000/60>3){
            JSONObject object = new JSONObject();
            object.put("result",0);
            object.put("msg","验证码超过3分钟");
            return object.toJSONString();
        }
        else{
            if(veriCode.equals(trueVeriCode)) {
                session.removeAttribute("veriCode");
                return "{\"result\":1}";
            }
            else{
                JSONObject object = new JSONObject();
                object.put("result",0);
                object.put("msg","验证码错误");
                return object.toJSONString();
            }
        }
    }


    /**
     * 设置密码
     * @param map
     * @return
     */
    @CrossOrigin
    @PostMapping("setPassword")
    public String setPassword(@RequestBody Map<String,String> map){
        String card=(String)map.get("card");
        String newPassword=(String)map.get("newPassword");
        User user = userService.getUserByCard(card);
        if(user!=null) {
            boolean result = userService.setPassword(user.getId(), newPassword);
            if (result)
                return "{\"result\":1}";;
        }
        return "{\"result\":0}";
    }

    /**
     * 修改密码
     * @param map 数据的字典
     * @return 返回是否成功
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("resetPassword")
    public String resetPassword(@RequestBody Map<String,Object> map){
        int userId=(int)map.get("userId");
        String oldPassword=(String)map.get("oldPassword");
        String newPassword=(String)map.get("newPassword");
        //老密码正确
        if(userService.checkPassword(userId,oldPassword)){
            if(userService.setPassword(userId,newPassword))
                return "{\"result\":1}";
        }
        return "{\"result\":0}";
    }


    /**
     * 绘制验证码
     * @param session
     * @param response
     */
    @CrossOrigin
    @GetMapping("drawImage")
    public void drawImage(HttpSession session,HttpServletResponse response){
        //1.在内存中创建一张图片
        BufferedImage bi = new BufferedImage(ImageUtil.WIDTH, ImageUtil.HEIGHT, BufferedImage.TYPE_INT_RGB);
        //2.得到图片
        Graphics g = bi.getGraphics();
        //3.设置图片的背影色
        imageUtil.setBackGround(g);
        //4.设置图片的边框
        imageUtil.setBorder(g);
        //5.在图片上画干扰线
        imageUtil.drawRandomLine(g);
        String num = imageUtil.drawRandomNum((Graphics2D) g);
        session.setAttribute("image",num);
        //8.设置响应头通知浏览器以图片的形式打开
        response.setHeader("Content-Type", "image/jpeg");
        //9.设置响应头控制浏览器不要缓存
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        //10.将图片写给浏览器
        try {
            ImageIO.write(bi, "jpg", response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 校验验证码
     * @param map
     * @param session
     * @return
     */
    @CrossOrigin
    @PostMapping("checkImage")
    public String checkImage(@RequestBody Map<String,String> map, HttpSession session){
        String image = map.get("image").toUpperCase();
        String trueImage = (String)session.getAttribute("image");
        if(trueImage==null||image==null)
            return "{\"result\":0}";
        else{
            if(image.equals(trueImage)) {
                session.removeAttribute("image");
                return "{\"result\":1}";
            }
            else
                return "{\"result\":0}";
        }
    }
}
