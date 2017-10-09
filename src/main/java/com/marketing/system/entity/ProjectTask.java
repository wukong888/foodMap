package com.marketing.system.entity;

public class ProjectTask {
    private Integer taskId;

    private Integer proid;

    private String squadId;

    private String taskname;

    private String sdate;

    private String edate;

    private String taskstate;

    private String taskprogress;

    private Integer tasklogrecordid;

    private Integer taskdeveloplogid;

    private String handler;

    private String workDate;

    /**
     * 距离逾期天数
     */
    private long betweenDays;

    private Integer idd;

    private String createDate;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    public long getBetweenDays() {
        return betweenDays;
    }

    public void setBetweenDays(long betweenDays) {
        this.betweenDays = betweenDays;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Integer getProid() {
        return proid;
    }

    public void setProid(Integer proid) {
        this.proid = proid;
    }

    public String getSquadId() {
        return squadId;
    }

    public void setSquadId(String squadId) {
        this.squadId = squadId;
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

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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