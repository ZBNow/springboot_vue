package com.zbn.mapper;

import com.zbn.entity.Admin;
import com.zbn.entity.Commit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommitMapper {
    //Mapper定义接口的方法
    //Mapper.xml里面来实现方法（sql语句）
    List<Commit> selectAll(Integer id);

    void insert(Commit commit);

    @Select("select * from `admin` where username = #{username}")
    Admin selectByUsername(String username);

    void updateByid(Admin admin);

    @Delete("delete from `admin` where id = #{id}")
    void deleteByid(Integer id);

    @Select("select comment.*, user.avatar as avatar, user.name as name from `comment` left join `user` on user.id = comment.user_id where introduction_id = #{id}")
    List<Commit> selectByid(@Param("id") Integer id);

}
