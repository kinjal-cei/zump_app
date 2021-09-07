package com.catch32.zumpbeta.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;


public class SharedPrefUtil
{
    private static final String DEFAULT_SHARED_PREFERENCE_TAG = "myHome";
    private SharedPreferences mSharedPreferences;

    private SharedPrefUtil(Context context, String tag)
    {
        if(tag==null)
        {
            tag = DEFAULT_SHARED_PREFERENCE_TAG;
        }
        mSharedPreferences = context.getSharedPreferences(tag, Activity.MODE_PRIVATE);
    }

    /**
     * Get SharedPreferences Object.
     * <br>
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link Activity} object.
     *
     * @param tag The tag to use to create SharedPreferences object.
     * <br>
     * @return Returns the SharedPreferences object
     */
    public static SharedPrefUtil getSharedPreferences(Context context, String tag) {
        return new SharedPrefUtil(context, tag);
    }

    /**
     * Get SharedPreferences Object.
     * <br>
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                 or {@link Activity} object.
     * <br>
     * @return Returns the SharedPreferences object
     */
    public static SharedPrefUtil getSharedPreferences(Context context) {
        return new SharedPrefUtil(context, DEFAULT_SHARED_PREFERENCE_TAG);
    }

    /**
     * Retrieve a String value from the preferences.
     * <br>
     * @param key      The name of the preference.
     * <br>
     * @return Returns the preference value if it exists, or null
     */
    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    /**
     * Retrieve a String value from the preferences.
     * <br>
     * @param key          The name of the preference.
     * @param defaultValue Value to return if this preference does not exist.
     * <br>
     * @return Returns the preference value if it exists, or defaultValue
     */
    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * Set a String value in the preferences
     * <br>
     * @param key      The name of the preference.
     * @param value    The value for the preference.
     * <br><br>
     * @return Returns true if the new values were successfully written
     * to persistent storage.
     */
    public SharedPrefUtil putString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        return this;
    }

    /**
     * Retrieve a int value from the preferences.
     * <br>
     * @param key      The name of the preference.
     * <br><br>
     * @return Returns the preference value if it exists, or -1
     */
    public int getInt(String key) {
        return mSharedPreferences.getInt(key, -1);
    }

    /**
     * Retrieve a int value from the preferences.
     * <br>
     * @param key          The name of the preference.
     * @param defaultValue Value to return if this preference does not exist.
     * <br><br>
     * @return Returns the preference value if it exists, or defaultValue
     */
    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Set a int value in the preferences
     * <br>
     * @param key      The name of the preference.
     * @param value    The value for the preference.
     * <br><br>
     * @return Returns true if the new values were successfully written
     * to persistent storage.
     */
    public SharedPrefUtil putInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
        return this;
    }

    /**
     * Retrieve a long value from the preferences.
     * <br>
     * @param key      The name of the preference.
     * <br><br>
     * @return Returns the preference value if it exists, or -1l
     */
    public long getLong(String key) {
        return mSharedPreferences.getLong(key, -1L);
    }

    /**
     * Retrieve a long value from the preferences.
     * <br>
     * @param key          The name of the preference.
     * @param defaultValue Value to return if this preference does not exist.
     * <br><br>
     * @return Returns the preference value if it exists, or defaultValue
     */
    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    /**
     * Set a long value in the preferences
     * <br>
     * @param key      The name of the preference.
     * @param value    The value for the preference.
     * <br><br>
     * @return Returns true if the new values were successfully written
     * to persistent storage.
     */
    public SharedPrefUtil putLong(String key, long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
        return this;
    }

    /**
     * Retrieve a boolean value from the preferences.
     * <br>
     * @param key      The name of the preference.
     * <br><br>
     * @return Returns the preference value if it exists, or false
     */
    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    /**
     * Retrieve a boolean value from the preferences.
     * <br>
     * @param key          The name of the preference.
     * @param defaultValue Value to return if this preference does not exist.
     * <br><br>
     * @return Returns the preference value if it exists, or defaultValue
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Set a boolean value in the preferences
     * <br>
     * @param key      The name of the preference.
     * @param value    The value for the preference.
     * <br><br>
     * @return Returns true if the new values were successfully written
     * to persistent storage.
     */
    public SharedPrefUtil putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
        return this;
    }

    /**
     * Retrieve a String Set from the preferences.
     *
     * @param key          The name of the preference.
     * <br><br>
     * @return Returns the preference value if it exists, or defaultValue
     */
    public Set<String> getStringSet(String key) {
        return mSharedPreferences.getStringSet(key, new LinkedHashSet<String>());
    }

    /**
     * Set a Set of String in the preferences
     *
     * @param key      The name of the preference.
     * @param value    The value for the preference.
     * <br><br>
     * @return Returns true if the new values were successfully written
     * to persistent storage.
     */
    public boolean putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    /**
     * Remove preference value.
     * <br>
     * @param key  The name of the preference to remove.
     * <br><br>
     * @return Returns true if preference value were successfully removed from
     * persistent storage.
     */
    public SharedPrefUtil remove(String key)
    {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
        return this;
    }

    /**
     * clear all preference value.
     * <br><br>
     * @return Returns true if all preference value successfully removed from
     * persistent storage.
     */
    public SharedPrefUtil clear()
    {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
        return this;
    }

}
