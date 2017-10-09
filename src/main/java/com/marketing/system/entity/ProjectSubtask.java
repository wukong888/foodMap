package com.marketing.system.entity;

public class ProjectSubtask {
    private Integer id;

    private Integer taskid;

    private String handler;

    private String subtaskname;

    private String sdate;

    private String edate;

    private String subtaskstate;

    private String subtaskprogress;

    private String subtaskhandler;

    private Integer subtasklogrecordid;

    private Integer subtaskdeveloplogid;

    private Integer subtaskid;

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    private String  createdate;

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    private Integer idd;

    /**
     * 距离逾期天数（24小时）
     */
    private long betweenHours;


    public long getBetweenHours() {
        return betweenHours;
    }

    public void setBetweenHours(long betweenHours) {
        this.betweenHours = betweenHours;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler == null ? null : handler.trim();
    }

    public String getSubtaskname() {
        return subtaskname;
    }

    public void setSubtaskname(String subtaskname) {
        this.subtaskname = subtaskname == null ? null : subtaskname.trim();
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

    public String getSubtaskstate() {
        return subtaskstate;
    }

    public void setSubtaskstate(String subtaskstate) {
        this.subtaskstate = subtaskstate == null ? null : subtaskstate.trim();
    }

    public String getSubtaskprogress() {
        return subtaskprogress;
    }

    public void setSubtaskprogress(String subtaskprogress) {
        this.subtaskprogress = subtaskprogress == null ? null : subtaskprogress.trim();
    }

    public String getSubtaskhandler() {
        return subtaskhandler;
    }

    public void setSubtaskhandler(String subtaskhandler) {
        this.subtaskhandler = subtaskhandler == null ? null : subtaskhandler.trim();
    }

    public Integer getSubtasklogrecordid() {
        return subtasklogrecordid;
    }

    public void setSubtasklogrecordid(Integer subtasklogrecordid) {
        this.subtasklogrecordid = subtasklogrecordid;
    }

    public Integer getSubtaskdeveloplogid() {
        return subtaskdeveloplogid;
    }

    public void setSubtaskdeveloplogid(Integer subtaskdeveloplogid) {
        this.subtaskdeveloplogid = subtaskdeveloplogid;
    }

    public Integer getSubtaskid() {
        return subtaskid;
    }

    public void setSubtaskid(Integer subtaskid) {
        this.subtaskid = subtaskid;
    }
}