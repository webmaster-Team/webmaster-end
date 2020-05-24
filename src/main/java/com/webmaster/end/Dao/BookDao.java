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
    //delete_time为0表示书籍已经被删除
    //出错时就返回较差的期望

    @Autowired
    private DataSource dataSource;

    //下划线转驼峰
    RowProcessor processor=new BasicRowProcessor(new GenerousBeanProcessor());

    /**
     * 根据书籍Id,查询该类书籍是否还存在
     * @param id 该类书籍的id
     * @return 返回是否存在
     */
    public Boolean isExist(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        //书籍还没有被删除
        String sql="select count(*) from book where id = ? and delete_time = '0'";
        Object[] params={id};
        int result = queryRunner.query(sql, new ScalarHandler<Long>(), params).intValue();
        return result==1?true:false;
    }

    /**
     * 增加一类书籍
     * @param book 该类书籍的数据
     * @return 返回是否成功
     */
    public boolean addBook(Book book) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="insert into book"+
                    "(name,author,isbn,publisher,price,version,typeid,summary,cover,library,layer,origin,entail,state,entry_time) "+
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params={book.getName(),
                        book.getAuthor(),
                        book.getISBN(),
                        book.getPublisher(),
                        book.getPrice(),
                        book.getVersion(),
                        book.getTypeId(),
                        book.getSummary(),
                        book.getCover(),
                        book.getLibrary(),
                        book.getLayer(),
                        book.getOrigin(),
                        book.getEntail(),
                        book.getState(),
                        book.getEntryTime()};
        return queryRunner.update(sql,params)>=1?true:false;
    }


    /**
     * 删除一个书籍
     * @param id 该类书籍的id
     * @param time 删除时间
     * @return 返回是否成功
     */
    public boolean deleteBook(int id, String time) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update book set delete_time= ? where id = ? and delete_time = '0'";
        Object[] params={time,id};
        return queryRunner.update(sql,params)>=1?true:false;
    }

    /**
     * 更新一类的书籍信息
     * @param book 新的一类书籍数据
     * @return 返回是否成功
     */
    public boolean updateBook(Book book) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="update book set name = ? , author = ? , isbn = ? , publisher = ? , "+
                    "price = ? , version = ? , typeid = ? , summary = ? , cover = ? ,library = ? ,"+
                    "layer = ? ,origin = ? , entail = ? , state = ? , entry_time = ? , delete_time = ? "+
                    "where id = ? and delete_time = '0'";
        Object[] params={book.getName(),
                        book.getAuthor(),
                        book.getISBN(),
                        book.getPublisher(),
                        book.getPrice(),
                        book.getVersion(),
                        book.getTypeId(),
                        book.getSummary(),
                        book.getCover(),
                        book.getLibrary(),
                        book.getLayer(),
                        book.getOrigin(),
                        book.getEntail(),
                        book.getState(),
                        book.getEntryTime(),
                        book.getDeleteTime(),
                        book.getId()};
        return queryRunner.update(sql,params)>=1?true:false;
    }


    /**
     * 根据书籍id来返回对应的书籍
     * @param id 书籍Id
     * @return 返回该书的定义
     */
    public Book getBook(int id) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book where id =? and delete_time = '0'";
        Object[] params={id};
        Book book = queryRunner.query(sql, new BeanHandler<>(Book.class,processor), params);
        return book;
    }


// ----------------------------------------根据各种需求进行的查询工作----------------------------------------------------

    /**
     * 查看书籍的所剩数量
     * @param id 书籍的id
     * @return 书籍的数量
     */
    public int getState(int id) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        String sql="select state from book where id = ? and delete_time = '0'";
        Object[] params={id};
        return queryRunner.query(sql,new ScalarHandler<Integer>(),params);
    }


    /**
     * 修改书籍的所剩数量
     * @param id 书籍的id
     * @param state 书籍的数量
     * @return 返回是否成功
     */
    public boolean updateState(int id,int state) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        String sql="update book set state = ? where id = ? and delete_time = '0'";
        Object[] params={state,id};
        return queryRunner.update(sql,params)>0?true:false;
    }


    /**
     * (借书)将对应的书籍的数量减1
     * @param id 书籍的id
     * @return 返回是否成功-1
     */
    public boolean declineState(int id) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        String sql="update book set state = state-1 where id = ? and delete_time = '0'";
        Object[] params={id};
        return queryRunner.update(sql,params)>0?true:false;
    }


    /**
     * (还书)将对应的书籍的数量加1
     * @param id 书籍的id
     * @return 返回是否成功+1
     */
    public boolean increaseState(int id) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        String sql="update book set state = state+1 where id = ? and delete_time = '0'";
        Object[] params={id};
        return queryRunner.update(sql,params)>0?true:false;
    }


    /**
     * 获得所有的书籍作者
     * @return 作者名
     */
    public List<String> getAuthors() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select distinct author from book";
        List<Object[]> authors = queryRunner.query(sql, new ArrayListHandler());
        List<String> result=new ArrayList<>();
        for (Object[] author : authors)
            result.add((String)(author[0]));
        return result;
    }


    /**
     * 获得所有的出版社
     * @return 出版社名
     */
    public List<String> getPublishers() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select distinct publisher from book";
        List<Object[]> publishers = queryRunner.query(sql, new ArrayListHandler());
        List<String> result=new ArrayList<>();
        for (Object[] publisher : publishers)
            result.add((String)(publisher[0]));
        return result;
    }


    /**
     * 返回所有的书籍
     * @return 返回书籍集合
     */
    public List<Book> getAllBooks() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book where delete_time = '0'";
        List<Book> books = queryRunner.query(sql, new BeanListHandler<>(Book.class,processor));
        return books;
    }


    /**
     * 返回类别的对应的书籍
     * @param typeId 书籍类别Id
     * @return 返回书籍集合
     */
    public List<Book> getBooksByTypeId(String typeId) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book where typeid= ? and delete_time = '0'";
        Object[] params={typeId};
        List<Book> books = queryRunner.query(sql, new BeanListHandler<>(Book.class,processor),params);
        return books;
    }


    /**
     * 根据ISBN返回对应的书籍
     * @param iSBN ISBN号
     * @return 返回对应的书籍
     */
    public Book getBookByISBN(String iSBN) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book where isbn= ? and delete_time = '0'";
        Object[] params={iSBN};
        Book book = queryRunner.query(sql, new BeanHandler<>(Book.class,processor), params);
        return book;
    }


    /**
     * 返回拥有关键字的书籍
     * @return 返回书籍集合
     */
    public List<Book> getBooksByKey(String key) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from book where delete_time = '0' and (name like ? or author like ? or publisher like ?) ";
        key="%"+key+"%";
        Object[] params={key,key,key};
        List<Book> books = queryRunner.query(sql, new BeanListHandler<>(Book.class,processor),params);
        return books;
    }
}
