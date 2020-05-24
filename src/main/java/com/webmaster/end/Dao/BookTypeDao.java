package com.webmaster.end.Dao;

import com.webmaster.end.Entity.BookType;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BookTypeDao {
    @Autowired
    private DataSource dataSource;

    /**
     * 根据id看书籍类型是否存在
     * @param id type表的id
     * @return 返回是否存在，报错则为已存在
     */
    public Boolean isExist(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from book_type where id = ?";
        Object[] params={id};
        int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
        return result==1?true:false;
    }


    /**
     * 判断该类型是否存在
     * @param typeid 类型id
     * @return 返回是否存在
     */
    public Boolean isExistByTypeid(String typeid) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from book_type where typeid = ?";
        Object[] params={typeid};
        int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
        return result==1?true:false;
    }

    /**
     * 根据具体类型名判断是否存在
     * @param title 类型名
     * @return 返回是否存在
     */
    public Boolean isExistByTitle(String title) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from book_type where title = ?";
        Object[] params={title};
        int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
        return result==1?true:false;
    }

    /**
     * 获得所有的书籍类型
     * @return 返回所有书籍类型
     */
    public List<BookType> getBookTypes() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book_type";
        List<BookType> bookTypes = queryRunner.query(sql, new BeanListHandler<>(BookType.class));
        return bookTypes;
    }


    /**
     * 根据id来获得BookType
     * @param id type表的id
     * @return BookType对象
     */
    public BookType getBookType(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book_type where id = ?";
        Object[] params={id};
        BookType bookType = queryRunner.query(sql, new BeanHandler<>(BookType.class),params);
        return bookType;
    }


    /**
     * 根据typeid来获得BookType
     * @param typeid 类型id
     * @return BookType对象
     */
    public BookType getBookTypeByTypeid(String typeid) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book_type where typeid = ?";
        Object[] params={typeid};
        BookType bookType = queryRunner.query(sql, new BeanHandler<>(BookType.class),params);
        return bookType;
    }


    /**
     * 根据title来获得BookType
     * @param title 类型名
     * @return BookType对象
     */
    public BookType getBookTypeByTitle(String title) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book_type where title = ?";
        Object[] params={title};
        BookType bookType = queryRunner.query(sql, new BeanHandler<>(BookType.class),params);
        return bookType;
    }
}
