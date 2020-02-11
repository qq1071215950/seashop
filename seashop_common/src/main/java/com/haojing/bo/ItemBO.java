package com.haojing.bo;

import lombok.Data;

import java.util.List;

@Data
public class ItemBO {

    /**
     * 商品名称 商品名称
     */
    private String itemName;

    /**
     * 分类外键id 分类id
     */
    private Integer catId;

    /**
     * 一级分类外键id
     */
    private Integer rootCatId;

    /**
     * 上下架状态 上下架状态,1:上架 2:下架
     */
    private Integer onOffStatus;

    /**
     * 商品内容 商品内容
     */
    private String content;

    /**
     * 商品图片
     */
    private List<ItemsImgBO> imgBOList;

    /**
     * 商品参数对象
     */
    private ItemsParamBO itemsParamBO;

    /**
     * 商品规格对象
     */
    private List<ItemsSpecBO> itemsSpecBOList;

}
