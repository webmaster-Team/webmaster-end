package com.webmaster.end.Service;

import com.webmaster.end.Entity.*;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookReturnService extends BookServiceCore {

    /**
     * 还书
     * @param bookId 书籍id
     * @param userId 用户id
     * @return BorrowInfo
     */
    public Map<String,Object> returnBook(int userId,int bookId){
        try {
            //1.用户是否存在
            if (iUserMapper.isExist(userId)) {
                if (iBookMapper.isExist(bookId)) {
                    User user = iUserMapper.getUser(userId);
                    Book book = iBookMapper.getBook(bookId);
                    if(iRentalMapper.isExistByUserBook(bookId,userId)) {
                        Rental rental = iRentalMapper.getRentalByUserBook(bookId, userId);
                        if (rental!=null){
                            if (iBookMapper.increaseState(bookId)) {
                                rental.setReturnTime(MyDateUtil.getCurrentString());
                                if (iRentalMapper.updateReturnTime(rental.getId(),rental.getReturnTime())) {
                                    BorrowInfo info = new BorrowInfo();
                                    info.setBookId(bookId);
                                    info.setUserId(userId);
                                    info.setUsername(user.getName());
                                    info.setBookname(book.getName());
                                    info.setBorrowtime(rental.getBorrowTime());
                                    info.setReturntime(rental.getReturnTime());
                                    info.setDuration(rental.getDuration());
                                    info.setIsReborrow(rental.getIsReborrow());
                                    return ResultMap.getResultMap(info, "还书成功");
                                } else
                                    return ResultMap.getResultMap(null, "更新还书时间失败");
                            } else
                                return ResultMap.getResultMap(null, "修改数量失败");
                        }
                        else
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
