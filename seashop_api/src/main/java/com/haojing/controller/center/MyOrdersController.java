package com.haojing.controller.center;

import com.haojing.controller.BaseController;
import com.haojing.entity.Orders;
import com.haojing.result.PagedGridResult;
import com.haojing.result.ResponseResult;
import com.haojing.service.center.MyOrdersService;
import com.haojing.vo.OrderStatusCountsVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value = "用户中心我的订单", tags = {"用户中心我的订单相关接口"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {
    @Autowired
    private MyOrdersService myOrdersService;
    @Autowired
    private HttpServletRequest request;

    @ApiOperation(value = "获得订单状态数概况", notes = "获得订单状态数概况", httpMethod = "GET")
    @GetMapping("/statusCounts")
    public ResponseResult statusCounts() {

        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.errorMsg(null);
        }
        OrderStatusCountsVO result = myOrdersService.getOrderStatusCounts(userId);
        return ResponseResult.ok(result);
    }

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "GET")
    @GetMapping("/query")
    public ResponseResult query(
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return ResponseResult.ok(grid);
    }


    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value="商家发货", notes="商家发货", httpMethod = "POST")
    @PostMapping("/deliver")
    public ResponseResult deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) throws Exception {
        if (StringUtils.isBlank(orderId)) {
            return ResponseResult.errorMsg("订单ID不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return ResponseResult.ok();
    }


    @ApiOperation(value="用户确认收货", notes="用户确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public ResponseResult confirmReceive(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) throws Exception {

        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();

        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return ResponseResult.errorMsg("订单不存在！");
        }
        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res) {
            return ResponseResult.errorMsg("订单确认收货失败！");
        }
        return ResponseResult.ok();
    }

    @ApiOperation(value="用户删除订单", notes="用户删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public ResponseResult delete(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) throws Exception {

        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return ResponseResult.errorMsg("订单不存在！");
        }

        boolean res = myOrdersService.deleteOrder(userId, orderId);
        if (!res) {
            return ResponseResult.errorMsg("订单删除失败！");
        }

        return ResponseResult.ok();
    }

    
    @ApiOperation(value = "查询订单动向", notes = "查询订单动向", httpMethod = "POST")
    @PostMapping("/trend")
    public ResponseResult trend(
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();

        if (StringUtils.isBlank(userId)) {
            return ResponseResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = myOrdersService.getOrdersTrend(userId, page, pageSize);
        if (grid == null || CollectionUtils.isEmpty(grid.getRows())){
            return ResponseResult.ok("您暂时还没有相关的订单信息");
        }
        return ResponseResult.ok(grid);
    }

}
