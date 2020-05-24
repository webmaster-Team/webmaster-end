package com.webmaster.end.Service;

import com.webmaster.end.Entity.*;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookBorrowService extends BookServiceCore {

    /**
     * 借书
     *
     * @param bookId 书籍id
     * @param userId 用户id
     * @return BorrowInfo
     */
    @Transactional
    public Map<String, Object> borrow(int userId, int bookId) {
        try {
            //1.用户是否存在
            if (userDao.isExist(userId)) {
                if (bookDao.isExist(bookId)) {
                    User user = userDao.getUser(userId);
                    Book book = bookDao.getBook(bookId);
                    if(!rentalDao.isExist(bookId,userId)) {
                        if(bookDao.getState(bookId)>0) {
                            if (bookDao.declineState(bookId)) {
                                Rental rental = new Rental();
                                rental.setBookId(bookId);
                                rental.setUserId(userId);
                                rental.setBorrowTime(MyDateUtil.getCurrentString());
                                rental.setDuration(user.getIdentity() == 0 ? 30 : 180);
                                rental.setIsReborrow(0);
                                if (rentalDao.addRental(rental)) {
                                    BorrowInfo info = new BorrowInfo();
                                    info.setBookId(bookId);
                                    info.setUserId(userId);
                                    info.setUsername(user.getName());
                                    info.setBookname(book.getName());
                                    info.setBorrowtime(rental.getBorrowTime());
                                    info.setDuration(rental.getDuration());
                                    info.setIsReborrow(rental.getIsReborrow());
                                    return ResultMap.getResultMap(info, "借书成功");
                                } else
                                    return ResultMap.getResultMap(null, "流水增加失败");
                            } else
                                return ResultMap.getResultMap(null, "修改数量失败");
                        }
                        else
                            return ResultMap.getResultMap(null, "此书剩余数量为0，无法借阅");
                    }
                    else
                        return ResultMap.getResultMap(null, "该用户已借阅过此书");
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
