package com.webmaster.end.Service;

import com.webmaster.end.Entity.*;

import java.util.HashMap;
import java.util.Map;

import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.stereotype.Service;

/**
 *
 * @Description: 续借类
 * @Author: Daniel
 * @Date: 2020/4/24 3:23 下午
 */
@Service
public class BookExtendborrowService extends BookServiceCore {

    /**
     * 续借
     * @param userId 用户id
     * @param bookId 书籍ID
     * @return BorrowInfo
     */
    public Map<String, Object> extendBorrow(int userId, int bookId) {
        try {
            //1.用户是否存在
            if (iUserMapper.isExist(userId)) {
                if (iBookMapper.isExist(bookId)) {
                    User user = iUserMapper.getUser(userId);
                    Book book = iBookMapper.getBook(bookId);
                    if(iRentalMapper.isExistByUserBook(bookId,userId)) {
                        Rental rental = iRentalMapper.getRentalByUserBook(bookId,userId);
                        if (rental!=null) {
                            if (iRentalMapper.updateReborrow(rental.getId())) {
                                BorrowInfo info = new BorrowInfo();
                                info.setBookId(bookId);
                                info.setUserId(userId);
                                info.setUsername(user.getName());
                                info.setBookname(book.getName());
                                info.setBorrowtime(rental.getBorrowTime());
                                info.setDuration(rental.getDuration());
                                info.setIsReborrow(rental.getIsReborrow());
                                return ResultMap.getResultMap(info, "续借成功");
                            } else
                                return ResultMap.getResultMap(null, "更新续借字段失败");
                        }else
                            return ResultMap.getResultMap(null, "流水获取失败");
                    }
                    else
                        return ResultMap.getResultMap(null, "该用户未借阅过此书");
                } else
                    return ResultMap.getResultMap(null, "书籍不存在");
            } else
                return ResultMap.getResultMap(null, "用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null, "系统内部错误");
        }
    }


}
