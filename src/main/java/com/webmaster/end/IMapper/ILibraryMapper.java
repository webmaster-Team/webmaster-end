package com.webmaster.end.IMapper;

import com.webmaster.end.Entity.Library;

import java.util.List;

public interface ILibraryMapper {
    /**
     * 获得所有的图书馆信息
     * @return 返回图书馆信息列表
     */
    public List<Library> getLibraries();
}
