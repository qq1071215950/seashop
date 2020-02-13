package com.haojing.controller;

import com.haojing.result.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult error(Exception e){
        e.printStackTrace();
        return ResponseResult.errorMsg("系统出错，请重新尝试");
    }
}
