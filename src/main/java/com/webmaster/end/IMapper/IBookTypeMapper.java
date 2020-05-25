package com.webmaster.end.IMapper;

import com.webmaster.end.Entity.BookType;

import java.util.List;

public interface IBookTypeMapper {
    /**
     * 根据id看书籍类型是否存在
     * @param id type表的id
     * @return 返回是否存在，报错则为已存在
     */
    public Boolean isExist(int id);


    /**
     * 判断该类型是否存在
     * @param typeid 类型id
     * @return 返回是否存在
     */
    public Boolean isExistByTypeid(String typeid);

    /**
     * 根据具体类型名判断是否存在
     * @param title 类型名
     * @return 返回是否存在
     */
    public Boolean isExistByTitle(String title);

    /**
     * 获得所有的书籍类型
     * @return 返回所有书籍类型
     */
    public List<BookType> getAllBookTypes();


    /**
     * 根据id来获得BookType
     * @param id type表的id
     * @return BookType对象
     */
    public BookType getBookType(int id);


    /**
     * 根据typeid来获得BookType
     * @param typeid 类型id
     * @return BookType对象
     */
    public BookType getBookTypeByTypeid(String typeid);


    /**
     * 根据title来获得BookType
     * @param title 类型名
     * @return BookType对象
     */
    public BookType getBookTypeByTitle(String title);
}
