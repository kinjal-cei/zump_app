package com.catch32.zumpbeta.model;

import java.util.List;

/**
 * @author Ruchi Mehta
 * @version Jun 28, 2019
 */
public class Reports {

    private String loginSuccess;
    private String menuData;
    private String gotListData;
    private List<Status> statusList;
    private int spinnerId;
    private int statusId;
    private List<CategoryListReport> categoryListReport;
    private List<SubcategoryListReport> subcategoryListReport;
    private List<Company> compList;
    private List<CategoryReportData> categoryReportData;
    private List<Year> yearList;

    public List<Year> getYearList() {
        return yearList;
    }

    public void setYearList(List<Year> yearList) {
        this.yearList = yearList;
    }

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

    public int getSpinnerId() {
        return spinnerId;
    }

    public void setSpinnerId(int spinnerId) {
        this.spinnerId = spinnerId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public List<CategoryListReport> getCategoryListReport() {
        return categoryListReport;
    }

    public void setCategoryListReport(List<CategoryListReport> categoryListReport) {
        this.categoryListReport = categoryListReport;
    }

    public List<SubcategoryListReport> getSubcategoryListReport() {
        return subcategoryListReport;
    }

    public void setSubcategoryListReport(List<SubcategoryListReport> subcategoryListReport) {
        this.subcategoryListReport = subcategoryListReport;
    }

    public List<Company> getCompList() {
        return compList;
    }

    public void setCompList(List<Company> compList) {
        this.compList = compList;
    }

    public List<CategoryReportData> getCategoryReportData() {
        return categoryReportData;
    }

    public void setCategoryReportData(List<CategoryReportData> categoryReportData) {
        this.categoryReportData = categoryReportData;
    }

    @Override
    public String toString() {
        return "Reports{" +
                "loginSuccess='" + loginSuccess + '\'' +
                ", menuData='" + menuData + '\'' +
                ", gotListData='" + gotListData + '\'' +
                ", statusList=" + statusList +
                ", spinnerId=" + spinnerId +
                ", statusId=" + statusId +
                ", categoryListReport=" + categoryListReport +
                ", subcategoryListReport=" + subcategoryListReport +
                ", compList=" + compList +
                ", categoryReportData=" + categoryReportData +
                ", yearList=" + yearList +
                '}';
    }
}
