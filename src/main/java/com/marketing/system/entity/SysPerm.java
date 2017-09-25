package com.marketing.system.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@TableName("SysPerm")
@ApiModel( description = "系统用户权限")
public class SysPerm {
    private Integer id;

    private String interfaceurl;

    private String name;

    private String permissioninit;

    private Date addtime;

    private Integer mid;

    private String adduser;

    @ApiModelProperty(notes = "主键id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ApiModelProperty(notes = "接口地址")
    public String getInterfaceurl() {
        return interfaceurl;
    }

    public void setInterfaceurl(String interfaceurl) {
        this.interfaceurl = interfaceurl;
    }

    @ApiModelProperty(notes = "接口名称")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(notes = "接口权限")
    public String getPermissioninit() {
        return permissioninit;
    }

    public void setPermissioninit(String permissioninit) {
        this.permissioninit = permissioninit;
    }

    @ApiModelProperty(notes = "添加时间")
    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @ApiModelProperty(notes = "菜单id")
    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    @ApiModelProperty(notes = "添加人")
    public String getAdduser() {
        return adduser;
    }

    public void setAdduser(String adduser) {
        this.adduser = adduser;
    }
}