package com.zbn.mapper;

import com.zbn.entity.Borrow;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface BorrowMapper {

    List<Borrow> selectAll(Borrow borrow);

    void insert(Borrow borrow);

    void updateByid(Borrow borrow);

    @Delete("delete from `borrow` where id = #{id}")
    void deleteByid(Integer id);

}
