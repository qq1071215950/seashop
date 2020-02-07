package com.haojing.controller;

import com.haojing.bo.AddressBO;
import com.haojing.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(value = "地址相关", tags = {"地址相关的api接口"})
@RequestMapping("address")
@RestController
public class AddressController {


    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public ResponseResult list(
            @RequestParam String userId) {
        return null;
    }

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody AddressBO addressBO) {

        return ResponseResult.ok();
    }


    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody AddressBO addressBO) {


        return ResponseResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public ResponseResult delete(
            @RequestParam String userId,
            @RequestParam String addressId) {

        return ResponseResult.ok();
    }


    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public ResponseResult setDefalut(
            @RequestParam String userId,
            @RequestParam String addressId) {

        return ResponseResult.ok();
    }
}
