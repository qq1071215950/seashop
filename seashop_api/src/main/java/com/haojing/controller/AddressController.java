package com.haojing.controller;

import com.haojing.bo.AddressBO;
import com.haojing.entity.UserAddress;
import com.haojing.entity.Users;
import com.haojing.result.ResponseResult;
import com.haojing.service.AddressService;
import com.haojing.service.UserService;
import com.haojing.utlis.MobileEmailUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "地址相关", tags = {"地址相关的api接口"})
@RequestMapping("address")
@RestController
@CrossOrigin
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作：
     * 1. 查询用户的所有收货地址列表
     * 2. 新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;

    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "GET")
    @GetMapping("/list")
    public ResponseResult list( ) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        Users users = userService.selectById(userId);
        List<UserAddress> list = addressService.queryAll(userId);
        if (CollectionUtils.isEmpty(list) || list.size() == 0){
            return ResponseResult.ok("地址信息为空");
        }
        return ResponseResult.ok(list);
    }

    @ApiOperation(value = "根据用户id和地址id查询收货地址详情", notes = "根据用户id和地址id查询收货地址", httpMethod = "GET")
    @GetMapping("/address")
    public ResponseResult getAddress( @RequestParam String addressId) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        Users users = userService.selectById(userId);
        if (users == null){
            return ResponseResult.errorMsg("用户不存在");
        }
        UserAddress address = addressService.queryUserAddres(userId, addressId);
        if (address == null){
            return ResponseResult.ok("地址信息为空");
        }
        return ResponseResult.ok(address);
    }
    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody AddressBO addressBO) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();

        ResponseResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }
        addressBO.setUserId(userId);
        addressService.addNewUserAddress(addressBO);

        return ResponseResult.ok();
    }

    private ResponseResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return ResponseResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return ResponseResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return ResponseResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return ResponseResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (isMobileOk) {
            return ResponseResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return ResponseResult.errorMsg("收货地址信息不能为空");
        }

        return ResponseResult.ok();
    }


    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody AddressBO addressBO) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return ResponseResult.errorMsg("修改地址错误：addressId不能为空");
        }

        ResponseResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }
        addressBO.setUserId(userId);
        addressService.updateUserAddress(addressBO);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseResult delete(
            @RequestParam String addressId) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ResponseResult.errorMsg("");
        }
        addressService.deleteUserAddress(userId, addressId);
        return ResponseResult.ok();
    }


    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public ResponseResult setDefalut(
            @RequestParam String addressId) {
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return ResponseResult.errorMsg("您还没有登录");
        }
        String userId = claims.getId();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ResponseResult.errorMsg("");
        }
        addressService.updateUserAddressToBeDefault(userId, addressId);
        return ResponseResult.ok();
    }
}
