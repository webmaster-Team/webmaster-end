package com.webmaster.end.Service;

import com.webmaster.end.Dao.BookDao;
import com.webmaster.end.Dao.RentalDao;
import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 *
 * @Description: 书籍核心类
 * @Author: Daniel
 * @Date: 2020/4/24 1:41 下午
 */
public class BookServiceCore {

    @Autowired
    protected BookDao bookDao;
    @Autowired
    protected UserDao userDao;
    @Autowired
    protected RentalDao rentalDao;

    /**
     * TODO 临时的返回数据，补完逻辑记得删
     */
    private final Rental rental = new Rental(
            1,
            1,
            1,
            "20200422",
            "20200422",
            180,
            1
    );
    private final Book book = bookDao.getBookById(1);
    protected BorrowState testBorrowState = new BorrowState(1, rental, book, "成功");

    /**
     * 返回书籍是否存在
     * @param bookid 书籍的id
     * @return 返回是否存在
     */
    public boolean bookIsExist(int bookid){
        return bookDao.isExist(bookid);
    }

    /**
     * 返回书籍信息
     * @param bookid 书籍的id
     * @return 返回是否存在
     */
    public Book getBookByid(int bookid){
        if(bookDao.isExist(bookid))
            return bookDao.getBookById(bookid);
        return null;
    }

    /**
     * 根据bookId，判断是否有借阅记录
     * @param bookId 书籍ID
     * @return 返回是否存在
     */
    public boolean rentalIsExist(int bookId){
        return rentalDao.getRentalIdByBookId(bookId)==-1?false:true;
    }
}
