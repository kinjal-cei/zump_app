package com.catch32.zumpbeta.model;

/**
 * @author Ruchi Mehta
 * @version Jun 28, 2019
 */
public class CallUs {
    private String loginSuccess;
    private String gotProfileData;
    private String updateProfile;
    private ProfileData profileData;

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getGotProfileData() {
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

    @Override
    public String toString() {
        return "Profile{" +
                "loginSuccess='" + loginSuccess + '\'' +
                ", gotProfileData='" + gotProfileData + '\'' +
                ", updateProfile='" + updateProfile + '\'' +
                ", profileData=" + profileData +
                '}';
    }
}
