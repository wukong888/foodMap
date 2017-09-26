package com.marketing.system.entity;

public class ProjectTask {
    private Integer id;

    private Integer proid;

    private Integer squadid;

    private String taskname;

    private String sdate;

    private String edate;

    private String taskstate;

    private String taskprogress;

    private Integer taskid;

    private Integer tasklogrecordid;

    private Integer taskdeveloplogid;

    private String handler;

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProid() {
        return proid;
    }

    public void setProid(Integer proid) {
        this.proid = proid;
    }

    public Integer getSquadid() {
        return squadid;
    }

    public void setSquadid(Integer squadid) {
        this.squadid = squadid;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname == null ? null : taskname.trim();
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate == null ? null : sdate.trim();
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate == null ? null : edate.trim();
    }

    public String getTaskstate() {
        return taskstate;
    }

    public void setTaskstate(String taskstate) {
        this.taskstate = taskstate == null ? null : taskstate.trim();
    }

    public String getTaskprogress() {
        return taskprogress;
    }

    public void setTaskprogress(String taskprogress) {
        this.taskprogress = taskprogress == null ? null : taskprogress.trim();
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public Integer getTasklogrecordid() {
        return tasklogrecordid;
    }

    public void setTasklogrecordid(Integer tasklogrecordid) {
        this.tasklogrecordid = tasklogrecordid;
    }

    public Integer getTaskdeveloplogid() {
        return taskdeveloplogid;
    }

    public void setTaskdeveloplogid(Integer taskdeveloplogid) {
        this.taskdeveloplogid = taskdeveloplogid;
    }
}