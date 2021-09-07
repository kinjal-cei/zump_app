package com.catch32.zumpbeta.model;

/**
 * @author SB
 * @version Dec 17, 2019
 */
public class TermsConditions {
    private String loginSuccess;
    //private String gotProfileData;
    //private String updateProfile;
    //private ProfileData profileData;
    private String agreeFlag;
    private String agreeDt;

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getAgreeFlag() {
        return agreeFlag;
    }
    public void setAgreeFlag(String agreeFlag) {
        this.agreeFlag = agreeFlag;
    }

    public String getAgreeDt() { return agreeDt; }
    public void setAgreeDt(String agreeDt) {
        this.agreeFlag = agreeDt;
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
        return "TermsConditions{" +
                "loginSuccess='" + loginSuccess + '\'' +
               /* ", gotProfileData='" + gotProfileData + '\'' +
                ", updateProfile='" + updateProfile + '\'' +
                ", profileData=" + profileData +*/
                ", agreeFlag='" + agreeFlag + '\'' +
                ", agreeDt='" + agreeDt + '\'' +
                '}';
    }
}
