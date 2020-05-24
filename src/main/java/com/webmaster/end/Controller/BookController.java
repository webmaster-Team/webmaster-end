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

import javax.print.DocFlavor;
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
        try {
            Integer userId = map.get("userId");
            Integer bookId = map.get("bookId");
            if (userId != null) {
                if (bookId != null) {
                    Map<String, Object> info = bookBorrowService.borrow(userId, bookId);
                    BorrowInfo borrowInfo = (BorrowInfo) info.get("state");
                    if (borrowInfo != null) {
                        JSONObject result = new JSONObject(true);
                        result.put("result", 1);
                        JSONObject data = new JSONObject(true);
                        data.put("bookid", borrowInfo.getBookId() + "");
                        data.put("uid", borrowInfo.getUserId() + "");
                        data.put("username", borrowInfo.getUsername());
                        data.put("bookname", borrowInfo.getBookname());
                        data.put("borrowtime", borrowInfo.getBorrowtime());
                        data.put("duration", borrowInfo.getDuration());
                        data.put("isReborrow", borrowInfo.getIsReborrow() == 1 ? true : false);
                        result.put("data", data);
                        return result.toJSONString();
                    } else
                        return MyJsonConverter.convertErrorToJson(info).toJSONString();
                } else
                    return MyJsonConverter.createErrorToJson("书籍id不能为空").toJSONString();
            } else
                return MyJsonConverter.createErrorToJson("用户id不能为空").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
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
        try {
            Integer userId = map.get("userId");
            Integer bookId = map.get("bookId");
            Integer trueUserId = (Integer) (session.getAttribute(userId + ""));
            if (userId != null) {
                if (bookId != null) {
                    if(trueUserId!=null) {
                        if (trueUserId.intValue() == userId.intValue()) {
                            Map<String, Object> info = bookExtendborrowService.extendBorrow(userId, bookId);
                            BorrowInfo borrowInfo = (BorrowInfo) info.get("state");
                            if (borrowInfo != null) {
                                JSONObject result = new JSONObject(true);
                                result.put("result", 1);
                                JSONObject data = new JSONObject(true);
                                data.put("bookid", borrowInfo.getBookId() + "");
                                data.put("uid", borrowInfo.getUserId() + "");
                                data.put("username", borrowInfo.getUsername());
                                data.put("bookname", borrowInfo.getBookname());
                                data.put("borrowtime", borrowInfo.getBorrowtime());
                                data.put("duration", borrowInfo.getDuration());
                                data.put("isReborrow", borrowInfo.getIsReborrow() == 1 ? true : false);
                                result.put("data", data);
                                return result.toJSONString();
                            } else
                                return MyJsonConverter.convertErrorToJson(info).toJSONString();
                        } else
                            return MyJsonConverter.createErrorToJson("续借用户信息与登录用户信息不符").toJSONString();
                    }
                    else
                        return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
                } else
                    return MyJsonConverter.createErrorToJson("书籍id不能为空").toJSONString();
            } else
                return MyJsonConverter.createErrorToJson("用户id不能为空").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
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
        try {
            Integer userId = map.get("userId");
            Integer bookId = map.get("bookId");
            Integer trueUserId = (Integer) (session.getAttribute(userId + ""));
            if (userId != null) {
                if (bookId != null) {
                    if(trueUserId!=null) {
                        if(trueUserId.intValue()==userId.intValue()) {
                            Map<String, Object> info = bookReturnService.returnBook(userId, bookId);
                            BorrowInfo borrowInfo = (BorrowInfo) info.get("state");
                            if (borrowInfo != null) {
                                JSONObject result = new JSONObject(true);
                                result.put("result", 1);
                                JSONObject data = new JSONObject(true);
                                data.put("bookid", borrowInfo.getBookId() + "");
                                data.put("uid", borrowInfo.getUserId() + "");
                                data.put("username", borrowInfo.getUsername());
                                data.put("bookname", borrowInfo.getBookname());
                                data.put("borrowtime", borrowInfo.getBorrowtime());
                                data.put("returntime",borrowInfo.getReturntime());
                                result.put("data", data);
                                return result.toJSONString();
                            } else
                                return MyJsonConverter.convertErrorToJson(info).toJSONString();
                        }else
                            return MyJsonConverter.createErrorToJson("还书用户信息与登录用户信息不符").toJSONString();
                    }
                    else
                        return MyJsonConverter.createErrorToJson("用户未登录").toJSONString();
                } else
                    return MyJsonConverter.createErrorToJson("书籍id不能为空").toJSONString();
            } else
                return MyJsonConverter.createErrorToJson("用户id不能为空").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }


    /**
     * 搜索条件
     * @return 返回搜索的信息
     */
    @CrossOrigin
    @PostMapping("getSearchTypes")
    public String getSearchTypes(){
        try {
            Map<String, Object> typesData = bookSearchService.getBookTypes();
            List<BookType> types= (List<BookType>) typesData.get("state");

            Map<String, Object> authorsData = bookSearchService.getAuthors();
            List<String> authors= (List<String>) authorsData.get("state");

            Map<String, Object> publishersData = bookSearchService.getPublishers();
            List<String> publishers= (List<String>) publishersData.get("state");

            Map<String, Object> librariesData = bookSearchService.getLibraries();
            List<Library> libraries= (List<Library>) librariesData.get("state");

            if(types!=null){
                if(authors!=null){
                    if(publishers!=null){
                        if(libraries!=null){
                            JSONObject jsonObject = new JSONObject(true);
                            jsonObject.put("result", 1);
                            JSONObject data = new JSONObject(true);
                            //添加书籍类别
                            JSONArray jsonBookTypes=new JSONArray();
                            for (BookType type : types)
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
                        return MyJsonConverter.convertErrorToJson(librariesData).toJSONString();
                    }
                    else
                        return MyJsonConverter.convertErrorToJson(publishersData).toJSONString();
                }
                else
                    return MyJsonConverter.convertErrorToJson(authorsData).toJSONString();
            }
            else
                return MyJsonConverter.convertErrorToJson(typesData).toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
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
        try {
            Integer bookId = null;
            Book singleBook = null;
            List<Book> books = null;
            //使用全体书籍作为一次结果的情况
            if (map == null || ((map.get("ISBN") == null) && (map.get("key") == null))) {
                Map<String, Object> booksData = bookSearchService.searchAllBooks();
                books = (List<Book>) booksData.get("state");
                if (books == null)
                    return MyJsonConverter.convertErrorToJson(booksData).toJSONString();
            }
            //使用ISBN
            else if (map.get("ISBN") != null) {
                String iSBN = (String) map.get("iSBN");
                Map<String, Object> bookData = bookSearchService.searchBookByISBN(iSBN);
                singleBook = (Book) bookData.get("state");
                if (singleBook == null)
                    return MyJsonConverter.convertErrorToJson(bookData).toJSONString();
            }
            //使用bookid
            else if (map.get("key") instanceof Integer) {
                bookId = (int) map.get("key");
                Map<String, Object> bookData = bookSearchService.getBook(bookId);
                singleBook = (Book) bookData.get("state");
                if (singleBook == null)
                    return MyJsonConverter.convertErrorToJson(bookData).toJSONString();
            }
            //使用关键字
            else {
                String key = (String) map.get("key");
                Map<String, Object> booksData = bookSearchService.searchBooksByKey(key);
                books = (List<Book>) booksData.get("state");
                if (books == null)
                    return MyJsonConverter.convertErrorToJson(booksData).toJSONString();
            }

            //如果单一的书籍查询返回书籍
            //单一书籍查询失败
            if (singleBook == null && books == null)
                return MyJsonConverter.createErrorToJson("书籍查询失败").toJSONString();
                //单一书籍查询成功
            else if (singleBook != null && books == null) {
                JSONObject jsonObject = new JSONObject(true);
                jsonObject.put("result", 1);
                JSONArray array = new JSONArray();
                JSONObject temp = MyJsonConverter.convertSimpleBookToJson(singleBook);
                array.add(temp);
                jsonObject.put("data", array);
                return jsonObject.toJSONString();
            }

            //多书籍查询成功
            if (books != null) {
                //判断前后时间
                if (map != null) {
                    String startDate = (String) map.get("startDate");
                    String endDate = (String) map.get("endDate");
                    if (!(startDate == null && endDate == null)) {
                        Map<String, Object> searchData = bookSearchService.filterBooksByDate(books, startDate, endDate);
                        books = (List<Book>) searchData.get("state");
                        if (books == null)
                            return MyJsonConverter.convertErrorToJson(searchData).toJSONString();
                    }
                    //需要进行时间排序
                    if (map.get("dateSort") != null) {
                        Integer dateSortInt = (Integer) map.get("dateSort");
                        if (dateSortInt == null || (dateSortInt.intValue() != 1) && (dateSortInt.intValue() != 0))
                            return MyJsonConverter.createErrorToJson("时间排序规则不符合要求").toJSONString();
                        else {
                            boolean dateSort = dateSortInt == 1 ? true : false;
                            Map<String, Object> searchData = bookSearchService.bookSortByDate(books, dateSort);
                            books = (List<Book>) searchData.get("state");
                            if (books == null)
                                return MyJsonConverter.convertErrorToJson(searchData).toJSONString();
                        }
                    }
                    //需要进行名字排序
                    if (map.get("nameSort") != null) {
                        Integer nameSortInt = (Integer) map.get("nameSort");
                        if (nameSortInt == null || (nameSortInt.intValue() != 1) && (nameSortInt.intValue() != 0))
                            return MyJsonConverter.createErrorToJson("名字排序规则不符合要求").toJSONString();
                        else {
                            boolean nameSort = nameSortInt == 1 ? true : false;
                            Map<String, Object> searchData = bookSearchService.bookSortByName(books, nameSort);
                            books = (List<Book>) searchData.get("state");
                            if (books == null)
                                return MyJsonConverter.convertErrorToJson(searchData).toJSONString();
                        }
                    }
                    //需要借阅状态查询
                    if (map.get("state") != null) {
                        Integer stateInt = (Integer) map.get("state");
                        if (stateInt == null || (stateInt.intValue() != 1) && (stateInt.intValue() != 0))
                            return MyJsonConverter.createErrorToJson("状态规则不符合要求").toJSONString();
                        else {
                            boolean state = stateInt == 1 ? true : false;
                            Map<String, Object> searchData = bookSearchService.filterBooksByState(books, state);
                            books = (List<Book>) searchData.get("state");
                            if (books == null)
                                return MyJsonConverter.convertErrorToJson(searchData).toJSONString();
                        }
                    }

                    //进行页数筛选
                    if (map.get("perpage") != null && map.get("pageIndex") != null) {
                        Integer perpageInt = (Integer) map.get("perpage");
                        Integer pageIndexInt = (Integer) map.get("pageIndex");
                        if (perpageInt == null || perpageInt.intValue() <= 0)
                            return MyJsonConverter.createErrorToJson("每页页数不符合要求").toJSONString();
                        else if (pageIndexInt == null || pageIndexInt.intValue() <= 0)
                            return MyJsonConverter.createErrorToJson("页索引不符合要求").toJSONString();
                        else {
                            Map<String, Object> searchData = bookSearchService.filterBooksByPage(books, perpageInt.intValue(), pageIndexInt.intValue());
                            books = (List<Book>) searchData.get("state");
                            if (books == null)
                                return MyJsonConverter.convertErrorToJson(searchData).toJSONString();
                        }
                    }
                    //需要类别筛选
                    if (map.get("type") != null) {
                        String title = (String) map.get("type");
                        if (title == null)
                            return MyJsonConverter.createErrorToJson("类别规则不符合要求").toJSONString();
                        else {
                            Map<String, Object> searchData = bookSearchService.filterBooksByTitle(books, title);
                            books = (List<Book>) searchData.get("state");
                            if (books == null)
                                return MyJsonConverter.convertErrorToJson(searchData).toJSONString();
                        }
                    }
                    //图书馆地址筛选
                    if (map.get("library") != null) {
                        String library = (String) map.get("library");
                        if (library == null)
                            return MyJsonConverter.createErrorToJson("图书馆地址不符合要求").toJSONString();
                        else {
                            Map<String, Object> searchData = bookSearchService.filterBooksByLibrary(books, library);
                            books = (List<Book>) searchData.get("state");
                            if (books == null)
                                return MyJsonConverter.convertErrorToJson(searchData).toJSONString();
                        }
                    }

                    //层数筛选
                    if (map.get("layer") != null) {
                        Integer layerInt = (Integer) map.get("layer");
                        if (layerInt == null)
                            return MyJsonConverter.createErrorToJson("层数规则不符合要求").toJSONString();
                        else {
                            String layer = layerInt.intValue() + "";
                            Map<String, Object> searchData = bookSearchService.filterBooksByLayer(books, layer);
                            books = (List<Book>) searchData.get("state");
                            if (books == null)
                                return MyJsonConverter.convertErrorToJson(searchData).toJSONString();
                        }
                    }

                    //区号筛选
                    if (map.get("origin") != null) {
                        String origin = (String) map.get("origin");
                        if (origin == null)
                            return MyJsonConverter.createErrorToJson("区号规则不符合要求").toJSONString();
                        else {
                            origin += "区";
                            Map<String, Object> searchData = bookSearchService.filterBooksByOrigin(books, origin);
                            books = (List<Book>) searchData.get("state");
                            if (books == null)
                                return MyJsonConverter.convertErrorToJson(searchData).toJSONString();
                        }
                    }
                }
                if (books == null)
                    return MyJsonConverter.createErrorToJson("书籍查询失败").toJSONString();
                else {
                    JSONObject jsonObject = new JSONObject(true);
                    jsonObject.put("result", 1);
                    JSONArray array = new JSONArray();
                    for (Book book : books)
                        array.add(MyJsonConverter.convertSimpleBookToJson(book));
                    jsonObject.put("data", array);
                    return jsonObject.toJSONString();
                }
            } else
                return MyJsonConverter.createErrorToJson("书籍查询失败").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }

    }


    /**
     * 查看具体书籍信息
     * @return 返回搜索的信息
     */
    @CrossOrigin
    @PostMapping("searchBookData")
    public String searchBookData(@RequestBody Map<String,Integer> map){
        try {
            Integer bookId = map.get("bookId");
            if(bookId!=null) {
                Map<String, Object> bookData = bookSearchService.getBook(bookId);
                Book book= (Book) bookData.get("state");
                if (book != null) {
                    JSONObject object = new JSONObject(true);
                    object.put("result", 1);
                    JSONObject data = MyJsonConverter.convertComplexBookToJson(book);
                    Map<String, Object> info = bookSearchService.getTitleByType(book.getTypeId());
                    String title= (String) info.get("state");
                    if(title!=null){
                        data.put("title", title);
                        object.put("data", data);
                        return object.toJSONString();
                    }
                    else
                        return MyJsonConverter.convertErrorToJson(info).toJSONString();
                }
                else
                    return MyJsonConverter.convertErrorToJson(bookData).toJSONString();
            }
            else
                return MyJsonConverter.createErrorToJson("书籍id不能为空").toJSONString();
        }catch (Exception e){
            e.printStackTrace();
            return MyJsonConverter.createErrorToJson("系统内部错误").toJSONString();
        }
    }
}