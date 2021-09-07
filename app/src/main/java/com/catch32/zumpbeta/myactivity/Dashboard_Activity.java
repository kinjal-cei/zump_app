package com.catch32.zumpbeta.myactivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.MainActivity;
import com.catch32.zumpbeta.activity.SignInActivity;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.fragment.PolicyFragment;
import com.catch32.zumpbeta.fragment.TermsConditionsFragment;
import com.catch32.zumpbeta.listener.OnBackPressedListener;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.RaisedOrder;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.process.AppUserManager;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class Dashboard_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ResponseListener
{
    int initialScrollY = 0;
    LinearLayout Menu,BookMyNewOrder;
    String Outlet=""; //SB20200323
    Context mContext; //SB20191231
    TextView mRaisedOrderTv;
    TextView mPolicyTerms; //SB20191218
    TextView mRaisedNotificationTv; //SB20191226
    TextView mTextMsgTv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);

            mContext = this;
            Outlet = SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY); //SB20191216

            final NestedScrollView nsvDashboard = findViewById(R.id.nsvDashboard);
            final LinearLayout llFooter = findViewById(R.id.llFooter);
            final int initialViewHeight = llFooter.getLayoutParams().height;
            initialScrollY = nsvDashboard.getScrollY();
            Menu = findViewById(R.id.Menu);
            BookMyNewOrder = findViewById(R.id.BookMyNewOrder);

            mTextMsgTv = findViewById(R.id.txt_contribution);
            mPolicyTerms = findViewById(R.id.txt_notification_count);
            mRaisedOrderTv = findViewById(R.id.txt_raised_orders_count);
            mRaisedNotificationTv = findViewById(R.id.txt_notification_count);

            mContext = this;
            String FirstTimeUserDtl = "Please Click "; //SB20191216
            String FirstTimePolicyUser = SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
            String FirstTimeTermUser = SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_TERM_KEY); //SB20191218
            String UniqueKey = "" + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.UNIQUE_KEY); //SB20200606

            if (FirstTimePolicyUser.equals("N"))
                FirstTimeUserDtl = FirstTimeUserDtl + "\"Privacy Policy\"";
            if (FirstTimeTermUser.equals("N"))
                FirstTimeUserDtl = FirstTimeUserDtl + " \"Terms & Conditions\"";

            TextView greetingTv = findViewById(R.id.txt_greeting);
            if (FirstTimePolicyUser.equals("N") || FirstTimeTermUser.equals("N")) //SB20191216
                greetingTv.setText("[" + FirstTimeUserDtl + "]\n" + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY));
            else
                greetingTv.setText("Last Login: " + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.LAST_LOGIN) + "\n" + "Hello [" + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY) + "] " + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY));
            String msg = "";
            String msgCondition = "";
            if (UniqueKey.equals("")) mTextMsgTv.setBackgroundColor(000000);
            msg = SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.LAST_LOGIN_MSG);
            mTextMsgTv.setText(msg);
            mRaisedOrderTv.setText("0");
            mRaisedNotificationTv.setText("0");
            mPolicyTerms.setText("0");
            if (FirstTimePolicyUser.equals("N")) mPolicyTerms.setText("0");
            if (FirstTimeTermUser.equals("N")) mPolicyTerms.setText("0");
            getRaisedOrders();

            BookMyNewOrder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(Dashboard_Activity.this, Book_My_New_Order_Activity.class);
                    startActivity(intent);
                }
            });

            TextView txtLastLogin = findViewById(R.id.txtLastLogin);
            txtLastLogin.setText("Last Login: " + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.LAST_LOGIN));
            TextView txtName = findViewById(R.id.txtName);
            txtName.setText("Hello, [" + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY) + "] " + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY));

            nsvDashboard.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
            {
                @Override
                public void onScrollChanged()
                {
                    ValueAnimator animator = ValueAnimator.ofInt(0, 1);
                    ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                    /* get the maximum height which we have scroll before performing any action */
                    int maxDistance = llFooter.getHeight();
                    /* how much we have scrolled */
                    float movement = nsvDashboard.getScrollY();
                    /*finally calculate the alpha factor and set on the view */
                    //float alphaFactor = ((movement * 1.0f) / (maxDistance + llFooter.getHeight()));
                    if (movement > initialScrollY) {

                        if (!animator.isRunning()) {
                            //Setting animation from actual value to the initial yourViewToHide height)
                            animator.setIntValues(params.height, 0);
                            //Animation duration
                            animator.setDuration(100);
                            //In this listener we update the view
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                                    params.height = (int) animation.getAnimatedValue();
                                    llFooter.setLayoutParams(params);
                                }
                            });
                            //Starting the animation
                            animator.start();
                        }
                        /*for image parallax with scroll */
                        //llFooter.setLayoutParams();
                        /* set visibility */
                        //llFooter.setAlpha(0);
                    } else {
                        if (!animator.isRunning()) {
                            //Setting animation from actual value to the target value (here 0, because we're hiding the view)
                            animator.setIntValues(params.height, initialViewHeight);
                            //Animation duration
                            animator.setDuration(500);
                            //In this listener we update the view
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                                    params.height = (int) animation.getAnimatedValue();
                                    llFooter.setLayoutParams(params);
                                }
                            });
                            //Starting the animation
                            animator.start();

                        }
                    }

                    initialScrollY = nsvDashboard.getScrollY();
                }
            });

            CardView lytorderStatus = findViewById(R.id.lytorderStatus);
            lytorderStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, Order_Status.class);
                    startActivity(i);
                }
            });

            CardView lyt_myOrders = findViewById(R.id.lyt_myOrders);
            lyt_myOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, My_Order_Activity.class);
                    startActivity(i);
                }
            });

            CardView lyt_shop = findViewById(R.id.lyt_shop);
            lyt_shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, MyShop.class);
                    startActivity(i);
                }
            });

            CardView lyt_addmorebrands = findViewById(R.id.lyt_addmorebrands);
            lyt_addmorebrands.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, Add_More_Brands.class);
                    startActivity(i);
                }
            });

            CardView lyt_my_reward = findViewById(R.id.lyt_my_reward);
            lyt_my_reward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, My_Rewards.class);
                    startActivity(i);
                }
            });

            CardView lyt_reports = findViewById(R.id.lyt_reports);
            lyt_reports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, MyShop.class);
                    startActivity(i);
                }
            });

            CardView lyt_summary = findViewById(R.id.lyt_summary);
            lyt_summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, Month_Wise_Summary.class);
                    startActivity(i);
                }
            });
            CardView lytBookMyorder = findViewById(R.id.lytBookMyorder);
            lytBookMyorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, Book_My_New_Order_Activity.class);
                    startActivity(i);
                }
            });
            CardView lyt_notification = findViewById(R.id.lyt_notification);
            lyt_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, Notification.class);
                    startActivity(i);
                }
            });

            CardView lyt_drafts = findViewById(R.id.lyt_drafts);
            lyt_drafts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Dashboard_Activity.this, Draft_Orders.class);
                    startActivity(i);
                }
            });
            final ImageView img1 = findViewById(R.id.img1);
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*Dialog dialog = new Dialog(Dashboard_Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.image_zoom);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView img = dialog.findViewById(R.id.img);
                img.setImageDrawable(img1.getDrawable());

                dialog.show();*/

                    Intent i = new Intent(Dashboard_Activity.this, ProductDetail.class);
                    startActivity(i);
                }
            });

            LinearLayout llReports = findViewById(R.id.llReports);
            llReports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Dashboard_Activity.this, MyShop.class);
                    startActivity(i);
                }
            });

            LinearLayout llOrderStatus = findViewById(R.id.llOrderStatus);
            llOrderStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Dashboard_Activity.this, My_Order_Activity.class);
                    startActivity(i);
                }
            });

            LinearLayout llMonthwiseSummary = findViewById(R.id.llMonthwiseSummary);
            llMonthwiseSummary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Dashboard_Activity.this, Month_Wise_Summary.class);
                    startActivity(i);
                }
            });

            TextView txt = findViewById(R.id.text);
            txt.setSelected(true);

        /*txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                TextView txt = findViewById(R.id.text);
                txt.setSelected(false);

                return false;
            }
        });*/

            final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
            //   String Outlet2= SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY); //SB20191216
            //  TextView header = findViewById(R.id.txt_user);
            navigationView.setNavigationItemSelectedListener(this);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int vid = item.getItemId();

        if (vid == R.id.nav_home)
        {
          //  Intent i=new Intent(Dashboard_Activity.this, Dashboard_Activity.class);
           // startActivity(i);
        }
        else if (vid == R.id.nav_book_my_order)
        {
            Intent i = new Intent(Dashboard_Activity.this, Book_My_New_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_order_status)
        {
            Intent i = new Intent(Dashboard_Activity.this, Order_Status.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_order)
        {
            Intent i = new Intent(Dashboard_Activity.this, My_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_shop)
        {
            Intent i = new Intent(Dashboard_Activity.this, MyShop.class);
            startActivity(i);
        }

        else if(vid == R.id.nav_add_more_brands)
        {
            Intent i = new Intent(Dashboard_Activity.this, Add_More_Brands.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_reward)
        {
            Intent i = new Intent(Dashboard_Activity.this, My_Rewards.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_summary)
        {
            Intent i = new Intent(Dashboard_Activity.this, Month_Wise_Summary.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_draft)
        {
            Intent i = new Intent(Dashboard_Activity.this, Draft_Orders.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_feedback)
        {
            Intent i=new Intent(Dashboard_Activity.this, Feedback_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_profile)
        {
             Intent i=new Intent(Dashboard_Activity.this, Profile_Activity.class);
             startActivity(i);
        }
          else if(vid == R.id.nav_Notification)
        {
             Intent i=new Intent(Dashboard_Activity.this, Notification.class);
             startActivity(i);
        }
        else if(vid == R.id.nav_policy)
        {
            Intent i=new Intent(Dashboard_Activity.this, Privacy_Policy.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_terms_conditions)
        {
            Intent i=new Intent(Dashboard_Activity.this, TermsAndCondition.class);
            startActivity(i);
        }
        else if(vid == R.id.about_us)
        {
            Intent i=new Intent(Dashboard_Activity.this, About_Us.class);
            startActivity(i);
        }
        else if(vid == R.id.call_us)
        {
            Intent i=new Intent(Dashboard_Activity.this, Contact_Us.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_logout)
        {
            String Outlet=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY); //SB20191216

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ic_logout_red_48dp);
            builder.setTitle("ALERT");
            builder.setMessage(Outlet+"\n"+"Are you sure you want to logout?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    AppUserManager.logout(Dashboard_Activity.this, SignInActivity.class);
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

    private void getRaisedOrders()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("distcd", "0");
        jsonObject.addProperty("compcd", "0");

        new PostDataToServerTask(mContext, AppConstant.Actions.GET_RAISED_ORDERS)
                .setPath(AppConstant.WebURL.GET_RAISED_ORDERS_PATH)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }

    @Override
    public void onResponse(String tag, String response)
    {
        Gson gson = GsonFactory.getInstance();
        SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(mContext);
        RaisedOrder raisedOrder = gson.fromJson(response, RaisedOrder.class);

        if (raisedOrder.getOrderList() != null && !raisedOrder.getOrderList().isEmpty())
            mRaisedOrderTv.setText(raisedOrder.getOrderList().size() + "");
        else
            mRaisedOrderTv.setText("0");

        com.catch32.zumpbeta.model.Notification notification = gson.fromJson(response, com.catch32.zumpbeta.model.Notification.class); //SB20191227
        if (notification.getNotificationList() != null && !notification.getNotificationList().isEmpty())
            mRaisedNotificationTv.setText(notification.getNotificationList().size() + "");
        else
            mRaisedNotificationTv.setText("0");
        /////////////////SB20191220//////////////////////////////////////////////
        String FirstTimeUserDtl="Please Click "; //SB20191216
        String FirstTimePolicyUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
        String FirstTimeTermUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_TERM_KEY); //SB20191218

        if (FirstTimePolicyUser.equals("N")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Privacy Policy Detail");
            builder.setMessage("Please Submit Privacy Policy");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            positiveButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));

            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
            negativeButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));

            sharedPrefUtil.putString(BaseSharedPref.STORE_NAME_KEY, raisedOrder.getName());
        }
        else if (FirstTimeTermUser.equals("N")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Terms & Conditions");
            builder.setMessage("Please Submit Terms & Conditions");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            positiveButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));

            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
            negativeButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));

            sharedPrefUtil.putString(BaseSharedPref.STORE_NAME_KEY, raisedOrder.getName());
        }
    }
    @Override
    public void onError(String tag, String error)
    {
        WidgetUtil.showErrorToast(mContext, error);
    }
}
