package com.webmaster.end.Dao;

import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Entity.User;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RentalDao {
    @Autowired
    private DataSource dataSource;
    //下划线转驼峰
    RowProcessor processor=new BasicRowProcessor(new GenerousBeanProcessor());

    /**
     * 查询该条查询记录是否存在
     * @param id 记录的id
     * @return 返回是否存在
     */
    public boolean isExist(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from rental where id = ? and return_time = '0'";
        Object[] params={id};
        int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
        return result>0?true:false;

    }

    /**
     * 查询该条流水是否存在
     * @param bookId 书籍id
     * @param userId 用户id
     * @return 返回是否存在
     */
    public boolean isExist(int bookId,int userId) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from rental where book_id = ? and user_id = ?  and return_time = '0'";
        Object[] params={bookId,userId};
        int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
        return result>0?true:false;

    }

    /**
     * 根据流水id判断书籍是否续借
     * @param id 流水id
     * @return
     */
    public boolean isReborrow(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select is_reborrow from rental where id= ? and return_time = '0'";
        Object[] params={id};
        return queryRunner.query(sql,new ScalarHandler<Integer>(),params)==1?true:false;
    }

    /**
     * 新增一个借书记录
     * @param rental 借阅数据
     * @return 返回是否成功
     */
    public boolean addRental(Rental rental) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="insert into rental(book_id,user_id,borrow_time,return_time,duration,is_reborrow) values (?,?,?,?,?,?)";
        Object[] params={rental.getBookId(),rental.getUserId(),rental.getBorrowTime(),rental.getReturnTime(),rental.getDuration(),rental.getIsReborrow()};
        return queryRunner.update(sql,params)>=1?true:false;
    }

    /**
     * 归还书籍，记录对应的归还时间
     * @param id 流水id
     * @param date 归还的时间
     * @return
     */
    public boolean updateReturnTime(int id, String date) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update rental set return_time= ? where id= ? and return_time = '0'";
        Object[] params={date,id};
        return queryRunner.update(sql,params)>0?true:false;
    }

    /**
     * 更改对应的续借状态
     * @param id 流水id
     * @return
     */
    public boolean updateReborrow(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update rental set is_reborrow= ? where id= ? and return_time = '0'";
        Object[] params={1,id};
        return queryRunner.update(sql,params)>0?true:false;
    }


    /**
     * 根据流水id查询对应的流水
     * @param id 流水id
     * @return 正确返回对象，否则为Null
     */
    public Rental getRental(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from rental where id= ? and return_time = '0'";
        Object[] params={id};
        return queryRunner.query(sql,new BeanHandler<>(Rental.class,processor),params);
    }


    /**
     * 根据书籍ID返回流水号
     * @param bookId 书籍ID
     * @param userId 用户ID
     * @return 返回流水号,-1则为错误
     */
    public int getRentalId(int bookId,int userId) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select id from rental where book_id= ? and user_id = ? and return_time = '0'";
        Object[] params={bookId,userId};
        return queryRunner.query(sql,new ScalarHandler<Integer>(),params);
    }


    /**
     * 根据书籍id查询对应的流水
     * @param bookId 书籍id
     * @param userId 用户Id
     * @return 返回Rental对象，失败返回Null
     */
    public Rental getRental(int bookId,int userId) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from rental where book_id= ? and user_id = ? and return_time = '0'";
        Object[] params={bookId,userId};
        return queryRunner.query(sql,new BeanHandler<>(Rental.class,processor),params);
    }


    /**
     * 根据用户id返回用户的借阅数量
     * @param userId 用户的id
     * @return 返回用户的借阅数量
     */
    public int getBorrowBooksByUserId(int userId) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from rental where user_id= ? and return_time = '0'";
        Object[] params={userId};
        return queryRunner.query(sql,new ScalarHandler<Long>(),params).intValue();
    }


    /**
     * 返回借阅较多的书籍
     * @return 热门书籍的id
     * @throws SQLException
     */
    public List<Integer> getHotBooksId() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select book_id from (select book_id,count(*) as number from rental group by book_id) aa ORDER BY number desc limit 3";
        List<Object[]> hotBooksId=queryRunner.query(sql,new ArrayListHandler());
        List<Integer> result=new ArrayList<>();
        for (Object[] hotBookId : hotBooksId)
            result.add((Integer)(hotBookId[0]));
        return result;
    }
}
