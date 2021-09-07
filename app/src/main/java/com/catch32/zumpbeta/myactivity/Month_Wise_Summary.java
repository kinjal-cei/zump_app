package com.catch32.zumpbeta.myactivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.androidnetworking.AndroidNetworking;
import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.SignInActivity;
import com.catch32.zumpbeta.apicall.ApiService;
import com.catch32.zumpbeta.apicall.OnResponseListener;
import com.catch32.zumpbeta.apicall.RequestType;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.process.AppUserManager;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.catch32.zumpbeta.apicall.ApiEndPoint.GET_MENU_DATA;
import static com.catch32.zumpbeta.apicall.ApiEndPoint.GET_MONTHLY_SUMMARY;

public class Month_Wise_Summary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Handler handler=new Handler();
    TextView lblFY,lblR1C1,lblR1C2,lblR1C3,lblR1C4,lblR1C5,lblR2C1,lblR2C2,lblR2C3,lblR2C4,lblR2C5,lblR3C1,lblR3C2,lblR3C3,lblR3C4,lblR3C5,
            lblR4C1,lblR4C2,lblR4C3,lblR4C4,lblR4C5,lblR5C1,lblR5C2,lblR5C3,lblR5C4,lblR5C5,lbldelivered_Amount,lbl_YearAmount,
            lbl_H1,lbl_H2,lbl_H3,lbl_H4;
    LinearLayout Menu,BookMyNewOrder;
    Spinner SpinnerMonth;
    TextView lblSummarySubTitle;
    public class Company
    {
        String comp_nm, comp_id;
    }
    public class Year
    {
        String year_nm, year_id;
    }
    ArrayList<Company> vendors = new ArrayList<Company>();
    ArrayList<Year> years = new ArrayList<Year>();

    String selectedVendor = "0";
    String selectedVendorStr = "", selectedYear = "2020-21", selectedYearStr = "", selectedMonth = "0", selectedMonthStr = "";
    int menuId = 6;

    int initialScrollY = 0;
    String userCd;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_wise_summary);
        Menu=findViewById(R.id.Menu);

        mContext = this;
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);

        BookMyNewOrder=findViewById(R.id.BookMyNewOrder);
        BookMyNewOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Month_Wise_Summary.this, Book_My_New_Order_Activity.class);
                startActivity(intent);
            }
        });


        lblFY=findViewById(R.id.lblFY);
        lblR1C1=findViewById(R.id.lblR1C1);
        lblR1C2=findViewById(R.id.lblR1C2);
        lblR1C3=findViewById(R.id.lblR1C3);
        lblR1C4=findViewById(R.id.lblR1C4);
        lblR1C5=findViewById(R.id.lblR1C5);

        lblR2C1=findViewById(R.id.lblR2C1);
        lblR2C2=findViewById(R.id.lblR2C2);
        lblR2C3=findViewById(R.id.lblR2C3);
        lblR2C4=findViewById(R.id.lblR2C4);
        lblR2C5=findViewById(R.id.lblR2C5);

        lblR3C1=findViewById(R.id.lblR3C1);
        lblR3C2=findViewById(R.id.lblR3C2);
        lblR3C3=findViewById(R.id.lblR3C3);
        lblR3C4=findViewById(R.id.lblR3C4);
        lblR3C5=findViewById(R.id.lblR3C5);

        lblR4C1=findViewById(R.id.lblR4C1);
        lblR4C2=findViewById(R.id.lblR4C2);
        lblR4C3=findViewById(R.id.lblR4C3);
        lblR4C4=findViewById(R.id.lblR4C4);
        lblR4C5=findViewById(R.id.lblR4C5);

        lblR5C1=findViewById(R.id.lblR5C1);
        lblR5C2=findViewById(R.id.lblR5C2);
        lblR5C3=findViewById(R.id.lblR5C3);
        lblR5C4=findViewById(R.id.lblR5C4);
        lblR5C5=findViewById(R.id.lblR5C5);

        lbldelivered_Amount=findViewById(R.id.lbldelivered_Amount);
        lbl_YearAmount=findViewById(R.id.lbl_YearAmount);

        lbl_H1=findViewById(R.id.lbl_H1);
        lbl_H2=findViewById(R.id.lbl_H2);
        lbl_H3=findViewById(R.id.lbl_H3);
        lbl_H4=findViewById(R.id.lbl_H4);

        lblSummarySubTitle = findViewById(R.id.lblSummarySubTitle);

        LinearLayout llOrderStatus = findViewById(R.id.llOrderStatus);
        llOrderStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Month_Wise_Summary.this, My_Order_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llHome = findViewById(R.id.llHome);
        llHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Month_Wise_Summary.this, Dashboard_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llReports = findViewById(R.id.llReports);
        llReports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Month_Wise_Summary.this, MyShop.class);
                startActivity(i);
            }
        });

        SpinnerMonth=findViewById(R.id.SpinnerMonth);
        String[] buildingtypes = new String[]
                {
                        "--All--", "January", "February", "March", "April", "May", "June", "July",
                        "August", "September", "October", "November", "December"
                };

        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_layout, R.id.text1, buildingtypes);
        SpinnerMonth.setAdapter(adapterCategories);
        SpinnerMonth.setSelection(0);

        SpinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                selectedMonth = (position)+"";

                JSONObject bodyParam = new JSONObject();
                try {
                    //bodyParam.accumulate("id","2VoTM29S");
                    bodyParam.accumulate("usercd",userCd);
                    bodyParam.accumulate("menuId",menuId);
                    bodyParam.accumulate("selectedDropId",selectedYear);
                    bodyParam.accumulate("selectedStatus",selectedVendor);
                    bodyParam.accumulate("extraSpinnerId",selectedMonth);

                    System.out.println(bodyParam);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                ApiService apiService1 = new ApiService(getSummaryData, RequestType.GET,
                        GET_MONTHLY_SUMMARY + bodyParam.toString(), new HashMap<String, String>(), bodyParam);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        final Button btnMonthwiseSummary = findViewById(R.id.btnMonthwiseSummary);
        final Button btnMonthwiseGSTSummary = findViewById(R.id.btnMonthwiseGSTSummary);
        final Button btnMonthwiseProductSummary = findViewById(R.id.btnMonthwiseProductSummary);
        final TextView lblVendor = findViewById(R.id.lblVendor);

        btnMonthwiseSummary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                menuId = 6;
                lblSummarySubTitle.setText("Orders");

                lblVendor.setText("Vendor");

                Drawable buttonDrawable = btnMonthwiseSummary.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.btnBlue));
                btnMonthwiseSummary.setBackground(buttonDrawable);

                btnMonthwiseSummary.setTextColor(Color.WHITE);

                buttonDrawable = btnMonthwiseProductSummary.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.LTGRAY);
                btnMonthwiseProductSummary.setBackground(buttonDrawable);

                btnMonthwiseProductSummary.setTextColor(Color.BLACK);

                buttonDrawable = btnMonthwiseGSTSummary.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.LTGRAY);
                btnMonthwiseGSTSummary.setBackground(buttonDrawable);

                btnMonthwiseGSTSummary.setTextColor(Color.BLACK);

                initiate();
            }
        });

        btnMonthwiseGSTSummary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                menuId = 9;
                lblSummarySubTitle.setText("GST");

                lblVendor.setText("Vendor");

                Drawable buttonDrawable = btnMonthwiseSummary.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.LTGRAY);
                btnMonthwiseSummary.setBackground(buttonDrawable);

                btnMonthwiseSummary.setTextColor(Color.BLACK);

                buttonDrawable = btnMonthwiseProductSummary.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.LTGRAY);
                btnMonthwiseProductSummary.setBackground(buttonDrawable);

                btnMonthwiseProductSummary.setTextColor(Color.BLACK);

                buttonDrawable = btnMonthwiseGSTSummary.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.btnBlue));
                btnMonthwiseGSTSummary.setBackground(buttonDrawable);

                btnMonthwiseGSTSummary.setTextColor(Color.WHITE);

                initiate();
            }
        });

        btnMonthwiseProductSummary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                menuId = 12;
                lblSummarySubTitle.setText("Products");

                lblVendor.setText("Company");

                Drawable buttonDrawable = btnMonthwiseSummary.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.LTGRAY);
                btnMonthwiseSummary.setBackground(buttonDrawable);

                btnMonthwiseSummary.setTextColor(Color.BLACK);

                buttonDrawable = btnMonthwiseProductSummary.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.btnBlue));
                btnMonthwiseProductSummary.setBackground(buttonDrawable);

                btnMonthwiseProductSummary.setTextColor(Color.WHITE);

                buttonDrawable = btnMonthwiseGSTSummary.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.LTGRAY);
                btnMonthwiseGSTSummary.setBackground(buttonDrawable);

                btnMonthwiseGSTSummary.setTextColor(Color.BLACK);

                initiate();
            }
        });

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
                }
                else
                {
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

        initiate();

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
        View v=navigationView.getHeaderView(0);
      //  String Outlet2= SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY); //SB20191216
      //  TextView header = findViewById(R.id.txt_user);

        navigationView.setNavigationItemSelectedListener(this);
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
            Intent i=new Intent(Month_Wise_Summary.this, Dashboard_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_book_my_order)
        {
            Intent i = new Intent(Month_Wise_Summary.this, Book_My_New_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_order_status)
        {
            Intent i = new Intent(Month_Wise_Summary.this, Order_Status.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_order)
        {
            Intent i = new Intent(Month_Wise_Summary.this, My_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_shop)
        {
            Intent i = new Intent(Month_Wise_Summary.this, MyShop.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_add_more_brands)
        {
            Intent i = new Intent(Month_Wise_Summary.this, Add_More_Brands.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_reward)
        {
            Intent i = new Intent(Month_Wise_Summary.this, My_Rewards.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_summary)
        {
          //  Intent i = new Intent(Month_Wise_Summary.this, Month_Wise_Summary.class);
            //startActivity(i);
        }
        else if(vid == R.id.nav_my_draft)
        {
            Intent i = new Intent(Month_Wise_Summary.this, Draft_Orders.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_feedback)
        {
             Intent i=new Intent(Month_Wise_Summary.this, Feedback_Activity.class);
             startActivity(i);
        }
        else if(vid == R.id.nav_profile)
        {
            Intent i=new Intent(Month_Wise_Summary.this, Profile_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_Notification)
        {
            Intent i=new Intent(Month_Wise_Summary.this, Notification.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_policy)
        {
            Intent i=new Intent(Month_Wise_Summary.this, Privacy_Policy.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_terms_conditions)
        {
            Intent i=new Intent(Month_Wise_Summary.this, TermsAndCondition.class);
            startActivity(i);
        }
        else if(vid == R.id.about_us)
        {
            Intent i=new Intent(Month_Wise_Summary.this, About_Us.class);
            startActivity(i);
        }
        else if(vid == R.id.call_us)
        {
            Intent i=new Intent(Month_Wise_Summary.this, Contact_Us.class);
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
                    AppUserManager.logout(Month_Wise_Summary.this, SignInActivity.class);
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

    public void initiate()
    {
        JSONObject Param = new JSONObject();
        try {
        //    Param.accumulate("id","2VoTM29S");
            Param.accumulate("usercd",userCd);
            Param.accumulate("menuId",menuId);
            Param.accumulate("statusId",0);
            Param.accumulate("spinnerId",0);
            Param.accumulate("compId",0);
            System.out.println(Param);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(getApplicationContext());

        ApiService apiService = new ApiService(getMenuData, RequestType.GET,
                GET_MENU_DATA + Param.toString(), new HashMap<String, String>(), Param);

        JSONObject bodyParam = new JSONObject();
        try {
          //  bodyParam.accumulate("id","2VoTM29S");
            bodyParam.accumulate("usercd",userCd);
            Param.accumulate("menuId",menuId);
            bodyParam.accumulate("selectedDropId",0);
            bodyParam.accumulate("selectedStatus",0);
            bodyParam.accumulate("extraSpinnerId",0);

            System.out.println(bodyParam);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        AndroidNetworking.initialize(getApplicationContext());

        ApiService apiService1 = new ApiService(getSummaryData, RequestType.GET,
                GET_MONTHLY_SUMMARY,  new HashMap<String, String>(), bodyParam);

    }

    OnResponseListener getMenuData = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response1, HashMap<String, String> headers)
        {
            final String response = response1;
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        System.out.println(responseJSON);

                        String loginSuccess = responseJSON.getString("loginSuccess");
                        String menuData = responseJSON.getString("menuData");
                        String spinnerId = responseJSON.getString("spinnerId");
                        String statusId = responseJSON.getString("statusId");

                        if (loginSuccess.equals("Y"))
                        {
                            int len;

                            //Status List
                            final JSONArray yearList = responseJSON.getJSONArray("yearList");
                            len = yearList.length();

                            final List<String> spinnerStatusArray = new ArrayList<>();

                            years.clear();
                            for (int j = 0; j < len; j++)
                            {
                                JSONObject status = (JSONObject) yearList.get(j);
                                final String YEAR = status.getString("YEAR");
                                spinnerStatusArray.add(YEAR);

                                Year yearObj = new Year();
                                if(j==0)
                                    yearObj.year_id = "0";
                                else
                                    yearObj.year_id = YEAR;
                                yearObj.year_nm = YEAR;

                                years.add(yearObj);
                            }

                            ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(Month_Wise_Summary.this,
                                    android.R.layout.simple_spinner_item,
                                    spinnerStatusArray
                            );

                            final Spinner spinnerYear = findViewById(R.id.SpinnerYear);
                            spinnerYear.setAdapter(adapterStatus);
                            spinnerYear.setSelection(Integer.parseInt(spinnerId));

                            //Company List
                            final JSONArray categoryList = responseJSON.getJSONArray("categoryListReport");
                            len = categoryList.length();

                            List<String> spinnerVendorArray = new ArrayList<>();

                            vendors.clear();
                            for (int j = 0; j < len; j++)
                            {
                                JSONObject category = (JSONObject) categoryList.get(j);

                                final String cat_nm = category.getString("cat_nm");
                                final String cat_id = category.getString("cat_id");
                                spinnerVendorArray.add(cat_nm);

                                Company companyObj = new Company();
                                companyObj.comp_id = cat_id;
                                companyObj.comp_nm = cat_nm;

                                vendors.add(companyObj);
                            }
                            ArrayAdapter<String> adapterVendor = new ArrayAdapter<String>(Month_Wise_Summary.this,
                                    android.R.layout.simple_spinner_item,
                                    spinnerVendorArray);

                            final Spinner spinnerVendor = findViewById(R.id.SpinnerVendor);
                            spinnerVendor.setAdapter(adapterVendor);

                            spinnerVendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {

                                    selectedVendor = vendors.get(position).comp_id;
                                    selectedVendorStr = vendors.get(position).comp_nm;

                                    JSONObject bodyParam = new JSONObject();
                                    try {
                                       // bodyParam.accumulate("id","2VoTM29S");
                                        bodyParam.accumulate("usercd",userCd);
                                        bodyParam.accumulate("menuId",menuId);
                                        bodyParam.accumulate("selectedDropId",selectedYear);
                                        bodyParam.accumulate("selectedStatus",selectedVendor);
                                        bodyParam.accumulate("extraSpinnerId",selectedMonth);

                                        System.out.println(bodyParam);
                                    } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }

                                    ApiService apiService1 = new ApiService(getSummaryData, RequestType.GET,
                                            GET_MONTHLY_SUMMARY + bodyParam.toString(), new HashMap<String, String>(), bodyParam);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {
                                }
                            });

                            spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {

                                    selectedYear = years.get(position).year_id;
                                    selectedYearStr = years.get(position).year_nm;

                                    JSONObject bodyParam = new JSONObject();
                                    try {
                                      //  bodyParam.accumulate("id","2VoTM29S");
                                        bodyParam.accumulate("usercd",userCd);
                                        bodyParam.accumulate("menuId",menuId);
                                        bodyParam.accumulate("selectedDropId",selectedYear);
                                        bodyParam.accumulate("selectedStatus",selectedVendor);
                                        bodyParam.accumulate("extraSpinnerId",selectedMonth);

                                        System.out.println(bodyParam);
                                    } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }

                                    ApiService apiService1 = new ApiService(getSummaryData, RequestType.GET,
                                            GET_MONTHLY_SUMMARY + bodyParam.toString(), new HashMap<String, String>(), bodyParam);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {

                                }
                            });
                        }
                        else
                        {
                            String errorString = responseJSON.getString("obj");
                            Toast.makeText(Month_Wise_Summary.this, errorString, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }});
        }

        @Override
        public void onError(String error)
        {

        }
    };

    OnResponseListener getSummaryData = new OnResponseListener()
    {
        @Override
        public void onSuccess(String response1, HashMap<String, String> headers)
        {

            final String response = response1;
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        JSONObject responseJSON = new JSONObject(response);

                        System.out.println(responseJSON);

                        String loginSuccess = responseJSON.getString("gotListData");

                        if (loginSuccess.equals("Y"))
                        {
                            int len;
                            final JSONArray categoryReportData = responseJSON.getJSONArray("categoryReportData");
                            len = categoryReportData.length();

                            if(len>0)
                            {
                                JSONObject jsonObject = (JSONObject) categoryReportData.get(0);

                                String R1C1=jsonObject.getString("R1C1");
                                String R1C2=jsonObject.getString("R1C2");
                                String R1C3=jsonObject.getString("R1C3");
                                String R1C4=jsonObject.getString("R1C4");
                                String R1C5=jsonObject.getString("R1C5");

                                String R2C1=jsonObject.getString("R2C1");
                                String R2C2=jsonObject.getString("R2C2");
                                String R2C3=jsonObject.getString("R2C3");
                                String R2C4=jsonObject.getString("R2C4");
                                String R2C5=jsonObject.getString("R2C5");

                                String R3C1=jsonObject.getString("R3C1");
                                String R3C2=jsonObject.getString("R3C2");
                                String R3C3=jsonObject.getString("R3C3");
                                String R3C4=jsonObject.getString("R3C4");
                                String R3C5=jsonObject.getString("R3C5");

                                String R5C1=jsonObject.getString("R5C1");
                                String R5C2=jsonObject.getString("R5C2");
                                String R5C3=jsonObject.getString("R5C3");
                                String R5C4=jsonObject.getString("R5C4");
                                String R5C5=jsonObject.getString("R5C5");

                                String R4C1=jsonObject.getString("R4C1");
                                String R4C2=jsonObject.getString("R4C2");
                                String R4C3=jsonObject.getString("R4C3");
                                String R4C4=jsonObject.getString("R4C4");
                                String R4C5=jsonObject.getString("R4C5");

                                String AVG_TIME=jsonObject.getString("AVG_TIME");
                                String YR_VAL=jsonObject.getString("YR_VAL");
                                String FY_VAL=jsonObject.getString("FY_VAL");
                                String FY_MTH=jsonObject.getString("FY_MTH");

                                lblR1C1.setText(R1C1);
                                lblR1C2.setText(R1C2);
                                lblR1C3.setText(R1C3);
                                lblR1C4.setText(R1C4);
                                lblR1C5.setText(R1C5);

                                lblR2C1.setText(R2C1);
                                lblR2C2.setText(R2C2);
                                lblR2C3.setText(R2C3);
                                lblR2C4.setText(R2C4);
                                lblR2C5.setText(R2C5);

                                lblR3C1.setText(R3C1);
                                lblR3C2.setText(R3C2);
                                lblR3C3.setText(R3C3);
                                lblR3C4.setText(R3C4);
                                lblR3C5.setText(R3C5);

                                lblR4C1.setText(R4C1);
                                lblR4C2.setText(R4C2);
                                lblR4C3.setText(R4C3);
                                lblR4C4.setText(R4C4);
                                lblR4C5.setText(R4C5);

                                lblR5C1.setText(R5C1);
                                lblR5C2.setText(R5C2);
                                lblR5C3.setText(R5C3);
                                lblR5C4.setText(R5C4);
                                lblR5C5.setText(R5C5);

                                lblFY.setText(FY_MTH);
                                lbldelivered_Amount.setText(FY_VAL);
                                lbl_YearAmount.setText(YR_VAL);
                            }

                            final RelativeLayout rlMonthlySummary = findViewById(R.id.rl_summarylist);
                            int len1;
                            final JSONArray distReportData = responseJSON.getJSONArray("distReportData");
                            len1 = distReportData.length();

                            if(len1>0)
                            {
                                JSONObject jsonObj = (JSONObject) distReportData.get(0);

                                String H1=jsonObj.getString("H1");
                                String H2=jsonObj.getString("H2");
                                String H3=jsonObj.getString("H3");
                                String H4=jsonObj.getString("H4");

                                lbl_H1.setText(H1);
                                lbl_H2.setText(H2);
                                lbl_H3.setText(H3);
                                lbl_H4.setText(H4);
                            }

                            rlMonthlySummary.removeAllViews();
                            for (int j = 1; j < len1; j++)
                            {
                                try
                                {
                                    final JSONObject test = (JSONObject) distReportData.get(j);
                                    String H1C10 = test.getString("H1C10");
                                    String H1C11 = test.getString("H1C11");
                                    String H1C12 = test.getString("H1C12");
                                    String H1C13 = test.getString("H1C13");
                                    String H1C2 = test.getString("H1C2");
                                    String H1C3 = test.getString("H1C3");
                                    String H1C40 = test.getString("H1C40");
                                    String H1C41 = test.getString("H1C41");

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 0, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.summary_layout, (ViewGroup) findViewById(android.R.id.content), false);
                                    linearLayout.setId(200 + j);
                                    linearLayout.setLayoutParams(lp);

                                    TextView txt_Po = (TextView) linearLayout.findViewById(R.id.txt_Po);
                                    TextView txt_Dt = (TextView) linearLayout.findViewById(R.id.txt_Dt);
                                    TextView txt_count = (TextView) linearLayout.findViewById(R.id.txt_count);
                                    TextView txt_h2Amount = (TextView) linearLayout.findViewById(R.id.txt_h2Amount);
                                    TextView txt_h3Amount = (TextView) linearLayout.findViewById(R.id.txt_h3Amount);
                                    TextView txt_h4Amount = (TextView) linearLayout.findViewById(R.id.txt_h4Amount);
                                    LinearLayout SummaryLayout=(LinearLayout) linearLayout.findViewById(R.id.SummaryLayout);

                                    System.out.println(H1C10);

                                    txt_Po.setText(H1C10);
                                    txt_Dt.setText(H1C12);
                                    txt_count.setText(H1C13);
                                    txt_h2Amount.setText(H1C2);
                                    txt_h3Amount.setText(H1C3);

                                    if(H1C41.equals(""))
                                    {
                                        String str = H1C40 + H1C41;
                                        txt_h4Amount.setText(str);
                                    }
                                    else
                                    {
                                        String str = H1C40 + ("\n(" + H1C41 + ")");
                                        txt_h4Amount.setText(str);
                                    }

                                    String SubTitle=lblSummarySubTitle.getText().toString();

                                    if(SubTitle.contains("Orders"))
                                    {
                                        if(H1C10.contains("PO#"))
                                        {
                                            SummaryLayout.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    try {
                                                        Intent intent = new Intent(Month_Wise_Summary.this, Order_Details_Activity.class);
                                                        String[] Cm_No = H1C10.split("#");
                                                        String Cm_No2 = Cm_No[1];
                                                        intent.putExtra("complaint_number", Cm_No2);
                                                        intent.putExtra("submit", false);
                                                        intent.putExtra("screen", "Month Wise Summary > Order Summary > Order Details");
                                                        startActivity(intent);
                                                    } catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    else {
                                        if(H1C10.contains("PO#"))
                                        {
                                            SummaryLayout.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    try {
                                                        Intent intent = new Intent(Month_Wise_Summary.this, Order_Details_Activity.class);
                                                        String[] Cm_No = H1C10.split("#");
                                                        String Cm_No2 = Cm_No[1];
                                                        intent.putExtra("complaint_number", Cm_No2);
                                                        intent.putExtra("submit", false);
                                                        intent.putExtra("screen", "Month Wise Summary > GST > Order Details");
                                                        startActivity(intent);
                                                    } catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    rlMonthlySummary.addView(linearLayout);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        {
                            String errorString = responseJSON.getString("obj");
                            Toast.makeText(Month_Wise_Summary.this, errorString, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        public void onError(String error)
        {
        }
    };
}