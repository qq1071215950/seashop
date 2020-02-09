package com.haojing.service;

import com.haojing.domain.ItemsCustom;
import com.haojing.domain.ItemsImgCustom;
import com.haojing.domain.ItemsParamCustom;
import com.haojing.domain.ItemsSpecCustom;
import com.haojing.entity.Items;
import com.haojing.entity.ItemsImg;
import com.haojing.entity.ItemsParam;
import com.haojing.entity.ItemsSpec;
import com.haojing.result.PagedGridResult;
import com.haojing.vo.CommentLevelCountsVO;
import com.haojing.vo.ShopcartVO;

import java.util.List;

public interface ItemService {

    /**
     * 通过商品id查询商品详情
     * @param itemId
     * @return
     */
    ItemsCustom queryItemById(String itemId);


    /**
     * 查询商品的图片信息
     * @param itemId
     * @return
     */
    List<ItemsImgCustom> queryItemImgList(String itemId);


    /**
     * 查询商品规格数据
     * @param itemId
     * @return
     */
    List<ItemsSpecCustom> queryItemSpecList(String itemId);


    /**
     * 查询商品参数
     * @param itemId
     * @return
     */
    ItemsParamCustom queryItemParam(String itemId);


    /**
     * 查询商品评论数量
     * @param itemId
     * @return
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);


    /**
     * 分页查询商品评论信息
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);


    /**
     * 商品搜索
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searhItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 根据分类Id查询商品信息
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searhItems(Integer catId, String sort, Integer page, Integer pageSize);


    /**
     *查找商品的信息
     * @param itemSpecIds
     * @return
     */
    List<ShopcartVO> queryItemsBySpecIds(String itemSpecIds);
}
