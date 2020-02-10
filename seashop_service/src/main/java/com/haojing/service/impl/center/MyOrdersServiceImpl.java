package com.haojing.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haojing.entity.OrderStatus;
import com.haojing.entity.Orders;
import com.haojing.enums.OrderStatusEnum;
import com.haojing.enums.YesOrNo;
import com.haojing.mapper.OrderStatusMapper;
import com.haojing.mapper.OrdersMapper;
import com.haojing.mapper.OrdersMapperCustom;
import com.haojing.result.PagedGridResult;
import com.haojing.service.center.MyOrdersService;
import com.haojing.vo.MyOrdersVO;
import com.haojing.vo.OrderStatusCountsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.NO.type);
        return ordersMapper.selectOne(orders);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNo.NO.type);
        int waitCommentCounts = ordersMapperCustom.getMyOrderStatusCounts(map);
        OrderStatusCountsVO countsVO = new OrderStatusCountsVO(waitPayCounts, waitDeliverCounts, waitReceiveCounts, waitCommentCounts);
        return countsVO;
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }
        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(map);
        return setterPagedGrid(list, page);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {
        // 先查订单的状态
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        if (orderStatus == null || orderStatus.getOrderStatus() == null || orderStatus.getOrderStatus() != 20){
            throw new RuntimeException("订单未付款");
        }
        OrderStatus updateOrder = new OrderStatus();
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());
        orderStatusMapper.updateByExampleSelective(updateOrder, example);
    }


    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        // 先查订单的状态
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        if (orderStatus == null || orderStatus.getOrderStatus() == null ||
                orderStatus.getOrderStatus() != 30){
            throw new RuntimeException("订单未发货");
        }
        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        updateOrder.setSuccessTime(new Date());
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int result = orderStatusMapper.updateByExampleSelective(updateOrder, example);
        return result == 1 ? true : false;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {
        Orders updateOrder = new Orders();
        updateOrder.setIsDelete(YesOrNo.YES.type);
        updateOrder.setUpdatedTime(new Date());
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", orderId);
        criteria.andEqualTo("userId", userId);
        int result = ordersMapper.updateByExampleSelective(updateOrder, example);
        return result == 1 ? true : false;
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        PageHelper.startPage(page, pageSize);
        List<OrderStatus> list = ordersMapperCustom.getMyOrderTrend(map);
        return setterPagedGrid(list, page);
    }

    private PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
   }
}
