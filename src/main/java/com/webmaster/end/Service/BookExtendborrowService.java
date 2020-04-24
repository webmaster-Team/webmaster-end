package com.webmaster.end.Service;

import com.webmaster.end.Entity.BorrowState;

/**
 * TODO 完善抽象类，先出模板供Controller调用
 *
 * @Description: 续借模板类
 * @Author: Daniel
 * @Date: 2020/4/24 3:23 下午
 */
public class BookExtendborrowService extends BookServiceCore {
    /**
     * @Description: 续借核心逻辑
     * @Author: Daniel
     * @Date: 2020/4/24 3:27 下午
     * @params: [username 用户名, bookid 书籍id]
     * @return: com.webmaster.end.Entity.BorrowState
     */
    public BorrowState extendBorrow(String userid, String bookid){
        return testBorrowState;
    }

}
