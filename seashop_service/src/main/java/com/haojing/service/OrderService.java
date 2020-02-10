package com.haojing.service;

import com.haojing.bo.SubmitOrderBO;
import com.haojing.entity.OrderStatus;
import com.haojing.result.PagedGridResult;
import com.haojing.vo.OrderVO;

import java.util.List;

public interface OrderService {

    /**
     * 创建订单
     * @param submitOrderBO
     * @return
     */
    OrderVO createOrder(SubmitOrderBO submitOrderBO);


    /**
     *更新订单状态
     * @param merchantOrderId
     * @param type
     */
    void updateOrderStatus(String merchantOrderId, Integer type);


    /**
     * 根据订单id查询订单详情
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);


    /**
     * 商家批量发货
     * @param orderIds
     */
    void deliverOrderPacth(List<String> orderIds);


    /**
     * 分页条件查询订单列表
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryOrders(Integer orderStatus, Integer page, Integer pageSize);
}
