package com.webmaster.end.Service;

import com.webmaster.end.Dao.BookDao;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookSearchService extends BookServiceCore {
    @Autowired
    private BookDao bookDao;

    public List<Book> searchBooks(){
        List<Book> books=null;
        books=bookDao.getBooks();
        return books;
    }

    public List<Book> searchBooks(String key){
        List<Book> books=null;
        books=bookDao.getBooksByKey(key);
        return books;
    }

    public Book searchBookByBookId(int bookId){
        Book books=null;
        books=bookDao.getBookByBookId(bookId);
        return books;
    }

    public Book searchBookByISBN(String iSBN){
        Book books=null;
        books=bookDao.getBookByISBN(iSBN);
        return books;
    }

    public List<Book> bookSortByDate(List<Book> books,boolean dateSort){
        Collections.sort(books,new BookServiceCore().new BookDateCompartor(dateSort));
        return books;
    }

    public List<Book> bookSortByName(List<Book> books,boolean nameSort){
        Collections.sort(books,new BookServiceCore().new BookNameCompartor(nameSort));
        return books;
    }

    /**
     * 筛选出时间范围内的的书籍
     * @param books 筛选书籍
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 筛选之后的书籍
     */
    public List<Book> filterBooksByDate(List<Book> books,String startDate,String endDate){
        List<Book> result=new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            boolean flag=true;
            Book book=books.get(i);
            if (!StringUtils.isEmpty(startDate)){
                if (!MyDateUtil.isFirstDatePrevious(startDate,book.getEntryTime()))
                    flag=false;
            }
            if(!StringUtils.isEmpty(endDate)){
                if(!MyDateUtil.isFirstDatePrevious(book.getEntryTime(),endDate))
                    flag=false;
            }
            if (flag)
                result.add(book);
        }
        return result;
    }

    public List<Book> filterBooksByState(List<Book> books,int state){
        List<Book> result=null;
        result=books.stream()
                    .filter((Book book)->book.getState()==state)
                    .collect(Collectors.toList());
        return result;
    }
}
