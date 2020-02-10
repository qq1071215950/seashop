package com.haojing.service.center;

import com.haojing.bo.center.OrderItemsCommentBO;
import com.haojing.entity.OrderItems;
import com.haojing.result.PagedGridResult;

import java.util.List;

public interface MyCommentsService {

    /**
     * 查询子订单
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);


    /**
     * 用户订单评论
     * @param orderId
     * @param userId
     * @param commentList
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);


    /**
     * 查询我的评论
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
