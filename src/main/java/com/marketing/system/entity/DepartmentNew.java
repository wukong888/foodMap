package com.marketing.system.entity;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

public class DepartmentNew {
    private Integer id;

    private String deptno;

    private String department;

    private Integer no;

    private Integer pid;

    private Boolean workorderuse;

    private Integer  _parentId;

    private List<DepartmentNew> children = new ArrayList<>();

    public Integer get_parentId() {
        return _parentId;
    }

    public void set_parentId(Integer _parentId) {
        this._parentId = _parentId;
    }

    public List<DepartmentNew> getChildren() {
        return children;
    }

    public void setChildren(List<DepartmentNew> children) {
        this.children = children;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno == null ? null : deptno.trim();
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Boolean getWorkorderuse() {
        return workorderuse;
    }

    public void setWorkorderuse(Boolean workorderuse) {
        this.workorderuse = workorderuse;
    }


}