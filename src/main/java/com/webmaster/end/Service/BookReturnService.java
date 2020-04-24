package com.webmaster.end.Service;

import com.webmaster.end.Entity.BorrowState;
import org.springframework.stereotype.Service;

/**
 * TODO 完善抽象类，先出模板供Controller调用
 *
 * @Description: 还书模板类
 * @Author: Daniel
 * @Date: 2020/4/24 3:30 下午
 */
@Service
public class BookReturnService extends BookServiceCore {
    /**
     * @Description: 还书核心逻辑
     * @Author: Daniel
     * @Date: 2020/4/24 4:21 下午
     * @params: [bookid 书籍号]
     * @return: com.webmaster.end.Entity.BorrowState 书籍信息实体类
     */
    public BorrowState returnBook(int bookid){
        return testBorrowState;
    }
}
