package com.marketing.system.service.impl;

import com.marketing.system.entity.Department;
import com.marketing.system.mapper_two.DepartmentMapper;
import com.marketing.system.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getDepartment() {

        return departmentMapper.getDepartment();
    }
}
