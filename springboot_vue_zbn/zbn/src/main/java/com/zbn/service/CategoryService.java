package com.zbn.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbn.entity.Account;
import com.zbn.entity.Category;
import com.zbn.exception.CustomerException;
import com.zbn.mapper.CategoryMapper;
import com.zbn.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//业务代码
public class CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    public void add(Category category) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!currentUser.getRole().equals("ADMIN")) {
            throw new CustomerException("您无权限进行操作");
        }

        categoryMapper.insert(category);
    }

    public List<Category> selectAll(Category category) {
        return categoryMapper.selectAll(category);
    }

    public void update(Category category) {
        categoryMapper.updateByid(category);
    }

    public void deleteByid(Integer id) {
        categoryMapper.deleteByid(id);
    }

    public void deleteBatch(List<Category> list) {
        for (Category category : list) {
            this.deleteByid(category.getId());
        }
    }


    public PageInfo<Category> selectPage(Integer pageNum, Integer pageSize, Category category) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Category> list = categoryMapper.selectAll(category);
        return PageInfo.of(list);
    }


}

