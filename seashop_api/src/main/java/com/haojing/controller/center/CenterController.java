package com.haojing.controller.center;

import com.haojing.entity.Users;
import com.haojing.result.ResponseResult;
import com.haojing.service.center.CenterUserService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(value = "center - 用户中心", tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private HttpServletRequest request;
    
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("userInfo")
    public ResponseResult userInfo() {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        Users user = centerUserService.queryUserInfo(userId);
        if (user == null){
            return ResponseResult.ok("用户信息不存在");
        }
        return ResponseResult.ok(user);
    }
}
