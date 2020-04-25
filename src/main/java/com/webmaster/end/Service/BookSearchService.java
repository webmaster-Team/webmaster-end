package com.webmaster.end.Service;

import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookSearchService extends BookServiceCore {
    public List<Book> searchBooks(String key){
        List<Book> books=null;
        return books;
    }
}
