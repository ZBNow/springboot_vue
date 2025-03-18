package com.zbn.controller;

import com.zbn.common.Result;
import com.zbn.entity.Account;
import com.zbn.entity.User;
import com.zbn.service.AdminService;
import com.zbn.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.zbn.entity.Admin;
import java.util.HashMap;
//接收前端发来的请求的接口
@RestController
public class WebController {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;
    //表示这是有一个get请求的接口
    public Result hello() {
        return Result.success("無");
    }
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if("ADMIN".equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else {
            dbAccount = userService.login(account);
        }
        return Result.success(dbAccount);
    }
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

}
