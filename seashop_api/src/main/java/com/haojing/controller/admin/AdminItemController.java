package com.haojing.controller.admin;

import com.haojing.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value = "商品管理", tags = {"管理员操作商品管理相关接口"})
@RestController
@RequestMapping("item")
@CrossOrigin
public class AdminItemController {

    @ApiOperation(value = "添加商品", notes = "添加上坪", httpMethod = "POST")
    @PostMapping("/addItem")
    public ResponseResult addTtem() {
        return null;
    }

    @ApiOperation(value = "商品的上下架", notes = "商品的上下架", httpMethod = "POST")
    @PutMapping("/upOrDown")
    public ResponseResult upOrDown() {
        return null;
    }
}
