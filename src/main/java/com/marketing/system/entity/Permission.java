package com.marketing.system.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("Permission")
@ApiModel( description = "系统用户拥有权限")
public class Permission {
    private Integer id;

    private String url;

    private String name;

    private Integer SystemId;

    @ApiModelProperty(notes = "主键id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ApiModelProperty(notes = "地址")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ApiModelProperty(notes = "权限名称")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(notes = "主键id")
    public Integer getSystemId() {
        return SystemId;
    }

    public void setSystemId(Integer systemid) {
        this.SystemId = SystemId;
    }
}