package com.webmaster.end.Service;

import com.webmaster.end.Dao.BookDao;
import com.webmaster.end.Dao.RentalDao;
import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

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

    protected final int STATE_SUCCESS = 1;
    protected final int STATE_FAIL = 0;

    /**
     * TODO 未编写的方法暂时使用反射调用(因为不会报错)
     */
    protected static HashMap<methodList, String> methodMap = new HashMap<>();
    protected enum methodList{
        isReborrow
    }

    static {
        for (methodList iter:
             methodList.values()) {
            methodMap.put(iter, iter.toString());
        }
    }

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

}
