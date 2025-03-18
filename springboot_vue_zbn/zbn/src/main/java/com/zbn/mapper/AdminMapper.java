package com.zbn.mapper;

import com.zbn.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {
    //Mapper定义接口的方法
    //Mapper.xml里面来实现方法（sql语句）
    List<Admin> selectAll(Admin admin);

    void insert(Admin admin);

    @Select("select * from `admin` where username = #{username}")
    Admin selectByUsername(String username);

    void updateByid(Admin admin);

    @Delete("delete from `admin` where id = #{id}")
    void deleteByid(Integer id);

    @Select("select * from `admin` where id = #{id}")
    Admin selectByid(String id);
}
