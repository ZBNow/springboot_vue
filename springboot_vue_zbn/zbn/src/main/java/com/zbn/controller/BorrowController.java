package com.zbn.controller;

import com.github.pagehelper.PageInfo;
import com.zbn.common.Result;
import com.zbn.entity.Borrow;
import com.zbn.service.BorrowService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Resource
    BorrowService borrowService;

    @GetMapping("/selectAll")   //http://ip:port/borrow//selectAll
    public Result selectAll(Borrow borrow) {
        List<Borrow> borrowList = borrowService.selectAll(borrow);
        //-->BorrowService -->BorrowMapper -->BorrowMapper.xml
        return Result.success(borrowList);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Borrow borrow) {
        borrowService.add(borrow);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Borrow borrow) {
        borrowService.update(borrow);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")//接受传来的路劲参数
    public Result delete(@PathVariable Integer id) {
        borrowService.deleteByid(id);
        return Result.success();
    }


    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Borrow borrow) {


        PageInfo<Borrow> pageInfo = borrowService.selectPage(pageNum, pageSize, borrow);
        return Result.success(pageInfo);
    }
}
