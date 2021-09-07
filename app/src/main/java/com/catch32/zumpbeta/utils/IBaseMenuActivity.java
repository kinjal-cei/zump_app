package com.catch32.zumpbeta.utils;

import com.catch32.zumpbeta.listener.OnBackPressedListener;

import androidx.fragment.app.Fragment;


public interface IBaseMenuActivity
{

    /**
     * @param title String to set as Action Bar Title
     */
    void setActionbarTitle(CharSequence title);

    void setActionbarSubtitleTitle(CharSequence title);

    /**
     * @param resId resource to set as Action Bar Title
     */
    void setActionbarTitle(int resId);

    /**
     * @param fragment Fragment to replace
     */
    void replaceFragment(Fragment fragment);

    /**
     * @param menuId menu to check in {@link com.google.android.material.internal.NavigationMenu}
     */
    void checkMenuItem(int menuId);

    /**
     * set Toolbar Elevation
     */
    void setElevation();

    /**
     * remove Toolbar Elevation
     */
    void hideElevation();

    void registerActivityBackPressListener(OnBackPressedListener listener);

    /**
     * Unregister Activity Back Press Listener
     */
    void unregisterActivityBackPressListener(OnBackPressedListener listener);

}
