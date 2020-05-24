package com.webmaster.end.Dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;

@Repository
public class PasswordDao {
    @Autowired
    private DataSource dataSource;

    /**
     * 判断该用户的密码是否存在
     * @param id 用户的Id
     * @return 是否存在
     */
    public boolean isExist(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from password where id = ?";
        Object[] params={id};
        Long count =  queryRunner.query(sql, new ScalarHandler<Long>(), params);
        return count.intValue()>0?true:false;
    }


    /**
     * 添加加密后的数据
     * @param id 用户ID
     * @param salt 加密用的盐值
     * @param password 加密后的密码
     * @return 是否添加成功
     */
    public Boolean addPassword(int id,String salt,String password) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="insert into password values(?,?,?)";
        Object[] params={id,salt,password};
        return queryRunner.update(sql,params)>0?true:false;
    }

    /**
     * 删除密码信息
     * @param id 用户ID
     * @return 返回是否删除成功
     */
    public Boolean deletePassword(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="delete from password where id = ?";
        Object[] params={id};
        return queryRunner.update(sql,params)>0?true:false;
    }


    /**
     * 修改密码
     * @param id 用户ID
     * @param salt 加密的盐值
     * @param password 密码
     * @return
     */
    public Boolean updatePassword(int id,String salt,String password) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update password set salt = ? , password = ? where id = ?";
        Object[] params={salt,password,id};
        return queryRunner.update(sql,params)>0?true:false;
    }


    /**
     * 根据用户id来获得对应的密码
     * @param id 用户的id
     * @return 返回密码
     */
    public String getPassword(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select password from password where id = ?";
        Object[] params={id};
        return queryRunner.query(sql,new ScalarHandler<String>(),params);
    }


    /**
     * 根据用户id来获得对应的盐值
     * @param id 用户的id
     * @return 返回盐值
     */
    public String getSalt(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select salt from password where id = ?";
        Object[] params={id};
        return queryRunner.query(sql,new ScalarHandler<String>(),params);
    }

}
