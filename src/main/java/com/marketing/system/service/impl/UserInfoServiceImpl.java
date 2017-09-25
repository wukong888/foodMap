package com.marketing.system.service.impl;

import com.marketing.system.entity.UserInfo;
import com.marketing.system.mapper.UserInfoMapper;
import com.marketing.system.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> selectByMap(Map<String, Object> map) {

        return userInfoMapper.selectByMap(map);
    }

    @Override
    public boolean updateById(UserInfo userInfo) {
        boolean i = userInfoMapper.updateById(userInfo);
        return i;
    }

    @Override
    public UserInfo selectByPrimaryKey(Long id) {

        return userInfoMapper.selectByPrimaryKey(id);
    }
}
