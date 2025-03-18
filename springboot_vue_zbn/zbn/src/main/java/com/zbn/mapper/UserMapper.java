package com.zbn.mapper;

import com.zbn.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    //Mapper定义接口的方法
    //Mapper.xml里面来实现方法（sql语句）
    List<User> selectAll(User user);

    void insert(User user);

    @Select("select * from `user` where username = #{username}")
    User selectByUsername(String username);

    void updateByid(User user);

    @Delete("delete from `user` where id = #{id}")
    void deleteByid(Integer id);

    @Select("select * from `user` where id = #{id}")
    User selectByid(String id);
}
