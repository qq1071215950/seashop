package com.haojing.controller.admin;

import com.haojing.bo.CarouselBO;
import com.haojing.result.ResponseResult;
import com.haojing.service.CarouselService;
import com.haojing.service.CategoryService;
import com.haojing.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "首页轮播图", tags = {"管理员操作首页轮播图管理"})
@RestController
@RequestMapping("carousle")
@CrossOrigin
public class AdminCarouselController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;


    @ApiOperation(value = "添加首页轮播", notes = "添加首页轮播对象", httpMethod = "POST")
    @PostMapping("/add")
    public ResponseResult addCarousel(@RequestBody CarouselBO carouselBO) {
        if (StringUtils.isBlank(carouselBO.getImageUrl()) || StringUtils.isBlank(carouselBO.getItemId())
                || StringUtils.isBlank(carouselBO.getCatId())) {
            return ResponseResult.errorMsg("轮播信息的图片或者所属商品或者所属分类为空");
        }
        if (carouselBO.getIsShow() == null) {
            carouselBO.setIsShow(1);
        }
        if (carouselBO.getType() == null) {
            carouselBO.setType(4);
        }
        Boolean flag1 = categoryService.queryCategoryIsExit(carouselBO.getCatId());
        if (!flag1){
            return ResponseResult.errorMsg("分类信息不存在");
        }
        Boolean flag2 = itemService.queryItemIsExit(carouselBO.getItemId());
        if (!flag2){
            return ResponseResult.errorMsg("商品信息不存在");
        }
        carouselService.addCarousel(carouselBO);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "删除首页轮播", notes = "删除首页轮播", httpMethod = "PUT")
    @PutMapping("/delete")
    public ResponseResult deleteCarousel(@RequestParam String carouselId) {
        if (StringUtils.isBlank(carouselId)){
            return ResponseResult.errorMsg("请选择分类");
        }
        carouselService.deleteCarouselById(carouselId);
        return ResponseResult.ok();
    }
}
