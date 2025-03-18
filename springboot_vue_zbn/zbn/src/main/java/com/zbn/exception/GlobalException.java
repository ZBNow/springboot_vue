package com.zbn.exception;

import com.zbn.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * 全局异常捕获器
 */
@ControllerAdvice("com.zbn")//对对应文件夹内的文件进行监督
public class GlobalException {
    private static final Logger log = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(Exception.class)//拦截系统异常
    @ResponseBody//返回json串，将result对象转换成json格式
    public Result exception(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统异常");//遇到系统异常，
    }

    @ExceptionHandler(CustomerException.class)//拦截CustomerException的异常
    @ResponseBody//返回json串，将result对象转换成json格式
    public Result customerError(CustomerException e) {
        log.error("自定义错误", e);
        return Result.error(e.getCode(),e.getMsg());//遇到系统异常，
    }
}
