package com.webmaster.end.Service;

import com.webmaster.end.Dao.RentalDao;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * TODO 完善抽象类，先出模板供Controller调用
 *
 * @Description: 还书模板类
 * @Author: Daniel
 * @Date: 2020/4/24 3:30 下午
 */
@Service
public class BookReturnService extends BookServiceCore {

    private static HashMap<state, String> stateStringHashMap = new HashMap<>();

    static {
        stateStringHashMap.put(state.SUCCESS, "还书成功!");
        stateStringHashMap.put(state.ERR, "还书失败!");
        stateStringHashMap.put(state.BOOK_NOT_EXISTED, "书本不存在!");
        stateStringHashMap.put(state.NOT_BORROWED, "书本未借出!");
    }

    /**
     * @Description: 还书核心逻辑
     * @Author: Daniel
     * @Date: 2020/4/24 4:21 下午
     * @params: [bookid 书籍号]
     * @return: com.webmaster.end.Entity.BorrowState 书籍信息实体类
     */
    public BorrowState returnBook(int bookid){
        try {
            state st;
            String date = MyDateUtil.getCurrentString();
            //1.书籍是否存在
            st = bookDao.isExist(bookid) ? state.SUCCESS : state.BOOK_NOT_EXISTED;
            if (!isSuccess(st)) {
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            //2.书本是否已借出
            switch (bookDao.getBookStateById(bookid)) {
                case Book.HAS_BORROW:
                    st = state.SUCCESS;
                    break;
                case Book.ERROR_BORROW:
                    st = state.ERR;
                    break;
                default:
                    st = state.NOT_BORROWED;
                    break;
            }
            if (!isSuccess(st)) {
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            //3.还书业务
            int rentalid = rentalDao.getRentalIdByBookId(bookid);
            if (rentalid == -1) {
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(state.ERR));
            }
            //TODO invoke
            RentalDao.class.getDeclaredMethod(methodMap.get(methodList.updateReturnTime))
                    .invoke(rentalDao, rentalid, date);
            st = bookDao.updateBookState(bookid, Book.CAN_BORROW) ? state.SUCCESS : state.ERR;
            if(!isSuccess(st)){
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            else{
                Rental rental = rentalDao.getRentalByBookId(bookid);
                Book book = bookDao.getBookById(bookid);
                return new BorrowState(STATE_FAIL, rental, book, stateStringHashMap.get(st));
            }
        }
        catch (Exception e){
            return new BorrowState(STATE_FAIL, stateStringHashMap.get(state.ERR));
        }
    }

}
