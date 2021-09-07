package com.catch32.zumpbeta.model;

import java.util.List;

/**
 * @author SB
 * @version Dec 17, 2019
 */
public class Notification {

    private String flag;
    private List<DistReportData> distReportData = null;
    private List<Company> compList = null;
    private List<NotificationList> notificationList = null;
    private String TermsText;
    private String agreeDt;
    private String TermsHeader;
    private String NotifyBy;
    private String ReadDt;
    private List<OrderListClr> orderList_clr = null;




    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<DistReportData> getDistReportData() {
        return distReportData;
    }

    public void setDistReportData(List<DistReportData> distReportData) {
        this.distReportData = distReportData;
    }

    public List<NotificationList> getNotificationList() {return notificationList;}
    public void setNotificationList(List<NotificationList> notificationList) {this.notificationList = notificationList;}

    public String getAgreeDt() {
        return agreeDt;
    }
    public void setAgreeDt(String agreeDt) {
        this.agreeDt = agreeDt;
    }
    public String getTermsText() {
        return TermsText;
    }
    public void setTermsText(String TermsText) {
        this.TermsText = TermsText;
    }
    public String getTermsHeader() {
        return TermsHeader;
    }
    public void setTermsHeader(String TermsHeader) {
        this.TermsHeader = TermsHeader;
    }
    public String getNotifyBy() {
        return NotifyBy;
    }
    public void setNotifyBy(String NotifyBy) {
        this.NotifyBy = NotifyBy;
    }
    public String getReadDt() {
        return ReadDt;
    }
    public void setReadDt(String ReadDt) {
        this.ReadDt = ReadDt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "agreeDt='" + agreeDt + '\'' +
                ", flag='" + flag + '\'' +
                ", distReportData=" + distReportData +
                ", compList=" + compList +
                ", notificationList=" + notificationList +
                ", TermsText='" + TermsText + '\'' +
                ", TermsHeader='" + TermsHeader + '\'' +
                ", NotifyBy='" + NotifyBy + '\'' +
                ", ReadDt='" + ReadDt + '\'' +
                ", orderList_clr=" + orderList_clr +
                '}';
    }
}
