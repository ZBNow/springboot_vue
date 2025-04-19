package com.zbn.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbn.entity.Account;
import com.zbn.entity.Category;
import com.zbn.entity.Introduction;
import com.zbn.entity.User;
import com.zbn.exception.CustomerException;
import com.zbn.mapper.CategoryMapper;
import com.zbn.mapper.IntroductionMapper;
import com.zbn.mapper.UserMapper;
import com.zbn.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
//业务代码
public class IntroductionService {

    @Resource
    IntroductionMapper introductionMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Resource
    UserMapper userMapper;
    public void add(Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        introduction.setUserId(currentUser.getId());
        introduction.setTime(DateUtil.now());
        introduction.setStatus("待审核");
        introductionMapper.insert(introduction);
    }

    public List<Introduction> selectAll(Introduction introduction) {
        List<Introduction> introductions = introductionMapper.selectAll(introduction);
        for (Introduction dbIntroduction : introductions) {
            String content = dbIntroduction.getContent();
            String cleanDescription = content != null ? HtmlUtil.cleanHtmlTag(content) : "";
            dbIntroduction.setDescription(cleanDescription);
        }
        return introductions;
    }

    public void update(Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(currentUser.getRole().equals("USER")){
            introduction.setStatus("待审核");
        }
        introductionMapper.updateByid(introduction);
    }

    public void deleteByid(Integer id) {
        introductionMapper.deleteByid(id);
    }

    public void deleteBatch(List<Introduction> list) {
        for (Introduction introduction : list) {
            this.deleteByid(introduction.getId());
        }
    }


    public PageInfo<Introduction> selectPage(Integer pageNum, Integer pageSize, Introduction introduction) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        //让用户只能查询自己发布的
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())) {
            introduction.setUserId(currentUser.getId());
        }
        // 存储了旅游攻略的原始数组 ，只有分类id
        List<Introduction> list = introductionMapper.selectAll(introduction);
//        for (Introduction introduction1 : list) {
//            // 拿到categoryId
//            Integer categoryId = introduction1.getCategoryId();
//            Integer userId = introduction1.getUserId();
//            //通过 categoryId从表里查出对应的title
//            Category category = categoryMapper.selectByid(categoryId);
//            User user = userMapper.selectByid(userId.toString());
//            if(ObjectUtil.isNotEmpty(category)){
//                // 把分类的title 复值给categoryTitle
//                introduction1.setCategoryTitle(category.getTitle());
//            }
//            if(ObjectUtil.isNotEmpty(user)){
//                introduction1.setUserName(user.getName());
//            }
//        }
        return PageInfo.of(list);
    }


    public Introduction selectByid(Integer id) {
        Introduction introduction1 = introductionMapper.selectByid(id);
        Integer categoryId = introduction1.getCategoryId();
        Integer userId = introduction1.getUserId();
        //通过 categoryId从表里查出对应的title
        Category category = categoryMapper.selectByid(categoryId);
        User user = userMapper.selectByid(userId.toString());
        if (ObjectUtil.isNotEmpty(category)) {
            // 把分类的title 复值给categoryTitle
            introduction1.setCategoryTitle(category.getTitle());
        }
        if (ObjectUtil.isNotEmpty(user)) {
            introduction1.setUserName(user.getName());
            introduction1.setUserAvatar(user.getAvatar());
        }
        return introduction1;
    }



    public List<Introduction> selectAllStatus(Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())) {
            introduction.setUserId(currentUser.getId());
        }
        List<Introduction> introductions = introductionMapper.selectAllStatus(introduction);
        for (Introduction dbIntroduction : introductions) {
            String content = dbIntroduction.getContent();
            String cleanDescription = content != null ? HtmlUtil.cleanHtmlTag(content) : "";
            dbIntroduction.setDescription(cleanDescription);
        }

        return introductions;
    }

    public List<Introduction> selectCategory(Introduction introduction) {
        List<Introduction> introductions = introductionMapper.selectAllCategory(introduction);

        return introductions;
    }
}

