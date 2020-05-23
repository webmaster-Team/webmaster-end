package com.webmaster.end.Service;

import com.webmaster.end.Dao.PasswordDao;
import com.webmaster.end.Dao.RentalDao;
import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.User;
import com.webmaster.end.Utils.MD5Util;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;

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
     * @return 如果登录成功返回用户的id,错误则返回-1
     */
    public int login(String card,String password){
        if (userDao.isExistByCard(card)){
            int id=userDao.getUserIdByCard(card);
            String salt = passwordDao.getSaltById(id);
            String truePassword = passwordDao.getPasswordById(id);
            String currentPassword = MD5Util.getMD5String(password, salt);
            if(currentPassword.equals(truePassword))
                return id;
        }
        return -1;
    }

    /**
     * 用户的注册
     * @param user 用户信息
     * @param  password 用户的密码
     * @return 返回注册成功后用户的id，注册失败返回-1
     */
    @Transactional
    public String register(User user,String password) throws SQLException {
        if (!userDao.isExistByCard(user.getCard())){
            if(!userDao.isExistByName(user.getName())) {
                if(!userDao.isExistByEmail(user.getEmail())||user.getEmail().equals("无")){
                    if (userDao.addUser(user)) {
                        //根据card获得id
                        int id = userDao.getUserIdByCard(user.getCard());
                        //产生盐值
                        String salt = "" + (new Date()).getTime();
                        //增加密码
                        if (passwordDao.addPassword(id, salt, MD5Util.getMD5String(password, salt)))
                            return id + "";
                        else
                            return "服务器内部错误";
                    }
                }
                else
                    return "邮箱已被使用";
            }
            else
                return "用户名已被使用";
        }
        return "学号已存在";
    }

    /**
     * 用户注销
     * @param id 用户的id
     * @return 返回是否注销成功
     */
    @Transactional
    public boolean deleteUser(int id) throws SQLException {
        if(userDao.isExist(id)) {
            passwordDao.deletePassword(id);
            return userDao.deleteUser(id, MyDateUtil.getCurrentString());
        }
        return false;
    }

    /**
     * 根据用户的id来返回对应的用户信息
     * @param id 用户的id
     * @return 返回用户信息，用户不存在，或者报错的时候返回null
     */
    public User getUserById(int id){
        if(userDao.isExist(id))
            return userDao.getUserById(id);
        return null;
    }

    /**
     * 根据用户的card来返回对应的用户信息
     * @param card 用户的card
     * @return 返回用户信息，用户不存在，或者报错的时候返回null
     */
    public User getUserByCard(String card){
        if (userDao.isExistByCard(card)){
            int id=userDao.getUserIdByCard(card);
            return userDao.getUserById(id);
        }
        return null;
    }

    /**
     * 获得用户的借书数量
     * @param userId 用户的id
     * @return 返回借书数量
     */
    public int getBorrowBooksByUserId(int userId){
        return rentalDao.getBorrowBooksByUserId(userId);
    }

    /**
     * 通过用户名来获得用户的ID
     * @param name 用户昵称
     * @return 返回用户ID，失败为-1
     */
    public int getUserIdByName(String name){
        if(userDao.isExistByName(name))
            return userDao.getUserIdByName(name);
        return -1;
    }

    /**
     * 设置密码
     * @param userId 用户的id
     * @param password 密码
     * @return 返回是否修改成功
     */
    public boolean setPassword(int userId,String password){
        if(passwordDao.isExist(userId)){
            String salt=MD5Util.getSalt();
            String truePassword = MD5Util.getMD5String(password, salt);
            Boolean result = passwordDao.updatePassword(userId, salt, truePassword);
            return result;
        }
        return false;
    }

    /**
     * 判断密码是否正确
     * @param userId 用户的id
     * @param password 密码
     * @return 是否正确
     */
    public boolean checkPassword(int userId,String password){
        if(passwordDao.isExist(userId)){
            String salt = passwordDao.getSaltById(userId);
            String currentPassword = MD5Util.getMD5String(password, salt);
            if(currentPassword.equals(passwordDao.getPasswordById(userId)))
                return true;
        }
        return false;
    }

}
