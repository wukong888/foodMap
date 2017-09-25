package com.marketing.system.mapper;

import com.marketing.system.entity.UserInfo;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Map;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    List<UserInfo> selectByMap(Map<String,Object> map);

    boolean updateById(UserInfo userInfo);

}