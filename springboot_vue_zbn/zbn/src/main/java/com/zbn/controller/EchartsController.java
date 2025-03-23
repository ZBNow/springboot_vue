package com.zbn.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zbn.common.Result;
import com.zbn.entity.Category;
import com.zbn.entity.Introduction;
import com.zbn.entity.User;
import com.zbn.service.CategoryService;
import com.zbn.service.IntroductionService;
import com.zbn.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    public CategoryService categoryService;
    @Resource
    public IntroductionService introductionService;
    @Resource
    public UserService userService;

    public EchartsController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/pie")
    public Result pie() {
        List<Map<String, Object>> list = new ArrayList<>();
        //开始封装数据
        // 查询所有分类信息
        List<Category> categories = categoryService.selectAll(new Category());
        // 查出所有的帖子信息
        List<Introduction> introductions = introductionService.selectAll(new Introduction());
        for (Category category : categories) {
            // 过滤攻略id与分类id相同的项，并进行统计
            long count = introductions.stream().filter(x -> category.getId().equals(x.getCategoryId())).count();
            Map<String, Object> map = new HashMap<>();
            map.put("name", category.getTitle());
            map.put("value",count);
            list.add(map);
        }
        return Result.success(list);
    }
    @GetMapping("/bar")
    public Result bar() {// 考验数据结构
        Map<String, Object> resultMap = new HashMap<>();
        List<String> xList = new ArrayList<>();
        List<Long> yList = new ArrayList<>();
        Map<String, Long> map = new HashMap<>();

        List<Introduction> introductions = introductionService.selectAll(new Introduction());
        // 业务代码，如何将数据传递,查询所有用户
        List<User> users = userService.selectAll(new User());
        for (User user : users) {
            long count = introductions.stream().filter(x -> user.getId().equals(x.getUserId())).count();
            map.put(user.getName(), count);

        }
        LinkedHashMap<String, Long> collect = map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (String key : collect.keySet()) {
            xList.add(key);
            yList.add(collect.get(key));
        }

        // top几的问题
        if (xList.size() > 5 && yList.size() > 5) {
            xList = xList.subList(0, 5);
            yList = yList.subList(0, 5);
        }
        resultMap.put("xAxis", xList);
        resultMap.put("yAxis", yList);
        return Result.success(resultMap);
    }
    @GetMapping("/line")
    public Result line() {
        Map<String, Object> resultMap = new HashMap<>();
        List<Long> yList = new ArrayList<>();

        // 获取最近多少天的年月日List
        Date today = new Date();
        DateTime start = DateUtil.offsetDay(today, -6);
        List<String> xList = DateUtil.rangeToList(start, today, DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).toList();

        // 把所有的帖子拿出来
        List<Introduction> introductions = introductionService.selectAll(new Introduction());

        for (String day : xList) {
            long count = introductions.stream().filter(x -> ObjectUtil.isNotEmpty(x.getTime()) && x.getTime().contains(day)).count();
            yList.add(count);
        }

        resultMap.put("xAxis", xList);
        resultMap.put("yAxis", yList);

        return Result.success(resultMap);
    }
}
