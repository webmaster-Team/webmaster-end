package com.webmaster.end.Service;

import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BookType;
import com.webmaster.end.Entity.ResultMap;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookSearchService extends BookServiceCore {
    /**
     * 搜索所有书籍
     * @return List<Book>类型
     */
    public Map<String,Object> searchAllBooks(){
        try {
            List<Book> books=iBookMapper.getAllBooks();
            if(books==null)
                return ResultMap.getResultMap(null,"搜索所有书籍失败");
            else
                return ResultMap.getResultMap(books,"搜索所有书籍成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }


    /**
     * 按照关键字搜索书籍
     * @param key 关键字
     * @return List<Book>类型
     */
    public Map<String,Object> searchBooksByKey(String key){
        try {
            List<Book> books=iBookMapper.getBooksByKey(key);
            if(books==null)
                return ResultMap.getResultMap(null,"关键字搜索书籍失败");
            else
                return ResultMap.getResultMap(books,"关键字搜索书籍成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }


    /**
     * 按照ISBN来获取书籍
     * @param iSBN isbn号
     * @return Book类型
     */
    public Map<String,Object> searchBookByISBN(String iSBN){
        try {
            Book book=iBookMapper.getBookByISBN(iSBN);
            if(book==null)
                return ResultMap.getResultMap(null,"ISBN搜索书籍失败");
            else
                return ResultMap.getResultMap(book,"ISBN搜索书籍成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 书籍按照时间排序
     * @param books 书籍列表
     * @param dateSort 是否正序
     * @return List<Book>类型
     */
    public Map<String,Object> bookSortByDate(List<Book> books,boolean dateSort){
        try {
            Collections.sort(books,new BookServiceCore().new BookDateCompartor(dateSort));
            if(books==null)
                return ResultMap.getResultMap(null,"时间排序失败");
            else
                return ResultMap.getResultMap(books,"时间排序成功");
        }catch (Exception e){
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }


    /**
     * 书籍按照名字排序
     * @param books 书籍列表
     * @param nameSort 是否正序
     * @return List<Book>类型
     */
    public Map<String,Object> bookSortByName(List<Book> books,boolean nameSort){
        try{
            Collections.sort(books,new BookServiceCore().new BookNameCompartor(nameSort));
            if(books==null)
                return ResultMap.getResultMap(null,"名字排序失败");
            else
                return ResultMap.getResultMap(books,"名字排序成功");
        }catch (Exception e){
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 筛选出时间范围内的的书籍
     * @param books 筛选书籍
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return List<Book>类型
     */
    public Map<String,Object> filterBooksByDate(List<Book> books,String startDate,String endDate){
        List<Book> result = new ArrayList<>();
        try {
            for (int i = 0; i < books.size(); i++) {
                boolean flag = true;
                Book book = books.get(i);
                if (!StringUtils.isEmpty(startDate)) {
                    if (!MyDateUtil.isFirstDatePrevious(startDate, book.getEntryTime()))
                        flag = false;
                }
                if (!StringUtils.isEmpty(endDate)) {
                    if (!MyDateUtil.isFirstDatePrevious(book.getEntryTime(), endDate))
                        flag = false;
                }
                if (flag)
                    result.add(book);
            }
            return ResultMap.getResultMap(result,"时间筛选成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 按照书籍状态过滤书籍
     * @param books 书籍
     * @param state 状态
     * @return List<Book>
     */
    public Map<String,Object> filterBooksByState(List<Book> books,boolean state){
        try {
            List<Book> result = null;
            result = books.stream()
                    .filter((Book book) -> (book.getState() > 0 ? true : false) == state)
                    .collect(Collectors.toList());
            return ResultMap.getResultMap(result,"状态筛选成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }


    /**
     * 分页搜索
     * @param books 书籍列表
     * @param perpage 每页的个数
     * @param pageIndex 页的索引
     * @return List<Book>
     */
    public Map<String,Object> filterBooksByPage(List<Book> books, int perpage, int pageIndex,boolean[] isLast) {
        try {
            int startIndex = (pageIndex - 1) * perpage;
            int endIndex = startIndex + perpage;
            //开始索引大于0，小于书籍总数
            if (startIndex >= 0 && startIndex < books.size()) {
                //结尾索引小于开始索引
                if (endIndex < startIndex)
                    return ResultMap.getResultMap(null,"结尾索引小于开始索引");
                    //结尾索引超过书籍总数
                else if (endIndex >= books.size()) {
                    isLast[0]=true;
                    endIndex = books.size();
                }
                return ResultMap.getResultMap(books.subList(startIndex, endIndex),"检索成功");
            }
            //开始索引不满足条件
            else
                return ResultMap.getResultMap(null,"开始索引就超过范围");
        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 按照类别筛选书籍
     * @param books 书籍列表
     * @param title 标题名
     * @return List<Book>类型
     */
    public Map<String,Object> filterBooksByTitle(List<Book> books, String title) {
        try {
            BookType bookType = iBookTypeMapper.getBookTypeByTitle(title);
            List<Book> books1=books.stream()
                            .filter((Book book) -> book.getTypeId().equals(bookType.getTypeid()))
                            .collect(Collectors.toList());
            if(books1==null)
                return ResultMap.getResultMap(null,"类别过滤失败");
            else
                return ResultMap.getResultMap(books1,"类别过滤成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 按照出版社筛选书籍
     * @param books 书籍列表
     * @param publisher 出版社
     * @return List<Book>类型
     */
    public Map<String,Object> filterBooksByPublisher(List<Book> books, String publisher) {
        try {
            List<Book> books1=books.stream()
                    .filter((Book book) -> book.getPublisher().equals(publisher))
                    .collect(Collectors.toList());
            if(books1==null)
                return ResultMap.getResultMap(null,"出版社过滤失败");
            else
                return ResultMap.getResultMap(books1,"出版社过滤成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }

    /**
     * 按照作者筛选书籍
     * @param books 书籍列表
     * @param author 作者
     * @return List<Book>类型
     */
    public Map<String,Object> filterBooksByAuthor(List<Book> books, String author) {
        try {
            List<Book> books1=books.stream()
                    .filter((Book book) -> book.getAuthor().equals(author))
                    .collect(Collectors.toList());
            if(books1==null)
                return ResultMap.getResultMap(null,"作者过滤失败");
            else
                return ResultMap.getResultMap(books1,"作者过滤成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }



    /**
     * 图书馆过滤书籍
     * @param books 书籍列表
     * @param library 图书馆
     * @return List<Book>
     */
    public Map<String,Object> filterBooksByLibrary(List<Book> books, String library) {
        try {
            List<Book> books1 = books.stream()
                                .filter((Book book) -> book.getLibrary().equals(library))
                                .collect(Collectors.toList());
            if(books1==null)
                return ResultMap.getResultMap(null,"图书馆过滤失败");
            else
                return ResultMap.getResultMap(books1,"图书馆过滤成功");

        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }


    /**
     * 层数过滤书籍
     * @param books 书籍列表
     * @param layer 层数
     * @return List<Book>
     */
    public Map<String,Object> filterBooksByLayer(List<Book> books, String layer) {
        try {
            List<Book> books1 = books.stream()
                    .filter((Book book) -> book.getLayer().equals(layer))
                    .collect(Collectors.toList());
            if(books1==null)
                return ResultMap.getResultMap(null,"层数过滤失败");
            else
                return ResultMap.getResultMap(books1,"层数过滤成功");

        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }


    /**
     * 区号过滤书籍
     * @param books 书籍列表
     * @param origin 区名
     * @return List<Book>
     */
    public Map<String,Object> filterBooksByOrigin(List<Book> books, String origin) {
        try {
            List<Book> books1 = books.stream()
                    .filter((Book book) -> book.getOrigin().equals(origin))
                    .collect(Collectors.toList());
            if(books1==null)
                return ResultMap.getResultMap(null,"区号过滤失败");
            else
                return ResultMap.getResultMap(books1,"区号过滤成功");

        }catch (Exception e){
            e.printStackTrace();
            return ResultMap.getResultMap(null,"系统内部错误");
        }
    }
}
