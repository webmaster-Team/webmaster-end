package com.webmaster.end.Service;

import com.webmaster.end.Dao.*;
import com.webmaster.end.Entity.*;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
    @Autowired
    protected BookTypeDao bookTypeDao;
    @Autowired
    protected LibraryDao libraryDao;

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

    /**
     * 获得所有的书籍类型
     * @return 类型列表
     */
    public List<BookType> getBookTypes(){
        return bookTypeDao.getBookTypes();
    }

    /**
     * 获得所有的图书馆类型
     * @return 图书馆列表
     */
    public List<Library> getLibraries(){
        return libraryDao.getLibraries();
    }

    /**
     * 获得所有的作者
     * @return 作者列表
     */
    public List<String> getAuthors(){
        return bookDao.getAuthors();
    }

    /**
     * 获得所有的出版社
     * @return 出版社列表
     */
    public List<String> getPublishers(){
        return bookDao.getPublishers();
    }

    /**
     * 根据类型获得具体的名字
     * @param type 书记类型
     * @return 类型名
     */
    public String getTitleByType(String type){
        return bookTypeDao.getBookTypeByType(type).getTitle();
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
