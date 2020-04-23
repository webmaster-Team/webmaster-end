package com.webmaster.end.Service;

import com.webmaster.end.Dao.BookDao;
import com.webmaster.end.Dao.RentalDao;
import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Utils.MD5Util;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * TODO
 *
 * @Description: 借书Service
 * @Author: Daniel
 * @Date: 2020/4/23 2:15 下午
 */
public class BookBorrowService {

    protected static BookDao bookDao        = new BookDao();
    protected static UserDao userDao        = new UserDao();
    protected static RentalDao rentalDao    = new RentalDao();
    protected static HashMap<Integer, String> borrowStateMap = new HashMap<>();

    static {
        borrowStateMap.put(0, "本书无法借阅!");
        borrowStateMap.put(1, "借阅成功!");
        borrowStateMap.put(2, "本书已借出!");
        borrowStateMap.put(-1, "查询错误!");
    }

    /**
     * @Description: 借书业务逻辑
     * @Author: Daniel
     * @Date: 2020/4/23 5:46 下午
     * @params: [bookId 书籍id, userId 用户id]
     * @return: com.webmaster.end.Service.BookBorrowService.BorrowState 返回状态，具体类在下方
     */
    public static BorrowState bookBorrow(int bookId, int userId){

        Rental rental = new Rental();
        boolean oprator = false;
        if(bookDao.isExist(bookId)){
            if(userDao.isExist(userId)){
                Book book = new Book();
                int state = bookDao.getBookStateById(bookId);
                if(state == 1){
                    oprator = bookDao.updateBookState(bookId, 2);
                    if(oprator){
                        rental = new Rental(
                                book.getId(),
                                userId,
                                new SimpleDateFormat("yyyymmdd").format(new Date()),
                                "",/**@TODO 还书日期先用空值顶着*/
                                180,
                                rentalDao.isExist(//TODO 缺少一个查询当前书籍所在借阅单号的方法));
                        //TODO 未完待续

                    }
                    oprator = rentalDao.addRental(rental);

                    return new BorrowState(
                            state,
                            bookDao.getBookById(bookId),
                            )
                }
            }
            else {
                return new BorrowState(0, "用户不存在!");
            }
        }
        else {
            return new BorrowState(0, "书籍不存在!");
        }
    }

    /**
     *
     * @Description: 书籍借阅状态的封装类，包含状态码、提示消息、书籍对象、借阅信息对象
     * @Author: Daniel
     * @Date: 2020/4/23 2:15 下午
     */
    protected static class BorrowState{

        private int state;
        private Rental rental;
        private Book book;
        private String msg;

        public BorrowState(){}

        public BorrowState(int state, Rental rental, Book book, String msg){
            setState(state); setBook(book); setRental(rental); setMsg(msg);
        }

        public BorrowState(int state, String msg){
            setMsg(msg); setRental(null); setBook(null); setState(state);
        }

        public void setState(int state){
            this.state = state;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setRental(Rental rental){
            this.rental = rental;
        }

        public void setBook(Book book) {
            this.book = book;
        }

        public Book getBook() {
            return book;
        }

        public Rental getRental() {
            return rental;
        }

        public int getState() {
            return state;
        }

        public String getMsg() {
            return msg;
        }

    }

}
