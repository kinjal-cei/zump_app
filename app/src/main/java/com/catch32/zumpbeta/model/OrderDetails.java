package com.catch32.zumpbeta.model;

import java.util.List;

/**
 * @author Ruchi Mehta
 * @version Oct 02, 2019
 */
public class OrderDetails {

    private String supervisor_flg;

    private String loginSuccess;

    private List<ProductList> productList = null;

    private ComplainData complainData;

    private String complainflag;

    public String getSupervisorFlg() {
        return supervisor_flg;
    }

    public void setSupervisorFlg(String supervisorFlg) {
        this.supervisor_flg = supervisorFlg;
    }

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public List<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }

    public ComplainData getComplainData() {
        return complainData;
    }

    public void setComplainData(ComplainData complainData) {
        this.complainData = complainData;
    }

    public String getComplainflag() {
        return complainflag;
    }

    public void setComplainflag(String complainflag) {
        this.complainflag = complainflag;
    }

}

