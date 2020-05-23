package com.webmaster.end.Dao;

import com.webmaster.end.Entity.Book;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDao {
    @Autowired
    private DataSource dataSource;
    //下划线转驼峰
    RowProcessor processor=new BasicRowProcessor(new GenerousBeanProcessor());
    /**
     * 查询书籍是否存在
     * @param id 书籍的id
     * @return 返回是否存在
     */
    public Boolean isExist(int id){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select count(*) from book where id = ? and delete_time is null";
        Object[] params={id};
        try {
            int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
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
        String sql="insert into book(name,author,isbn,publisher,price,version,typeid,summary,cover,library,layer,entail,state,entry_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params={book.getName(),book.getAuthor(),book.getISBN(),book.getPublisher(),book.getPrice(),
                        book.getVersion(),book.getTypeId(),book.getSummary(),book.getCover(),book.getLibrary(),
                        book.getLayer(),book.getEntail(),book.getState(), book.getEntryTime()};
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
        String sql="update book set delete_time= ? where id = ? and delete_time is null";
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
        String sql="update book set name = ? , author = ? , isbn = ? , publisher = ? , price = ? , version = ? , typeid = ? , summary = ? , cover = ? ,library = ? ,layer = ? ,entail = ? , state = ? , entry_time = ? , delete_time = ? where id = ? and delete_time is null";
        Object[] params={book.getName(),book.getAuthor(),book.getISBN(),book.getPublisher(),book.getPrice(),
                        book.getVersion(),book.getTypeId(),book.getSummary(),book.getCover(),book.getLibrary(),
                        book.getLayer(),book.getEntail(),book.getState(), book.getEntryTime(),
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
        String sql="select * from book where id =? and delete_time is null";
        Object[] params={id};
        try {
            Book book = queryRunner.query(sql, new BeanHandler<>(Book.class,processor), params);
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
        String sql="select * from book where delete_time is null";
        try {

            List<Book> books = queryRunner.query(sql, new BeanListHandler<>(Book.class,processor));
            return books;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回对应的typeId对应的书籍
     * @return 返回书籍集合
     */
    public List<Book> getBooksByTypeId(String typeId){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book where typeid= ? and delete_time is null";
        Object[] params={typeId};
        try {
            List<Book> books = queryRunner.query(sql, new BeanListHandler<>(Book.class,processor),params);
            return books;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回所有相同名字的书籍
     * @return 返回书籍集合
     */
    public List<Book> getBooksByKey(String key){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book where delete_time is null and (name like ? or author like ? or publisher like ?) ";
        key="%"+key+"%";
        Object[] params={key,key,key};
        try {
            List<Book> books = queryRunner.query(sql, new BeanListHandler<>(Book.class,processor),params);
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
        String sql="select state from book where id = ? and delete_time is null";
        Object[] params={id};
        try {
            return queryRunner.query(sql,new ScalarHandler<Integer>(),params);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 修改书籍状态
     * @param id
     * @param state
     * @return
     */
    public boolean updateBookState(int id,int state){
        QueryRunner queryRunner = new QueryRunner(dataSource);
        String sql="update book set state = ? where id = ? and delete_time is null";
        Object[] params={state,id};
        try {
            return queryRunner.update(sql,params)>0?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据bookid来获得对应的书籍
     * @param bookId 书籍ID
     * @return 返回书籍
     */
    public Book getBookByBookId(int bookId) {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book where id= ? and delete_time is null";
        Object[] params={bookId};
        try {
            Book book = queryRunner.query(sql, new BeanHandler<>(Book.class,processor), params);
            return book;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ISBN返回对应的书籍
     * @param iSBN ISBN号
     * @return 返回对应的书籍
     */
    public Book getBookByISBN(String iSBN){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book where isbn= ? and delete_time is null";
        Object[] params={iSBN};
        try {
            Book book = queryRunner.query(sql, new BeanHandler<>(Book.class,processor), params);
            return book;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得所有的书籍作者
     * @return 作者名
     */
    public List<String> getAuthors(){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select distinct author from book";
        try {
            List<Object[]> authors = queryRunner.query(sql, new ArrayListHandler());
            List<String> result=new ArrayList<>();
            for (Object[] author : authors)
                result.add((String)(author[0]));
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得所有的出版社
     * @return 出版社名
     */
    public List<String> getPublishers(){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select distinct publisher from book";
        try {
            List<Object[]> publishers = queryRunner.query(sql, new ArrayListHandler());
            List<String> result=new ArrayList<>();
            for (Object[] publisher : publishers)
                result.add((String)(publisher[0]));
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
