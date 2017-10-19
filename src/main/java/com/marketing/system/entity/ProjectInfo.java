package com.marketing.system.entity;

public class ProjectInfo implements Comparable<ProjectInfo>{
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

    private Integer againState;

    //项目扮演角色
    private String duty;

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    /**
     * 距离逾期天数
     */
    private Long betweenDays;

    public Integer getAgainState() {
        return againState;
    }

    public void setAgainState(Integer againState) {
        this.againState = againState;
    }

    public Long getBetweenDays() {
        return betweenDays;
    }

    public void setBetweenDays(Long betweenDays) {
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

    @Override
    public int compareTo(ProjectInfo o) {

        /*int flag = o.prostate.compareTo(this.prostate);

        return flag;*/
        //先按项目状态排序
        if (o.getProstate().compareTo(this.prostate) > 0) {

            return 1;
        }
        if (o.getProstate().compareTo(this.prostate) < 0) {

            return -1;
        }

        //按创建时间排序
        if (o.getCreatedate().compareTo(this.createdate) > 0) {

            return 1;
        }
        if (o.getCreatedate().compareTo(this.createdate) < 0) {

            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectInfo that = (ProjectInfo) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (proid != null ? !proid.equals(that.proid) : that.proid != null) return false;
        if (proname != null ? !proname.equals(that.proname) : that.proname != null) return false;
        if (protype != null ? !protype.equals(that.protype) : that.protype != null) return false;
        if (plansdate != null ? !plansdate.equals(that.plansdate) : that.plansdate != null) return false;
        if (planedate != null ? !planedate.equals(that.planedate) : that.planedate != null) return false;
        if (prodeclare != null ? !prodeclare.equals(that.prodeclare) : that.prodeclare != null) return false;
        if (profilepath != null ? !profilepath.equals(that.profilepath) : that.profilepath != null) return false;
        if (createdate != null ? !createdate.equals(that.createdate) : that.createdate != null) return false;
        if (prostate != null ? !prostate.equals(that.prostate) : that.prostate != null) return false;
        if (creater != null ? !creater.equals(that.creater) : that.creater != null) return false;
        if (prologrecordid != null ? !prologrecordid.equals(that.prologrecordid) : that.prologrecordid != null)
            return false;
        if (prodeveloplogid != null ? !prodeveloplogid.equals(that.prodeveloplogid) : that.prodeveloplogid != null)
            return false;
        if (proprogress != null ? !proprogress.equals(that.proprogress) : that.proprogress != null) return false;
        if (createrSquadId != null ? !createrSquadId.equals(that.createrSquadId) : that.createrSquadId != null)
            return false;
        if (onlineDate != null ? !onlineDate.equals(that.onlineDate) : that.onlineDate != null) return false;
        if (workTatalDay != null ? !workTatalDay.equals(that.workTatalDay) : that.workTatalDay != null) return false;
        if (finishDate != null ? !finishDate.equals(that.finishDate) : that.finishDate != null) return false;
        if (rejectDate != null ? !rejectDate.equals(that.rejectDate) : that.rejectDate != null) return false;
        if (cancelDate != null ? !cancelDate.equals(that.cancelDate) : that.cancelDate != null) return false;
        if (againState != null ? !againState.equals(that.againState) : that.againState != null) return false;
        if (duty != null ? !duty.equals(that.duty) : that.duty != null) return false;
        return betweenDays != null ? betweenDays.equals(that.betweenDays) : that.betweenDays == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (proid != null ? proid.hashCode() : 0);
        result = 31 * result + (proname != null ? proname.hashCode() : 0);
        result = 31 * result + (protype != null ? protype.hashCode() : 0);
        result = 31 * result + (plansdate != null ? plansdate.hashCode() : 0);
        result = 31 * result + (planedate != null ? planedate.hashCode() : 0);
        result = 31 * result + (prodeclare != null ? prodeclare.hashCode() : 0);
        result = 31 * result + (profilepath != null ? profilepath.hashCode() : 0);
        result = 31 * result + (createdate != null ? createdate.hashCode() : 0);
        result = 31 * result + (prostate != null ? prostate.hashCode() : 0);
        result = 31 * result + (creater != null ? creater.hashCode() : 0);
        result = 31 * result + (prologrecordid != null ? prologrecordid.hashCode() : 0);
        result = 31 * result + (prodeveloplogid != null ? prodeveloplogid.hashCode() : 0);
        result = 31 * result + (proprogress != null ? proprogress.hashCode() : 0);
        result = 31 * result + (createrSquadId != null ? createrSquadId.hashCode() : 0);
        result = 31 * result + (onlineDate != null ? onlineDate.hashCode() : 0);
        result = 31 * result + (workTatalDay != null ? workTatalDay.hashCode() : 0);
        result = 31 * result + (finishDate != null ? finishDate.hashCode() : 0);
        result = 31 * result + (rejectDate != null ? rejectDate.hashCode() : 0);
        result = 31 * result + (cancelDate != null ? cancelDate.hashCode() : 0);
        result = 31 * result + (againState != null ? againState.hashCode() : 0);
        result = 31 * result + (duty != null ? duty.hashCode() : 0);
        result = 31 * result + (betweenDays != null ? betweenDays.hashCode() : 0);
        return result;
    }
}