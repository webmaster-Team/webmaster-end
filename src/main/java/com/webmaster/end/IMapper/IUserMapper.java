package com.webmaster.end.IMapper;

import com.webmaster.end.Entity.User;

import java.util.List;

public interface IUserMapper {
    /**
     * 根据id查询用户是否存在
     * @param id 用户的id
     * @return 返回是否存在
     */
    public boolean isExist(int id);

    /**
     * 根据卡号查询用户是否存在
     * @param card 用户的学号
     * @return 返回是否存在
     */
    public boolean isExistByCard(String card);

    /**
     * 根据用户名查询用户是否存在
     * @param name 用户名
     * @return 返回是否存在
     */
    public boolean isExistByName(String name);

    /**
     * 判断邮箱是否存在
     * @param email 用户的邮箱
     * @return 返回是否存在
     */
    public boolean isExistByEmail(String email) ;


    /**
     * 注册一个用户
     * @param user 用户数据
     * @return 返回是否成功
     */
    public boolean addUser(User user) ;


//    /**
//     * 删除一个用户
//     * @param id 用户的id
//     * @param time 注销时间
//     * @return 返回是否成功
//     */
//    public boolean deleteUser(int id, String time);


    /**
     * 更新一个用户信息
     * @param user 新的用户数据
     * @return 返回是否成功
     */
    public boolean updateUser(User user) ;


    /**
     * 根据用户id来返回对应的用户
     * @param id 用户id
     * @return 用户的所有信息
     */
    public User getUser(int id) ;


    /**
     * 返回所有的用户
     * @return 返回用户集合
     */
    public List<User> getAllUsers();


    /**
     * 根据传入的card号来返回用户对应的id
     * @param card 输入的卡号
     * @return 返回用户的id，错误返回-1
     */
    public int getUserIdByCard(String card);


    /**
     * 根据传入的name来返回用户对应的name
     * @param name 用户名
     * @return 返回用户的id
     */
    public int getUserIdByName(String name);
}
