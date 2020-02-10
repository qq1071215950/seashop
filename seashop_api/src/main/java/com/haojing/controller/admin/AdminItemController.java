package com.haojing.controller.admin;

import com.haojing.result.ResponseResult;
import com.haojing.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品管理", tags = {"管理员操作商品管理相关接口"})
@RestController
@RequestMapping("item")
@CrossOrigin
public class AdminItemController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "添加商品", notes = "添加上坪", httpMethod = "POST")
    @PostMapping("/addItem")
    public ResponseResult addTtem() {
        return null;
    }

    @ApiOperation(value = "商品的上下架", notes = "商品的上下架", httpMethod = "POST")
    @PostMapping("/upOrDown")
    public ResponseResult upOrDown(
            @ApiParam(name = "itemIds", value = "商品ids", required = true)
            @RequestParam List<String> itemIds,
            @ApiParam(name = "type", value = "上下架标志", required = true)
            @RequestParam Integer type) {
        if (CollectionUtils.isEmpty(itemIds)){
            return ResponseResult.errorMsg("商品ids不能为空");
        }
        itemService.upOrDownItems(itemIds,type);
        return ResponseResult.ok();
    }

}