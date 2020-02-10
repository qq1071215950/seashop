package com.haojing.service.center;

import com.haojing.bo.center.CenterUserBO;
import com.haojing.entity.Users;

public interface CenterUserService {

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);


    /**
     * 修改用户中心的个人信息
     * @param userId
     * @param centerUserBO
     * @return
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);


    /**
     * 用户头像信息
     * @param userId
     * @param faceUrl
     * @return
     */
    Users updateUserFace(String userId, String faceUrl);
}
