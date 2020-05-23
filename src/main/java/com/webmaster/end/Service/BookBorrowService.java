package com.webmaster.end.Service;

import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Entity.User;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 *
 * @Description: 借书Service
 * @Author: Daniel
 * @Date: 2020/4/23 2:15 下午
 */
@Service
public class BookBorrowService extends BookServiceCore {

    private static HashMap<state, String> stateStringHashMap = new HashMap<>();

    static {
        stateStringHashMap.put(state.SUCCESS, "借阅成功!");
        stateStringHashMap.put(state.ERR, "本书无法借阅!");
        stateStringHashMap.put(state.BOOK_NOT_EXISTED, "书籍不存在!");
        stateStringHashMap.put(state.USER_NOT_EXISTED, "用户不存在!");
        stateStringHashMap.put(state.BORROWED, "书籍已借出!请尝试续借");
        stateStringHashMap.put(state.BOOK_CANT_BORROWED, "本书不可借阅!请咨询管理员");
    }

    /**
     * @Description: 借书业务逻辑
     * @Author: Daniel
     * @Date: 2020/4/25 3:22 下午
     * @params: [bookId 书籍id, card 用户id]
     * @return: com.webmaster.end.Entity.BorrowState 书籍状态
     */
    public BorrowState bookBorrow(int bookId, int userid) {
        try{
            state st;
            String date = MyDateUtil.getCurrentString();
            //1.用户是否存在
            st = userDao.isExist(userid) ? state.SUCCESS : state.USER_NOT_EXISTED;
            if(!isSuccess(st)){
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            //2.书籍是否存在
            st = bookDao.isExist(bookId) ? state.SUCCESS : state.BOOK_NOT_EXISTED;
            if(!isSuccess(st)){
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            //3.根据书籍状态进行操作
            switch (bookDao.getBookStateById(bookId)){
                case Book.NOT_BORROW:
                    return new BorrowState(STATE_FAIL, stateStringHashMap.get(state.BOOK_CANT_BORROWED));
                case Book.HAS_BORROW:
                    return new BorrowState(STATE_FAIL, stateStringHashMap.get(state.BORROWED));
                case Book.CAN_BORROW:
                    break;
                default:
                    return new BorrowState(STATE_FAIL, stateStringHashMap.get(state.ERR));
            }
            User user = userDao.getUserById(userid);
            //添加rental
            Rental rental = new Rental(
                    bookId,
                    userid,
                    date,
                    null,
                    user.getIdentity()==0?30:180, //学生为0，教师为180
                    NOT_REBORROWED
            );
            st = rentalDao.addRental(rental) ? state.SUCCESS : state.ERR;
            if(!isSuccess(st)){
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            //更改书籍状态
            st = bookDao.updateBookState(bookId, Book.HAS_BORROW) ? state.SUCCESS : state.ERR;
            if(!isSuccess(st)){
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            else{
                return new BorrowState(
                        STATE_SUCCESS,
                        rental,
                        bookDao.getBookById(bookId),
                        stateStringHashMap.get(st));
            }
        }
        catch (Exception e){
            return new BorrowState(STATE_FAIL, stateStringHashMap.get(state.ERR));
        }
    }

}
