package com.haojing.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品规格对象
 */
@Data
public class ItemsSpecBO implements Serializable {

    private static final long serialVersionUID = -5668696537572029401L;
    /**
     * 规格名称
     */
    private String name;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 折扣力度
     */
    private BigDecimal discounts;

    /**
     * 优惠价
     */
    private Integer priceDiscount;

    /**
     * 原价
     */
    private Integer priceNormal;

}