package com.haojing.controller.admin;

import com.haojing.controller.BaseController;
import com.haojing.result.PagedGridResult;
import com.haojing.result.ResponseResult;
import com.haojing.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "订单管理", tags = {"管理员操作订单管理相关接口"})
@RestController
@RequestMapping("order")
@CrossOrigin
public class AdminOrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "商家发货", notes = "商家发货批量操作", httpMethod = "POST")
    @PostMapping("/deliver")
    public ResponseResult deliverOrders(
            @ApiParam(name = "orderIds", value = "订单ids", required = true)
            @RequestParam List<String> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)){
            return ResponseResult.errorMsg("订单ids不能为空");
        }
        orderService.deliverOrderPacth(orderIds);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "分页条件查询订单", notes = "分页查询订单", httpMethod = "POST")
    @PostMapping("/searchorder")
    public ResponseResult searchOrder(
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = orderService.queryOrders(orderStatus, page, pageSize);
        if (grid == null || CollectionUtils.isEmpty(grid.getRows())){
            return ResponseResult.errorMsg("目前还没有订单");
        }
        return ResponseResult.ok(grid);
    }
}
