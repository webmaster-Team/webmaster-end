package com.webmaster.end.IMapper;

import com.webmaster.end.Entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IBookMapper {

    /**
     * 根据书籍Id,查询该类书籍是否还存在
     * @param id 该类书籍的id
     * @return 返回是否存在
     */
    public Boolean isExist(int id);

    /**
     * 增加一类书籍
     * @param book 该类书籍的数据
     * @return 返回是否成功
     */
    public boolean addBook(Book book);


//    /**
//     * 删除一个书籍
//     * @param id 该类书籍的id
//     * @param time 删除时间
//     * @return 返回是否成功
//     */
//    public boolean deleteBook(int id, String time);
//
//    /**
//     * 更新一类的书籍信息
//     * @param book 新的一类书籍数据
//     * @return 返回是否成功
//     */
//    public boolean updateBook(Book book);


    /**
     * 根据书籍id来返回对应的书籍
     * @param id 书籍Id
     * @return 返回该书的定义
     */
    public Book getBook(int id);


// ----------------------------------------根据各种需求进行的查询工作----------------------------------------------------

    /**
     * 查看书籍的所剩数量
     * @param id 书籍的id
     * @return 书籍的数量
     */
    public int getState(int id);


    /**
     * 修改书籍的所剩数量
     * @param id 书籍的id
     * @param state 书籍的数量
     * @return 返回是否成功
     */
    public boolean updateState(@Param("id") int id, @Param("state") int state);


    /**
     * (借书)将对应的书籍的数量减1
     * @param id 书籍的id
     * @return 返回是否成功-1
     */
    public boolean declineState(int id);


    /**
     * (还书)将对应的书籍的数量加1
     * @param id 书籍的id
     * @return 返回是否成功+1
     */
    public boolean increaseState(int id) ;


    /**
     * 获得所有的书籍作者
     * @return 作者名
     */
    public List<String> getAuthors();


    /**
     * 获得所有的出版社
     * @return 出版社名
     */
    public List<String> getPublishers();


    /**
     * 返回所有的书籍
     * @return 返回书籍集合
     */
    public List<Book> getAllBooks();


    /**
     * 返回类别的对应的书籍
     * @param typeId 书籍类别Id
     * @return 返回书籍集合
     */
    public List<Book> getBooksByTypeId(String typeId);


    /**
     * 根据ISBN返回对应的书籍
     * @param iSBN ISBN号
     * @return 返回对应的书籍
     */
    public Book getBookByISBN(String iSBN);


    /**
     * 返回拥有关键字的书籍
     * @return 返回书籍集合
     */
    public List<Book> getBooksByKey(String key);
}
