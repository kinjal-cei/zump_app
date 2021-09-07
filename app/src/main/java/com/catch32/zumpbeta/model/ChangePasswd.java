package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class ChangePasswd {

    private String loginSuccess;
    private String changePassword; //SB20191212
    private String chng_pwd; //SB20191217

    public String getData()
    {
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

    public String getChangePassword() {
        return changePassword;
    }
    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }
    public String getChng_pwd() { return chng_pwd;}
    public void setChng_pwd(String chng_pwd) { this.chng_pwd = chng_pwd;}
    ///////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "Login{" +
                "loginSuccess='" + loginSuccess + '\'' +
                ", changePassword='" + changePassword + '\'' +
                ", chng_pwd='" + chng_pwd + '\'' +
                '}';
    }
}
