package com.haojing.service.impl.center;

import com.haojing.bo.center.CenterUserBO;
import com.haojing.entity.Users;
import com.haojing.mapper.UsersMapper;
import com.haojing.service.center.CenterUserService;
import com.haojing.utlis.MD5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        if (users != null){
            users.setPassword(null);
        }
        return  users;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUserBO, updateUser);
        updateUser.setId(userId);
        updateUser.setUpdatedTime(new Date());
        try {
            // 密码进行加密处理
            updateUser.setPassword(MD5Utils.getMD5Str(updateUser.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users updateUser = new Users();
        updateUser.setId(userId);
        updateUser.setFace(faceUrl);
        updateUser.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(userId);
    }
}
