package com.haojing.bo.center;

import lombok.Data;

/**
 * 订单评论对象
 */
@Data
public class OrderItemsCommentBO {
    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    /**
     * 评论等级
     */
    private Integer commentLevel;
    /**
     * 评论内容
     */
    private String content;

}