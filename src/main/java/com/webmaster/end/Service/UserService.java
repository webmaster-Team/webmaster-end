package com.webmaster.end.Service;

import com.webmaster.end.Dao.PasswordDao;
import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.User;
import com.webmaster.end.Utils.MD5Util;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordDao passwordDao;

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
     * @return 返回注册成功后用户的id，注册失败返回-1
     */
    public int register(User user){
        if (!userDao.isExistByCard(user.getCard())){
            if(userDao.addUser(user))
                return userDao.getUserIdByCard(user.getCard());
        }
        return -1;
    }

    /**
     * 用户注销
     * @param id 用户的id
     * @return 返回是否注销成功
     */
    public boolean deleteUser(int id){
        if(userDao.isExist(id))
            return userDao.deleteUser(id, MyDateUtil.getCurrentString());
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
}
