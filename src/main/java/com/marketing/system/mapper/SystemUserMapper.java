package com.marketing.system.mapper;


import com.marketing.system.entity.SystemUser;

import java.util.List;
import java.util.Map;

public interface SystemUserMapper {

    List<SystemUser> selectByMap(Map<String,Object> map);

    boolean updateById(SystemUser userInfo);

    SystemUser selectByPrimaryKey(int id);
}
