
package com.zbn.mapper;

import com.zbn.entity.Introducion;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface IntroducttionMapper {

    List<Introducion> selectAll(Introducion introducion);

    void insert(Introducion introducion);

    void updateByid(Introducion introducion);

    @Delete("delete from `introducion` where id = #{id}")
    void deleteByid(Integer id);

}
