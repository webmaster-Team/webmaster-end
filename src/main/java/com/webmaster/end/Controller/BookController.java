package com.webmaster.end.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Entity.Book;
import com.webmaster.end.Entity.BookType;
import com.webmaster.end.Entity.BorrowState;
import com.webmaster.end.Entity.Rental;
import com.webmaster.end.Service.*;
import com.webmaster.end.Utils.LoginAccess;
import com.webmaster.end.Utils.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
     * @param map 用户id和书籍id的map
     * @return 对应的JSON串
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("borrow")
    public String borrow(@RequestBody Map<String,Integer> map, HttpSession session){
        int userid=map.get("userid");
        int bookid=map.get("bookid");
        if (bookBorrowService.bookIsExist(bookid)) {
            BorrowState state = bookBorrowService.bookBorrow(bookid, userid);
            if (state.getState() == 1) {
                JSONObject jsonObject = new JSONObject(true);
                jsonObject.put("result", 1);

                JSONObject data = new JSONObject(true);
                data.put("bookid", bookid + "");
                data.put("uid", userid + "");
                Rental rental = state.getRental();
                data.put("username", userService.getUserById(userid).getName());
                data.put("bookname", bookBorrowService.getBookByid(bookid).getName());
                data.put("borrowtime", rental.getBorrowTime());
                data.put("duration", rental.getDuration());
                data.put("isReborrow", (rental.getIsReborrow() == 1 ? true : false));

                jsonObject.put("data", data);
                return jsonObject.toJSONString();
            }
        }
        return "{\"result\":0}";
    }

    /**
     * 续借书籍
     * @param map 用户id和书籍id的map
     * @return 返回对应的信息
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("extendborrow")
    public String extendborrow(@RequestBody Map<String,Integer> map,  HttpSession session){
        int userid=map.get("userid");
        int bookid=map.get("bookid");
        if(!StringUtils.isEmpty((String)(session.getAttribute(userid+"")))) {
            if (bookExtendborrowService.rentalIsExist(bookid)) {
                BorrowState state = bookExtendborrowService.extendBorrow(userid, bookid);
                if (state.getState() == 1) {
                    JSONObject jsonObject = new JSONObject(true);
                    jsonObject.put("result", 1);

                    JSONObject data = new JSONObject(true);
                    data.put("bookid", bookid + "");
                    data.put("uid", userid + "");
                    Rental rental = state.getRental();
                    data.put("username", userService.getUserById(userid).getName());
                    data.put("bookname", bookExtendborrowService.getBookByid(bookid).getName());
                    data.put("borrowtime", rental.getBorrowTime());
                    data.put("duration", rental.getDuration());
                    data.put("isReborrow", (rental.getIsReborrow() == 1 ? true : false));

                    jsonObject.put("data", data);
                    return jsonObject.toJSONString();
                }
            }
        }
        return "{\"result\":0}";
    }

    /**
     * 归还书籍
     * @param map 用户id和书籍的id
     * @return 返回相关信息
     */
    @CrossOrigin
    @LoginAccess
    @PostMapping("returnbook")
    public String returnbook(@RequestBody Map<String,Integer> map,  HttpSession session){
        int userid=map.get("userid");
        int bookid=map.get("bookid");
        if(!StringUtils.isEmpty((String)(session.getAttribute(userid+"")))) {
            if (bookReturnService.rentalIsExist(bookid)) {
                BorrowState state = bookReturnService.returnBook(bookid);
                if (state.getState() == 1) {
                    JSONObject jsonObject = new JSONObject(true);
                    jsonObject.put("result", 1);

                    JSONObject data = new JSONObject(true);
                    Rental rental = state.getRental();
                    data.put("bookid", rental.getBookId() + "");
                    data.put("uid", rental.getUserId() + "");
                    data.put("username", userService.getUserById(rental.getUserId()).getName());
                    data.put("bookname", bookReturnService.getBookByid(rental.getBookId()).getName());
                    data.put("borrowtime", rental.getBorrowTime());
                    data.put("returntime", rental.getReturnTime());

                    jsonObject.put("data", data);
                    return jsonObject.toJSONString();
                }
            }
        }
        return "{\"result\":0}";
    }

    @CrossOrigin
    @PostMapping("getBookTypes")
    public String getBookTypes(){
        try {
            List<BookType> bookTypes = bookSearchService.getBookTypes();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", 1);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < bookTypes.size(); i++)
                jsonArray.add(bookTypes.get(i).getTitle());
            jsonObject.put("data",jsonArray);
            return jsonObject.toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return "{\"result\":0}";
        }
    }
    /**
     * 根据对应的key返回所有相关的书籍
     * @param map 关键字的map
     * @return 返回所有书籍
     */
    @CrossOrigin
    @LoginAccess(value = false)
    @PostMapping("searchbooks")
    public String searchBooks(@RequestBody(required = false) Map<String,Object> map){
        int bookId=-1;
        Book singleBook=null;
        List<Book> books=null;
        //使用全体书籍作为一次结果的情况
        if(map==null||((map.get("ISBN")==null) && (map.get("key")==null))){
            books=bookSearchService.searchBooks();
        }
        //使用ISBN
        else if (map.get("ISBN")!=null){
            String iSBN=(String)map.get("iSBN");
            singleBook = bookSearchService.searchBookByISBN(iSBN);
        }
        //使用bookid
        else if(map.get("key") instanceof Integer) {
            bookId = (int) map.get("key");
            singleBook = bookSearchService.searchBookByBookId(bookId);
        }
        //使用关键字
        else{
            String key=(String)map.get("key");
            books = bookSearchService.searchBooks(key);
        }
        //如果单一的书籍查询返回书籍
        //单一书籍查询失败
        if (singleBook == null&&books==null)
            return "{\"result\":0}";
        //单一书籍查询成功
        else if(singleBook!=null&&books==null){
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("result", 1);
            JSONArray array = new JSONArray();
            JSONObject temp = (JSONObject) JSONObject.toJSON(singleBook);
            array.add(temp);
            jsonObject.put("data", array);
            return jsonObject.toJSONString();
        }
        //多书籍查询成功
        if(books!=null){
            //判断前后时间
            if(map!=null) {
                String startDate = (String) map.get("startDate");
                String endDate = (String) map.get("endDate");
                if (!(startDate == null && endDate == null)) {
                    try {
                        books = bookSearchService.filterBooksByDate(books, startDate, endDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "{\"result\":0}";
                    }
                }
                //需要进行时间排序
                if (map.get("dateSort") != null) {
                    try {
                        boolean dateSort = (int) map.get("dateSort") == 1 ? true : false;
                        books = bookSearchService.bookSortByDate(books, dateSort);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "{\"result\":0}";
                    }
                }
                //需要进行名字排序
                if (map.get("nameSort") != null) {
                    try {
                        boolean nameSort = (int) map.get("nameSort") == 1 ? true : false;
                        books = bookSearchService.bookSortByName(books, nameSort);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "{\"result\":0}";
                    }
                }
                //需要日期筛选
                if (map.get("state") != null) {
                    try {
                        int state = (int) map.get("state");
                        books = bookSearchService.filterBooksByState(books, state);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "{\"result\":0}";
                    }
                }
                //进行页数筛选
                if (map.get("perpage") != null&&map.get("pageIndex") != null) {
                    int perpage=(int)map.get("perpage");
                    int pageIndex=(int)map.get("pageIndex");
                    books = bookSearchService.filterBooksByPage(books, perpage, pageIndex);
                    if (books==null)
                        return "{\"result\":0}";
                }
                //需要类别筛选
                if (map.get("type") != null) {
                    try {
                        String title = (String) map.get("type");
                        books=bookSearchService.filterBooksByTitle(books,title);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "{\"result\":0}";
                    }
                }
            }
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("result", 1);
            JSONArray array = new JSONArray();
            for (int i = 0; i < books.size(); i++) {
                Book book=books.get(i);
                book.setTypeId(bookSearchService.getTitleByType(book.getTypeId()));
                String temp = JSONObject.toJSONString(book);
                array.add(temp);
            }
            jsonObject.put("data", array);
            return jsonObject.toJSONString();
        }
        //多书籍查询失败
        else
            return "{\"result\":0}";
    }
}