package com.zbn.controller;

import com.github.pagehelper.PageInfo;
import com.zbn.common.Result;
import com.zbn.entity.Category;
import com.zbn.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/selectAll")   //http://ip:port/category//selectAll
    public Result selectAll(Category category) {
        List<Category> categoryList = categoryService.selectAll(category);
        //-->CategoryService -->CategoryMapper -->CategoryMapper.xml
        return Result.success(categoryList);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Category category) {
        categoryService.add(category);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Category category) {
        categoryService.update(category);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")//接受传来的路劲参数
    public Result delete(@PathVariable Integer id) {
        categoryService.deleteByid(id);
        return Result.success();
    }


    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Category category) {


        PageInfo<Category> pageInfo = categoryService.selectPage(pageNum, pageSize, category);
        return Result.success(pageInfo);
    }
}
