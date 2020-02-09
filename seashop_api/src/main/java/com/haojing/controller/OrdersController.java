package com.haojing.controller;

import com.haojing.bo.SubmitOrderBO;
import com.haojing.entity.OrderStatus;
import com.haojing.enums.OrderStatusEnum;
import com.haojing.enums.PayMethod;
import com.haojing.result.ResponseResult;
import com.haojing.service.OrderService;
import com.haojing.vo.MerchantOrdersVO;
import com.haojing.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController {
    @Autowired
    private OrderService orderService;
    

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public ResponseResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type ) {
            return ResponseResult.errorMsg("支付方式不支持！");
        }
        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();
        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        //       CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();

        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
        merchantOrdersVO.setAmount(1);
        // todo 向支付中心支付
        return ResponseResult.ok("订单创建成功",orderId);
    }

    // 异步回调通知 支付成功后修改订单的状态
    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    // 查看订单状态
    @PostMapping("getPaidOrderInfo")
    public ResponseResult getPaidOrderInfo(@RequestParam String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return ResponseResult.ok("ok",orderStatus);
    }
}
