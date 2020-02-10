package com.haojing.controller.admin;

import com.haojing.entity.Carousel;
import com.haojing.enums.YesOrNo;
import com.haojing.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


@Api(value = "分类管理", tags = {"管理员操作分类管理相关接口"})
@RestController
@RequestMapping("category")
@CrossOrigin
public class AdminCategoryController {

    @ApiOperation(value = "添加一级分类", notes = "添加一级分类", httpMethod = "POST")
    @PostMapping("/add")
    public ResponseResult add() {
       return null;
    }

    @ApiOperation(value = "添加子分类", notes = "添加子分类", httpMethod = "POST")
    @PostMapping("/addsecord")
    public ResponseResult addSecord() {
        return null;
    }

    @ApiOperation(value = "删除分类信息", notes = "删除分类信息", httpMethod = "PUT")
    @PutMapping("/delete")
    public ResponseResult delete() {
        return null;
    }
}
