package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jul 01, 2019
 */
public class CategoryReportData implements Comparable<CategoryReportData> {
    private String CM_NO;
    private String CM_TIME;
    private String CM_STATUS;
    private String CM_ROOM_NO;
    private String CM_DESC;
    private String CM_NAME;
    private String OFFER;
    private String SHARE; //SB20191219
    private String TAP; //SB20191219
    private String RATING; //SB20191223
    public String getOFFER() {
        return OFFER;
    }

    public void setOFFER(String OFFER) {
        this.OFFER = OFFER;
    }

    public String getCM_NO() {
        return CM_NO;
    }

    public void setCM_NO(String CM_NO) {
        this.CM_NO = CM_NO;
    }

    public String getCM_TIME() {
        return CM_TIME;
    }

    public void setCM_TIME(String CM_TIME) {
        this.CM_TIME = CM_TIME;
    }

    public String getCM_STATUS() {
        return CM_STATUS;
    }

    public void setCM_STATUS(String CM_STATUS) {
        this.CM_STATUS = CM_STATUS;
    }

    public String getCM_ROOM_NO() {
        return CM_ROOM_NO;
    }

    public void setCM_ROOM_NO(String CM_ROOM_NO) {
        this.CM_ROOM_NO = CM_ROOM_NO;
    }

    public String getCM_DESC() {
        return CM_DESC;
    }

    public void setCM_DESC(String CM_DESC) {
        this.CM_DESC = CM_DESC;
    }

    public String getCM_NAME() {
        return CM_NAME;
    }

    public void setCM_NAME(String CM_NAME) {
        this.CM_NAME = CM_NAME;
    }

    //////////SB20191219//////////////////
    public String getSHARE() {
        return SHARE;
    }
    public void setSHARE(String SHARE) {
        this.SHARE = SHARE;
    }
    public String getTAP() {
        return TAP;
    }
    public void setTAP(String TAP) {
        this.TAP = TAP;
    }
    public String getRATING() {
        return RATING;
    }
    public void setRATING(String RATING) {
        this.TAP = RATING;
    }
    //////////////////////////////////////
    @Override
    public String toString() {
        return "CategoryReportData{" +
                "CM_NO='" + CM_NO + '\'' +
                ", CM_TIME='" + CM_TIME + '\'' +
                ", CM_STATUS='" + CM_STATUS + '\'' +
                ", CM_ROOM_NO='" + CM_ROOM_NO + '\'' +
                ", CM_DESC='" + CM_DESC + '\'' +
                ", CM_NAME='" + CM_NAME + '\'' +
                ", OFFER='" + OFFER + '\'' +
                ", SHARE='" + SHARE + '\'' + //SB20191219
                ", TAP='" + TAP + '\'' + //SB20191219
                ", RATING='" + RATING + '\'' + //SB20191219
                '}';
    }

    @Override
    public int compareTo(CategoryReportData o) {
        return getCM_DESC().compareTo(o.getCM_DESC());
    }
}
