package com.webmaster.end.Service;

import com.webmaster.end.Dao.*;
import com.webmaster.end.Entity.*;
import com.webmaster.end.IMapper.*;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.*;


public class BookServiceCore {
    //错误返回最差预期

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected IBookMapper iBookMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected IUserMapper iUserMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected IRentalMapper iRentalMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected IBookTypeMapper iBookTypeMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected ILibraryMapper iLibraryMapper;


    /**
     * 返回书籍是否存在
     * @param id 书籍的id
     * @return boolean类型
     */
    public Map<String,Object> isExist(int id) {
        try {
            if(iBookMapper.isExist(id))
                return ResultMap.getResultMap(true,"书籍存在");
            else
                return ResultMap.getResultMap(false,"书籍不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(false,"系统内部错误");
        }
    }


    /**
     * 返回书籍对象
     * @param id 书籍的id
     * @return book
     */
    public Map<String,Object> getBook(int id) {
        try {
            if (iBookMapper.isExist(id)){
                Book book = iBookMapper.getBook(id);
                if(book==null)
                    return ResultMap.getResultMap(null,"获取书籍失败");
                else
                    return ResultMap.getResultMap(book,"获取书籍成功");
            }
            else
                return ResultMap.getResultMap(null,"书籍不存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 获得书籍被借阅的次数
     * @param id 书籍的id
     * @return int
     */
    public Map<String,Object> getBookBorrowedTimes(int id) {
        try {
            if (iBookMapper.isExist(id)){
                List<Rental> rentals = iRentalMapper.getRentalsByBookId(id);
                if(rentals==null)
                    return ResultMap.getResultMap(null,"获取书籍被借阅次数失败");
                else
                    return ResultMap.getResultMap(rentals.size(),"获取书籍被借阅次数成功");
            }
            else
                return ResultMap.getResultMap(null,"书籍不存在");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }


    /**
     * 获得所有的书籍类型
     * @return List<BookType>类型
     */
    public Map<String,Object> getBookTypes(){
        try {
            List<BookType> types = iBookTypeMapper.getAllBookTypes();
            if(types==null)
                return ResultMap.getResultMap(null,"获取所有书籍类型失败");
            else
                return ResultMap.getResultMap(types,"获取所有书籍类型成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 获得所有的图书馆类型
     * @return List<Library>类型
     */
    public Map<String,Object> getLibraries(){
        try {
            List<Library> libraries = iLibraryMapper.getLibraries();
            if(libraries==null)
                return ResultMap.getResultMap(null,"获取图书馆类型失败");
            else
                return ResultMap.getResultMap(libraries,"获取图书馆类型成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 获得所有的作者
     * @return List<String>类型
     */
    public Map<String,Object> getAuthors(){
        try {
            List<String> authors = iBookMapper.getAuthors();
            if(authors==null)
                return ResultMap.getResultMap(null,"获取所有作者失败");
            else
                return ResultMap.getResultMap(authors,"获取所有作者成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 获得所有的出版社
     * @return List<String>类型
     */
    public Map<String,Object> getPublishers(){
        try {
            List<String> publishers = iBookMapper.getPublishers();
            if(publishers==null)
                return ResultMap.getResultMap(null,"获取所有出版社失败");
            else
                return ResultMap.getResultMap(publishers,"获取所有出版社成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 获得前3的热门书籍的名称
     * @return List<String>
     */
    public Map<String,Object> getHotBooks(){
        try {
            List<Integer> hotBooksId = iRentalMapper.getHotBooksId();
            List<String> hotBooksName=new ArrayList<>();
            for (Integer integer : hotBooksId) {
                int bookId=integer.intValue();
                Book book = iBookMapper.getBook(bookId);
                hotBooksName.add(book.getName());
            }
            return ResultMap.getResultMap(hotBooksName,"查询成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 根据类型获得具体的名字
     * @param type 书记类型
     * @return String类型
     */
    public Map<String,Object> getTitleByType(String type){
        try {
            String s = iBookTypeMapper.getBookTypeByTypeid(type).getTitle();
            if(s==null)
                return ResultMap.getResultMap(null,"获取类别名称失败");
            else
                return ResultMap.getResultMap(s,"获取类别名称成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    //名字排序
    class BookNameCompartor implements Comparator<Book> {
        private boolean isUpSort=true;
        public BookNameCompartor(boolean isUpSort) {
            this.isUpSort=isUpSort;
        }

        @Override
        public int compare(Book o1, Book o2) {
            if(isUpSort)
                return o1.getName().compareTo(o2.getName());
            else
                return (-1)*(o1.getName().compareTo(o2.getName()));
        }
    }


    //日期排序
    class BookDateCompartor implements Comparator<Book> {
        private boolean isUpSort=true;
        public BookDateCompartor(boolean isUpSort) {
            this.isUpSort=isUpSort;
        }

        @Override
        public int compare(Book o1, Book o2) {
            if(isUpSort)
                return MyDateUtil.isFirstDatePrevious(o1.getEntryTime(),o2.getEntryTime())?-1:1;
            else
                return MyDateUtil.isFirstDatePrevious(o1.getEntryTime(),o2.getEntryTime())?1:-1;
        }
    }

}
