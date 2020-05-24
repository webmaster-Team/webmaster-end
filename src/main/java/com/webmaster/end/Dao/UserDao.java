package com.webmaster.end.Dao;

import com.webmaster.end.Entity.User;
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
import java.util.List;

@Repository
public class UserDao {
    //phone和delete_time默认值都是0
    // identity默认值是0
    
    @Autowired
    private DataSource dataSource;
    //下划线转驼峰
    RowProcessor processor=new BasicRowProcessor(new GenerousBeanProcessor());
    
    
    /**
     * 根据id查询用户是否存在
     * @param id 用户的id
     * @return 返回是否存在
     */
    public Boolean isExist(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from user where id = ? and delete_time = '0'";
        Object[] params={id};
        int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
        return result==1?true:false;
    }

    
    /**
     * 根据卡号查询用户是否存在
     * @param card 用户的学号
     * @return 返回是否存在
     */
    public Boolean isExistByCard(String card) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from user where card = ? and delete_time = '0'";
        Object[] params={card};
        int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
        return result==1?true:false;
    }

    /**
     * 根据用户名查询用户是否存在
     * @param name 用户名
     * @return 返回是否存在
     */
    public Boolean isExistByName(String name) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from user where name = ? and delete_time = '0'";
        Object[] params={name};
            int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
            return result==1?true:false;
    }

    /**
     * 判断邮箱是否存在
     * @param email 用户的邮箱
     * @return 返回是否存在
     */
    public boolean isExistByEmail(String email) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from user where email = ? and delete_time = '0'";
        Object[] params={email};
            int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
            return result==1?true:false;
    }


    /**
     * 注册一个用户
     * @param user 用户数据
     * @return 返回是否成功
     */
    public boolean addUser(User user) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="insert into user(card,name,sex,email,phone,cover,sign_time,identity) values (?,?,?,?,?,?,?,?)";
        Object[] params={user.getCard(),user.getName(),user.getSex(),user.getEmail(), user.getPhone(),user.getCover(),user.getSignTime(),user.getIdentity()};
        return queryRunner.update(sql,params)>=1?true:false;
    }


    /**
     * 删除一个用户
     * @param id 用户的id
     * @param time 注销时间
     * @return 返回是否成功
     */
    public boolean deleteUser(int id, String time) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update user set delete_time= ? where id = ? and delete_time = '0'";
        Object[] params={time,id};
        return queryRunner.update(sql,params)>=1?true:false;
    }


    /**
     * 更新一个用户信息
     * @param user 新的用户数据
     * @return 返回是否成功
     */
    public boolean updateUser(User user) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update user set card = ? , name = ? , sex = ? , email = ? , phone = ? , cover = ? , sign_time = ? , delete_time = ? , identity = ? where id = ? and delete_time = '0'";
        Object[] params={user.getCard(),user.getName(),user.getSex(),user.getEmail(),user.getPhone(),user.getCover(), user.getSignTime(),user.getDeleteTime(),user.getIdentity(),user.getId()};
        return queryRunner.update(sql,params)>=1?true:false;
    }


    /**
     * 根据用户id来返回对应的值
     * @param id 用户id
     * @return 用户的所有信息
     */
    public User getUser(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from user where id =? and delete_time = '0'";
        Object[] params={id};
        User user = queryRunner.query(sql, new BeanHandler<>(User.class,processor), params);
        return user;
    }


    /**
     * 返回所有的用户
     * @return 返回用户集合
     */
    public List<User> getAllUsers() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from user where delete_time = '0'";
        List<User> users = queryRunner.query(sql, new BeanListHandler<>(User.class,processor));
        return users;
    }


    /**
     * 根据传入的card号来返回用户对应的id
     * @param card 输入的卡号
     * @return 返回用户的id，错误返回-1
     */
    public int getUserIdByCard(String card) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select id from user where card = ? and delete_time = '0'";
        Object[] params={card};
        return queryRunner.query(sql, new ScalarHandler<Integer>(), params);
    }


    /**
     * 根据传入的name来返回用户对应的name
     * @param name 用户名
     * @return 返回用户的id
     */
    public int getUserIdByName(String name) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select id from user where name =? and delete_time = '0'";
        Object[] params={name};
        return queryRunner.query(sql, new ScalarHandler<Integer>(), params);
    }
}
