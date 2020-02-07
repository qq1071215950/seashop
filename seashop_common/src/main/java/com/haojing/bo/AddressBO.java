package com.haojing.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 地址对象
 */
@Setter
@Getter
public class AddressBO {
    /**
     * 地址id
     */
    private String addressId;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 收货人
     */
    private String receiver;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 街道
     */
    private String district;
    /**
     * 详情
     */
    private String detail;

}
