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