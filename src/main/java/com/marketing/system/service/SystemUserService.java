package com.marketing.system.service;

import com.marketing.system.entity.SystemUser;

import java.util.List;
import java.util.Map;

public interface SystemUserService {

    List<SystemUser> selectByMap(Map<String,Object> map);

    boolean updateById(SystemUser userInfo);

    SystemUser selectByPrimaryKey(int id);

    List<Map<String, Object>> selectManagerBydepartment(Map<String,Object> map);

    List<Map<String, Object>> selectUserGroupBydepartment(Map<String,Object> map);

    SystemUser selectIdByName(String UserName);

    //组长本部门小组成员
    List<Map<String, Object>> getGroupMembers(Map<String,Object> map);

    //经理本部门成员
    List<Map<String, Object>> getGroupMembersByManeger(Map<String,Object> map);

    List<Map<String,Object>> getMembersById();

    //获取对应组成员
    List<Map<String,Object>> getMembersByUserGroupId(Map<String,Object> map);
}
