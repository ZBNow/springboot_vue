package com.zbn.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbn.entity.Account;
import com.zbn.entity.Admin;
import com.zbn.entity.User;
import com.zbn.exception.CustomerException;
import com.zbn.mapper.AdminMapper;
import com.zbn.mapper.UserMapper;
import com.zbn.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import com.zbn.entity.User;
@Service
//业务代码
public class UserService {

    @Resource
    UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;

    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser != null) {
            throw new CustomerException("账号重复");
        }
        if(StrUtil.isBlank(user.getPassword())){
            user.setPassword("user");
        }
        user.setRole("USER");
        userMapper.insert(user);
    }

    //查询对应条件的所有数据
    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public void update(User user) {
        userMapper.updateByid(user);
    }

    public void deleteByid(Integer id) {
        userMapper.deleteByid(id);
    }

    public void deleteBatch(List<User> list) {
        for (User user : list) {
            this.deleteByid(user.getId());
        }
    }


    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize, User user) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public User login(Account account) {
       User dbuser =  userMapper.selectByUsername(account.getUsername());
       if(dbuser == null) {
           throw new CustomerException("账号不存在");
       } else if(!dbuser.getPassword().equals(account.getPassword())) {
           throw new CustomerException("账号或者密码错误");
       }
        String token = TokenUtils.createToken(dbuser.getId() + "-" + "USER", dbuser.getPassword());
        dbuser.setToken(token);
       return dbuser;
    }

    public User selectByid(String id) {
        return userMapper.selectByid(id);
    }
}

