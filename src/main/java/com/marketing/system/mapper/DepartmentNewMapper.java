package com.marketing.system.mapper;

import com.marketing.system.entity.DepartmentNew;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.rmi.MarshalledObject;
import java.util.List;
import java.util.Map;

public interface DepartmentNewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DepartmentNew record);

    int insertSelective(DepartmentNew record);

    DepartmentNew selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DepartmentNew record);

    int updateByPrimaryKey(DepartmentNew record);

    //获取中心 一级
    List<DepartmentNew> getDepartment();

    //获取部门 二级
    List<DepartmentNew> getGroupNo();

    //根据id获取部门 二级
    List<DepartmentNew> getGroup(Integer id);

    //项目涉及部门
    List<Map<String,Object>> groupList(Map<String,Object> map);

    /**
     * 判断是总监、经理、组员
     * listDuty = 1 经理
     * listDuty > 1 总监
     * listDuty 为空 组员
     */
    List<Map<String,Object>> getCheckDuty(Integer userGroupId);

    //总监
    List<Map<String,Object>> getZjMember(Integer userGroupId);

    //经理
    List<Map<String,Object>> getJlMember(Integer userGroupId);

    //组员
    List<Map<String,Object>> getMemMember(Integer userGroupId);

    //经理 总监
    List<Map<String,Object>> getGmMember();

    //根据Id查询小组名称
    DepartmentNew getDeptnoBySquadId(Integer Id);

    //根据部门Id查部门-new
    @Select("SELECT Deptno FROM Department_new where id =#{squadId}")
    String selectSquadByIdNew(@Param("squadId")String squadId);

    //根据小组id查找部门id-new
    @Select("select Pid from Department_new where id=#{squadId}")
    String selectDepartmentIdBySquadIdNew(@Param("squadId")String squadId);

    //根据姓名查找对应部门
    String getGroupByCreater(String UserName);

}