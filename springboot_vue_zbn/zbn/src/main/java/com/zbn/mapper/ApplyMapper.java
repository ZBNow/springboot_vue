package com.zbn.mapper;

import com.zbn.entity.Apply;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface ApplyMapper {

    List<Apply> selectAll(Apply apply);

    void insert(Apply apply);

    void updateByid(Apply apply);

    @Delete("delete from `apply` where id = #{id}")
    void deleteByid(Integer id);

}
