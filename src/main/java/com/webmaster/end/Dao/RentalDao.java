package com.webmaster.end.Dao;

import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Entity.User;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;

@Repository
public class RentalDao {
    @Autowired
    private DataSource dataSource;
    //下划线转驼峰
    RowProcessor processor=new BasicRowProcessor(new GenerousBeanProcessor());

    /**
     * 查询该条查询记录是否存在
     * @param id
     * @return
     */
    public boolean isExist(int id){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from rental where id = ?";
        Object[] params={id};
        try {
            int result = queryRunner.query(sql, new ScalarHandler<Integer>(), params);
            return result>0?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 新增一个借书记录
     * @param rental 借阅数据
     * @return 返回是否成功
     */
    public boolean addRental(Rental rental){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="insert into rental(book_id,user_id,borrow_time,return_time,duration,is_reborrow) values (?,?,?,?,?,?)";
        Object[] params={rental.getBookId(),rental.getUserId(),rental.getBorrowTime(),rental.getReturnTime(),rental.getDuration(),rental.getIsReborrow()};
        try {
            return queryRunner.update(sql,params)>=1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}