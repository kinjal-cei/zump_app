package com.catch32.zumpbeta.constant;

import com.catch32.zumpbeta.utils.Logger;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class AppConstant
{
    public static final String FRAGMENT = "FRAGMENT";
    public static final long SPLASH_DELAY = 5 * 1000;
    public static final String RESPONSE_HTTP_ERROR = "RESPONSE_HTTP_ERROR";
    public static final String RESPONSE_TIMEOUT_ERROR = "RESPONSE_TIMEOUT_ERROR";
    public static final String RESPONSE_APP_ERROR = "RESPONSE_APP_ERROR";
    public static final int REQUEST_TIMEOUT = 2 * 30 * 1000;
    public static final String[] MONTHS = new String[]{"--All-", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static final Logger.LogLevel LOG_LEVEL = Logger.LogLevel.VERBOSE;
    public static final Boolean CONSOLE_LOG_ENABLE = true;

    public static class Actions
    {
        public static final String LOGIN = "login";
        public static final String GET_RAISED_ORDERS = "getDataforRmsDms";
        public static final String GET_RAISED_ORDERS_DISTRIBUTOR = "getDataforRmsDmsDistributor";
        public static final String GET_RAISED_ORDERS_COMPANY = "getDataforRmsDmsCompany";
        public static final String GET_MENU_DATA = "getMenuData";
        public static final String GET_DATA = "getData";
        public static final String GET_PROFILE = "getProfile";
        public static final String GET_LIST_DATA_SECOND_SPINNER = "getSecondSpinner";
        public static final String GET_LIST_DATA_THIRD_SPINNER = "getThirdSpinner";
        public static final String GET_LIST_DATA = "getListData";
        public static final String GET_CATEGORY_DATA = "getCategoryData";
        public static final String GET_SUB_CATEGORY_DATA = "getSubCategoryData";
        public static final String GET_SUB_CATEGORY_BRAND_DATA = "getSubCategoryBrandData";
        public static final String GET_SUB_CATEGORY_PRODUCT_DATA = "getSubCategoryProductData";
        public static final String SUBMIT_NEW_ORDER = "newComplainRegistration";
        public static final String SUBMIT_DRAFT_ORDER = "newDraftRegistration";
        public static final String SEARCH_PRODUCT_NEW_ORDER = "getSingleProductData";
        public static final String GET_ORDER_DETAILS = "getComplaintData";
        public static final String SUBMIT_ORDER_DETAILS = "updateComplaintStatus";
        public static final String GET_FEEDBACK_DATA = "getFeedBackDtl";
        public static final String SUBMIT_FEEDBACK_DATA = "saveFeedbackDtl";
        public static final String CHANGE_PASSWORD = "updatePassword";
        public static final String FORGOT_PASSWORD = "Forgot_Password";
        public static final String GET_PRIVACY_POLICY = "PrivacyPolicy"; //SB20191211
        public static final String GET_TERMS_CONDITIONS = "TermsConditions"; //SB20191211
        public static final String PRIVACY_POLICY = "submitPrivacyPolicy"; //SB20191211
        public static final String TERMS_CONDITIONS = "submitTermsConditions"; //SB20191211
        public static final String NOTIFICATION = "Notification"; //SB20191226
        public static final String USER_UNREGD = "UnregdUser"; //SB20191231
        public static final String GET_NOTIFICATION = "getNotification"; //SB20200109
        public static final String CHANGE_PROFILE = "updateProfile"; //SB20200208
    }

    public static class WebURL
    {
     //   private static final String PATH = "http://billpower.in:80/BP10/";
        // private static final String CHECK_LOGIN = PATH_NEW + "CheckLogin.php?";

        private static final String PATH_NEW = "http://billpower.in/DEMO10/";
        private static final String CHECK_LOGIN = PATH_NEW + "DemoMyOrder.php?";
        public static final String LOGIN_PATH = CHECK_LOGIN + "option=checkNewConnection";
        public static final String GET_RAISED_ORDERS_PATH = CHECK_LOGIN + "option=getDataforRmsDms";
        public static final String GET_MENU_DATA = CHECK_LOGIN + "option=getMenuData";
        public static final String GET_PROFILE = CHECK_LOGIN + "option=getProfileData";
        public static final String GET_LIST_DATA = CHECK_LOGIN + "option=getListData";
        public static final String GET_DATA = CHECK_LOGIN + "option=getData";
        public static final String GET_CATEGORY_DATA = CHECK_LOGIN + "option=getCategoryData";
        public static final String GET_SUB_CATEGORY_DATA = CHECK_LOGIN + "option=getSubCategoryData";
        public static final String GET_SUB_CATEGORY_BRAND_DATA = CHECK_LOGIN + "option=getSubCategoryBrandData";
        public static final String GET_SUB_CATEGORY_PRODUCT_DATA = CHECK_LOGIN + "option=getSubCategoryProductData";
        public static final String SUBMIT_NEW_ORDER = CHECK_LOGIN + "option=newComplainRegistration";
        public static final String SUBMIT_DRAFT_ORDER = CHECK_LOGIN + "option=newDraftRegistration";
        public static final String SEARCH_PRODUCT_NEW_ORDER = CHECK_LOGIN + "option=getSingleProductData";
        public static final String GET_ORDER_DETAILS = CHECK_LOGIN + "option=getComplaintData";
        public static final String SUBMIT_ORDER_DETAILS = CHECK_LOGIN + "option=updateComplaintStatus";
        public static final String GET_FEEDBACK_DATA = CHECK_LOGIN + "option=getFeedBackDtl";
        public static final String SUBMIT_FEEDBACK_DATA = CHECK_LOGIN + "option=saveFeedbackDtl";
        public static final String CHANGE_PASSWORD = CHECK_LOGIN + "option=updatePassword";
        public static final String FORGOT_PASSWORD = CHECK_LOGIN + "option=Forgot_Password";
        public static final String GET_PRIVACY_POLICY = CHECK_LOGIN + "option=PrivacyPolicy"; //SB20191211
        public static final String GET_TERMS_CONDITIONS = CHECK_LOGIN + "option=TermsConditions"; //SB20191211
        public static final String PRIVACY_POLICY = CHECK_LOGIN + "option=submitPrivacyPolicy"; //SB20191211
        public static final String TERMS_CONDITIONS = CHECK_LOGIN + "option=submitTermsConditions"; //SB20191211
        public static final String GET_CALL_US = CHECK_LOGIN + "option=getCallUsData"; //SB20191211
        public static final String NOTIFICATION = CHECK_LOGIN + "option=Notification"; //SB20191226
        public static final String USER_UNREGD = CHECK_LOGIN + "option=UnregdUser"; //SB20191231
        public static final String GET_NOTIFICATION = CHECK_LOGIN + "option=getNotification"; //SB20200109
        public static final String CHANGE_PROFILE = CHECK_LOGIN + "option=updateProfile"; //SB20200208
    }
}
