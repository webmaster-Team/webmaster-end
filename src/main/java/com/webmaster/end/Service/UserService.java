package com.webmaster.end.Service;

import com.webmaster.end.Entity.*;
import com.webmaster.end.IMapper.IBookMapper;
import com.webmaster.end.IMapper.IPasswordMapper;
import com.webmaster.end.IMapper.IRentalMapper;
import com.webmaster.end.IMapper.IUserMapper;
import com.webmaster.end.Utils.MD5Util;
import com.webmaster.end.Utils.MyDateUtil;
import com.webmaster.end.Utils.MyRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IUserMapper iUserMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IBookMapper iBookMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IPasswordMapper iPasswordMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IRentalMapper iRentalMapper;

    @Autowired
    private MyRedisUtil myRedisUtil;

    /**
     * 用户的登录
     * @param card 用户输入的卡号
     * @param password 用户的密码
     * @return boolean的state
     */
    public Map<String,Object> login(String card, String password){
        try {
            if (iUserMapper.isExistByCard(card)) {
                int id = iUserMapper.getUserIdByCard(card);
                String salt = iPasswordMapper.getSalt(id);
                String truePassword = iPasswordMapper.getPassword(id);
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
            if (!iUserMapper.isExistByCard(user.getCard())) {
                if (!iUserMapper.isExistByEmail(user.getEmail()) || user.getEmail().equals("无")) {
                    if (iUserMapper.addUser(user)) {
                        //根据card获得id
                        int id = iUserMapper.getUserIdByCard(user.getCard());
                        //产生盐值
                        String salt = "" + (new Date()).getTime();
                        //增加密码
                        if (iPasswordMapper.addPassword(id, salt, MD5Util.getMD5String(password, salt)))
                            return ResultMap.getResultMap(true,"注册成功");
                        else
                            return ResultMap.getResultMap(false,"增加密码失败");
                    }
                    else
                        return ResultMap.getResultMap(false,"增加用户失败");
                } else
                    return ResultMap.getResultMap(false,"邮箱已被使用");
            } else
                return ResultMap.getResultMap(false,"学号已存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(false,"服务器内部错误");
        }
    }


//    /**
//     * 用户删除
//     * @param id 用户的id
//     * @return 0为失败，1为成功，-1为系统错误
//     */
//    @Transactional
//    public Map<String,Object> deleteUser(int id) {
//        HashMap<String, Object> map = new HashMap<>();
//        try {
//            if(iUserMapper.isExist(id)) {
//                if(iPasswordMapper.deletePassword(id)){
//                    if(iUserMapper.deleteUser(id, MyDateUtil.getCurrentString()))
//                        return ResultMap.getResultMap(true,"删除成功");
//                    else
//                        return ResultMap.getResultMap(false,"删除失败");
//                } else
//                    return ResultMap.getResultMap(false,"删除密码失败");
//            }
//            else
//                return ResultMap.getResultMap(false,"用户不存在");
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResultMap.getResultMap(false,"服务器内部错误");
//        }
//    }


    /**
     * 根据用户的id来返回对应的用户信息
     * @param id 用户的id
     * @return User对象
     */
    public Map<String,Object> getUser(int id){
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (iUserMapper.isExist(id)) {
                User user = iUserMapper.getUser(id);
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
            if (iUserMapper.isExistByCard(card)){
                int id=iUserMapper.getUserIdByCard(card);
                User user = iUserMapper.getUser(id);
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
     * @return List<Integer>类型
     */
    public Map<String,Object> getBorrowBooksByUserId(int userId){
        //获得缓存中的Orderz中的书籍的数量
        List<Order> orders = myRedisUtil.getOrdersByUserId(userId);
        int num=0;
        for (Order order : orders)
            num+=order.getBooks().size();

        List<Integer> borrowBooks=new ArrayList<>();
        try {
            int count = iRentalMapper.getHasBorrowedBooksByUserId(userId);
            borrowBooks.add(count+num);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(-1,"获得总借书数量失败");
        }

        try {
            int count = iRentalMapper.getIsBorrowingBooksByUserId(userId);
            borrowBooks.add(count+num);
            return ResultMap.getResultMap(borrowBooks,"获取借书数量成功");
        }catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(-1, "获得正在借书数量失败");
        }
    }

    /**
     * 获得用户正在借阅的书籍
     * @param userId 用户id
     * @return List<Map<String,Object>>
     */
    public Map<String,Object> getIsRentalsingByUserId(int userId){
        List<Map<String,Object>> result = new ArrayList<>();
        try{
            List<Rental> isRentalsingData = iRentalMapper.getIsRentalsingByUserId(userId);
            for (Rental isRentalsingDatum : isRentalsingData) {
                Map<String, Object> data = new HashMap<>();
                int bookId = isRentalsingDatum.getBookId();
                Book book = iBookMapper.getBook(bookId);
                if(book!=null){
                    data.put("book",book);
                    data.put("distance",MyDateUtil.reckonDateDistance(isRentalsingDatum));
                    data.put("isReborrow",isRentalsingDatum.getIsReborrow()==0?false:true);
                    result.add(data);
                }
                else
                    return ResultMap.getResultMap(null, "获取书籍失败");
            }
            //将缓存中的书籍放到对应的集合中
            List<Order> orders = myRedisUtil.getOrdersByUserId(userId);
            for (Order order : orders) {
                for (Book book : order.getBooks()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("book",book);
                    data.put("distance",30);
                    result.add(data);
                }
            }
            return ResultMap.getResultMap(result,"获取用户正在借阅的书籍成功");
        }catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null, "服务器内部错误");
        }
    }


    /**
     * 获得用户已经归还的书籍
     * @param userId 用户id
     * @return List<Map<String,Object>>
     */
    public Map<String,Object> getHasRentalsedByUserId(int userId){
        List<Book> result = new ArrayList<>();
        try{
            List<Rental> isRentalsingData = iRentalMapper.getHasRentalsedByUserId(userId);
            for (Rental isRentalsingDatum : isRentalsingData) {
                int bookId = isRentalsingDatum.getBookId();
                Book book = iBookMapper.getBook(bookId);
                if(book!=null)
                    result.add(book);
                else
                    return ResultMap.getResultMap(null, "获取书籍失败");
            }
            return ResultMap.getResultMap(result,"获取用户已经归还的书籍成功");
        }catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null, "服务器内部错误");
        }
    }

    /**
     * 通过用户名来获得用户的ID
     * @param name 用户昵称
     * @return int类型
     */
    public Map<String,Object> getWeiboUserIdByName(String name){
        try {
            if(iUserMapper.isExistWeiboByName(name)){
                int id = iUserMapper.getWeiboUserIdByName(name);
                return ResultMap.getResultMap(id,"获取微博用户id成功");
            }
            else
                return ResultMap.getResultMap(-1,"该微博用户不存在");
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
            if (iPasswordMapper.isExist(userId)) {
                String salt = MD5Util.getSalt();
                String truePassword = MD5Util.getMD5String(password, salt);
                if(iPasswordMapper.updatePassword(userId, salt, truePassword))
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
            if (iPasswordMapper.isExist(userId)) {
                String salt = iPasswordMapper.getSalt(userId);
                String currentPassword = MD5Util.getMD5String(password, salt);
                if (currentPassword.equals(iPasswordMapper.getPassword(userId)))
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
