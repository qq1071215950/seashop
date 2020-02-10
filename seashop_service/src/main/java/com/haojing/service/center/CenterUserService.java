package com.haojing.service.center;

import com.haojing.entity.Users;

public interface CenterUserService {

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);
}
