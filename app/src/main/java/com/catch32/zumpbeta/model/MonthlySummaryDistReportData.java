package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class MonthlySummaryDistReportData {

    private String DIST_NM;

    private String DIST_TOT_MRP;

    private String DIST_TOT_TRP;

    private String DIST_TOT_ORD;

    private String DIST_AVG_VAL;

    private String DIST_MARGIN;

    private String DIST_SHR;
    private String ORD_PREFIX="";

    public String getDIST_NM() {
        return DIST_NM;
    }

    public void setDIST_NM(String DIST_NM) {
        this.DIST_NM = DIST_NM;
    }

    public String getDIST_TOT_MRP() {
        return DIST_TOT_MRP;
    }

    public void setDIST_TOT_MRP(String DIST_TOT_MRP) {
        this.DIST_TOT_MRP = DIST_TOT_MRP;
    }

    public String getDIST_TOT_TRP() {
        return DIST_TOT_TRP;
    }

    public void setDIST_TOT_TRP(String DIST_TOT_TRP) {
        this.DIST_TOT_TRP = DIST_TOT_TRP;
    }

    public String getDIST_TOT_ORD() {
        return DIST_TOT_ORD;
    }

    public void setDIST_TOT_ORD(String DIST_TOT_ORD) {
        this.DIST_TOT_ORD = DIST_TOT_ORD;
    }

    public String getDIST_AVG_VAL() {
        return DIST_AVG_VAL;
    }

    public void setDIST_AVG_VAL(String DIST_AVG_VAL) {
        this.DIST_AVG_VAL = DIST_AVG_VAL;
    }

    public String getDIST_MARGIN() {
        return DIST_MARGIN;
    }

    public void setDIST_MARGIN(String DIST_MARGIN) {
        this.DIST_MARGIN = DIST_MARGIN;
    }

    public String getDIST_SHR() {
        return DIST_SHR;
    }

    public void setDIST_SHR(String DIST_SHR) {
        this.DIST_SHR = DIST_SHR;
    }

    public String getORD_PREFIX() {
        return ORD_PREFIX;
    } //SB20200623
    public void setORD_PREFIX(String ORD_PREFIX) {
        this.DIST_SHR = ORD_PREFIX;
    } //SB20200623
    @Override
    public String toString() {
        return "MonthlySummaryDistReportData{" +
                "DIST_NM='" + DIST_NM + '\'' +
                ", DIST_TOT_MRP='" + DIST_TOT_MRP + '\'' +
                ", DIST_TOT_TRP='" + DIST_TOT_TRP + '\'' +
                ", ORD_PREFIX='" + ORD_PREFIX + '\'' + //SB20200623
                ", DIST_TOT_ORD='" + DIST_TOT_ORD + '\'' +
                ", DIST_AVG_VAL='" + DIST_AVG_VAL + '\'' +
                ", DIST_MARGIN='" + DIST_MARGIN + '\'' +
                ", DIST_SHR='" + DIST_SHR + '\'' +
                '}';
    }
}
