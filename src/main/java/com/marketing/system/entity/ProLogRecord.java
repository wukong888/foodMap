package com.marketing.system.entity;

public class ProLogRecord {
    private Integer prologrecordid;

    private Integer proid;

    private String type;

    private String date;

    private String squadid;

    private String emp;

    private String explain;

    private String filepath;

    public Integer getPrologrecordid() {
        return prologrecordid;
    }

    public void setPrologrecordid(Integer prologrecordid) {
        this.prologrecordid = prologrecordid;
    }

    public Integer getProid() {
        return proid;
    }

    public void setProid(Integer proid) {
        this.proid = proid;
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