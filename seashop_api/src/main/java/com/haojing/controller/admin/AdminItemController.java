package com.haojing.controller.admin;

import com.haojing.bo.ItemBO;
import com.haojing.bo.ItemsParamBO;
import com.haojing.bo.ItemsSpecBO;
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
    public ResponseResult addTtem(@RequestBody ItemBO itemBO) {
        if (itemBO == null){
            return ResponseResult.errorMsg("商品信息不能为空");
        }
        if (itemBO.getItemsParamBO() == null || CollectionUtils.isEmpty(itemBO.getItemsSpecBOList()) ||CollectionUtils.isEmpty(itemBO.getImgBOList())){
            return ResponseResult.errorMsg("商品的参数不能为空或者商品规格参数不能为空或者商品图片信息不能为空");
        }
        // todo 做些关键参数的校验
        ItemsParamBO itemsParamBO = itemBO.getItemsParamBO();
        if (StringUtils.isBlank(itemsParamBO.getBrand())){
            return ResponseResult.errorMsg("商品的商标信息不能为空");
        }
        if (StringUtils.isBlank(itemsParamBO.getFactoryAddress())){
            return ResponseResult.errorMsg("商家厂址信息不能为空");
        }
        if (StringUtils.isBlank(itemsParamBO.getFactoryName())){
            return ResponseResult.errorMsg("厂家名称不能为空");
        }
        if (StringUtils.isBlank(itemsParamBO.getStorageMethod())){
            return ResponseResult.errorMsg("存储方式不能为空");
        }
        if (StringUtils.isBlank(itemsParamBO.getFootPeriod())){
            return ResponseResult.errorMsg("食品的保质期不能为空");
        }
        List<ItemsSpecBO> specBOList = itemBO.getItemsSpecBOList();
        for (ItemsSpecBO itemsSpecBO: specBOList) {
            if (StringUtils.isBlank(itemsSpecBO.getName())){
                return ResponseResult.errorMsg("商品规格名称不能为空");
            }
            if (itemsSpecBO.getStock() == null){
                return ResponseResult.errorMsg("商品库存不能为空");
            }
            if (itemsSpecBO.getPriceNormal() == null){
                return ResponseResult.errorMsg("商品的价格不能为空");
            }
        }
        itemService.addItems(itemBO);
        return ResponseResult.ok();
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