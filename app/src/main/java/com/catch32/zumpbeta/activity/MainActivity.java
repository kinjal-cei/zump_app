package com.catch32.zumpbeta.activity;

import android.content.DialogInterface;
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
import android.widget.Button;
import android.content.Context; //SB20191231
import android.widget.TextView; //SB20191231

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;

import com.catch32.zumpbeta.fragment.FeedbackFragment;
import com.catch32.zumpbeta.fragment.MainContentFragment;
import com.catch32.zumpbeta.fragment.MyOrdersFragment;
import com.catch32.zumpbeta.fragment.NewProductMyAreaFragment; //SB20191211
import com.catch32.zumpbeta.fragment.DraftFragment; //SB20191211
import com.catch32.zumpbeta.fragment.MonthlySummaryFragment; //SB20191211
import com.catch32.zumpbeta.fragment.RaisedOrderFragment; //SB20191211
import com.catch32.zumpbeta.fragment.ProfileFragment;
import com.catch32.zumpbeta.fragment.PolicyFragment; //SB20191211
import com.catch32.zumpbeta.fragment.AboutUsFragment; //SB20191211
import com.catch32.zumpbeta.fragment.TermsConditionsFragment; //SB20191211
import com.catch32.zumpbeta.fragment.CallUsFragment; //SB20191211
import com.catch32.zumpbeta.listener.OnBackPressedListener;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.RaisedOrder;
import com.catch32.zumpbeta.myactivity.MyShop;
import com.catch32.zumpbeta.process.AppUserManager;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements IBaseMenuActivity, NavigationView.OnNavigationItemSelectedListener,
        ResponseListener
{
    private Context mContext;
    private static final String TAG = MainActivity.class.getSimpleName();
    private AppBarLayout mAppBarLayout;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationViewLeft;
    private Map<Integer, Fragment> mFragmentMap;
    private Set<OnBackPressedListener> mOnActivityBackPressListenerSet;
    private TextView mRaisedOrderTv;
    private TextView mRetailer; //SB20200322
    String Outlet=""; //SB20200323
    public CardView cardview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initMenuFragment();
        initView();

//        initCentreView();
    }
    private void initToolbar()
    {
        Outlet=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY); //SB20191216
        mAppBarLayout = (AppBarLayout) findViewById(R.id.lyt_app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        int[][] states = new int[][]
                {
                new int[]{android.R.attr.state_checked},
                new int[]{}
        };

        int[] colors = new int[]
                {
                Color.parseColor("#2752C5"),
                Color.parseColor("#212121")
        };

        ColorStateList navigationViewColorStateList = new ColorStateList(states, colors);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {

            @Override
            public void onDrawerStateChanged(int newState)
            {
                super.onDrawerStateChanged(newState);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationViewLeft = (NavigationView) findViewById(R.id.nav_view_left);
        mNavigationViewLeft.setNavigationItemSelectedListener(this);

        mNavigationViewLeft.setItemTextColor(navigationViewColorStateList);
        mNavigationViewLeft.setItemIconTintList(navigationViewColorStateList);

        String Outlet2=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY); //SB20191216
      //  TextView header = findViewById(R.id.txt_user);
    }
    private void initMenuFragment()
    {
        if (mFragmentMap == null)
            mFragmentMap = new LinkedHashMap<>();
        if (mOnActivityBackPressListenerSet == null)
            mOnActivityBackPressListenerSet = new LinkedHashSet<>();
        addMenuFragment(new MainContentFragment(), R.id.nav_home);

        addMenuFragment(new ProfileFragment(), R.id.nav_profile);
        //addMenuFragment(new SettingsFragment(), R.id.nav_settings);
        //addMenuFragment(new MyOrdersFragment(), R.id.nav_new_order);
        addMenuFragment(new MyOrdersFragment(), R.id.nav_my_order);
      //  addMenuFragment(new MyShopFragment(), R.id.nav_my_shop);//SB20191211
        addMenuFragment(new NewProductMyAreaFragment(), R.id.nav_add_more_brands);//SB20191211
        addMenuFragment(new DraftFragment(), R.id.nav_my_draft); //SB20191211
        addMenuFragment(new MonthlySummaryFragment(), R.id.nav_my_summary); //SB20191211
        //addMenuFragment(new MonthlySummaryGstFragment(), R.id.nav_my_gst_summary); //SB20191211
        if(!Outlet.equals("")) addMenuFragment(new RaisedOrderFragment(), R.id.nav_order_status); //SB20191211
        addMenuFragment(new PolicyFragment(), R.id.nav_policy); //SB20191211
        addMenuFragment(new TermsConditionsFragment(), R.id.nav_terms_conditions); //SB20191211
        addMenuFragment(new CallUsFragment(), R.id.call_us); //SB20191211
        addMenuFragment(new AboutUsFragment(), R.id.about_us); //SB20191216
        addMenuFragment(new FeedbackFragment(), R.id.nav_feedback); //SB20191223
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String Outlet=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY); //SB20191216

        switch (item.getItemId())
        {
            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.ic_logout_red_48dp);
                builder.setTitle("ALERT");
                builder.setMessage(Outlet+"\n"+"Are you sure you want to logout?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        AppUserManager.logout(MainActivity.this, SignInActivity.class);
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));


                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));


            return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void addMenuFragment(Fragment fragment, int id) {
        mFragmentMap.put(id, fragment);
    }

   private void initView()
   {
        /* Load Center View*/
        Fragment initialFragment = new ArrayList<>(mFragmentMap.values()).get(0);
        if (getIntent() != null && getIntent().getExtras() != null)
        {
            Bundle bundle = getIntent().getExtras();
            String fragmentName = bundle.getString(AppConstant.FRAGMENT);
            if (fragmentName != null)
            {
                try {
                    Class<?> baseCommandHandler = Class.forName(fragmentName);
                    initialFragment = (Fragment) baseCommandHandler.newInstance();
                    initialFragment.setArguments(bundle);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        replaceFragment(initialFragment);
    }

    public void BookMyOrderOnClick(View view)
    {
        cardview = (CardView) findViewById(R.id.lyt_new_order);
        cardview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NewOrderActivity();
            }
        });
    }

    public void NewOrderActivity()
    {
        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
    }
    /*
    private void initCentreView(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.your_placeholder, new MainContentFragment());
        ft.commit();
    }
*/
    public void onResponse(String tag, String response)
    {
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

    public boolean closeDrawer()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else
            {
            return false;
        }
        return true;
    }

    public void setActionbarTitle(int resId)
    {
        setActionbarTitle(getText(resId));
    }

    public void setActionbarTitle(CharSequence title)
    {
    }
    @Override
    public void setActionbarSubtitleTitle(CharSequence title)
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            String Outlet=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY); //SB20191216
            String subTitle = Outlet;//SharedPrefFactory.getProfileSharedPreference(this).getString(BaseSharedPref.STORE_NAME_KEY);
            actionBar.setSubtitle(subTitle);
        }
    }

    public void checkMenuItem(int menuId)
    {
        mNavigationViewLeft.setCheckedItem(menuId);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed()
    {
        if (!closeDrawer())
        {
            boolean handle = false;
            if (mOnActivityBackPressListenerSet != null)
            {
                for (OnBackPressedListener listener : mOnActivityBackPressListenerSet)
                {
                    if (listener.onBackPressed())
                    {
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_my_shop:
                Intent i = new Intent(MainActivity.this, MyShop.class);
                startActivity(i);

            default:
                Fragment fragment = mFragmentMap.get(item.getItemId());
                replaceFragmentAfterDelay(fragment);
                break;
        }

        closeDrawer();
        return true;
    }

    private void setElevation(float elevation)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
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

    public void replaceFragment(Fragment fragment, String fragmentTag)
    {
        if (fragment != null)
        {
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
