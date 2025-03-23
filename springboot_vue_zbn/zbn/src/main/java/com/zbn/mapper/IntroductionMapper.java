
package com.zbn.mapper;

import com.zbn.entity.Introduction;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface IntroductionMapper {

    List<Introduction> selectAll(Introduction introduction);

    void insert(Introduction introduction);

    void updateByid(Introduction introduction);

    @Delete("delete from `introduction` where id = #{id}")
    void deleteByid(Integer id);

}
