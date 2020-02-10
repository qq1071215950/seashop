package com.haojing.controller.center;

import com.haojing.bo.center.OrderItemsCommentBO;
import com.haojing.controller.BaseController;
import com.haojing.entity.OrderItems;
import com.haojing.entity.Orders;
import com.haojing.enums.YesOrNo;
import com.haojing.result.PagedGridResult;
import com.haojing.result.ResponseResult;
import com.haojing.service.center.MyCommentsService;
import com.haojing.service.center.MyOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "用户中心评价模块", tags = {"用户中心评价模块相关接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @Autowired
    public MyOrdersService myOrdersService;


    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "GET")
    @GetMapping("/pending")
    public ResponseResult pending(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        // 判断用户和订单是否关联
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return ResponseResult.errorMsg("订单不存在！");
        }
        // 判断该笔订单是否已经评价过，评价过了就不再继续
        if (order.getIsComment() == YesOrNo.YES.type) {
            return ResponseResult.errorMsg("该笔订单已经评价");
        }
        List<OrderItems> list = myCommentsService.queryPendingComment(orderId);
        if (CollectionUtils.isEmpty(list)){
            return ResponseResult.ok("订单不存在");
        }
        return ResponseResult.ok(list);
    }


    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public ResponseResult saveList(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {

        System.out.println(commentList);

        // 判断用户和订单是否关联
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return ResponseResult.errorMsg("订单不存在！");
        }
        // 判断评论内容list不能为空
        if (commentList == null || commentList.isEmpty() || commentList.size() == 0) {
            return ResponseResult.errorMsg("评论内容不能为空！");
        }
        myCommentsService.saveComments(orderId, userId, commentList);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "查询我的评价", notes = "查询我的评价", httpMethod = "GET")
    @GetMapping("/query")
    public ResponseResult query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return ResponseResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = myCommentsService.queryMyComments(userId, page, pageSize);
        if (CollectionUtils.isEmpty(grid.getRows())){
            return ResponseResult.ok("您目前暂时还没有评论");
        }
        return ResponseResult.ok(grid);
    }
}
