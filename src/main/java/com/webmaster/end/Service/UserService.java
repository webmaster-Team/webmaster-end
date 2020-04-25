package com.webmaster.end.Service;

import com.webmaster.end.Dao.PasswordDao;
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
    public int register(User user,String password) throws SQLException {
        if (!userDao.isExistByCard(user.getCard())){
            if(userDao.addUser(user)) {
                //根据card获得id
                int id = userDao.getUserIdByCard(user.getCard());
                //产生盐值
                String salt=""+(new Date()).getTime();
                //增加密码
                if(passwordDao.addPassword(id,salt,MD5Util.getMD5String(password,salt)))
                    return id;
                else
                    return -1;
            }
        }
        return -1;
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

}
