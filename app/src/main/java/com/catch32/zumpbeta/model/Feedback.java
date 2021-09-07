package com.catch32.zumpbeta.model;

import java.util.List;

/**
 * @author Ruchi Mehta
 * @version Aug 23, 2019
 */
public class Feedback {

    private String loginSuccess;

    private List<FeedbackDatum> feedbackData = null;

    private String feedbackFlag;

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public List<FeedbackDatum> getFeedbackData() {
        return feedbackData;
    }

    public void setFeedbackData(List<FeedbackDatum> feedbackData) {
        this.feedbackData = feedbackData;
    }

    public String getFeedbackFlag() {
        return feedbackFlag;
    }

    public void setFeedbackFlag(String feedbackFlag) {
        this.feedbackFlag = feedbackFlag;
    }
}
