package com.webmaster.end.Service;

import com.webmaster.end.Entity.BorrowState;
import org.springframework.stereotype.Service;

/**
 * TODO 完善抽象类，先出模板供Controller调用
 *
 * @Description: 续借模板类
 * @Author: Daniel
 * @Date: 2020/4/24 3:23 下午
 */
@Service
public class BookExtendborrowService extends BookServiceCore {
    /**
     * @Description: 续借核心逻辑
     * @Author: Daniel
     * @Date: 2020/4/24 3:27 下午
     * @params: [username 用户名, bookid 书籍id]
     * @return: com.webmaster.end.Entity.BorrowState
     */
    public BorrowState extendBorrow(int userid, int bookid){
        return testBorrowState;
    }

}
