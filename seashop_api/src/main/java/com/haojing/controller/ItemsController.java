package com.haojing.controller;

import com.haojing.domain.ItemsCustom;
import com.haojing.domain.ItemsImgCustom;
import com.haojing.domain.ItemsParamCustom;
import com.haojing.domain.ItemsSpecCustom;
import com.haojing.entity.Items;
import com.haojing.entity.ItemsImg;
import com.haojing.entity.ItemsParam;
import com.haojing.entity.ItemsSpec;
import com.haojing.result.PagedGridResult;
import com.haojing.result.ResponseResult;
import com.haojing.service.ItemService;
import com.haojing.vo.CommentLevelCountsVO;
import com.haojing.vo.ItemInfoVO;
import com.haojing.vo.ShopcartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
@CrossOrigin
public class ItemsController extends BaseController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public ResponseResult info(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return ResponseResult.errorMsg("商品id为空");
        }

        ItemsCustom item = itemService.queryItemById(itemId);
        List<ItemsImgCustom> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpecCustom> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParamCustom itemsParam = itemService.queryItemParam(itemId);
        // 构造商品vo对象
        if (CollectionUtils.isEmpty(itemImgList) && CollectionUtils.isEmpty(itemsSpecList) && item == null && itemsParam == null){
            return ResponseResult.ok("商品信息不存在");
        }
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return ResponseResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价等级数量", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public ResponseResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return ResponseResult.ok("商品id不能为空");
        }
        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);
        if (countsVO == null){
            return ResponseResult.ok("商品信息不存在");
        }
        return ResponseResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public ResponseResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = true)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = true)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return ResponseResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = itemService.queryPagedComments(itemId, level, page, pageSize);
        if (grid == null || grid.getRecords() == 0){
            return ResponseResult.ok("商品暂时还没有评论");
        }
        return ResponseResult.ok(grid);
    }

    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public ResponseResult search(
            @ApiParam(name = "keywords", value = "关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序")
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数")
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(keywords)) {
            return ResponseResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searhItems(keywords, sort, page, pageSize);
        if (grid == null || CollectionUtils.isEmpty(grid.getRows())){
            return ResponseResult.ok("您搜索的商品不存在");
        }
        return ResponseResult.ok(grid);

    }

    @ApiOperation(value = "通过分类id搜索商品列表", notes = "通过分类id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public ResponseResult catItems(
            @ApiParam(name = "catId", value = "三级分类id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (catId == null) {
            return ResponseResult.errorMsg("分类id不能为空");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searhItems(catId, sort, page, pageSize);
        if (grid == null || CollectionUtils.isEmpty(grid.getRows())){
            return ResponseResult.ok("商品不存在");
        }
        return ResponseResult.ok(grid);
    }

    // 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
    @ApiOperation(value = "根据商品规格ids查找最新的商品数据", notes = "根据商品规格ids查找最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public ResponseResult refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1003,1005")
            @RequestParam String itemSpecIds) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return ResponseResult.ok();
        }
        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);
        if (CollectionUtils.isEmpty(list)){
            return ResponseResult.errorMsg("商品规格信息出错");
        }
        return ResponseResult.ok(list);
    }
}
