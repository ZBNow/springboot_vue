package com.zbn.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbn.entity.Account;
import com.zbn.entity.Notice;
import com.zbn.exception.CustomerException;
import com.zbn.mapper.AdminMapper;
import com.zbn.mapper.NoticeMapper;
import com.zbn.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//业务代码
public class NoticeService {

    @Resource
    NoticeMapper noticeMapper;

    public void add(Notice notice) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!currentUser.getRole().equals("ADMIN")) {
            throw new CustomerException("您无权限进行操作");
        }

        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    public List<Notice> selectAll(Notice notice) {
        return noticeMapper.selectAll(notice);
    }

    public void update(Notice notice) {
        noticeMapper.updateByid(notice);
    }

    public void deleteByid(Integer id) {
        noticeMapper.deleteByid(id);
    }

    public void deleteBatch(List<Notice> list) {
        for (Notice notice : list) {
            this.deleteByid(notice.getId());
        }
    }


    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize, Notice notice) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }


}

