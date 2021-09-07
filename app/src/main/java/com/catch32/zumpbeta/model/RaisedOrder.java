package com.catch32.zumpbeta.model;

import java.util.List;

/**
 * @author Ruchi Mehta
 * @version Jun 17, 2019
 */
public class RaisedOrder
{

    private String loginSuccess;
    private String flag;
    private List<DistReportData> distReportData = null;
    private List<Company> compList = null;
    private List<OrderList> orderList = null;
    private String name;
    private String feedbackApply;
    private List<OrderListClr> orderList_clr = null;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

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

    public List<OrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderList> orderList) {
        this.orderList = orderList;
    }



    public String getFeedbackApply() {
        return feedbackApply;
    }

    public void setFeedbackApply(String feedbackApply) {
        this.feedbackApply = feedbackApply;
    }

    public List<OrderListClr> getOrderList_clr() {
        return orderList_clr;
    }

    public void setOrderList_clr(List<OrderListClr> orderList_clr) {
        this.orderList_clr = orderList_clr;
    }

    public List<Company> getCompList() {
        return compList;
    }

    public void setCompList(List<Company> compList) {
        this.compList = compList;
    }

    @Override
    public String toString() {
        return "RaisedOrder{" +
                "loginSuccess='" + loginSuccess + '\'' +
                ", flag='" + flag + '\'' +
                ", distReportData=" + distReportData +
                ", compList=" + compList +
                ", orderList=" + orderList +
                ", name='" + name + '\'' +
                ", feedbackApply='" + feedbackApply + '\'' +
                ", orderList_clr=" + orderList_clr +
                '}';
    }
}
