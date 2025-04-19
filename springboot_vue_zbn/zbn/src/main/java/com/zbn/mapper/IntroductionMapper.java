
package com.zbn.mapper;

import com.zbn.entity.Introduction;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IntroductionMapper {

    List<Introduction> selectAll(Introduction introduction);

    void insert(Introduction introduction);

    void updateByid(Introduction introduction);

    @Delete("delete from `introduction` where id = #{id}")
    void deleteByid(Integer id);
    @Select("select * from `introduction` where id = #{id}")
    Introduction selectByid(Integer id);

    List<Introduction> selectAllStatus(Introduction introduction);

    List<Introduction> selectAllCategory(Introduction introduction);
}
