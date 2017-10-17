package com.marketing.system.service;

import com.marketing.system.entity.*;

import java.util.List;
import java.util.Map;

public interface ApplyService {

    int insertApplyProject(ProjectInfo projectInfo);

    int selectMaxProId();

    int insertSelective(ProjectTask task);

    int insertProLogRecord(ProLogRecord proLogRecord);

    List<String> getCreaterByName(String creater);

    //根据项目id查询子任务负责人
    List<Map<String, Object>> selectProSubtaskByProId(int proId);

    //根据负责人查找squadId(小组id)
    List<Map<String, Object>> getSquadIdList(Map<String,Object> map);

    //根据squadId(小组id)查找小组所属部门id
    List<Map<String, Object>> getSquadList(Map<String,Object> map);

    //根据部门id查找部门名称
    List<Map<String, Object>> getDepartmentList(Map<String,Object> map);

    Members selectSquadIdByMember(String member);

    //根据项目id查询任务负责人
    List<Map<String, Object>> selecttaskInfoByProId(int proId);
}
