package com.catch32.zumpbeta.process;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.catch32.zumpbeta.utils.SharedPrefFactory;

import androidx.fragment.app.FragmentActivity;

public class AppUserManager {

    private static String TAG = AppUserManager.class.getSimpleName();

    public static void logout(Context context, Class activityClass) {
        resetAppInfo(context);
        logoutFromCurrentActivity(context, activityClass);
    }


    public static void resetAppInfo(Context context) {
        Log.w(TAG, "Resetting App");
        //Clear SharedPreference Database
        SharedPrefFactory.getProfileSharedPreference(context).clear();
    }

    public static void logoutFromCurrentActivity(Context context, Class nextActivityClass) {
        Intent intent = new Intent(context, nextActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        if (context instanceof FragmentActivity) {
            ((FragmentActivity) context).finish();
        }
    }
}
