package com.marketing.system.mapper_two;

import com.marketing.system.entity.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

    List<Department> getDepartment();

    Department getDepartmentIdByMent(@Param("department")String department);

    List<Map<String,Object>> getDepartmentList(Map<String,Object> map);
}