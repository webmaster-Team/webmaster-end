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

    public List<Library> getLibraries(){
        QueryRunner queryRunner=new QueryRunner(dataSource);
        String sql="select * from library";
        try {
            List<Library> libraries = queryRunner.query(sql, new BeanListHandler<>(Library.class));
            return libraries;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
