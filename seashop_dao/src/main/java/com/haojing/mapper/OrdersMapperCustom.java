package com.haojing.mapper;

import com.haojing.entity.OrderStatus;
import com.haojing.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {

    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

    public int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);

    public List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);


    /**
     * 查询相关订单列表
     * @param map
     * @return
     */
    List<MyOrdersVO> queryOrders(@Param("paramsMap") Map<String, Object> map);
}
