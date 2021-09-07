package com.catch32.zumpbeta.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.fragment.AboutUsFragment;
import com.catch32.zumpbeta.fragment.CallUsFragment;
import com.catch32.zumpbeta.fragment.DashboardFragment;
import com.catch32.zumpbeta.fragment.DraftFragment;
import com.catch32.zumpbeta.fragment.FeedbackFragment;
import com.catch32.zumpbeta.fragment.MonthlySummaryFragment;
import com.catch32.zumpbeta.fragment.MyOrdersFragment;
import com.catch32.zumpbeta.fragment.MyShopFragment;
import com.catch32.zumpbeta.fragment.NewProductMyAreaFragment;
import com.catch32.zumpbeta.fragment.PolicyFragment;
import com.catch32.zumpbeta.fragment.ProfileFragment;
import com.catch32.zumpbeta.fragment.RaisedOrderFragment;
import com.catch32.zumpbeta.fragment.TermsConditionsFragment;
import com.catch32.zumpbeta.listener.OnBackPressedListener;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.RaisedOrder;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class LeftpanelActivity extends AppCompatActivity implements IBaseMenuActivity, NavigationView.OnNavigationItemSelectedListener, ResponseListener {
    private Context mContext; //SB20191231
    private static final String TAG = MainActivity.class.getSimpleName();
    private AppBarLayout mAppBarLayout;
    public static DrawerLayout mDrawerLayout;
    private NavigationView mNavigationViewLeft;
    private Map<Integer, Fragment> mFragmentMap;
    private Set<OnBackPressedListener> mOnActivityBackPressListenerSet;
    private TextView mRaisedOrderTv;
    private TextView mRetailer; //SB20200322
    String Outlet=""; //SB20200323



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initMenuFragment();
        initView();
    }

    private void initToolbar() {
        Outlet=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY); //SB20191216
        mAppBarLayout = (AppBarLayout) findViewById(R.id.lyt_app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{}
        };

        int[] colors = new int[]{
                Color.parseColor("#2752C5"),
                Color.parseColor("#212121")
        };

        ColorStateList navigationViewColorStateList = new ColorStateList(states, colors);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        mNavigationViewLeft = (NavigationView) findViewById(R.id.nav_view_left);
        mNavigationViewLeft.setNavigationItemSelectedListener(this);

        mNavigationViewLeft.setItemTextColor(navigationViewColorStateList);
        mNavigationViewLeft.setItemIconTintList(navigationViewColorStateList);

        // mRetailer=mNavigationViewLeft.findViewById(R.id.nav_view_left);
        // mRetailer.setText("SB");

        ///  setActionbarSubtitleTitle("**");
        String Outlet2= SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY); //SB20191216


    }

    private void initMenuFragment() {
        if (mFragmentMap == null)
            mFragmentMap = new LinkedHashMap<>();
        if (mOnActivityBackPressListenerSet == null)
            mOnActivityBackPressListenerSet = new LinkedHashSet<>();
        addMenuFragment(new DashboardFragment(), R.id.nav_home);

        addMenuFragment(new ProfileFragment(), R.id.nav_profile);

        //   addMenuFragment(new SettingsFragment(), R.id.nav_settings);

        //addMenuFragment(new MyOrdersFragment(), R.id.nav_new_order);
        addMenuFragment(new MyOrdersFragment(), R.id.nav_my_order);
        addMenuFragment(new MyShopFragment(), R.id.nav_my_shop); //SB20191211
        addMenuFragment(new NewProductMyAreaFragment(), R.id.nav_add_more_brands); //SB20191211
        addMenuFragment(new DraftFragment(), R.id.nav_my_draft); //SB20191211
        addMenuFragment(new MonthlySummaryFragment(), R.id.nav_my_summary); //SB20191211
       // addMenuFragment(new MonthlySummaryGstFragment(), R.id.nav_my_gst_summary); //SB20191211
        if(!Outlet.equals(""))
            addMenuFragment(new RaisedOrderFragment(), R.id.nav_order_status); //SB20191211
        addMenuFragment(new PolicyFragment(), R.id.nav_policy); //SB20191211
        addMenuFragment(new TermsConditionsFragment(), R.id.nav_terms_conditions); //SB20191211
        addMenuFragment(new CallUsFragment(), R.id.call_us); //SB20191211
        addMenuFragment(new AboutUsFragment(), R.id.about_us); //SB20191216
        addMenuFragment(new FeedbackFragment(), R.id.nav_feedback); //SB20191223
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    private void addMenuFragment(Fragment fragment, int id) {
        mFragmentMap.put(id, fragment);
    }

    private void initView() {
        /* Load Center View*/
        Fragment initialFragment = new ArrayList<>(mFragmentMap.values()).get(0);
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String fragmentName = bundle.getString(AppConstant.FRAGMENT);
            if (fragmentName != null) {
                try {
                    Class<?> baseCommandHandler = Class.forName(fragmentName);
                    initialFragment = (Fragment) baseCommandHandler.newInstance();
                    initialFragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        replaceFragment(initialFragment);
    }

    public void onResponse(String tag, String response) {
        mRaisedOrderTv = findViewById(R.id.txt_raised_orders_count);
        mRaisedOrderTv.setText("0");
        Gson gson = GsonFactory.getInstance();
        SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(mContext);
        RaisedOrder raisedOrder = gson.fromJson(response, RaisedOrder.class);

        if (raisedOrder.getOrderList() != null && !raisedOrder.getOrderList().isEmpty())
            mRaisedOrderTv.setText(raisedOrder.getOrderList().size() + "");
        else
            mRaisedOrderTv.setText("0");

    }

    public boolean closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            return false;
        }
        return true;
    }

    public void setActionbarTitle(int resId) {
        setActionbarTitle(getText(resId));
    }

    public void setActionbarTitle(CharSequence title) {
      /*SB20191231  ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }*/
    }

    @Override
    public void setActionbarSubtitleTitle(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String Outlet=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY); //SB20191216
            String subTitle = Outlet;//SharedPrefFactory.getProfileSharedPreference(this).getString(BaseSharedPref.STORE_NAME_KEY);
            actionBar.setSubtitle(subTitle);
        }
    }
    public void checkMenuItem(int menuId) {
        mNavigationViewLeft.setCheckedItem(menuId);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            boolean handle = false;
            if (mOnActivityBackPressListenerSet != null) {
                for (OnBackPressedListener listener : mOnActivityBackPressListenerSet) {
                    if (listener.onBackPressed()) {
                        handle = true;
                    }
                }
            }

            if (!handle) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (1 == fragmentManager.getBackStackEntryCount()) {
                    FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(0);
                    Fragment initialFragment = new ArrayList<>(mFragmentMap.values()).get(0);
                    if (entry.getName() != null && !initialFragment.getClass().getName().equals(entry.getName())) {
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        replaceFragment(initialFragment, null);
                    } else {
                        // finishAffinity();
                    }
                } else {
                    super.onBackPressed();
                }
            }
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            default:
                Fragment fragment = mFragmentMap.get(item.getItemId());
                replaceFragmentAfterDelay(fragment);
                break;
        }

        closeDrawer();
        return true;
    }

    private void setElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppBarLayout.setElevation(elevation);
        }
    }

    public void setElevation() {
        setElevation(25.0f);
    }

    public void hideElevation() {
        setElevation(0.0f);
    }

    private final Handler mDrawerHandler = new Handler();

    public void replaceFragmentAfterDelay(final Fragment fragment) {
        mDrawerHandler.removeCallbacksAndMessages(null);
        mDrawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceFragment(fragment);
            }
        }, 350);
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment != null) replaceFragment(fragment, fragment.getClass().getName());
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            boolean fragmentPopped = false;
            if (backStackEntryCount > 0)
                fragmentPopped = fragmentManager.popBackStackImmediate(fragmentTag, 0);

            if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.nav_menu_main_frame_container, fragment, fragmentTag);
                ft.addToBackStack(fragmentTag);
                ft.commitAllowingStateLoss();
            } else {
                Log.e(TAG, "Fragment popped from back stack.");
            }
        } else {
            Log.e(TAG, "Error in replace fragment");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2294) {
            replaceFragment(new RaisedOrderFragment());
        }
    }

    @Override
    public void registerActivityBackPressListener(OnBackPressedListener listener) {
        mOnActivityBackPressListenerSet.add(listener);
    }

    @Override
    public void unregisterActivityBackPressListener(OnBackPressedListener listener) {
        mOnActivityBackPressListenerSet.remove(listener);
    }
    @Override
    public void onError(String tag, String error) {
        if (error.isEmpty()) error = "Something went wrong.";
        WidgetUtil.showErrorToast(this, error);
    }

}

