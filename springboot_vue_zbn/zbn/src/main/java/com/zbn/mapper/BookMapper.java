package com.zbn.mapper;

import com.zbn.entity.Book;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface BookMapper {

    List<Book> selectAll(Book book);

    void insert(Book book);

    void updateByid(Book book);

    @Delete("delete from `book` where id = #{id}")
    void deleteByid(Integer id);

}
