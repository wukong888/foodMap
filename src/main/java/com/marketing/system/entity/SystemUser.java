package com.marketing.system.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("SystemUser")
@ApiModel( description = "系统用户信息")
public class SystemUser {

    private int id;

    private String UserName;

    private String Password;

    private String Department;

    private String UserGroup;

    private String WorkWeixinId;

    private String mobile;

    private String email;

    private String duty;

    private String isAdmin;

    private int UserGroupId;

    private String job;

    @ApiModelProperty(notes = "用户ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ApiModelProperty(notes = "用户姓名")
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @ApiModelProperty(notes = "用户密码")
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @ApiModelProperty(notes = "部门")
    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    @ApiModelProperty(notes = "部门小组")
    public String getUserGroup() {
        return UserGroup;
    }

    public void setUserGroup(String userGroup) {
        UserGroup = userGroup;
    }

    @ApiModelProperty(notes = "微信工作名称")
    public String getWorkWeixinId() {
        return WorkWeixinId;
    }

    public void setWorkWeixinId(String workWeixinId) {
        WorkWeixinId = workWeixinId;
    }

    @ApiModelProperty(notes = "手机号码")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @ApiModelProperty(notes = "电子邮箱")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ApiModelProperty(notes = "岗位")
    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    @ApiModelProperty(notes = "超级管理员 1：是 0：否")
    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    @ApiModelProperty(notes = "部门小组id")
    public int getUserGroupId() {
        return UserGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        UserGroupId = userGroupId;
    }

    @ApiModelProperty(notes = "用户ID")
    public String getJob() {
        return job;
    }
    @ApiModelProperty(notes = "job")
    public void setJob(String job) {
        this.job = job;
    }
}
