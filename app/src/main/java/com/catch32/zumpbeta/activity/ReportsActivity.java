package com.catch32.zumpbeta.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.adapter.FragmentPagerAdapter;
import com.catch32.zumpbeta.fragment.BrandFragment;
import com.catch32.zumpbeta.fragment.DistributorFragment;
import com.catch32.zumpbeta.fragment.MonthlyProductSummaryFragment;
import com.catch32.zumpbeta.fragment.OrderProductSummaryFragment;
import com.catch32.zumpbeta.fragment.ProductTypeFragment;
import com.catch32.zumpbeta.listener.TabSelectedListener;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {
    private List<Fragment> mFragmentList;
    private Context mContext;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        mFragmentList = new ArrayList<>();
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        addTab(new DistributorFragment(), "Vendors");
        addTab(new BrandFragment(), "Brands");
        addTab(new ProductTypeFragment(), "Product Types");
        addTab(new OrderProductSummaryFragment(), "Ordered Product Summary");
        addTab(new MonthlyProductSummaryFragment(), "Month Wise Product Summary");

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setOnTabSelectedListener(new TabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }
    private void addTab(Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mTabLayout.addTab(mTabLayout.newTab().setText(title));
    }
}
