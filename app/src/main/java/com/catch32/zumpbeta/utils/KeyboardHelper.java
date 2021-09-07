package com.catch32.zumpbeta.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class KeyboardHelper
{

    private KeyboardHelper()
    {
    }

    /**
     * Hides keyboard using currently focused view.<br/>
     * Shortcut for {@link #hideSoftKeyboard(Context, View...) hideSoftKeyboard(activity, activity.getCurrentFocus())}.
     */
    public static void hideSoftKeyboard(Activity activity) {
        hideSoftKeyboard(activity, activity.getCurrentFocus());
    }

    /**
     * Uses given views to hide soft keyboard and to clear current focus.
     *
     * @param context Context
     * @param views   Currently focused views
     */
    public static void hideSoftKeyboard(Context context, View... views) {
        if (views == null) return;
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        for (View currentView : views) {
            if (null == currentView) continue;
            manager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
            currentView.clearFocus();
        }
    }

    /**
     * Shows soft keyboard and requests focus for given view.
     */
    public static void showSoftKeyboard(Context context, View view) {
        if (view == null) return;
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        manager.showSoftInput(view, 0);
    }

}
