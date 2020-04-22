package com.webmaster.end.Dao;

import com.webmaster.end.Entity.Book;
import com.webmaster.end.Utils.MyDateUtil;
import org.apache.commons.dbutils.QueryRunner;
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
public class BookDao {
    @Autowired
    private DataSource dataSource;

    /**
     * 查询书籍是否存在
     * @param id 书籍的id
     * @return 返回是否存在
     */
    public Boolean isExist(int id){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from Book where id = ?";
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
     * 增加一个书籍
     * @param book 书籍数据
     * @return 返回是否成功
     */
    public boolean addBook(Book book){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="insert into Book(name,author,isbn,publisher,price,version,typeid,summary,cover,state,entry_time) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params={book.getName(),book.getAuthor(),book.getISBN(),book.getPublisher(),book.getPrice(),
                        book.getVersion(),book.getTypeId(),book.getSummary(),book.getCover(),book.getState(),
                        book.getEntry_time()};
        try {
            return queryRunner.update(sql,params)>=1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 注销一个书籍
     * @param id 书籍的id
     * @param time 注销时间
     * @return 返回是否成功
     */
    public boolean deleteBook(int id, String time){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update Book set delete_time= ? where id = ?";
        Object[] params={time,id};
        try {
            return queryRunner.update(sql,params)>=1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新一个书籍信息
     * @param book 新的书籍数据
     * @return 返回是否成功
     */
    public boolean updateBook(Book book){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update Book set name = ? , author = ? , isbn = ? , publisher = ? , price = ? , version = ? , typeid = ? , summary = ? , cover = ? , state = ? , entry_time = ? , delete_time = ? where id = ?";
        Object[] params={book.getName(),book.getAuthor(),book.getISBN(),book.getPublisher(),book.getPrice(),
                        book.getVersion(),book.getTypeId(),book.getSummary(),book.getCover(),book.getState(),
                        book.getEntry_time(),
                        book.getDelete_time(),book.getId()};
        try {
            return queryRunner.update(sql,params)>=1?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据书籍id来返回对应的值
     * @param id
     * @return
     */
    public Book getBookById(int id){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from Book where id =?";
        Object[] params={id};
        try {
            Book book = queryRunner.query(sql, new BeanHandler<>(Book.class), params);
            return book;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回所有的书籍
     * @return 返回书籍集合
     */
    public List<Book> getBooks(){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from Book";
        try {
            List<Book> books = queryRunner.query(sql, new BeanListHandler<>(Book.class));
            return books;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查看书籍的状态
     * @param id
     * @return 0表示无法借阅，1表示可借阅，2表示已经借出,-1表示查询错误
     */
    public int getBookStateById(int id){
        QueryRunner queryRunner = new QueryRunner(dataSource);
        String sql="select state from book where id = ?";
        Object[] params={id};
        try {
            return queryRunner.query(sql,new ScalarHandler<Integer>(),params);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


}
