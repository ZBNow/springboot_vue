package com.zbn.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.pagehelper.PageInfo;
import com.zbn.common.Result;
import com.zbn.entity.Admin;
import com.zbn.service.AdminService;
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
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;

    @GetMapping("/selectAll")   //http://ip:port/admin//selectAll
    public Result selectAll(Admin admin) {
        List<Admin> adminList = adminService.selectAll(admin);
        //-->AdminService -->AdminMapper -->AdminMapper.xml
        return Result.success(adminList);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        adminService.update(admin);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")//接受传来的路劲参数
    public Result delete(@PathVariable Integer id) {
        adminService.deleteByid(id);
        return Result.success();
    }
    @DeleteMapping("/deleteBatch")//接受传来的路劲参数
    public Result delete(@RequestBody List<Admin> list) {
        adminService.deleteBatch(list);
        return Result.success();


    }
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Admin admin) {


        PageInfo<Admin> pageInfo = adminService.selectPage(pageNum, pageSize, admin);

        return Result.success(pageInfo);
    }

        /**
         * 数据导出
         */
    @GetMapping("/export")
    public void exportData(Admin admin, HttpServletResponse response) throws Exception {
            // 1. 获取所有数据
        List<Admin> list = adminService.selectAll(admin);

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
        List<Admin> list = reader.readAll(Admin.class);
        for (Admin admin : list) {
            adminService.add(admin);
        }
        return Result.success();

    }

}
