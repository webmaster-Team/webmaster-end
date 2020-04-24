package com.webmaster.end.Dao;

import com.webmaster.end.Entity.User;
import com.webmaster.end.Utils.MyDateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class UserDao {
    @Autowired
    private DataSource dataSource;
    //下划线转驼峰
    RowProcessor processor=new BasicRowProcessor(new GenerousBeanProcessor());
    /**
     * 查询用户是否存在
     * @param id 用户的id
     * @return 返回是否存在
     */
    public Boolean isExist(int id){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from user where id = ? and delete_time is null";
        Object[] params={id};
        try {
            int result = queryRunner.query(sql, new ScalarHandler<Integer>(), params);
            return result==1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 查询用户是否存在
     * @param card 用户的卡号
     * @return 返回是否存在
     */
    public Boolean isExistByCard(String card){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from user where card = ? and delete_time is null";
        Object[] params={card};
        try {
            int result = queryRunner.query(sql, new ScalarHandler<Integer>(), params);
            return result==1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 注册一个用户
     * @param user 用户数据
     * @return 返回是否成功
     */
    public boolean addUser(User user){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="insert into user(card,name,sex,email,phone,sign_time) values (?,?,?,?,?,?)";
        Object[] params={user.getCard(),user.getName(),user.getSex(),user.getEmail(), user.getPhone(),user.getSignTime()};
        try {
            return queryRunner.update(sql,params)>=1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 注销一个用户
     * @param id 用户的id
     * @param time 注销时间
     * @return 返回是否成功
     */
    public boolean deleteUser(int id, String time){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update user set delete_time= ? where id = ? and delete_time is null";
        Object[] params={time,id};
        try {
            return queryRunner.update(sql,params)>=1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新一个用户信息
     * @param user 新的用户数据
     * @return 返回是否成功
     */
    public boolean updateUser(User user){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update user set card = ? , name = ? , sex = ? , email = ? , phone = ? , sign_time = ? , delete_time = ? where id = ? and delete_time is null";
        Object[] params={user.getCard(),user.getName(),user.getSex(),user.getEmail(),user.getPhone(), user.getSignTime(),user.getDeleteTime(),user.getId()};
        try {
            return queryRunner.update(sql,params)>=1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据用户id来返回对应的值
     * @param id 用户id
     * @return 用户的所有信息
     */
    public User getUserById(int id){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from user where id =? and delete_time is null";
        Object[] params={id};
        try {
            User user = queryRunner.query(sql, new BeanHandler<>(User.class,processor), params);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回所有的用户
     * @return 返回用户集合
     */
    public List<User> getUsers(){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from user where delete_time is null";
        try {
            List<User> users = queryRunner.query(sql, new BeanListHandler<>(User.class,processor));
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据传入的card号来返回用户对应的id
     * @param card 输入的卡号
     * @return 返回用户的id
     */
    public int getUserIdByCard(String card){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select id from user where card = ? and delete_time is null";
        Object[] params={card};
        try {
            return queryRunner.query(sql, new ScalarHandler<Integer>(), params);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
