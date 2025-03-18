package com.zbn.controller;

import cn.hutool.core.io.FileUtil;
import com.zbn.common.Result;
import com.zbn.exception.CustomerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 *
 * 处理文件上传和下载的接口
 */
@RestController
@RequestMapping("/files")
public class FileController {
    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        String filepath = System.getProperty("user.dir") + "/springboot_vue_zbn/files/";//获取当前项目的根目录
        if (!FileUtil.isDirectory(filepath)){
            FileUtil.mkdir(filepath);
        }
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        //写入文件
        FileUtil.writeBytes(bytes, filepath + fileName);
        String url = "http://localhost:9999/files/download/" + fileName;
        return Result.success(url);
    }

    /**
     * 文件下载
     *  http://localhost:9999/files/download/1742109749019_(G36%{S2JYX@HZ0VDH2XR75.png
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        // 找到文件的位置， 名字路径
        String filepath = System.getProperty("user.dir") + "/springboot_vue_zbn/files/";//获取当前项目的根目录
        String realpath = filepath + fileName;
        boolean exist = FileUtil.exist(realpath);
        if(!exist) {
            throw new CustomerException("文件不存在");
        }
        //读取文件的字节流
        byte[] bytes = FileUtil.readBytes(realpath);
        //创建响应的输出流，输出到对应的前端
        ServletOutputStream os = response.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
    }
}
