package com.webmaster.end.IMapper;

import com.webmaster.end.Entity.Rental;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

public interface IRentalMapper {
    /**
     * 查询该条查询记录是否存在
     * @param id 记录的id
     * @return 返回是否存在
     */
    public boolean isExist(int id);

    /**
     * 查询该条流水是否存在
     * @param bookId 书籍id
     * @param userId 用户id
     * @return 返回是否存在
     */
    public boolean isExistByUserBook(@Param("bookId") int bookId,@Param("userId") int userId);

    /**
     * 根据流水id判断书籍是否续借
     * @param id 流水id
     * @return
     */
    public boolean isReborrow(int id) ;

    /**
     * 新增一个借书记录
     * @param rental 借阅数据
     * @return 返回是否成功
     */
    public boolean addRental(Rental rental);

    /**
     * 新增一些借书记录
     * @param rentals 表示需要添加的的所有Rental
     * @return 返回是否成功
     */
    public boolean addAllRentals( List<Rental> rentals);

    /**
     * 归还书籍，记录对应的归还时间
     * @param id 流水id
     * @param date 归还的时间
     * @return
     */
    public boolean updateReturnTime(@Param("id") int id,@Param("date") String date);

    /**
     * 更改对应的续借状态
     * @param id 流水id
     * @return
     */
    public boolean updateReborrow(int id);


    /**
     * 根据流水id查询对应的流水
     * @param id 流水id
     * @return 正确返回对象，否则为Null
     */
    public Rental getRental(int id);


    /**
     * 根据书籍ID返回流水号
     * @param bookId 书籍ID
     * @param userId 用户ID
     * @return 返回流水号,-1则为错误
     */
    public int getRentalId(@Param("bookId") int bookId,@Param("userId") int userId);


    /**
     * 根据书籍id查询对应的流水
     * @param bookId 书籍id
     * @param userId 用户Id
     * @return 返回Rental对象，失败返回Null
     */
    public Rental getRentalByUserBook(int bookId,int userId);

    /**
     * 根据书籍id查询所有的流水
     * @param bookId 书籍id
     * @return 返回Rental列表，失败返回Null
     */
    public List<Rental> getRentalsByBookId(int bookId);

    /**
     * 获得用户所有的所借的流水
     * @param userId 用户id
     * @return 用户正在借阅的书籍
     */
    public List<Rental> getIsRentalsingByUserId(int userId);

    /**
     * 获得用户所有的已归还的流水
     * @param userId 用户id
     * @return 用户已经归还的书籍
     */
    public List<Rental> getHasRentalsedByUserId(int userId);

    /**
     * 根据用户id总共的借阅数量
     * @param userId 用户的id
     * @return 返回用户的总共借阅数量
     */
    public int getHasBorrowedBooksByUserId(int userId);
    /**
     * 根据用户id正在借阅的数量
     * @param userId 用户的id
     * @return 返回用户的正在借阅数量
     */
    public int getIsBorrowingBooksByUserId(int userId);

    /**
     * 返回借阅较多的书籍
     * @return 热门书籍的id
     * @throws SQLException
     */
    public List<Integer> getHotBooksId();
}
