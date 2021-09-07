package com.catch32.zumpbeta.listener;


import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;


public class TabSelectedListener implements TabLayout.OnTabSelectedListener {

    private ViewPager mViewPager;
    public TabSelectedListener(ViewPager viewPager){
        mViewPager = viewPager;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
