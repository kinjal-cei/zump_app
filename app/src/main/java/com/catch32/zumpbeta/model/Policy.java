package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 28, 2019
 */
public class Policy {
    private String loginSuccess;
    //private String gotProfileData;
    //private String updateProfile;
    //private ProfileData profileData;
    private String PolicyagreeFlag;
    private String PolicyagreeDt;

    public String getLoginSuccess() {
        return loginSuccess;
    }
    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getPolicyAgreeFlag() {
        return PolicyagreeFlag;
    }
    public void setPolicyAgreeFlag(String PolicyagreeFlag) { this.PolicyagreeFlag = PolicyagreeFlag;}

    public String getPolicyAgreeDt() { return PolicyagreeDt; }
    public void setPolicyAgreeDt(String agreeDt) {
        this.PolicyagreeDt = PolicyagreeDt;
    }
   /* public String getGotProfileData() {
        return gotProfileData;
    }

    public void setGotProfileData(String gotProfileData) {
        this.gotProfileData = gotProfileData;
    }

    public String getUpdateProfile() {
        return updateProfile;
    }

    public void setUpdateProfile(String updateProfile) {
        this.updateProfile = updateProfile;
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }
*/
    @Override
    public String toString() {
       /* return "Policy{" +
                "loginSuccess='" + loginSuccess + '\'' +
                ", gotProfileData='" + gotProfileData + '\'' +
                ", updateProfile='" + updateProfile + '\'' +
                ", profileData=" + profileData +
                '}';*/
        return "Policy{" +
                "loginSuccess='" + loginSuccess + '\'' +
                ", PolicyagreeFlag='" + PolicyagreeFlag + '\'' +
                ", PolicyagreeDt='" + PolicyagreeDt + '\'' +
                '}';
    }
}
