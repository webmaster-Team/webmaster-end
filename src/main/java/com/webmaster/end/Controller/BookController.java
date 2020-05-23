package com.webmaster.end.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.webmaster.end.Entity.*;
import com.webmaster.end.Service.*;
import com.webmaster.end.Utils.LoginAccess;
import com.webmaster.end.Utils.MyDateUtil;
import com.webmaster.end.Utils.MyJsonConverter;
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
    //@LoginAccess
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
    @PostMapping("extendBorrow")
    public String extendBorrow(@RequestBody Map<String,Integer> map,  HttpSession session){
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
    @PostMapping("returnBook")
    public String returnBook(@RequestBody Map<String,Integer> map,  HttpSession session){
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


    /**
     * 借阅书籍
     * @return 返回搜索的信息
     */
    @CrossOrigin
    @PostMapping("getSearchTypes")
    public String getSearchTypes(){
        try {
            List<BookType> bookTypes = bookSearchService.getBookTypes();
            List<String> authors = bookSearchService.getAuthors();
            List<String> publishers = bookSearchService.getPublishers();
            List<Library> libraries = bookSearchService.getLibraries();
            //所有都查询成功
            if(bookTypes!=null&&authors!=null&&publishers!=null&&libraries!=null) {
                JSONObject jsonObject = new JSONObject(true);
                jsonObject.put("result", 1);
                JSONObject data = new JSONObject(true);
                //添加书籍类别
                JSONArray jsonBookTypes=new JSONArray();
                for (BookType type : bookTypes)
                    jsonBookTypes.add(type.getTitle());
                data.put("bookTypes",jsonBookTypes);

                //添加作者
                JSONArray jsonAuthors=new JSONArray();
                for (String author : authors)
                    jsonAuthors.add(author);
                data.put("authors",jsonAuthors);

                //添加出版社
                JSONArray jsonPublishers=new JSONArray();
                for (String publisher : publishers)
                    jsonPublishers.add(publisher);
                data.put("publishers",jsonPublishers);

                //添加图书馆信息
                JSONArray jsonLibraries = new JSONArray();
                for (Library library : libraries) {
                    JSONObject temp = new JSONObject(true);
                    temp.put("name",library.getLibrary());
                    //将对应的层数放入到Array中
                    String[] split = library.getLayer().split(",");
                    JSONArray array=new JSONArray();
                    for (String s : split)
                        array.add(Integer.parseInt(s));
                    temp.put("layers",array);
                    jsonLibraries.add(temp);
                }
                data.put("libraries",jsonLibraries);

                jsonObject.put("data",data);
                return jsonObject.toJSONString();
            }
            else
                return "{\"result\":0}";
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
    @PostMapping("searchBooks")
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
            JSONObject temp = MyJsonConverter.convertSimpleBookToJson(singleBook);
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
                //图书馆地址筛选
                if (map.get("library") != null) {
                    try {
                        String library = (String) map.get("library");
                        books=bookSearchService.filterBooksByLibrary(books,library);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "{\"result\":0}";
                    }
                }
                //图书馆地址筛选
                if (map.get("layer") != null) {
                    try {
                         String layer = (int) map.get("layer")+"";
                        books=bookSearchService.filterBooksByLayer(books,layer);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "{\"result\":0}";
                    }
                }
            }
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("result", 1);
            JSONArray array = new JSONArray();
            for (Book book : books)
                 array.add(MyJsonConverter.convertSimpleBookToJson(book));
            jsonObject.put("data", array);
            return jsonObject.toJSONString();
        }
        //多书籍查询失败
        else
            return "{\"result\":0}";
    }


    /**
     * 借阅书籍
     * @return 返回搜索的信息
     */
    @CrossOrigin
    @PostMapping("searchBookData")
    public String searchBookData(@RequestBody Map<String,Integer> map){
        try {
            int bookId = map.get("bookId");
            Book book = bookSearchService.searchBookByBookId(bookId);
            JSONObject object = new JSONObject(true);
            object.put("result",1);
            JSONObject data = MyJsonConverter.convertComplexBookToJson(book);
            data.put("title",bookSearchService.getTitleByType(book.getTypeId()));
            object.put("data",data);
            return object.toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return "{\"result\":0}";
        }
    }
}