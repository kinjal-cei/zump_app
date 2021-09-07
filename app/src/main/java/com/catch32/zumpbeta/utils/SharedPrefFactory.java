package com.catch32.zumpbeta.utils;


import android.content.Context;
import com.catch32.zumpapp.BuildConfig;

public class SharedPrefFactory
{
    private static final String PROFILE_SHARED_PREF_FILE_NAME = BuildConfig.APPLICATION_ID;


    private static SharedPrefUtil mProfileSharedPrefUtil;


    public static SharedPrefUtil getProfileSharedPreference(Context context)
    {
        if (mProfileSharedPrefUtil == null)
        {
            mProfileSharedPrefUtil = SharedPrefUtil.getSharedPreferences(context, PROFILE_SHARED_PREF_FILE_NAME);
        }
        return mProfileSharedPrefUtil;
    }

    public static void clear() {
        if (mProfileSharedPrefUtil != null) mProfileSharedPrefUtil.clear();
    }

}
