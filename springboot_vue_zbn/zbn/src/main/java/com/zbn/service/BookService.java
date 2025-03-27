package com.zbn.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbn.entity.Account;
import com.zbn.entity.Book;
import com.zbn.exception.CustomerException;
import com.zbn.mapper.BookMapper;
import com.zbn.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//业务代码
public class BookService {

    @Resource
    BookMapper bookMapper;

    public void add(Book book) {
        Account currentUser = TokenUtils.getCurrentUser();
        bookMapper.insert(book);
    }

    public List<Book> selectAll(Book book) {
        return bookMapper.selectAll(book);
    }

    public void update(Book book) {
        bookMapper.updateByid(book);
    }

    public void deleteByid(Integer id) {
        bookMapper.deleteByid(id);
    }

    public void deleteBatch(List<Book> list) {
        for (Book book : list) {
            this.deleteByid(book.getId());
        }
    }


    public PageInfo<Book> selectPage(Integer pageNum, Integer pageSize, Book book) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Book> list = bookMapper.selectAll(book);
        return PageInfo.of(list);
    }


}

