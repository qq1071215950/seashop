package com.haojing.bo;

import lombok.Data;

/**
 * 购物车对象
 */
@Data
public class ShopcartBO {

    /**
     * 商品id
     */
    private String itemId;
    /**
     * 商品图片地址
     */
    private String itemImgUrl;
    /**
     * 商品名称
     */
    private String itemName;
    /**
     * 规格id
     */
    private String specId;
    /**
     * 规格名称
     */
    private String specName;
    /**
     * 购买数量
     */
    private Integer buyCounts;
    /**
     * 折扣价格
     */
    private String priceDiscount;
    /**
     * 售价
     */
    private String priceNormal;
}
