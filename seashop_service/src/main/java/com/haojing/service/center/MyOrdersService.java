package com.haojing.service.center;

import com.haojing.entity.Orders;

public interface MyOrdersService {

    /**
     * 查询用户和订单的关系
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);
}
