package com.catch32.zumpbeta.model;

import java.util.List;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class MontlySummaryGstReport {

    private String loginSuccess;

    private String menuData;

    private String gotListData;

    private List<Status> statusList = null;

    private List<MonthlySummaryCategoryReportData> categoryReportData = null;

    private List<MonthlySummaryDistReportData> distReportData = null;

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getMenuData() {
        return menuData;
    }

    public void setMenuData(String menuData) {
        this.menuData = menuData;
    }

    public String getGotListData() {
        return gotListData;
    }

    public void setGotListData(String gotListData) {
        this.gotListData = gotListData;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }

    public List<MonthlySummaryCategoryReportData> getCategoryReportData() {
        return categoryReportData;
    }

    public void setCategoryReportData(List<MonthlySummaryCategoryReportData> categoryReportData) {
        this.categoryReportData = categoryReportData;
    }

    public List<MonthlySummaryDistReportData> getDistReportData() {
        return distReportData;
    }

    public void setDistReportData(List<MonthlySummaryDistReportData> distReportData) {
        this.distReportData = distReportData;
    }

    @Override
    public String toString() {
        return "MontlySummaryReport{" +
                "loginSuccess='" + loginSuccess + '\'' +
                ", menuData='" + menuData + '\'' +
                ", gotListData='" + gotListData + '\'' +
                ", statusList=" + statusList +
                ", categoryReportData=" + categoryReportData +
                ", distReportData=" + distReportData +
                '}';
    }
}
