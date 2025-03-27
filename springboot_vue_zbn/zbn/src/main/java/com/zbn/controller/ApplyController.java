package com.zbn.controller;

import com.github.pagehelper.PageInfo;
import com.zbn.common.Result;
import com.zbn.entity.Apply;
import com.zbn.service.ApplyService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {
    @Resource
    ApplyService applyService;

    @GetMapping("/selectAll")   //http://ip:port/apply//selectAll
    public Result selectAll(Apply apply) {
        List<Apply> applyList = applyService.selectAll(apply);
        //-->ApplyService -->ApplyMapper -->ApplyMapper.xml
        return Result.success(applyList);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Apply apply) {
        applyService.add(apply);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Apply apply) {
        applyService.update(apply);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")//接受传来的路劲参数
    public Result delete(@PathVariable Integer id) {
        applyService.deleteByid(id);
        return Result.success();
    }


    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Apply apply) {


        PageInfo<Apply> pageInfo = applyService.selectPage(pageNum, pageSize, apply);
        return Result.success(pageInfo);
    }
}
