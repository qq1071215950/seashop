package com.haojing.controller;

import com.haojing.entity.Carousel;
import com.haojing.entity.Category;
import com.haojing.enums.YesOrNo;
import com.haojing.result.ResponseResult;
import com.haojing.service.CarouselService;
import com.haojing.service.CategoryService;
import com.haojing.vo.CategoryVO;
import com.haojing.vo.NewItemsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
@CrossOrigin
public class IndexController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public ResponseResult carousel() {
        List<Carousel> list = carouselService.queryAll(YesOrNo.YES.type);
        return ResponseResult.ok(list);
    }

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类(一级分类)", notes = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("/cats")
    public ResponseResult cats() {
        List<Category> list = categoryService.queryAllRootLevelCat();
        return ResponseResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public ResponseResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return ResponseResult.errorMsg("分类不存在");
        }
        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
        if (CollectionUtils.isEmpty(list)){
            return ResponseResult.errorMsg("分类信息不存在");
        }
        return ResponseResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public ResponseResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return ResponseResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        if (CollectionUtils.isEmpty(list)){
            return ResponseResult.errorMsg("该分类下目前暂时没有商品信息，请进行其它的操作");
        }
        return ResponseResult.ok(list);
    }
}
