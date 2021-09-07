package com.catch32.zumpbeta.myactivity;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.SignInActivity;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.process.AppUserManager;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ProductDetail extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener,NavigationView.OnNavigationItemSelectedListener
{
    SliderLayout mDemoSlider;
    int initialScrollY = 0;
    String userCd;
    private Context mContext;
    LinearLayout NewOrderNow;
    LinearLayout Menu,BookMyNewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mContext = this;
        Menu=findViewById(R.id.Menu);

        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);

        BookMyNewOrder=findViewById(R.id.BookMyNewOrder);
        BookMyNewOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(ProductDetail.this, Book_My_New_Order_Activity.class);
                startActivity(intent);
            }
        });


        NewOrderNow=findViewById(R.id.NewOrderNow);

        final NestedScrollView nsvDashboard = findViewById(R.id.nsvDashboard);
        final LinearLayout llFooter = findViewById(R.id.llFooter);
        final int initialViewHeight = llFooter.getLayoutParams().height;
        initialScrollY = nsvDashboard.getScrollY();

        nsvDashboard.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
        {
            @Override
            public void onScrollChanged()
            {

                ValueAnimator animator = ValueAnimator.ofInt(0, 1);
                ViewGroup.LayoutParams params = llFooter.getLayoutParams();

                int maxDistance = llFooter.getHeight();
                float movement = nsvDashboard.getScrollY();
                if (movement > initialScrollY)
                {
                    if (!animator.isRunning())
                    {
                        animator.setIntValues(params.height, 0);
                        animator.setDuration(100);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation)
                            {
                                ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                                params.height = (int) animation.getAnimatedValue();
                                llFooter.setLayoutParams(params);
                            }
                        });
                        animator.start();
                    }
                }
                else
                {
                    if (!animator.isRunning())
                    {
                        animator.setIntValues(params.height, initialViewHeight);
                        animator.setDuration(500);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation)
                            {
                                ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                                params.height = (int) animation.getAnimatedValue();
                                llFooter.setLayoutParams(params);
                            }
                        });
                        animator.start();
                    }
                }
                initialScrollY = nsvDashboard.getScrollY();
            }
        });

        final LinearLayout moreOffers = findViewById(R.id.moreOffers);
        final LinearLayout closeOffers = findViewById(R.id.closeOffers);

        final LinearLayout offer2 = findViewById(R.id.offer2);
        final LinearLayout offer3 = findViewById(R.id.offer3);
        final LinearLayout offer4 = findViewById(R.id.offer4);

        final ImageView sp1 = findViewById(R.id.sp1);
        final ImageView sp2 = findViewById(R.id.sp2);
        final ImageView sp3 = findViewById(R.id.sp3);

        moreOffers.setVisibility(View.VISIBLE);
        closeOffers.setVisibility(View.GONE);

        offer2.setVisibility(View.GONE);
        offer3.setVisibility(View.GONE);
        offer4.setVisibility(View.GONE);

        sp1.setVisibility(View.GONE);
        sp2.setVisibility(View.GONE);
        sp3.setVisibility(View.GONE);

        NewOrderNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ProductDetail.this, Book_My_New_Order_Activity.class);
                startActivity(i);
            }
        });

        closeOffers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                moreOffers.setVisibility(View.VISIBLE);
                closeOffers.setVisibility(View.GONE);

                offer2.setVisibility(View.GONE);
                offer3.setVisibility(View.GONE);
                offer4.setVisibility(View.GONE);

                sp1.setVisibility(View.GONE);
                sp2.setVisibility(View.GONE);
                sp3.setVisibility(View.GONE);
            }
        });

        moreOffers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                moreOffers.setVisibility(View.GONE);
                closeOffers.setVisibility(View.VISIBLE);

                offer2.setVisibility(View.VISIBLE);
                offer3.setVisibility(View.VISIBLE);
                offer4.setVisibility(View.VISIBLE);

                sp1.setVisibility(View.VISIBLE);
                sp2.setVisibility(View.VISIBLE);
                sp3.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout llReports = findViewById(R.id.llReports);
        llReports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ProductDetail.this, MyShop.class);
                startActivity(i);
            }
        });

        LinearLayout llOrderStatus = findViewById(R.id.llOrderStatus);
        llOrderStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ProductDetail.this, My_Order_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llHome = findViewById(R.id.llHome);
        llHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ProductDetail.this, Dashboard_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llMonthwiseSummary = findViewById(R.id.llMonthwiseSummary);
        llMonthwiseSummary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ProductDetail.this, Month_Wise_Summary.class);
                startActivity(i);
            }
        });

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Image1",R.drawable.image_2);
        file_maps.put("Image2",R.drawable.i2);
        file_maps.put("Image3",R.drawable.image_1);

        for(String name : file_maps.keySet())
        {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        /*ListView l = (ListView)findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
                Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onStop()
    {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }
    @Override
    public void onSliderClick(BaseSliderView slider)
    {
        Dialog builder = new Dialog(ProductDetail.this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //nothing;
            }
        });
        ImageView imageView = new ImageView(ProductDetail.this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.i2));
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imageView);
        pAttacher.update();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {}
    @Override
    public void onPageSelected(int position)
    {
        Log.d("Slider Demo", "Page Changed: " + position);
    }
    @Override
    public void onPageScrollStateChanged(int state)
    {}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Handle navigation view item clicks here.
        int vid = item.getItemId();

        if (vid == R.id.nav_home)
        {
            Intent i=new Intent(ProductDetail.this, Dashboard_Activity.class);
            startActivity(i);
        }
       else if (vid == R.id.nav_book_my_order)
        {
            Intent i = new Intent(ProductDetail.this, Book_My_New_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_order_status)
        {
            Intent i = new Intent(ProductDetail.this, Order_Status.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_order)
        {
            Intent i = new Intent(ProductDetail.this, My_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_shop)
        {
            Intent i = new Intent(ProductDetail.this, MyShop.class);
            startActivity(i);
        }

        else if(vid == R.id.nav_add_more_brands)
        {
            Intent i = new Intent(ProductDetail.this, Add_More_Brands.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_reward)
        {
            Intent i = new Intent(ProductDetail.this, My_Rewards.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_summary)
        {
            Intent i = new Intent(ProductDetail.this, Month_Wise_Summary.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_draft)
        {
            Intent i = new Intent(ProductDetail.this, Draft_Orders.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_feedback)
        {
            Intent i=new Intent(ProductDetail.this, Feedback_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_profile)
        {
            Intent i=new Intent(ProductDetail.this, Profile_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_Notification)
        {
           Intent i=new Intent(ProductDetail.this, Notification.class);
           startActivity(i);
        }
        else if(vid == R.id.nav_policy)
        {
            Intent i=new Intent(ProductDetail.this, Privacy_Policy.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_terms_conditions)
        {
            Intent i=new Intent(ProductDetail.this, TermsAndCondition.class);
            startActivity(i);
        }
        else if(vid == R.id.about_us)
        {
              Intent i=new Intent(ProductDetail.this, About_Us.class);
              startActivity(i);
        }
        else if(vid == R.id.call_us)
        {
            Intent i=new Intent(ProductDetail.this, Contact_Us.class);
            startActivity(i);
        }

        else if(vid == R.id.nav_logout)
        {

            String Outlet= SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY); //SB20191216

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ic_logout_red_48dp);
            builder.setTitle("ALERT");
            builder.setMessage(Outlet+"\n"+"Are you sure you want to logout?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    AppUserManager.logout(ProductDetail.this, SignInActivity.class);
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}