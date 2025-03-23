package com.zbn.mapper;

import com.github.pagehelper.ISelect;
import com.zbn.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryMapper {

    List<Category> selectAll(Category category);

    void insert(Category category);

    void updateByid(Category category);

    @Delete("delete from `category` where id = #{id}")
    void deleteByid(Integer id);

    @Select("Select * from `category` where id = #{id}")
    Category selectByid(Integer Id);
}
