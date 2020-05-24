package com.webmaster.end.Service;

import com.webmaster.end.Dao.PasswordDao;
import com.webmaster.end.Dao.RentalDao;
import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.ResultMap;
import com.webmaster.end.Entity.User;
import com.webmaster.end.Utils.MD5Util;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordDao passwordDao;
    @Autowired
    private RentalDao rentalDao;

    /**
     * 用户的登录
     * @param card 用户输入的卡号
     * @param password 用户的密码
     * @return boolean的state
     */
    public Map<String,Object> login(String card, String password){
        try {
            if (userDao.isExistByCard(card)) {
                int id = userDao.getUserIdByCard(card);
                String salt = passwordDao.getSalt(id);
                String truePassword = passwordDao.getPassword(id);
                String currentPassword = MD5Util.getMD5String(password, salt);
                if (currentPassword.equals(truePassword))
                    return ResultMap.getResultMap(true,"登录成功");
                else
                    return ResultMap.getResultMap(false,"密码错误");
            }
            else
                return ResultMap.getResultMap(false,"用户已存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false,"服务器内部错误");
        }
    }

    /**
     * 用户的注册
     * @param user 用户信息
     * @param  password 用户的密码
     * @return boolean的state
     */
    @Transactional
    public Map<String,Object> register(User user,String password){
        try {
            if (!userDao.isExistByCard(user.getCard())) {
                if (!userDao.isExistByName(user.getName())) {
                    if (!userDao.isExistByEmail(user.getEmail()) || user.getEmail().equals("无")) {
                        if (userDao.addUser(user)) {
                            //根据card获得id
                            int id = userDao.getUserIdByCard(user.getCard());
                            //产生盐值
                            String salt = "" + (new Date()).getTime();
                            //增加密码
                            if (passwordDao.addPassword(id, salt, MD5Util.getMD5String(password, salt)))
                                return ResultMap.getResultMap(true,"注册成功");
                            else
                                return ResultMap.getResultMap(false,"增加密码失败");
                        }
                        else
                            return ResultMap.getResultMap(false,"增加用户失败");
                    } else
                        return ResultMap.getResultMap(false,"邮箱已被使用");
                } else
                    return ResultMap.getResultMap(false,"用户名已被使用");
            } else
                return ResultMap.getResultMap(false,"学号已存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false,"服务器内部错误");
        }
    }


    /**
     * 用户删除
     * @param id 用户的id
     * @return 0为失败，1为成功，-1为系统错误
     */
    @Transactional
    public Map<String,Object> deleteUser(int id) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            if(userDao.isExist(id)) {
                if(passwordDao.deletePassword(id)){
                    if(userDao.deleteUser(id, MyDateUtil.getCurrentString()))
                        return ResultMap.getResultMap(true,"删除成功");
                    else
                        return ResultMap.getResultMap(false,"删除失败");
                } else
                    return ResultMap.getResultMap(false,"删除密码失败");
            }
            else
                return ResultMap.getResultMap(false,"用户不存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false,"服务器内部错误");
        }
    }


    /**
     * 根据用户的id来返回对应的用户信息
     * @param id 用户的id
     * @return User对象
     */
    public Map<String,Object> getUser(int id){
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (userDao.isExist(id)) {
                User user = userDao.getUser(id);
                if(user==null)
                    return ResultMap.getResultMap(null,"获取用户失败");
                else
                    return ResultMap.getResultMap(user,"获取用户成功");
            }
            else
                return ResultMap.getResultMap(null,"用户不存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"服务器内部错误");
        }
    }

    /**
     * 根据用户的card来返回对应的用户信息
     * @param card 用户的card
     * @return User对象
     */
    public Map<String,Object> getUserByCard(String card) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (userDao.isExistByCard(card)){
                int id=userDao.getUserIdByCard(card);
                User user = userDao.getUser(id);
                if(user==null)
                    return ResultMap.getResultMap(null,"获取用户失败");
                else
                    return ResultMap.getResultMap(user,"获取用户成功");
            }
            else
                return ResultMap.getResultMap(null,"用户不存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"服务器内部错误");
        }
    }

    /**
     * 获得用户的借书数量
     * @param userId 用户的id
     * @return int类型
     */
    public Map<String,Object> getBorrowBooksByUserId(int userId){
        HashMap<String, Object> map = new HashMap<>();
        try {
            int count = rentalDao.getBorrowBooksByUserId(userId);
            return ResultMap.getResultMap(count,"获取借书数量成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(-1,"服务器内部错误");
        }
    }

    /**
     * 通过用户名来获得用户的ID
     * @param name 用户昵称
     * @return int类型
     */
    public Map<String,Object> getUserIdByName(String name){
        HashMap<String, Object> map = new HashMap<>();
        try {
            if(userDao.isExistByName(name)){
                int id = userDao.getUserIdByName(name);
                return ResultMap.getResultMap(id,"获取用户id成功");
            }
            else
                return ResultMap.getResultMap(-1,"用户不存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(-1,"服务器内部错误");
        }
    }

    /**
     * 设置密码
     * @param userId 用户的id
     * @param password 密码
     * @return boolean类型
     */
    public Map<String,Object> setPassword(int userId,String password){
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (passwordDao.isExist(userId)) {
                String salt = MD5Util.getSalt();
                String truePassword = MD5Util.getMD5String(password, salt);
                if(passwordDao.updatePassword(userId, salt, truePassword))
                    return ResultMap.getResultMap(true,"设置密码成功");
                else
                    return ResultMap.getResultMap(false,"设置密码失败");
            }
            else
                return ResultMap.getResultMap(false,"用户密码不存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false,"服务器内部错误");
        }
    }


    /**
     * 判断密码是否正确
     * @param userId 用户的id
     * @param password 密码
     * @return boolean类型
     */
    public Map<String,Object> checkPassword(int userId,String password){
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (passwordDao.isExist(userId)) {
                String salt = passwordDao.getSalt(userId);
                String currentPassword = MD5Util.getMD5String(password, salt);
                if (currentPassword.equals(passwordDao.getPassword(userId)))
                    return ResultMap.getResultMap(true,"旧密码正确");
                else
                    return ResultMap.getResultMap(false,"旧密码错误");
            }
            else
                return ResultMap.getResultMap(false,"用户密码不存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false,"服务器内部错误");
        }
    }

}
