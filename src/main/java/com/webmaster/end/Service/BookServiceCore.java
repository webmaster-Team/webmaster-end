package com.webmaster.end.Service;

import com.webmaster.end.Dao.BookDao;
import com.webmaster.end.Dao.RentalDao;
import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
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

    protected final int REBORROWED = 1;
    protected final int NOT_REBORROWED = 0;

    /**
     * @Description: 描述在各种操作中可能产生的状态，方便操作
     */
    protected enum state {
        SUCCESS, ERR, USER_NOT_EXISTED, BOOK_NOT_EXISTED,
        REBORROW_EXCUTED, NOT_BORROWED, BORROWED, BOOK_CANT_BORROWED
    }

    /**
     * TODO 未编写的方法暂时使用反射调用(因为不会报错)
     */
    protected static HashMap<methodList, String> methodMap = new HashMap<>();

    protected enum methodList {
        updateReturnTime
    }

    static {
        for (methodList iter :
                methodList.values()) {
            methodMap.put(iter, iter.toString());
        }
    }

    /**
     * 返回书籍是否存在
     *
     * @param bookid 书籍的id
     * @return 返回是否存在
     */
    public boolean bookIsExist(int bookid) {
        return bookDao.isExist(bookid);
    }

    /**
     * 返回书籍信息
     *
     * @param bookid 书籍的id
     * @return 返回是否存在
     */
    public Book getBookByid(int bookid) {
        if (bookDao.isExist(bookid)) {
            return bookDao.getBookById(bookid);
        }
        return null;
    }

    /**
     * 根据bookId，判断是否有借阅记录
     *
     * @param bookId 书籍ID
     * @return 返回是否存在
     */
    public boolean rentalIsExist(int bookId) {
        return rentalDao.getRentalIdByBookId(bookId) == -1 ? false : true;
    }

    /**
     * @Description: 判断状态是否为success
     * @Author: Daniel
     * @Date: 2020/4/25 3:24 下午
     * @params: [st 状态]
     * @return: boolean
     */
    protected boolean isSuccess(state st) {
        return st == state.SUCCESS;
    }
    //名字排序
    class BookNameCompartor implements Comparator<Book> {
        private boolean isUpSort=true;
        public BookNameCompartor(boolean isUpSort) {
            this.isUpSort=isUpSort;
        }

        @Override
        public int compare(Book o1, Book o2) {
            if(isUpSort)
                return o1.getName().compareTo(o2.getName());
            else
                return (-1)*(o1.getName().compareTo(o2.getName()));
        }
    }
    //日期排序
    class BookDateCompartor implements Comparator<Book> {
        private boolean isUpSort=true;
        public BookDateCompartor(boolean isUpSort) {
            this.isUpSort=isUpSort;
        }

        @Override
        public int compare(Book o1, Book o2) {
            if(isUpSort)
                return MyDateUtil.isFirstDatePrevious(o1.getEntryTime(),o2.getEntryTime())?-1:1;
            else
                return MyDateUtil.isFirstDatePrevious(o1.getEntryTime(),o2.getEntryTime())?1:-1;
        }
    }


}
