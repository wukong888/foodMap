package com.marketing.system.entity;

public class SubtaskDevelopLog {
    private Integer subtaskdeveloplogid;

    private Integer subtaskid;

    private String date;

    private Integer squadid;

    private String emp;

    private String explain;

    private String filepath;

    private String progress;

    private String type;

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public Integer getSquadid() {
        return squadid;
    }

    public void setSquadid(Integer squadid) {
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