package com.marketing.system.entity;

public class ProjectInfo {
    private Integer id;

    private String proid;

    private String proname;

    private String protype;

    private String plansdate;

    private String planedate;

    private String prodeclare;

    private Integer joinid;

    private String profilepath;

    private String createdate;

    private String prostate;

    private String creater;

    private Integer prologrecordid;

    private Integer prodeveloplogid;

    private Integer taskid;

    private String proprogress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid == null ? null : proid.trim();
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname == null ? null : proname.trim();
    }

    public String getProtype() {
        return protype;
    }

    public void setProtype(String protype) {
        this.protype = protype == null ? null : protype.trim();
    }

    public String getPlansdate() {
        return plansdate;
    }

    public void setPlansdate(String plansdate) {
        this.plansdate = plansdate == null ? null : plansdate.trim();
    }

    public String getPlanedate() {
        return planedate;
    }

    public void setPlanedate(String planedate) {
        this.planedate = planedate == null ? null : planedate.trim();
    }

    public String getProdeclare() {
        return prodeclare;
    }

    public void setProdeclare(String prodeclare) {
        this.prodeclare = prodeclare == null ? null : prodeclare.trim();
    }

    public Integer getJoinid() {
        return joinid;
    }

    public void setJoinid(Integer joinid) {
        this.joinid = joinid;
    }

    public String getProfilepath() {
        return profilepath;
    }

    public void setProfilepath(String profilepath) {
        this.profilepath = profilepath == null ? null : profilepath.trim();
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate == null ? null : createdate.trim();
    }

    public String getProstate() {
        return prostate;
    }

    public void setProstate(String prostate) {
        this.prostate = prostate == null ? null : prostate.trim();
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Integer getPrologrecordid() {
        return prologrecordid;
    }

    public void setPrologrecordid(Integer prologrecordid) {
        this.prologrecordid = prologrecordid;
    }

    public Integer getProdeveloplogid() {
        return prodeveloplogid;
    }

    public void setProdeveloplogid(Integer prodeveloplogid) {
        this.prodeveloplogid = prodeveloplogid;
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public String getProprogress() {
        return proprogress;
    }

    public void setProprogress(String proprogress) {
        this.proprogress = proprogress == null ? null : proprogress.trim();
    }
}