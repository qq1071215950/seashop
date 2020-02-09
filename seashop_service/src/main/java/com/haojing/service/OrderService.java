package com.haojing.service;

import com.haojing.bo.SubmitOrderBO;
import com.haojing.entity.OrderStatus;
import com.haojing.vo.OrderVO;

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
}
