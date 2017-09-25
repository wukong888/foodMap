package com.marketing.system.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@TableName("t_user")
@ApiModel( description = "用户信息")
public class UserInfo {
    private Long id;

    private String uuid;

    public UserInfo() {
    }

    public UserInfo(Long id, String uuid, String loginName, String loginPwd, Date loginpwdModifyTime, Date registTime, String registerClient, String tradePwd, Date tradepwdModifyTime, String invitationCode, Long channelId, String level, Date loginTime) {
        this.id = id;
        this.uuid = uuid;

        this.loginName = loginName;
        this.loginPwd = loginPwd;
        this.loginpwdModifyTime = loginpwdModifyTime;
        this.registTime = registTime;
        this.registerClient = registerClient;
        this.tradePwd = tradePwd;
        this.tradepwdModifyTime = tradepwdModifyTime;
        this.invitationCode = invitationCode;
        this.channelId = channelId;
        this.level = level;
        this.loginTime = loginTime;
    }

    private String loginName;

    private String loginPwd;

    private Date loginpwdModifyTime;

    private Date registTime;

    private String registerClient;

    private String tradePwd;

    private Date tradepwdModifyTime;

    private String invitationCode;

    private Long channelId;

    private String level;

    private Date loginTime;

    @ApiModelProperty(notes = "用户ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(notes = "uuid",required = true)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    @ApiModelProperty(notes = "用户名",required =true)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @ApiModelProperty(notes = "密码",required = true)
    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd == null ? null : loginPwd.trim();
    }

    @ApiModelProperty(notes = "密码设置时间",required = true)
    public Date getLoginpwdModifyTime() {
        return loginpwdModifyTime;
    }

    public void setLoginpwdModifyTime(Date loginpwdModifyTime) {
        this.loginpwdModifyTime = loginpwdModifyTime;
    }

    @ApiModelProperty(notes = "注册时间",required = true)
    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    @ApiModelProperty(notes = "注册客户端",required = true)
    public String getRegisterClient() {
        return registerClient;
    }

    public void setRegisterClient(String registerClient) {
        this.registerClient = registerClient == null ? null : registerClient.trim();
    }

    @ApiModelProperty(notes = "交易密码",required = true)
    public String getTradePwd() {
        return tradePwd;
    }

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd == null ? null : tradePwd.trim();
    }

    @ApiModelProperty(notes = "上次交易密码修改时间",required = true)
    public Date getTradepwdModifyTime() {
        return tradepwdModifyTime;
    }

    public void setTradepwdModifyTime(Date tradepwdModifyTime) {
        this.tradepwdModifyTime = tradepwdModifyTime;
    }

    @ApiModelProperty(notes = "邀请码",required = true)
    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode == null ? null : invitationCode.trim();
    }

    @ApiModelProperty(notes = "渠道",required = true)
    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    @ApiModelProperty(notes = "代理等级 ，1一级，2二级，3普通用户",required = true)
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    @ApiModelProperty(notes = "登录时间",required = true)
    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}