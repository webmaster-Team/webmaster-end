package com.webmaster.end.Dao;

import com.webmaster.end.Entity.Library;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LibraryDao {
    @Autowired
    private DataSource dataSource;

    /**
     * 获得所有的图书馆信息
     * @return 返回图书馆信息列表
     */
    public List<Library> getLibraries() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from library";
        List<Library> libraries = queryRunner.query(sql, new BeanListHandler<>(Library.class));
        return libraries;
    }

}
