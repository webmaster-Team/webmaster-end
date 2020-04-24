package com.webmaster.end.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/book/")
public class BookController {
    @Autowired
    private BookBorrowService bookBorrowService;

    @Autowired
    private BookExtendborrowService bookExtendborrowService;

    @Autowired
    private BookReturnService bookReturnService;

    @Autowired
    private BookSearchService bookSearchService;

    @Autowired
    private UserService userService;

    /**
     * 借书
     * @param userid 用户的id
     * @param bookid 书籍的id
     * @return 对应的JSON串
     */
    @PostMapping("borrow")
    public String borrow(int userid,int bookid){
        if(bookBorrowService.bookIsExist(bookid)){
            BorrowState state = bookBorrowService.bookBorrow(bookid, userid);
            if(state.getState()==1){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result",1);

                JSONObject data=new JSONObject();
                data.put("bookid",bookid+"");
                data.put("uid",userid+"");
                Rental rental = state.getRental();
                data.put("username",userService.getUserById(userid).getName());
                data.put("bookname",bookBorrowService.getBookByid(bookid).getName());
                data.put("borrowtime",rental.getBorrowTime());
                data.put("duration",rental.getDuration());
                data.put("isReborrow",(rental.getIsReborrow()==1?true:false));

                jsonObject.put("data",data);
                return jsonObject.toJSONString();
            }
        }
        return "{\"result\":0}";
    }

    /**
     * 续借书籍
     * @param userid 用户的id
     * @param bookid 书籍id
     * @return 返回对应的信息
     */
    @PostMapping("extendborrow")
    public String extendborrow(int userid,int bookid){
        if(bookExtendborrowService.rentalIsExist(bookid)){
            BorrowState state = bookExtendborrowService.extendBorrow(bookid, userid);
            if(state.getState()==1){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result",1);

                JSONObject data=new JSONObject();
                data.put("bookid",bookid+"");
                data.put("uid",userid+"");
                Rental rental = state.getRental();
                data.put("username",userService.getUserById(userid).getName());
                data.put("bookname",bookExtendborrowService.getBookByid(bookid).getName());
                data.put("borrowtime",rental.getBorrowTime());
                data.put("duration",rental.getDuration());
                data.put("isReborrow",(rental.getIsReborrow()==1?true:false));

                jsonObject.put("data",data);
                return jsonObject.toJSONString();
            }
        }
        return "{\"result\":0}";
    }

    /**
     * 归还书籍
     * @param bookid 书籍的id
     * @return 返回相关信息
     */
    @PostMapping("returnbook")
    public String returnbook(int bookid){
        if(bookReturnService.rentalIsExist(bookid)){
            BorrowState state = bookReturnService.returnBook(bookid);
            if(state.getState()==1){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result",1);

                JSONObject data=new JSONObject();
                Rental rental = state.getRental();
                data.put("bookid",rental.getBookId()+"");
                data.put("uid",rental.getUserId()+"");
                data.put("username",userService.getUserById(rental.getUserId()).getName());
                data.put("bookname",bookReturnService.getBookByid(rental.getBookId()).getName());
                data.put("borrowtime",rental.getBorrowTime());
                data.put("returntime",rental.getReturnTime());

                jsonObject.put("data",data);
                return jsonObject.toJSONString();
            }
        }
        return "{\"result\":0}";
    }

    /**
     * 根据对应的key返回所有相关的书籍
     * @param key 关键字
     * @return 返回所有书籍
     */
    @PostMapping("searchBooks")
    public String searchBooks(String key){
        List<Book> books = bookSearchService.searchBooks(key);
        if(books.size()==0)
            return "{\"result\":0}";
        else{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("result",1);
            JSONArray array = new JSONArray();
            for (Book book : books) {
                JSONObject temp = (JSONObject) JSON.toJSON(book);
                array.add(temp);
            }
            jsonObject.put("data",array);
            return jsonObject.toJSONString();
        }
    }
}
