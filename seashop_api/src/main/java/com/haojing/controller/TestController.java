package com.haojing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// 开启跨域
@CrossOrigin
@Api(value = "测试类" ,tags = {"环境测试相关接口"})
public class TestController {
    @GetMapping("/test")
    @ApiOperation(value = "测试方法",httpMethod = "GET")
    public String test(){
        return "hello world";
    }
}
// todo 引入redis
// todo 静态资源地 访问
// todo 权限的拦截
// todo 全局错误显示
