package com.marketing.system.entity;

public class Menu {
    private Integer id;

    private String name;

    private String url;

    private Integer groupid;

    private Integer px;

    private Integer parentid;

    private String scriptid;

    private Integer systemid;

    private String isButton;

    public String getIsButton() {
        return isButton;
    }

    public void setIsButton(String isButton) {
        this.isButton = isButton;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getPx() {
        return px;
    }

    public void setPx(Integer px) {
        this.px = px;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getScriptid() {
        return scriptid;
    }

    public void setScriptid(String scriptid) {
        this.scriptid = scriptid == null ? null : scriptid.trim();
    }

    public Integer getSystemid() {
        return systemid;
    }

    public void setSystemid(Integer systemid) {
        this.systemid = systemid;
    }
}