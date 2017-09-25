package com.marketing.system.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@TableName("RoleMenus")
@ApiModel( description = "角色菜单表")
public class RoleMenus {
    private Integer Rid;

    private Integer Mid;

    @ApiModelProperty(notes = "角色id")
    public Integer getRid() {
        return Rid;
    }

    public void setRid(Integer Rid) {
        this.Rid = Rid;
    }

    @ApiModelProperty(notes = "菜单id")
    public Integer getMid() {
        return Mid;
    }

    public void setMid(Integer Mid) {
        this.Mid = Mid;
    }
}