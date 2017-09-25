package com.marketing.system.service;

import com.marketing.system.entity.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoService {

    List<UserInfo> selectByMap(Map<String,Object> map);

    boolean updateById(UserInfo userInfo);

    UserInfo selectByPrimaryKey(Long id);


}
