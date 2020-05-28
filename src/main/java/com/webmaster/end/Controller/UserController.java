package com.webmaster.end.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.User;
import com.webmaster.end.Service.UserService;
import com.webmaster.end.Utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.*;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MyMail myMail;
    @Autowired
    private ImageUtil imageUtil;

    /**
     * 获得用户信息
     * @param session
     * @return 用户的相关信息
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("getUserData")
    public String getUserData(HttpSession session){
        try {
            Object userId = session.getAttribute("userId");
            if (userId == null)
                return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
            else {
                int trueUserId = (int) userId;
                Map<String, Object> info = userService.getUser(trueUserId);
                if (info.get("state") == null)
                    return MyJsonConverter.convertErrorToJson(info).toJSONString();
                else {
                    User user = (User) info.get("state");
                    JSONObject result = new JSONObject();
                    result.put("result", 1);
                    JSONObject temp = MyJsonConverter.convertUserToJson(user);
                    Map<String, Object> borrowData = userService.getBorrowBooksByUserId(trueUserId);
                    List<Integer> borrowBooks = (List<Integer>) borrowData.get("state");
                    if (borrowBooks != null) {
                        temp.put("hasBorrowed", borrowBooks.get(0));
                        temp.put("isBorrowing", borrowBooks.get(1));
                        result.put("data", temp);
                        return result.toJSONString();
                    } else
                        return MyJsonConverter.convertErrorToJson(borrowData).toJSONString();
                }
            }
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("服务器内部错误").toJSONString();
        }
    }


    /**
     * 获得用户正在借阅的书籍
     * @return 用户的借阅信息
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("getUserIsBorrowingBook")
    public String getUserIsBorrowingBook(HttpSession session){
        try {
            Object userId = session.getAttribute("userId");
            if (userId == null)
                return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
            else {
                int trueUserId = (int) userId;
                Map<String, Object> isRentalsingData = userService.getIsRentalsingByUserId(trueUserId);
                List<Map<String,Object>> isRentalsing= (List<Map<String, Object>>) isRentalsingData.get("state");
                if(isRentalsing!=null){
                    JSONObject result = new JSONObject();
                    result.put("result", 1);
                    JSONArray array = new JSONArray();
                    for (Map<String, Object> objectMap : isRentalsing) {
                        JSONObject tempBook = new JSONObject();
                        Book book = (Book) objectMap.get("book");
                        int distance=(int)objectMap.get("distance");
                        tempBook.put("id",book.getId()+"");
                        tempBook.put("name",book.getName());
                        tempBook.put("author",book.getAuthor());
                        tempBook.put("publisher",book.getPublisher());
                        tempBook.put("cover",book.getCover());
                        tempBook.put("distance",distance);
                        array.add(tempBook);
                    }
                    result.put("data",array);
                    return result.toJSONString();
                }
                else
                    return MyJsonConverter.convertErrorToJson(isRentalsingData).toJSONString();
            }
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("服务器内部错误").toJSONString();
        }
    }


    /**
     * 获得用户已经归还的书籍
     * @return 用户的借阅信息
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("getUserHasReturnedBook")
    public String getUserHasReturnedBook(HttpSession session){
        try {
            Object userId = session.getAttribute("userId");
            if (userId == null)
                return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
            else {
                int trueUserId = (int) userId;
                Map<String, Object> hasRentalsedData = userService.getHasRentalsedByUserId(trueUserId);
                List<Book> hasRentaled= (List<Book>) hasRentalsedData.get("state");
                if(hasRentaled!=null){
                    JSONObject result = new JSONObject();
                    result.put("result", 1);
                    JSONArray array = new JSONArray();
                    for (Book book : hasRentaled) {
                        JSONObject jsonObject = MyJsonConverter.convertSimpleBookToJson(book);
                        jsonObject.remove("state");
                        array.add(jsonObject);
                    }
                    result.put("data",array);
                    return result.toJSONString();
                }
                else
                    return MyJsonConverter.convertErrorToJson(hasRentalsedData).toJSONString();
            }
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("服务器内部错误").toJSONString();
        }
    }



    /**
     * 用户进行登录
     * @param map 数据map
     * @return 返回对应的字符串
     */
    @CrossOrigin
    @PostMapping("login")
    public String login(@RequestBody Map<String,String> map, HttpSession session, HttpServletResponse response){
        try {
            String card = map.get("card");
            String password = map.get("password");
            if (card != null) {
                if (password != null) {
                    Map<String, Object> info = userService.login(card, password);
                    boolean state = (Boolean) info.get("state");
                    if (state) {
                        Map<String, Object> userData = userService.getUserByCard(card);
                        User user = (User) userData.get("state");
                        if (user != null) {
                            JSONObject jsonObject = new JSONObject(true);
                            jsonObject.put("result", 1);
                            //生成Token
                            Map<String, Object> param = new HashMap<>() {
                                {
                                    put("id", user.getId());
                                }
                            };
                            String token = JwtUtil.encode(param);
                            //加入
                            jsonObject.put("token", token);
                            return jsonObject.toJSONString();
                        } else
                            return MyJsonConverter.convertErrorToJson(userData).toJSONString();
                    } else
                        return MyJsonConverter.convertErrorToJson(info).toJSONString();
                } else
                    return MyJsonConverter.createErrorToJson("密码不能为空").toJSONString();
            } else
                return MyJsonConverter.createErrorToJson("学号不能为空").toJSONString();
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("服务器内部错误").toJSONString();
        }
    }


    /**
     * 用户进行注册
     * @return 返回对应的字符串
     */
    @CrossOrigin
    @PostMapping("register")
    public String register(@RequestBody Map<String,Object> map, HttpSession session, HttpServletResponse response){
        try {
        User user = new User();
        //必须项
        Object card=map.get("card");
        Object name=map.get("name");
        Object password=map.get("password");
        Object email=map.get("email");
        //非必须项，已有默认值
        Integer sex=(Integer) map.get("sex");
        String phone=(String)map.get("phone");
        String coverPath=(String)map.get("cover");
        Integer identity=(Integer)map.get("identity");
        //非默认项有值的话，进行赋值
        if(sex!=null)
            user.setSex(sex);
        if(phone!=null)
            user.setPhone(phone);
        if(coverPath!=null)
            user.setCover(coverPath);
        if(identity!=null)
            user.setIdentity(identity);

        String truePassword=null;
        if(card!=null){
            String trueCard=(String)card;
            if(name!=null){
                String trueName=(String)name;
                if(password!=null){
                    truePassword=(String)password;
                    if(email!=null){
                        String trueEmail=(String)email;
                        user.setCard(trueCard);
                        user.setName(trueName);
                        user.setEmail(trueEmail);
                    }
                    else
                        return MyJsonConverter.createErrorToJson("邮箱不能为空").toJSONString();
                }
                else
                    return MyJsonConverter.createErrorToJson("密码不能为空").toJSONString();
            }
            else
                return MyJsonConverter.createErrorToJson("姓名不能为空").toJSONString();
        }
        else
            return MyJsonConverter.createErrorToJson("学号不能为空").toJSONString();
        user.setSignTime(MyDateUtil.getCurrentString());
        Map<String, Object> info = userService.register(user, truePassword);
        boolean state=(Boolean)info.get("state");
        if(state){
            Map<String, Object> userDate = userService.getUserByCard(user.getCard());
            User trueUser= (User) userDate.get("state");
            if(trueUser!=null)
                return MyJsonConverter.createSuccessrToJson("注册成功").toJSONString();
            else
                return MyJsonConverter.convertErrorToJson(userDate).toJSONString();
        }
        else
            return MyJsonConverter.convertErrorToJson(info).toJSONString();
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("服务器内部错误").toJSONString();
        }
    }




    /**
     * 用户注销
     * @return 返回对应的字符串
     */
    @LoginAccess
    @CrossOrigin
    @PostMapping("logout")
    public String logout(HttpSession session){
        try {
            Object userId = session.getAttribute("userId");
            if(userId!=null){
                session.removeAttribute("userId");
                return MyJsonConverter.createSuccessrToJson("注销成功").toJSONString();
            }
            else
                return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }


    /**
     * 上传图片
     * @param file
     * @return
     */
    @CrossOrigin
    @PostMapping("upload")
    @ResponseBody
    public String upload(@RequestParam(value="portrait") MultipartFile file) {
        try {
            if (file != null) {
                JSONObject result = new JSONObject(true);
                String prefix="/home/image/tou";
                String coverPath = MyStringUtil.toSimpleString((new Date().getTime() + file.getOriginalFilename().substring(0,5)))+".png";
                coverPath=prefix+coverPath;
                File image = new File(coverPath);
                file.transferTo(image);
                result.put("result", 1);
                result.put("url", "http://123.56.3.135:8081" + coverPath.substring(5));
                return result.toJSONString();
            } else
                return MyJsonConverter.createErrorToJson("未上传照片").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("服务器内部错误").toJSONString();
        }
    }

    /**
     * 微博三方登录接口
     * @param code
     * @return 对应的用户数据
     */
    @CrossOrigin
    @GetMapping("weiboThreeLogin")
    public void threeLogin(String code,HttpServletResponse response){
        try {
            PrintWriter  writer = response.getWriter();
            String app_id="323275235";
            String app_secret="1409df204947454234cf764953ac6549";
            String redirect_uri="http://123.56.3.135:8080/api/user/weiboThreeLogin";
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
            Map<String, Object> info = userService.getWeiboUserIdByName(name);
            int userId= (int) info.get("state");
            String msg= (String) info.get("msg");
            if(userId==-1){
                if(msg.equals("用户不存在")){
                    User user = new User();
                    user.setCard(RandomUtil.getRandomCard());
                    user.setName((String) data.get("name"));
                    user.setSex(((String) data.get("gender")).equals("m") ? 1 : 0);
                    user.setCover((String) data.get("profile_image_url"));
                    user.setSignTime(MyDateUtil.getCurrentString());
                    //默认密码是111111
                    Map<String, Object> registerData = userService.register(user, "111111");
                    boolean state=(Boolean)registerData.get("state");
                    String registerMsg= (String) registerData.get("msg");
                    if(state){
                        //登录或者是注册完成之后返回数据
                        //获得用户
                        Map<String, Object> userData = userService.getUser(userId);
                        User trueUser= (User) userData.get("state");
                        if(trueUser!=null){
                            //生成token
                            Map<String,Object> param=new HashMap<>(){
                                {put("id",trueUser.getId());}
                            };
                            String token=JwtUtil.encode(param);
                            response.sendRedirect("http://www.solingjees.site:11010/#/index?token="+access_token);
                        }
                        else
                            response.sendRedirect("http://www.solingjees.site:11010/#/login?msg="+registerMsg);
                    }
                    else
                        response.sendRedirect("http://www.solingjees.site:11010/#/login?msg="+registerMsg);
                }
                else
                    return;
            }
            else{
                Map<String, Object> userData = userService.getUser(userId);
                User trueUser= (User) userData.get("state");
                String registerMsg= (String) userData.get("msg");
                if(trueUser!=null){
                    //生成token
                    Map<String,Object> param=new HashMap<>(){
                        {put("id",trueUser.getId());}
                    };
                    String token=JwtUtil.encode(param);
                    response.sendRedirect("http://www.solingjees.site:11010/#/index?token="+access_token);
                }
                else
                    response.sendRedirect("http://www.solingjees.site:11010/#/login?msg="+registerMsg);
            }
        }catch (ClassCastException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送邮箱验证码
     * @param map 卡号
     * @param session
     * @return 返回是否成功
     */
    @CrossOrigin
    @PostMapping("sendVerificationCode")
    public String sendVerificationCode(@RequestBody Map<String,String> map,HttpSession session){
        try {
            Object card = map.get("card");
            if (card != null) {
                String trueCard = (String) card;
                Map<String, Object> userData = userService.getUserByCard(trueCard);
                User user = (User) userData.get("state");
                if (user != null) {
                    Map<String, Object> data = myMail.sendVerificationCode(user.getEmail());
                    String veriCode = (String) data.get("veriCode");
                    long sendTime = (long) data.get("sendTime");
                    session.setAttribute("veriCode", veriCode);
                    session.setAttribute("sendTime", sendTime);
                    return MyJsonConverter.createSuccessrToJson("发送邮件验证码成功").toJSONString();
                } else
                    return MyJsonConverter.convertErrorToJson(userData).toJSONString();
            } else
                return MyJsonConverter.createErrorToJson("学号不能为空").toJSONString();
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }


    /**
     * 检验邮箱对应的验证码
     * @param map 验证码
     * @param session
     * @return 是否成功
     */
    @CrossOrigin
    @PostMapping("checkVerificationCode")
    public String checkVerificationCode(@RequestBody Map<String,String> map,HttpSession session){
        try {
            String veriCode = map.get("veriCode");
            if (veriCode != null) {
                long currentTime = MyDateUtil.getCurrentTime();
                String trueVeriCode = (String) session.getAttribute("veriCode");
                long sendTime = (long) session.getAttribute("sendTime");
                if ((currentTime - sendTime) / 1000 / 60 > 3)
                    return MyJsonConverter.createErrorToJson("邮件验证码超过3分钟").toJSONString();
                else {
                    if (veriCode.equals(trueVeriCode)) {
                        session.removeAttribute("veriCode");
                        return MyJsonConverter.createSuccessrToJson("校验成功").toJSONString();
                    } else
                        return MyJsonConverter.createErrorToJson("邮件验证码错误").toJSONString();
                }
            } else
                return MyJsonConverter.createErrorToJson("邮件验证码不能为空").toJSONString();
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
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
        try {
            String card = (String) map.get("card");
            String newPassword = (String) map.get("newPassword");
            if (card != null) {
                if (newPassword != null) {
                    Map<String, Object> userData = userService.getUserByCard(card);
                    User user = (User) userData.get("state");
                    if (user != null) {
                        Map<String, Object> passwordData = userService.setPassword(user.getId(), newPassword);
                        boolean flag = (boolean) passwordData.get("state");
                        if (flag)
                            return MyJsonConverter.convertSuccessToJson(passwordData).toJSONString();
                        else
                            return MyJsonConverter.convertErrorToJson(passwordData).toJSONString();
                    } else
                        return MyJsonConverter.convertErrorToJson(userData).toJSONString();
                } else
                    return MyJsonConverter.createErrorToJson("新密码不能为空").toJSONString();
            } else
                return MyJsonConverter.createErrorToJson("学号不能为空").toJSONString();
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }

    /**
     * 修改密码
     * @param map 数据的字典
     * @return 返回是否成功
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("resetPassword")
    public String resetPassword(@RequestBody Map<String,Object> map,HttpSession session){
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if(userId!=null) {
                String oldPassword = (String) map.get("oldPassword");
                String newPassword = (String) map.get("newPassword");
                if (oldPassword != null) {
                    if (newPassword != null) {
                        Map<String, Object> passwordData = userService.checkPassword(userId, oldPassword);
                        boolean flag = (boolean) passwordData.get("state");
                        if (flag) {
                            Map<String, Object> newPasswordData = userService.setPassword(userId, newPassword);
                            boolean flag2 = (boolean) passwordData.get("state");
                            if (flag2)
                                return MyJsonConverter.convertSuccessToJson(newPasswordData).toJSONString();
                            else
                                return MyJsonConverter.convertErrorToJson(newPasswordData).toJSONString();
                        } else
                            return MyJsonConverter.convertErrorToJson(passwordData).toJSONString();
                    }
                    return MyJsonConverter.createErrorToJson("新密码不能为空").toJSONString();
                } else
                    return MyJsonConverter.createErrorToJson("旧密码不能为空").toJSONString();
            }else
                return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }


    /**
     * 绘制验证码
     * @param session
     * @param response
     */
    @CrossOrigin
    @GetMapping("drawImage")
    public void drawImage(HttpSession session, HttpServletResponse response) {
        try {
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
            session.setAttribute("image", num);
            response.setHeader("Content-Type", "image/jpeg");
            //9.设置响应头控制浏览器不要缓存
            response.setDateHeader("expries", -1);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            ServletOutputStream outputStream = response.getOutputStream();
            String s = Base64.getEncoder().encodeToString(imageInByte);
            outputStream.print(s);
            outputStream.flush();
            outputStream.close();
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
        try {
            String image = map.get("image");
            if(image!=null){
                String image1 = image.toUpperCase();
                String trueImage = (String) session.getAttribute("image");
                if (trueImage != null){
                    if (image1.equals(trueImage)) {
                        session.removeAttribute("image");
                        return MyJsonConverter.createSuccessrToJson("校验成功").toJSONString();
                    } else
                        return MyJsonConverter.createErrorToJson("验证码错误").toJSONString();
                }
                else
                    return  MyJsonConverter.createErrorToJson("无对应验证码").toJSONString();
            }
            else
                return MyJsonConverter.createErrorToJson("验证码不能为空").toJSONString();
        }catch (ClassCastException e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("参数类型错误").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }
}
