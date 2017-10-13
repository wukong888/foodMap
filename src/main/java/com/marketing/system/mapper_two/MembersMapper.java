package com.marketing.system.mapper_two;

import com.marketing.system.entity.Members;

import java.util.List;
import java.util.Map;

public interface MembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Members record);

    int insertSelective(Members record);

    Members selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Members record);

    int updateByPrimaryKey(Members record);

    List<Members> getMembersBysquadId(String squadId);

    List<Map<String,Object>> getMembers(Map<String,Object> map);
}