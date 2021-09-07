package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class MonthlySummaryCategoryReportData {

    private String TOTAL_CM;

    private String TOTAL_CM_MRP;

    private String TOTAL_CM_TRP;

    private String TOTAL_RESOLVED;

    private String TOTAL_RESOLVED_MRP;

    private String TOTAL_RESOLVED_TRP;

    private String TOTAL_INPROCESS;

    private String TOTAL_INPROCESS_MRP;

    private String TOTAL_INPROCESS_TRP;

    private String TOTAL_PENDING;

    private String TOTAL_PENDING_MRP;

    private String TOTAL_PENDING_TRP;

    private String AVG_TIME;

    private String YR_VAL;

    private String FY_VAL;
    private String FY_MTH; //Sb20200618
    private String Reward_Point=""; //Sb20200709
    private String Reward_Value=""; //Sb20200709
    private String Order_Status=""; //Sb20200709

    public String getTOTAL_CM() {
        return TOTAL_CM;
    }

    public void setTOTAL_CM(String TOTAL_CM) {
        this.TOTAL_CM = TOTAL_CM;
    }

    public String getTOTAL_CM_MRP() {
        return TOTAL_CM_MRP;
    }

    public void setTOTAL_CM_MRP(String TOTAL_CM_MRP) {
        this.TOTAL_CM_MRP = TOTAL_CM_MRP;
    }

    public String getTOTAL_CM_TRP() {
        return TOTAL_CM_TRP;
    }

    public void setTOTAL_CM_TRP(String TOTAL_CM_TRP) {
        this.TOTAL_CM_TRP = TOTAL_CM_TRP;
    }

    public String getTOTAL_RESOLVED() {
        return TOTAL_RESOLVED;
    }

    public void setTOTAL_RESOLVED(String TOTAL_RESOLVED) {
        this.TOTAL_RESOLVED = TOTAL_RESOLVED;
    }

    public String getTOTAL_RESOLVED_MRP() {
        return TOTAL_RESOLVED_MRP;
    }

    public void setTOTAL_RESOLVED_MRP(String TOTAL_RESOLVED_MRP) {
        this.TOTAL_RESOLVED_MRP = TOTAL_RESOLVED_MRP;
    }

    public String getTOTAL_RESOLVED_TRP() {
        return TOTAL_RESOLVED_TRP;
    }

    public void settOTALRESOLVEDTRP(String tOTALRESOLVEDTRP) {
        this.TOTAL_RESOLVED_TRP = tOTALRESOLVEDTRP;
    }

    public String getTOTAL_INPROCESS() {
        return TOTAL_INPROCESS;
    }

    public void setTOTAL_INPROCESS(String TOTAL_INPROCESS) {
        this.TOTAL_INPROCESS = TOTAL_INPROCESS;
    }

    public String getTOTAL_INPROCESS_MRP() {
        return TOTAL_INPROCESS_MRP;
    }

    public void setTOTAL_INPROCESS_MRP(String TOTAL_INPROCESS_MRP) {
        this.TOTAL_INPROCESS_MRP = TOTAL_INPROCESS_MRP;
    }

    public String getTOTAL_INPROCESS_TRP() {
        return TOTAL_INPROCESS_TRP;
    }

    public void setTOTAL_INPROCESS_TRP(String TOTAL_INPROCESS_TRP) {
        this.TOTAL_INPROCESS_TRP = TOTAL_INPROCESS_TRP;
    }

    public String getTOTAL_PENDING() {
        return TOTAL_PENDING;
    }

    public void setTOTAL_PENDING(String TOTAL_PENDING) {
        this.TOTAL_PENDING = TOTAL_PENDING;
    }

    public String getTOTAL_PENDING_MRP() {
        return TOTAL_PENDING_MRP;
    }

    public void setTOTAL_PENDING_MRP(String TOTAL_PENDING_MRP) {
        this.TOTAL_PENDING_MRP = TOTAL_PENDING_MRP;
    }

    public String getTOTAL_PENDING_TRP() {
        return TOTAL_PENDING_TRP;
    }

    public void setTOTAL_PENDING_TRP(String TOTAL_PENDING_TRP) {
        this.TOTAL_PENDING_TRP = TOTAL_PENDING_TRP;
    }

    public String getAVG_TIME() {
        return AVG_TIME;
    }

    public void setAVG_TIME(String AVG_TIME) {
        this.AVG_TIME = AVG_TIME;
    }

    public String getYR_VAL() {
        return YR_VAL;
    }

    public void setYR_VAL(String YR_VAL) {
        this.YR_VAL = YR_VAL;
    }

    public String getFY_VAL() {
        return FY_VAL;
    }
    public void setFY_VAL(String FY_VAL) {
        this.FY_VAL = FY_VAL;
    }
    public String getFY_MTH() {
        return FY_MTH;
    }
    public void setFY_MTH(String FY_MTH) {
        this.Reward_Point = Reward_Point;
    }
    public String getReward_Point() {
        return Reward_Point;
    }
    public void setReward_Point(String Reward_Point) {
        this.Reward_Point = Reward_Point;
    }
    public String getReward_Value() {
        return Reward_Value;
    }
    public void setReward_Value(String Reward_Value) {
        this.Reward_Value = Reward_Value;
    }
    public String getOrder_Status() {
        return Order_Status;
    }
    public void setOrder_Status(String Order_Status) {
        this.Order_Status = Order_Status;
    }

    @Override
    public String toString() {
        return "MonthlySummaryCategoryReportData{" +
                "TOTAL_CM='" + TOTAL_CM + '\'' +
                ", TOTAL_CM_MRP='" + TOTAL_CM_MRP + '\'' +
                ", TOTAL_CM_TRP='" + TOTAL_CM_TRP + '\'' +
                ", TOTAL_RESOLVED='" + TOTAL_RESOLVED + '\'' +
                ", TOTAL_RESOLVED_MRP='" + TOTAL_RESOLVED_MRP + '\'' +
                ", tOTALRESOLVEDTRP='" + TOTAL_RESOLVED_TRP + '\'' +
                ", TOTAL_INPROCESS='" + TOTAL_INPROCESS + '\'' +
                ", TOTAL_INPROCESS_MRP='" + TOTAL_INPROCESS_MRP + '\'' +
                ", TOTAL_INPROCESS_TRP='" + TOTAL_INPROCESS_TRP + '\'' +
                ", TOTAL_PENDING=" + TOTAL_PENDING +
                ", TOTAL_PENDING_MRP='" + TOTAL_PENDING_MRP + '\'' +
                ", TOTAL_PENDING_TRP='" + TOTAL_PENDING_TRP + '\'' +
                ", AVG_TIME='" + AVG_TIME + '\'' +
                ", YR_VAL='" + YR_VAL + '\'' +
                ", FY_VAL='" + FY_VAL + '\'' +
                ", FY_MTH='" + FY_MTH + '\'' +
                ", Reward_Point='" + Reward_Point + '\'' +
                ", Reward_Value='" + Reward_Value + '\'' +
                ", Order_Status='" + Order_Status + '\'' +
                '}';
    }
}
