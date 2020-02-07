package com.haojing.bo;

import lombok.Data;

/**
 * 用于创建订单的BO对象
 */
@Data
public class SubmitOrderBO {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 商品规格ids
     */
    private String itemSpecIds;
    private String addressId;
    /**
     * 支付方式
     */
    private Integer payMethod;
    /**
     * 买家留言
     */
    private String leftMsg;
}
