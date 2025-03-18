package com.zbn.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.pagehelper.PageInfo;
import com.zbn.common.Result;
import com.zbn.entity.User;
import com.zbn.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.propertyeditors.ReaderEditor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("/selectAll")   //http://ip:port/user//selectAll
    public Result selectAll(User user) {
        List<User> userList = userService.selectAll(user);
        //-->UserService -->UserMapper -->UserMapper.xml
        return Result.success(userList);
    }
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")//接受传来的路劲参数
    public Result delete(@PathVariable Integer id) {
        userService.deleteByid(id);
        return Result.success();
    }
    @DeleteMapping("/deleteBatch")//接受传来的路劲参数
    public Result delete(@RequestBody List<User> list) {
        userService.deleteBatch(list);
        return Result.success();
    }
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             User user) {


        PageInfo<User> pageInfo = userService.selectPage(pageNum, pageSize, user);
        return Result.success(pageInfo);
    }

    /**
     * 数据导出
     */
    @GetMapping("/export")
    public void exportData(User user, HttpServletResponse response) throws Exception {
        // 1. 获取所有数据
        List<User> list = userService.selectAll(user);

        // 2. 构建ExcelWriter对象
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 3. 设置中文表头
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("email", "邮箱");

        // 默认情况下，未添加别名的属性也会写出。如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);

        // 4. 将数据写入ExcelWriter
        writer.write(list);

        // 5. 设置输出的文件名称和输出流的信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理员信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        // 6. 将数据写入输出流并关闭writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
        //  1.获取MultiparFile对象，创建输入流
        InputStream inputStream = file.getInputStream();
        //  2.创建reder对象来接受inputStream作为输入
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        reader.addHeaderAlias("账号","username");
        reader.addHeaderAlias("名称","name");
        reader.addHeaderAlias("电话","phone");
        reader.addHeaderAlias("邮箱","email");
        //  3.将读到的数据传入list
        List<User> list = reader.readAll(User.class);
        for (User user : list) {
            userService.add(user);
        }
        return Result.success();
    }


}
