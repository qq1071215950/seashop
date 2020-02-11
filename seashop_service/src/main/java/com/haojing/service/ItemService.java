package com.haojing.service;

import com.haojing.bo.ItemBO;
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


    /**
     * 根据规格Id查询规格对象
     * @param itemSpecId
     * @return
     */
    ItemsSpec queryItemSpecById(String itemSpecId);


    /**
     * 根据商品Id查询商品的主图
     * @param itemId
     * @return
     */
    String queryItemMainImgById(String itemId);


    /**
     * 减库存
     * @param itemSpecId
     * @param buyCounts
     */
    void decreaseItemSpecStock(String itemSpecId, int buyCounts);


    /**
     * 商品的上下架
     * @param itemIds
     * @param type
     */
    void upOrDownItems(List<String> itemIds, Integer type);


    /**
     * 查询商品是否存在
     * @param itemId
     * @return
     */
    Boolean queryItemIsExit(String itemId);


    /**
     * 添加商品
     * @param itemBO
     */
    void addItems(ItemBO itemBO);
}
