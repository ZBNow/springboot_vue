package com.zbn.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.pagehelper.PageInfo;
import com.zbn.common.Result;
import com.zbn.entity.Book;
import com.zbn.service.BookService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    BookService bookService;

    @GetMapping("/selectAll")   //http://ip:port/book//selectAll
    public Result selectAll(Book book) {
        List<Book> bookList = bookService.selectAll(book);
        //-->BookService -->BookMapper -->BookMapper.xml
        return Result.success(bookList);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Book book) {
        bookService.add(book);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Book book) {
        bookService.update(book);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")//接受传来的路劲参数
    public Result delete(@PathVariable Integer id) {
        bookService.deleteByid(id);
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Book book) {


        PageInfo<Book> pageInfo = bookService.selectPage(pageNum, pageSize, book);
        return Result.success(pageInfo);
    }
}
