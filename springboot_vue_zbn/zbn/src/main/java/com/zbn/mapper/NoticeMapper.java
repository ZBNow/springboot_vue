package com.zbn.mapper;

import com.zbn.entity.Notice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticeMapper {

    List<Notice> selectAll(Notice notice);

    void insert(Notice notice);

    void updateByid(Notice notice);

    @Delete("delete from `notice` where id = #{id}")
    void deleteByid(Integer id);

}
