package com.marketing.system.entity;

public class TaskDevelopLog {
    private Integer taskdeveloplogid;

    private Integer taskid;

    private String date;

    private String squadid;

    private String emp;

    private String explain;

    private String filepath;

    public Integer getTaskdeveloplogid() {
        return taskdeveloplogid;
    }

    public void setTaskdeveloplogid(Integer taskdeveloplogid) {
        this.taskdeveloplogid = taskdeveloplogid;
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public String getSquadid() {
        return squadid;
    }

    public void setSquadid(String squadid) {
        this.squadid = squadid;
    }

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp == null ? null : emp.trim();
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain == null ? null : explain.trim();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }
}