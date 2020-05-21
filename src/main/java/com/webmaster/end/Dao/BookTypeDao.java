package com.webmaster.end.Dao;

import com.webmaster.end.Entity.Book;
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

    public Boolean isExist(int id){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from book_type where id = ?";
        Object[] params={id};
        try {
            int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
            return result==1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public Boolean isExistByType(String type){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from book_type where type = ?";
        Object[] params={type};
        try {
            int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
            return result==1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public Boolean isExistByTitle(String title){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from book_type where title = ?";
        Object[] params={title};
        try {
            int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
            return result==1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public List<BookType> getBookTypes(){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book_type";
        try {
            List<BookType> bookTypes = queryRunner.query(sql, new BeanListHandler<>(BookType.class));
            return bookTypes;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BookType getBookTypeById(int id){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book_type where id = ?";
        Object[] params={id};
        try {
            BookType bookType = queryRunner.query(sql, new BeanHandler<>(BookType.class),params);
            return bookType;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BookType getBookTypeByType(String type){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book_type where type = ?";
        Object[] params={type};
        try {
            BookType bookType = queryRunner.query(sql, new BeanHandler<>(BookType.class),params);
            return bookType;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BookType getBookTypeByTitle(String title){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book_type where title = ?";
        Object[] params={title};
        try {
            BookType bookType = queryRunner.query(sql, new BeanHandler<>(BookType.class),params);
            return bookType;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
