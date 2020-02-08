package com.haojing.service;

import com.haojing.bo.UserBO;
import com.haojing.entity.Users;

public interface UserService {

    /**
     * 查看用户是否已经存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 用户注册
     * @param userBO
     * @return
     */
    Users createUser(UserBO userBO);


    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    Users queryUserForLogin(String username, String password) throws Exception;


    Users selectById(String userId);
}
