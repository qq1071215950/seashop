package com.haojing.controller.center;

import com.haojing.bo.center.CenterUserBO;
import com.haojing.controller.BaseController;
import com.haojing.entity.Users;
import com.haojing.result.ResponseResult;
import com.haojing.service.UploadService;
import com.haojing.service.center.CenterUserService;
import com.haojing.utlis.CookieUtils;
import com.haojing.utlis.JsonUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private HttpServletRequest request;

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("uploadFace")
    public ResponseResult uploadFace(
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        String finalUserFaceUrl = uploadService.upload(file);
        if (StringUtils.isBlank(finalUserFaceUrl)){
            return ResponseResult.errorMsg("头像图片上传失败");
        }
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);
        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话
        return ResponseResult.ok("头像上传成功",finalUserFaceUrl);
    }



    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public ResponseResult update(
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        System.out.println(centerUserBO);

        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return ResponseResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);

        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话

        return ResponseResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            // 发生验证错误所对应的某一个属性
            String errorField = error.getField();
            // 验证错误的信息
            String errorMsg = error.getDefaultMessage();

            map.put(errorField, errorMsg);
        }
        return map;
    }
    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

}
