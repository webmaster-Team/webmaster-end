package com.webmaster.end.Service;

import com.webmaster.end.Dao.UserDao;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;

import java.util.HashMap;

/**
 * TODO 完善抽象类，先出模板供Controller调用
 *
 * @Description: 续借类
 * @Author: Daniel
 * @Date: 2020/4/24 3:23 下午
 */
public class BookExtendborrowService extends BookServiceCore {

    protected static HashMap<state, String> stateStringHashMap = new HashMap<>();
    private enum state{
        SUCCESS, ERR
    }

    static {
        stateStringHashMap.put(state.ERR, "续借失败!请重试");
        stateStringHashMap.put(state.SUCCESS, "续借成功!");
    }

    /**
     * @Description: 续借核心逻辑
     * @Author: Daniel
     * @Date: 2020/4/24 3:27 下午
     * @params: [username 用户名, bookid 书籍id]
     * @return: com.webmaster.end.Entity.BorrowState
     */
    public BorrowState extendBorrow(String card, int bookid){
        try{
            int userid = userDao.getUserIdByCard(card);
            
            return null;
        }
        catch (Exception e){
            return new BorrowState(STATE_FAIL, stateStringHashMap.get(state.ERR));
        }
    }

}
