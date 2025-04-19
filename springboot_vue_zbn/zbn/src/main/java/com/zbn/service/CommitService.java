package com.zbn.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HtmlUtil;
import com.zbn.entity.Account;
import com.zbn.entity.Commit;
import com.zbn.entity.Commit;
import com.zbn.mapper.CategoryMapper;
import com.zbn.mapper.CommitMapper;
import com.zbn.mapper.CommitMapper;
import com.zbn.mapper.UserMapper;
import com.zbn.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;

@Service
//业务代码
public class CommitService {

   
    @Resource
    CommitMapper commitMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Resource
    UserMapper userMapper;

    public List<Commit> selectByid(Integer id) {
        List<Commit> commit = commitMapper.selectByid(id);
        return commit;
    }

    public void add(Commit commit) {
        Account currentUser = TokenUtils.getCurrentUser();
        commit.setUserId(currentUser.getId());
        String time = DateUtil.format(new Date(), "MM-dd HH:mm:ss");
        commit.setTime(time);
        commitMapper.insert(commit);
    }

//    public List<Commit> selectAll(Commit commit) {
//        List<commit> commits = commitMapper.selectAll(commit);
//        return commits;
//    }
}

