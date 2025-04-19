package com.zbn.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.zbn.common.Result;
import com.zbn.exception.CustomerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();//设置文件名
        //写入文件
        FileUtil.writeBytes(bytes, filepath + fileName);
        String url = "http://43.143.94.185:9999/files/download/" + fileName;// 设置返回的下载图片地址
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
    /**
     * 实现富文本编辑器的图片上传
     */
    @PostMapping("/wang/upload")//实现wangEditor文本图片上传
    public Map<String, Object> wangEditorUpload(MultipartFile file) throws Exception {
        //生成唯一文件名
        String flag = System.currentTimeMillis() + "";
        String fileName = file.getOriginalFilename();
        try {
            String filePath = System.getProperty("user.dir") + "/springboot_vue_zbn/files/";//服务器存储路径
            //保存文件
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            System.out.println(fileName + "上传成功");
            Thread.sleep(1L);

        } catch (Exception e) {
            System.out.println("上传失败");
            throw new RuntimeException(e);
        }
        String http = "http://43.143.94.185:9999/files/download/";

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("errno", 0);
        resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
        return resMap;
    }
}
