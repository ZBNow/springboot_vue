package com.zbn.controller;

import com.github.pagehelper.PageInfo;
import com.zbn.common.Result;
import com.zbn.entity.Introduction;
import com.zbn.service.IntroductionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/introduction")
public class IntroductionController {
    @Resource
    IntroductionService introductionService;

    @GetMapping("/selectAll")
    public Result selectAll(Introduction introduction) {
        List<Introduction> introductionList = introductionService.selectAll(introduction);
        return Result.success(introductionList);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Introduction introduction) {
        introductionService.add(introduction);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Introduction introduction) {
        introductionService.update(introduction);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")//接受传来的路劲参数
    public Result delete(@PathVariable Integer id) {
        introductionService.deleteByid(id);
        return Result.success();
    }
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Introduction introduction) {


        PageInfo<Introduction> pageInfo = introductionService.selectPage(pageNum, pageSize, introduction);
        return Result.success(pageInfo);
    }
    /**
     * 根据id查询
     */
    @GetMapping("/selectByid/{id}")
    public Result selectByid(@PathVariable Integer id) {
        Introduction introduction = introductionService.selectByid(id);
        return Result.success(introduction);
    }
}
