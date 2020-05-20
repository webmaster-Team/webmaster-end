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
     * 添加加密后的数据
     * @param userId 用户ID
     * @param salt 加密用的盐值
     * @param password 加密后的密码
     * @return 是否添加成功
     */
    public Boolean addPassword(int userId,String salt,String password) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="insert into password values(?,?,?)";
        Object[] params={userId,salt,password};
        return queryRunner.update(sql,params)>0?true:false;
    }

    /**
     * 删除密码信息
     * @param userId 用户ID
     * @return 返回是否删除成功
     */
    public Boolean deletePassword(int userId) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="delete from password where id = ?";
        Object[] params={userId};
        return queryRunner.update(sql,params)>0?true:false;
    }

    /**
     * 修改密码
     * @param userId 用户ID
     * @param salt 加密的盐值
     * @param password 密码
     * @return
     */
    public Boolean updatePassword(int userId,String salt,String password){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update password set salt = ? , password = ? where id = ?";
        Object[] params={salt,password,userId};
        try {
            return queryRunner.update(sql,params)>0?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据用户id来获得对应的密码
     * @param userId 用户的id
     * @return 返回密码
     */
    public String getPasswordById(int userId){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select password from password where id = ?";
        Object[] params={userId};
        try {
            String password=queryRunner.query(sql,new ScalarHandler<String>(),params);
            return password;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据用户id来获得对应的盐值
     * @param userId 用户的id
     * @return 返回盐值
     */
    public String getSaltById(int userId){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select salt from password where id = ?";
        Object[] params={userId};
        try {
            String salt=queryRunner.query(sql,new ScalarHandler<String>(),params);
            return salt;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断密码条款是否存在
     * @param userId 用户的Id
     * @return 是否存在
     */
    public boolean isExist(int userId){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from password where id = ?";
        Object[] params={userId};
        try {
            Long count =   queryRunner.query(sql, new ScalarHandler<Long>(), params);
            return count.intValue()>0?true:false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
