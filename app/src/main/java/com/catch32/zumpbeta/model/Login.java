package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class Login {

    private String loginSuccess;
    private int login_type;
    private String usercd;
    private String empName;
    private String mobileNo;
    private String ALIAS;
    private String superflag;
    private String agreeFlag; //SB20191212
    private String agreeDt; //SB20191217
    private String PolicyagreeFlag; //SB20191212
    private String PolicyagreeDt; //SB20191217

    public String getData() {
        return Data;
    }

    public void setData(String DATA) {
        this.Data = DATA;
    }

    private String Data;

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public int getLogin_type() {
        return login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }

    public String getUsercd() {
        return usercd;
    }

    public void setUsercd(String usercd)
    {
        this.usercd = usercd;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getALIAS() {
        return ALIAS;
    }

    public void setALIAS(String ALIAS) {
        this.ALIAS = ALIAS;
    }

    public String getSuperflag() {
        return superflag;
    }

    public void setSuperflag(String superflag) {
        this.superflag = superflag;
    }
    ////////////SB20191212: Terms & Conditions/////////////
    public String getAgreeDt() {
        return agreeDt;
    }
    public void setAgreeDt(String agreeDt) {
        this.agreeDt = agreeDt;
    }
    public String getAgreeFlag() {
        return agreeFlag;
    }
    public void setAgreeFlag(String agreeFlag) {
        this.agreeFlag = agreeFlag;
    }
    public String getPolicyAgreeDt() {
        return PolicyagreeDt;
    }
    public void setPolicyAgreeDt(String PolicyagreeDt) {
        this.PolicyagreeDt = PolicyagreeDt;
    }
    public String getPolicyAgreeFlag() { return PolicyagreeFlag;}
    public void setPolicyAgreeFlag(String PolicyagreeFlag) { this.PolicyagreeFlag = PolicyagreeFlag;}
    ///////////////////////////////////////////////////////
    private String last_login; //SB20200606
    public String getLast_login() {return last_login;} //SB20200606
    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }
    private String last_login_msg; //SB20200606
    public String getLast_login_msg() {return last_login_msg;} //SB20200606
    public void setLast_login_msg(String last_login_msg) {
        this.last_login_msg = last_login_msg;
    }
    private String unique_key; //SB20200606
    public String getUnique_key() {return unique_key;} //SB20200606
    public void setUnique_key(String unique_key) {
        this.unique_key = unique_key;
    }
    ////////////////SB20200708///////////////////////
    @Override
    public String toString()
    {
        return "Login{" +
                "loginSuccess='" + loginSuccess + '\'' +
                ", login_type=" + login_type +
                ", usercd='" + usercd + '\'' +
                ", empName='" + empName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", ALIAS='" + ALIAS + '\'' +
                ", superflag='" + superflag + '\'' +
                ", agreeFlag='" + agreeFlag + '\'' +
                ", agreeDt='" + agreeDt + '\'' +
                ", PolicyagreeFlag='" + PolicyagreeFlag + '\'' +
                ", PolicyagreeDt='" + PolicyagreeDt + '\'' +
                ", last_login='" + last_login + '\'' +
                ", last_login_msg='" + last_login_msg + '\'' +
                ", unique_key='" + unique_key + '\'' +
                '}';
    }
}
