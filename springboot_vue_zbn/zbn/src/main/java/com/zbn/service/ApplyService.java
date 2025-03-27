package com.zbn.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbn.entity.Account;
import com.zbn.entity.Apply;
import com.zbn.exception.CustomerException;
import com.zbn.mapper.ApplyMapper;
import com.zbn.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//业务代码
public class ApplyService {

    @Resource
    ApplyMapper applyMapper;

    public void add(Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        apply.setTime(DateUtil.now());
        apply.setUserId(currentUser.getId());
        apply.setStatus("待审核");
        applyMapper.insert(apply);
    }

    public List<Apply> selectAll(Apply apply) {
        return applyMapper.selectAll(apply);
    }

    public void update(Apply apply) {
        applyMapper.updateByid(apply);
    }

    public void deleteByid(Integer id) {
        applyMapper.deleteByid(id);
    }

    public void deleteBatch(List<Apply> list) {
        for (Apply apply : list) {
            this.deleteByid(apply.getId());
        }
    }


    public PageInfo<Apply> selectPage(Integer pageNum, Integer pageSize, Apply apply) {
        //开启分页查询
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())){
            apply.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyMapper.selectAll(apply);
        return PageInfo.of(list);
    }


}

