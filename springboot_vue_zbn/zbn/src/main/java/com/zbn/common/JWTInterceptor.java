package com.zbn.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.zbn.entity.Account;
import com.zbn.exception.CustomerException;
import com.zbn.service.AdminService;
import com.zbn.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    @Resource
    AdminService adminService;

    @Resource
    UserService userService;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.拿到token
        String token = request.getHeader("token");
        //参数中拿到
        if (StrUtil.isEmptyIfStr(token)) {
            token = request.getParameter("token");
        }
        // 2. 认证token
        if (StrUtil.isBlank(token)){
            throw new CustomerException("401","您無权限进行操作");
        }
        Account account = null;
        try{
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            String userId = split[0];
            String role = split[1];
            //根据token解析出的role查询对应的表的信息
            if ("ADMIN".equals(role)) {
                account =  adminService.selectByid(userId);
            } else if ("USER".equals(role)) {
                account = userService.selectByid(userId);
            }
        } catch (Exception e){
            throw new CustomerException("401", "您无权限操作");
        }
        if (account == null) {
            throw new CustomerException("401", "您无权限操作");
        }
        try{
            //从查找到的用户的密码进行sha256计算，与传进的token进行比较正确通过执行下面代码
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);
        } catch (Exception e){
            throw new CustomerException("401", "您无权限操作");
        }

        return true;
    }
}
