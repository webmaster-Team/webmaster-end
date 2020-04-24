package com.webmaster.end.Service;

import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * TODO
 *
 * @Description: 借书Service
 * @Author: Daniel
 * @Date: 2020/4/23 2:15 下午
 */
@Service
public class BookBorrowService extends BookServiceCore {

    protected static HashMap<Integer, String> borrowStateMap = new HashMap<>();
    protected static HashMap<existState, String> existStateMap = new HashMap<>();
    protected final int STATE_SUCCESS = 1;
    protected final int STATE_FAIL = 0;
    protected final String failMsg = "借书失败!";

    protected enum existState {
        USER_NOT_EXISTED,
        BOOK_NOT_EXISTED,
        BOTH_EXISTED
    }
    static {
        borrowStateMap.put(0, "本书无法借阅!");
        borrowStateMap.put(1, "借阅成功!");
        borrowStateMap.put(2, "本书已借出!");
        borrowStateMap.put(-1, "查询错误!");
    }

    static {
        existStateMap.put(existState.USER_NOT_EXISTED, "用户不存在!");
        existStateMap.put(existState.BOOK_NOT_EXISTED, "书籍不存在!");
    }

    /**
     * @Description: 借书业务逻辑
     * @Author: Daniel
     * @Date: 2020/4/23 5:46 下午
     * @params: [bookId 书籍id, userId 用户id]
     */
    public BorrowState bookBorrow(int bookId, int userId) {
        Rental rental = new Rental();
        existState Estate = getState(userId, bookId);
        boolean operator = false;
        if (Estate == existState.BOTH_EXISTED) {
            Book book = new Book();
            int state = bookDao.getBookStateById(bookId);
            if (state == Book.CAN_BORROW) {
                operator = bookDao.updateBookState(bookId, Book.HAS_BORROW);
                if (operator) {
                    rental = new Rental(
                            book.getId(),
                            userId,
                            MyDateUtil.getCurrentString(),
                            "",
                            180,//TODO 可能存在不同的借书时间，先用180填充
                            (rentalDao.isExist(rentalDao.getRentalIdByBookId(bookId)) ? 1 : 0)
                    );
                    // STOPSHIP: 2020/4/24  

                } else {
                    return new BorrowState(STATE_FAIL, failMsg);
                }
                operator = rentalDao.addRental(rental);
                if (operator) {
                    return new BorrowState(
                            STATE_SUCCESS,
                            rental,
                            bookDao.getBookById(bookId),
                            borrowStateMap.get(state)
                    );
                }
            } else {
                return new BorrowState(STATE_FAIL,
                        (existStateMap.get(Estate) == null ?
                                failMsg : existStateMap.get(Estate)));
            }
        }
        return null;//TODO
    }

    /**
     * @Description: 判定用户和书籍是否存在，返回对应的状态码
     * @Author: Daniel
     * @Date: 2020/4/24 2:55 下午
     * @params: [userId 用户id, bookId 书籍id]
     * @return: com.webmaster.end.Service.BookBorrowService.existState
     * BOTH_EXISTED
     * BOOK_NOT_EXISTED
     * USER_NOT_EXISTED
     */
    protected existState getState(int userId, int bookId){
        boolean userState = userDao.isExist(userId);
        boolean bookState = bookDao.isExist(bookId);
        if(userState){
            if (bookState) {
                return existState.BOTH_EXISTED;
            }
            else {
                return existState.BOOK_NOT_EXISTED;
            }
        }
        else{
            return existState.USER_NOT_EXISTED;
        }
    }
}
