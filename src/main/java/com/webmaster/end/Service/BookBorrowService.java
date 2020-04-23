package com.webmaster.end.Service;

import com.webmaster.end.Dao.BookDao;
import com.webmaster.end.Dao.RentalDao;
import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Utils.MD5Util;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Autowired
    private BookDao bookDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RentalDao rentalDao;

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
     * @return: com.webmaster.end.Service.BookBorrowService.BorrowState 返回状态
     */
    public  BorrowState bookBorrow(int bookId, int userId) {

        Rental rental = new Rental();
        boolean oprator = false;
        if (bookDao.isExist(bookId)) {
            if (userDao.isExist(userId)) {
                Book book = new Book();
                int state = bookDao.getBookStateById(bookId);
                if (state == Book.CAN_BORROW) {
                    oprator = bookDao.updateBookState(bookId, Book.HAS_BORROW);
                    if (oprator) {
                        rental = new Rental(
                                book.getId(),
                                userId,
                                MyDateUtil.convertDateToStr(new Date()),
                                "",/**@TODO 还书日期先用空值顶着*/
                                180,
                                (rentalDao.isExist(rentalDao.getRentalIdByBookId(bookId))?1:0)
                        );
                        //TODO 未完待续

                    }
                    oprator = rentalDao.addRental(rental);
                    if(oprator)
                        return new BorrowState(
                                state,
                                rental,
                                bookDao.getBookById(bookId),
                                borrowStateMap.get(state)
                        );
                }
            } else {
                return new BorrowState(0, "用户不存在!");
            }
        } else {
            return new BorrowState(0, "书籍不存在!");
        }
        return new BorrowState(0, "借书错误!");
    }
}
