package com.webmaster.end.Service;

import com.webmaster.end.Dao.BookDao;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookSearchService extends BookServiceCore {
    @Autowired
    private BookDao bookDao;

    public List<Book> searchBooks(String key){
        List<Book> books=null;
        books=bookDao.getBooksByName(key);
        return books;
    }
}
