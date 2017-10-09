package com.marketing.system.entity;

public class Group {
    private Integer id;

    private Integer departmentid;

    private Integer squadid;

    private String squad;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(Integer departmentid) {
        this.departmentid = departmentid;
    }

    public Integer getSquadid() {
        return squadid;
    }

    public void setSquadid(Integer squadid) {
        this.squadid = squadid;
    }

    public String getSquad() {
        return squad;
    }

    public void setSquad(String squad) {
        this.squad = squad == null ? null : squad.trim();
    }
}