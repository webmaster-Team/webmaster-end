package com.webmaster.end.Service;

import com.webmaster.end.Dao.RentalDao;
import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @Description: 续借类
 * @Author: Daniel
 * @Date: 2020/4/24 3:23 下午
 */
@Service
public class BookExtendborrowService extends BookServiceCore {

    protected static HashMap<state, String> stateStringHashMap = new HashMap<>();

    static {
        stateStringHashMap.put(state.ERR, "续借失败!请重试");
        stateStringHashMap.put(state.SUCCESS, "续借成功!");
        stateStringHashMap.put(state.BOOK_NOT_EXISTED, "书籍不存在!");
        stateStringHashMap.put(state.USER_NOT_EXISTED, "用户不存在!");
        stateStringHashMap.put(state.REBORROW_EXCUTED, "本书已被续借，无法延长!");
        stateStringHashMap.put(state.NOT_BORROWED, "本书未被借出!请前往借书");
    }

    /**
     * @Description: 续借核心逻辑
     * @Author: Daniel
     * @Date: 2020/4/24 3:27 下午
     * @params: [username 用户名, bookid 书籍id]
     * @return: com.webmaster.end.Entity.BorrowState 书籍状态
     */
    public BorrowState extendBorrow(int userid, int bookid) {
        try {
            state st;
            //1.用户是否存在
            st = userDao.isExist(userid)? state.SUCCESS : state.USER_NOT_EXISTED;
            if(!isSuccess(st)){
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            //2.书籍是否存在
            st = bookDao.isExist(bookid) ? state.SUCCESS : state.BOOK_NOT_EXISTED;
            if(!isSuccess(st)){
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            //3.书籍是否未被借出就续借
            int rentalid = rentalDao.getRentalIdByBookId(bookid);
            if(rentalid == -1){
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(state.NOT_BORROWED));
            }
            //4.是否已续借过
            st = (!rentalDao.isReborrow(rentalid)) ? state.SUCCESS : state.REBORROW_EXCUTED;
            if(!isSuccess(st)){
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
            //5.处理续借事务
            Rental rental = new Rental(
                    bookid,
                    userid,
                    MyDateUtil.getCurrentString(),
                    null,
                    180, //先用180顶着
                    REBORROWED
            );
            st = rentalDao.updateReborrow(rentalid) ? state.SUCCESS : state.ERR;
            if(isSuccess(st)){
                return new BorrowState(
                        STATE_SUCCESS,
                        rental,
                        bookDao.getBookById(bookid),
                        stateStringHashMap.get(st));
            }
            else{
                return new BorrowState(STATE_FAIL, stateStringHashMap.get(st));
            }
        } catch (Exception e) {
            return new BorrowState(STATE_FAIL, stateStringHashMap.get(state.ERR));
        }
    }


}
