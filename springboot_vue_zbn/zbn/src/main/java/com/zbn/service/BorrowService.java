package com.zbn.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbn.entity.Account;
import com.zbn.entity.Borrow;
import com.zbn.exception.CustomerException;
import com.zbn.mapper.BorrowMapper;
import com.zbn.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//业务代码
public class BorrowService {

    @Resource
    BorrowMapper borrowMapper;

    public void add(Borrow borrow) {
        Account currentUser = TokenUtils.getCurrentUser();
        borrow.setUserId(currentUser.getId());
        borrowMapper.insert(borrow);
    }

    public List<Borrow> selectAll(Borrow borrow) {
        return borrowMapper.selectAll(borrow);
    }

    public void update(Borrow borrow) {
        borrowMapper.updateByid(borrow);
    }

    public void deleteByid(Integer id) {
        borrowMapper.deleteByid(id);
    }

    public void deleteBatch(List<Borrow> list) {
        for (Borrow borrow : list) {
            this.deleteByid(borrow.getId());
        }
    }


    public PageInfo<Borrow> selectPage(Integer pageNum, Integer pageSize, Borrow borrow) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Borrow> list = borrowMapper.selectAll(borrow);
        return PageInfo.of(list);
    }


}

