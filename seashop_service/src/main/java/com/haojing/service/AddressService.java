package com.haojing.service;

import com.haojing.bo.AddressBO;
import com.haojing.entity.UserAddress;

import java.util.List;

public interface AddressService {

    /**
     * 查询地址列表
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 添加地址
     * @param addressBO
     */
    void addNewUserAddress(AddressBO addressBO);


    /**
     * 更新地址
     * @param addressBO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 删除地址
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId, String addressId);


    /**
     * 将地址设置为默认地址
     * @param userId
     * @param addressId
     */
    void updateUserAddressToBeDefault(String userId, String addressId);


    UserAddress queryUserAddres(String userId, String addressId);
}
