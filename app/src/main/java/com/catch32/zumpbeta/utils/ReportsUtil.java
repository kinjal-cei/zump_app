package com.catch32.zumpbeta.utils;

import android.content.Context;

import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.google.gson.JsonObject;

/**
 * @author Ruchi Mehta
 * @version Jul 19, 2019
 */
public class ReportsUtil
{
    public static void getListData(Context context, String tag, String menuId, String spinnerId, String statusId, String compId, ResponseListener listener) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("menuId", menuId);
        jsonObject.addProperty("selectedDropId", Integer.valueOf(spinnerId));
        jsonObject.addProperty("selectedStatus", Integer.valueOf(statusId));
        jsonObject.addProperty("extraSpinnerId", 0);
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(context).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("logintyp", 0);
        jsonObject.addProperty("selectedCompId", Integer.valueOf(compId));

        new PostDataToServerTask(context, tag)
                .setPath(AppConstant.WebURL.GET_LIST_DATA)
                .setResponseListener(listener)
                .setRequestParams(jsonObject)
                .makeCall();
    }

    public static void getSummaryListData(Context context, String tag, String menuId, String spinnerId, String extraSpinnerId, String statusId, ResponseListener listener) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("menuId", menuId);
        jsonObject.addProperty("selectedDropId", Integer.valueOf(spinnerId));
        jsonObject.addProperty("selectedStatus", Integer.valueOf(statusId));
        jsonObject.addProperty("extraSpinnerId", Integer.valueOf(extraSpinnerId));
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(context).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("logintyp", 0);
        jsonObject.addProperty("selectedCompId", 0);

        new PostDataToServerTask(context, tag)
                .setPath(AppConstant.WebURL.GET_LIST_DATA)
                .setResponseListener(listener)
                .setRequestParams(jsonObject)
                .makeCall();
    }

    public static void getMenuData(Context context, String menuId, ResponseListener listener)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("menuId", menuId);
        jsonObject.addProperty("spinnerId", "0");
        jsonObject.addProperty("statusId", "0");
        jsonObject.addProperty("monthId", "0");
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(context).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("compId", "0");

        new PostDataToServerTask(context, AppConstant.Actions.GET_MENU_DATA)
                .setPath(AppConstant.WebURL.GET_MENU_DATA)
                .setResponseListener(listener)
                .setRequestParams(jsonObject)
                .makeCall();

    }
}
