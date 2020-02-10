package com.haojing.service.center;

import com.haojing.entity.Orders;
import com.haojing.result.PagedGridResult;
import com.haojing.vo.OrderStatusCountsVO;

public interface MyOrdersService {

    /**
     * 查询用户和订单的关系
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);


    /**
     * 查询我的订单数
     * @param userId
     * @return
     */
    OrderStatusCountsVO getOrderStatusCounts(String userId);


    /**
     * 根据订单状态查询订单列表
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);


    /**
     *订单发货
     * @param orderId
     */
    void updateDeliverOrderStatus(String orderId);


    /**
     * 用户确认收货
     * @param orderId
     * @return
     */
    boolean updateReceiveOrderStatus(String orderId);


    /**
     * 用户删除订单
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId, String orderId);


    /**
     * 查询各订单信息
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize);
}
