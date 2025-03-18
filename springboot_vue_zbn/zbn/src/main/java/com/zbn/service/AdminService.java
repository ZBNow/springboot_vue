package com.zbn.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbn.entity.Account;
import com.zbn.entity.Admin;
import com.zbn.entity.User;
import com.zbn.exception.CustomerException;
import com.zbn.mapper.AdminMapper;
import com.zbn.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import com.zbn.entity.Admin;

import static com.zbn.utils.TokenUtils.getCurrentUser;

@Service
//业务代码
public class AdminService {

    @Resource
    AdminMapper adminMapper;

    public void add(Admin admin) {
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if(dbAdmin != null) {
            throw new CustomerException("账号重复");
        }
        if(StrUtil.isBlank(admin.getPassword())){
            admin.setPassword("admin");
        }
        adminMapper.insert(admin);
    }

    //查询对应条件的所有数据
    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateByid(admin);
    }

    public void deleteByid(Integer id) {
        adminMapper.deleteByid(id);
    }

    public void deleteBatch(List<Admin> list) {
        for (Admin admin : list) {
            this.deleteByid(admin.getId());
        }
    }


    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        //开启分页查询
        Account currentUser = getCurrentUser();
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public Admin login(Account account) {
        //先进行判断数据库中是否存在用户，或者密码是否正确
        Admin dbadmin =  adminMapper.selectByUsername(account.getUsername());
        if(dbadmin == null) {
            throw new CustomerException("账号不存在");
        } else if(!dbadmin.getPassword().equals(account.getPassword())) {
            throw new CustomerException("账号或者密码错误");
        }
        //创建token并返回给卡暖
        String token = TokenUtils.createToken(dbadmin.getId() + "-" + "ADMIN", dbadmin.getPassword());
        dbadmin.setToken(token);
        return dbadmin;
    }

    public Admin selectByid(String id) {
        return adminMapper.selectByid(id);
    }
}

