package com.catch32.zumpbeta.apicall;

public class ApiEndPoint
{
    // ApiEndPoint
    public static final String BASE_URL = "http://billpower.in/DEMO10/";
    public static final String UPLOAD_BASE_URL = "http://targetfire.techregnum.com/api/admin/fileupload/";

    public static final String TEST_MENU = "DemoMyOrder.php?option=getMenuData&Param={%22id%22:%222VoTM29S%22,%22usercd%22:%22301418%22,%22menuId%22:%225%22,%22statusId%22:%220%22,%22spinnerId%22:%220%22,%22compId%22:%220%22}";
    public static final String TEST_MENU_COPY = "DemoMyOrder.php?option=getMenuData&Param={%22id%22:%222VoTM29S%22,%22usercd%22:%22301418%22,%22menuId%22:%225%22,%22statusId%22:%220%22,%22spinnerId%22:%220%22,%22compId%22:%220%22}";
    public static final String TEST_DATA = "DemoMyOrder.php?option=getListData&Param={%22id%22:%222VoTM29S%22,%22usercd%22:%22301418%22,%22menuId%22:%225%22,%22selectedDropId%22:%220%22,%22selectedStatus%22:%220%22,%22extraSpinnerId%22:%220%22,%22selectedCompId%22:%220%22}";
    public static final String TEST_DATA_COPY = "DemoMyOrder.php?option=getListData&Param=";
    public static final String GET_ADMIN_USERS_LIST = "getAdminUsersList";
    public static final String GET_EMPLOYEES_LIST = "getEmployeesList";
    public static final String GET_PROJECT_SITE_LIST = "getProjectSiteList";
    public static final String GET_PROJECT_SITE_ITEM_LIST = "getProjectSiteItemList";
    public static final String GET_PROJECT_SITE_MACHINERY_LIST = "getProjectSiteMachineryList";
    public static final String GET_ITEM_LIST = "getItemList";
    public static final String GET_MACHINERY_LIST = "getMachineryList";
    public static final String GET_TOOL_LIST = "getToolList";
    public static final String GET_EMPLOYEE_TOOL_LINKING_LIST = "getEmployeeToolLinkingList";
    public static final String GET_DELIVERY_CHALLAN_LIST = "getdeliveryChallanList";
    public static final String GET_DELIVERY_CHALLAN_OF_PROJECT_SITE = "getDeliveryChallanOfProjectSite";
    public static final String GET_PURCHASE_ORDER_LIST = "getPurchaseOrderList";
    public static final String SAVE_ITEM_REQUEST = "saveItemRequest";

    //
    public static final String TEST_MENU_DATA = "DemoMyOrder.php?option=getMenuData&Param=";
    public static final String QUICK_ORDER = "DemoMyOrder.php?option=getComplaintData&Param=";
    public static final String GET_MENU_DATA = "DemoMyOrder.php?option=getMenuData&Param=";
    public static final String GET_MONTHLY_SUMMARY = "DemoMyOrder.php?option=getListData&Param=";

    //NEW
    public static final String TERMS_CONDITIONS = "DemoMyOrder.php?option=TermsConditions&Param=";
    public static final String PRIVACY_POLICY = "DemoMyOrder.php?option=PrivacyPolicy&Param=";
    public static final String NOTIFICATION = "DemoMyOrder.php?option=Notification&Param=";
    public static final String UPDATE_PROFILE = "DemoMyOrder.php?option=updateProfile&Param=";
    public static final String GET_PROFILE = "DemoMyOrder.php?option=getProfileData&Param=";
    public static final String LOGIN ="DemoMyOrder.php?option=checkNewConnection&Param=";
    public static final String GETFEEDBACK ="DemoMyOrder.php?option=getFeedBackDtl&Param=";
    public static final String SAVE_FEEDBACK ="DemoMyOrder.php?option=saveFeedbackDtl&Param=";



    public static final String GET_RAISED_ORDERS_PATH ="DemoMyOrder.php?option=getDataforRmsDms&Param=";
    public static final String GET_DRAFT_ORDER_LIST ="DemoMyOrder.php?option=getListData&Param=";

    public static final String VERSION_API = "v1/";
}
