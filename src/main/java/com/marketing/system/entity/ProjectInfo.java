package com.marketing.system.entity;

public class ProjectInfo {
    private Integer id;

    private Integer proid;

    private String proname;

    private String protype;

    private String plansdate;

    private String planedate;

    private String prodeclare;

    private String profilepath;

    private String createdate;

    private String prostate;

    private String creater;

    private Integer prologrecordid;

    private Integer prodeveloplogid;

    private String proprogress;

    private String createrSquadId;

    private String onlineDate;

    private String workTatalDay;

    private String finishDate;

    private String rejectDate;

    private String cancelDate;


    /**
     * 距离逾期天数
     */
    private Integer betweenDays;

    public Integer getBetweenDays() {
        return betweenDays;
    }

    public void setBetweenDays(Integer betweenDays) {
        this.betweenDays = betweenDays;
    }

    public String getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(String onlineDate) {
        this.onlineDate = onlineDate;
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

    public String getProprogress() {
        return proprogress;
    }

    public void setProprogress(String proprogress) {
        this.proprogress = proprogress == null ? null : proprogress.trim();
    }

    public String getCreaterSquadId() {
        return createrSquadId;
    }

    public void setCreaterSquadId(String createrSquadId) {
        this.createrSquadId = createrSquadId;
    }

    public String getWorkTatalDay() {
        return workTatalDay;
    }

    public void setWorkTatalDay(String workTatalDay) {
        this.workTatalDay = workTatalDay;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(String rejectDate) {
        this.rejectDate = rejectDate;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }
}