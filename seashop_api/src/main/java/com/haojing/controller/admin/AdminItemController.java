package com.haojing.controller.admin;

import com.haojing.result.ResponseResult;
import com.haojing.service.ItemService;
import com.haojing.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(value = "商品管理", tags = {"管理员操作商品管理相关接口"})
@RestController
@RequestMapping("item")
@CrossOrigin
public class AdminItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "添加商品", notes = "添加上坪", httpMethod = "POST")
    @PostMapping("/addItem")
    public ResponseResult addTtem() {
        return null;
    }

    @ApiOperation(value = "商品图片上传", notes = "商品图片上传", httpMethod = "POST")
    @PostMapping("/upload")
    public ResponseResult uploadPicture(
            @ApiParam(name = "file", value = "商品图片", required = true)
            @RequestParam MultipartFile file) {
        String url = uploadService.upload(file);
        if (StringUtils.isBlank(url)) {
            // url为空，证明上传失败
            return ResponseResult.errorMsg("图片上传失败");
        }
        // 返回200，并且携带url路径
        return ResponseResult.ok("上传成功",url);
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