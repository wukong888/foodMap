package com.marketing.system.entity;

public class TaskLogRecord {
    private Integer tasklogrecordid;

    private Integer taskid;

    private String type;

    private String date;

    private String squadid;

    private String emp;

    private String explain;

    private String filepath;

    public Integer getTasklogrecordid() {
        return tasklogrecordid;
    }

    public void setTasklogrecordid(Integer tasklogrecordid) {
        this.tasklogrecordid = tasklogrecordid;
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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