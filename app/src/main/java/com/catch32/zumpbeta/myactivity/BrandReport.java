package com.catch32.zumpbeta.myactivity;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

import static com.catch32.zumpbeta.apicall.ApiEndPoint.QUICK_ORDER;
import static com.catch32.zumpbeta.apicall.ApiEndPoint.TEST_DATA_COPY;
import static com.catch32.zumpbeta.apicall.ApiEndPoint.TEST_MENU_DATA;

public class BrandReport extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Handler handler = new Handler();
    LinearLayout Menu,BookMyNewOrder;
    private Context mContext;
    String userCd;

    public class Company
    {
        public String comp_nm, comp_id;
    }
    ArrayList<Company> subbrands = new ArrayList<Company>();
    ArrayList<Company> brands = new ArrayList<Company>();
    ArrayList<Company> subcategories = new ArrayList<Company>();

    String selectedBrand = "0", selectedSubCategory = "0", selectedSubBrand = "0";
    String selectedBrandStr = "", selectedSubCategoryStr = "", selectedSubBrandStr = "";
    int initialScrollYTab1 = 0;

    boolean loadData = true;

    String previousBrand= "", previousSubCategory = "", previousSubBrand = "";

    ProgressDialog Loading;

    LinearLayout llSummary;
    ImageView imgSummaryLeft;
    ImageView imgSummaryRight;
    LinearLayout llSummaryToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_report);

        mContext = this;

        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);

        Menu=findViewById(R.id.Menu);

        BookMyNewOrder=findViewById(R.id.BookMyNewOrder);
        BookMyNewOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(BrandReport.this, Book_My_New_Order_Activity.class);
                startActivity(intent);
            }
        });


        Button btnMyShop = findViewById(R.id.btnMyShop);
        btnMyShop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BrandReport.this, MyShop.class);
                startActivity(i);
            }
        });

        Button btnVendorReport = findViewById(R.id.btnVendorReport);
        btnVendorReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(BrandReport.this, VendorReport.class);
                startActivity(i);
            }
        });

        Button btnProductTypeReport = findViewById(R.id.btnProductTypeReport);
        btnProductTypeReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(BrandReport.this, ProductTypeReport.class);
                startActivity(i);
            }
        });

        Button btnOrderedProduct = findViewById(R.id.btnOrderedProductSummaryReport);
        btnOrderedProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(BrandReport.this, OrderedProductReport.class);
                startActivity(i);
            }
        });

        LinearLayout llOrderStatus = findViewById(R.id.llOrderStatus);
        llOrderStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BrandReport.this, My_Order_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llHome = findViewById(R.id.llHome);
        llHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BrandReport.this, Dashboard_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llMonthwiseSummary = findViewById(R.id.llMonthwiseSummary);
        llMonthwiseSummary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(BrandReport.this, Month_Wise_Summary.class);
                startActivity(i);
            }
        });

        try {

            final RelativeLayout rlFilterOpen = findViewById(R.id.rlOpenFilter);
            final ImageView imgDownArrow = findViewById(R.id.imgDownArrow);
            final ImageView imgUpArrow = findViewById(R.id.imgUpArrow);
            final Spinner spinnerVendor = findViewById(R.id.spinnerBrand);
            final Spinner spinnerStatus = findViewById(R.id.spinnerStatus);
            final Spinner spinnerCompany = findViewById(R.id.spinnerSubBrand);
            final TextView lblFilter1 = findViewById(R.id.lblFilter1);
            final TextView lblVendor = findViewById(R.id.lblBrand);
            final TextView lblStatus = findViewById(R.id.lblStatus);
            final TextView lblCompany = findViewById(R.id.lblSubBrand);
            final TextView txtSummaryWithArrow = findViewById(R.id.txtSummaryWithArrow);

            imgDownArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    spinnerVendor.setVisibility(View.VISIBLE);
                    spinnerStatus.setVisibility(View.VISIBLE);
                    spinnerCompany.setVisibility(View.VISIBLE);
                    lblVendor.setVisibility(View.VISIBLE);
                    lblStatus.setVisibility(View.VISIBLE);
                    lblCompany.setVisibility(View.VISIBLE);
                    imgUpArrow.setVisibility(View.VISIBLE);
                    rlFilterOpen.setVisibility(View.GONE);

                    if (llSummary.getVisibility() == View.VISIBLE) {
                        llSummary.setVisibility(View.GONE);
                        imgSummaryLeft.setImageDrawable(getDrawable(R.drawable.down_arrow));
                        imgSummaryRight.setImageDrawable(getDrawable(R.drawable.down_arrow));
                        //txtSummaryWithArrow.setText("Open Summary");
                    }
                }
            });


            imgUpArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    spinnerVendor.setVisibility(View.GONE);
                    spinnerStatus.setVisibility(View.GONE);
                    spinnerCompany.setVisibility(View.GONE);
                    lblVendor.setVisibility(View.GONE);
                    lblStatus.setVisibility(View.GONE);
                    lblCompany.setVisibility(View.GONE);
                    imgUpArrow.setVisibility(View.GONE);
                    rlFilterOpen.setVisibility(View.VISIBLE);

                    /*if (llSummary.getVisibility() == View.GONE){
                        llSummary.setVisibility(View.VISIBLE);
                        imgSummaryLeft.setImageDrawable(getDrawable(R.drawable.up_arrow));
                        imgSummaryRight.setImageDrawable(getDrawable(R.drawable.up_arrow));
                        //txtSummaryWithArrow.setText("Close Summary");
                    }*/

                }
            });

            llSummary = findViewById(R.id.llSummary);
            llSummaryToggle = findViewById(R.id.llSummaryToggle);
            imgSummaryLeft = findViewById(R.id.imgSummaryLeft);
            imgSummaryRight = findViewById(R.id.imgSummaryRight);

            llSummaryToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (llSummary.getVisibility() == View.VISIBLE) {
                        llSummary.setVisibility(View.GONE);
                        imgSummaryLeft.setImageDrawable(getDrawable(R.drawable.down_arrow));
                        imgSummaryRight.setImageDrawable(getDrawable(R.drawable.down_arrow));
                        //txtSummaryWithArrow.setText("Open Summary");
                    } else {
                        llSummary.setVisibility(View.VISIBLE);
                        imgSummaryLeft.setImageDrawable(getDrawable(R.drawable.up_arrow));
                        imgSummaryRight.setImageDrawable(getDrawable(R.drawable.up_arrow));
                        //txtSummaryWithArrow.setText("Close Summary");
                    }
                }
            });

            final NestedScrollView nsvDashboard = findViewById(R.id.nsvDashboard);
            final LinearLayout llFooter = findViewById(R.id.llFooter);
            final int initialViewHeight = llFooter.getLayoutParams().height;
            initialScrollYTab1 = nsvDashboard.getScrollY();

            nsvDashboard.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {

                    ValueAnimator animator = ValueAnimator.ofInt(0, 1);
                    ViewGroup.LayoutParams params = llFooter.getLayoutParams();

                    /* get the maximum height which we have scroll before performing any action */
                    int maxDistance = llFooter.getHeight();
                    /* how much we have scrolled */
                    float movement = nsvDashboard.getScrollY();
                    /*finally calculate the alpha factor and set on the view */
                    //float alphaFactor = ((movement * 1.0f) / (maxDistance + llFooter.getHeight()));
                    if (movement > initialScrollYTab1) {

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

                    initialScrollYTab1 = nsvDashboard.getScrollY();
                }
            });

            AndroidNetworking.initialize(BrandReport.this);

            JSONObject Param = new JSONObject();
            try {
                Param.accumulate("usercd", userCd);
                Param.accumulate("menuId", "8");
                Param.accumulate("statusId", 0);
                Param.accumulate("spinnerId", 0);
                Param.accumulate("compId", 0);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            System.out.println(Param);

            ApiService apiService1 = new ApiService(getBrandMenuData, RequestType.GET,
                    TEST_MENU_DATA + Param.toString(), new HashMap<String, String>(), Param);

            Loading = ProgressDialog.show(BrandReport.this, "Loading Details", "Please Wait...", true, true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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
     //   String Outlet2= SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY); //SB20191216
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
            Intent i=new Intent(BrandReport.this, Dashboard_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_book_my_order)
        {
            Intent i = new Intent(BrandReport.this, Book_My_New_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_order_status)
        {
            Intent i = new Intent(BrandReport.this, Order_Status.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_order)
        {
            Intent i = new Intent(BrandReport.this, My_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_shop)
        {
            Intent i = new Intent(BrandReport.this, MyShop.class);
            startActivity(i);
        }

        else if(vid == R.id.nav_add_more_brands)
        {
              Intent i = new Intent(BrandReport.this, Add_More_Brands.class);
              startActivity(i);
        }
        else if(vid == R.id.nav_my_reward)
        {
            Intent i = new Intent(BrandReport.this, My_Rewards.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_summary)
        {
            Intent i = new Intent(BrandReport.this, Month_Wise_Summary.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_draft)
        {
            Intent i = new Intent(BrandReport.this, Draft_Orders.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_feedback)
        {
            Intent i=new Intent(BrandReport.this, Feedback_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_profile)
        {
            Intent i=new Intent(BrandReport.this, Profile_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_Notification)
        {
            Intent i=new Intent(BrandReport.this, Notification.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_policy)
        {
            Intent i=new Intent(BrandReport.this, Privacy_Policy.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_terms_conditions)
        {
            Intent i=new Intent(BrandReport.this, TermsAndCondition.class);
            startActivity(i);
        }
        else if(vid == R.id.about_us)
        {
            Intent i=new Intent(BrandReport.this, About_Us.class);
            startActivity(i);
        }
        else if(vid == R.id.call_us)
        {
            Intent i=new Intent(BrandReport.this, Contact_Us.class);
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
                    AppUserManager.logout(BrandReport.this, SignInActivity.class);
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
    OnResponseListener getBrandMenuData = new OnResponseListener()
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

                            //subcategoryListReport
                            final JSONArray SubcategoryList= responseJSON.getJSONArray("subcategoryListReport");
                            len = SubcategoryList.length();

                            final List<String> spinnerSubcategoryArray = new ArrayList<>();

                            for (int j = 0; j < len; j++)
                            {

                                JSONObject Subcategory = (JSONObject) SubcategoryList.get(j);

                                final String sub_cat_nm = Subcategory.getString("sub_cat_nm");
                                final String sub_cat_agent_id=Subcategory.getString("sub_cat_agent_id");
                                final String sub_cat_id=Subcategory.getString("sub_cat_id");
                                spinnerSubcategoryArray.add(sub_cat_nm);

                                Company companyObj = new Company();
                                companyObj.comp_id = sub_cat_id;
                                companyObj.comp_nm = sub_cat_nm;

                                subcategories.add(companyObj);

                            }

                            previousSubCategory = subcategories.get(0).comp_id;
                            selectedSubCategory = subcategories.get(0).comp_id;

                            ArrayAdapter<String> adapterSubcategory = new ArrayAdapter<String>(
                                    BrandReport.this,
                                    android.R.layout.simple_spinner_item,
                                    spinnerSubcategoryArray
                            );

                            final Spinner SpinnerSubcategory = findViewById(R.id.spinnerStatus);
                            SpinnerSubcategory.setAdapter(adapterSubcategory);
                            SpinnerSubcategory.setSelection(Integer.parseInt(spinnerId));

                            //Company List
                            final JSONArray compList = responseJSON.getJSONArray("compList");
                            len = compList.length();

                            List<String> spinnerSubBrandArray = new ArrayList<>();

                            for (int j = 0; j < len; j++)
                            {
                                JSONObject company = (JSONObject) compList.get(j);

                                final String comp_nm = company.getString("comp_nm");
                                final String comp_id = company.getString("comp_id");
                                spinnerSubBrandArray.add(comp_nm);

                                Company companyObj = new Company();
                                companyObj.comp_id = comp_id;
                                companyObj.comp_nm = comp_nm;

                                subbrands.add(companyObj);

                            }
                            previousSubBrand = subbrands.get(0).comp_id;
                            selectedSubBrand = subbrands.get(0).comp_id;

                            ArrayAdapter<String> adapterCompany = new ArrayAdapter<String>(
                                    BrandReport.this,
                                    android.R.layout.simple_spinner_item,
                                    spinnerSubBrandArray
                            );

                            final Spinner spinnerSubBrand = findViewById(R.id.spinnerSubBrand);
                            spinnerSubBrand.setAdapter(adapterCompany);
                            //spinnerCompany.setSelection(Integer.parseInt(compId));

                            //categoryListReport
                            final JSONArray categoryList = responseJSON.getJSONArray("categoryListReport");
                            len = categoryList.length();

                            List<String> spinnerBrandArray = new ArrayList<>();

                            for (int j = 0; j < len; j++)
                            {

                                JSONObject category = (JSONObject) categoryList.get(j);

                                final String cat_nm = category.getString("cat_nm");
                                final String cat_id = category.getString("cat_id");
                                spinnerBrandArray.add(cat_nm);

                                Company companyObj = new Company();
                                companyObj.comp_id = cat_id;
                                companyObj.comp_nm = cat_nm;

                                brands.add(companyObj);

                            }
                            previousBrand = brands.get(0).comp_id;
                            selectedBrand = brands.get(0).comp_id;


                            ArrayAdapter<String> adapterVendor = new ArrayAdapter<String>
                                    (BrandReport.this,
                                            android.R.layout.simple_spinner_item,
                                            spinnerBrandArray
                                    );

                            final Spinner spinnerBrand = findViewById(R.id.spinnerBrand);
                            spinnerBrand.setAdapter(adapterVendor);
                            //spinnerCompany.setSelection(Integer.parseInt(compId));

                            spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {

                                    selectedBrand = brands.get(position).comp_id;
                                    selectedBrandStr = brands.get(position).comp_nm;

                                    RelativeLayout rlProductSummaries = findViewById(R.id.rlProductSummaries);

                                    if(selectedBrand.equals("0") && selectedSubCategory.equals("0") && selectedSubCategory.equals("0")){

                                        rlProductSummaries.removeAllViews();

                                        TextView lblTotalVendors = findViewById(R.id.lblTotalVendors);
                                        lblTotalVendors.setText("Total Vendor(s): ");

                                        TextView lblTotalBrands = findViewById(R.id.lblTotalBrands);
                                        lblTotalBrands.setText("Total Brand(s): ");

                                        TextView txt_ProdItemTotal = findViewById(R.id.txt_ProdItemTotal);
                                        txt_ProdItemTotal.setText("");

                                        TextView txt_ProdActive = findViewById(R.id.txt_ProdActive);
                                        txt_ProdActive.setText("");

                                        TextView txt_ProdInactive = findViewById(R.id.txt_ProdInactive);
                                        txt_ProdInactive.setText("");

                                        TextView txt_Address = findViewById(R.id.txt_Address);
                                        txt_Address.setText("");

                                        TextView txt_TotalLP = findViewById(R.id.txt_TotalLP);
                                        txt_TotalLP.setText("");

                                        TextView txt_TotalTRP = findViewById(R.id.txt_TotalTRP);
                                        txt_TotalTRP.setText("");

                                        TextView txt_TotalGST = findViewById(R.id.txt_TotalGST);
                                        txt_TotalGST.setText("");

                                        TextView txt_TotalOrdQty = findViewById(R.id.txt_TotalOrdQty);
                                        txt_TotalOrdQty.setText("");

                                        TextView txt_TotalSchemeMGn = findViewById(R.id.txt_TotalSchemeMGn);
                                        txt_TotalSchemeMGn.setText("");

                                        TextView txt_TotalTRdMgn = findViewById(R.id.txt_TotalTRdMgn);
                                        txt_TotalTRdMgn.setText("");

                                        TextView txtSummaryWithArrow = findViewById(R.id.txtSummaryWithArrow);
                                        txtSummaryWithArrow.setText("Summary");

                                        if(!loadData) {
                                            JSONObject Param = new JSONObject();
                                            try {
                                                Param.accumulate("usercd", userCd);
                                                Param.accumulate("menuId", "8");
                                                Param.accumulate("statusId", 0);
                                                Param.accumulate("spinnerId", 0);
                                                Param.accumulate("compId", 0);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            System.out.println(Param);

                                            ApiService apiService1 = new ApiService(getBrandMenuData, RequestType.GET,
                                                    TEST_MENU_DATA + Param.toString(),  new HashMap<String, String>(), Param);

                                            loadData = true;
                                        }

                                        llSummaryToggle.setBackground(null);
                                        Loading.cancel();

                                    }
                                    else if(!previousBrand.equals(selectedBrand) || !previousSubCategory.equals(selectedSubCategory) || !previousSubCategory.equals(selectedSubCategory)){

                                        rlProductSummaries.removeAllViews();

                                        previousBrand = selectedBrand;

                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        LinearLayout loadingLayout = (LinearLayout) inflater.inflate(R.layout.loading, (ViewGroup) findViewById(android.R.id.content), false);

                                        rlProductSummaries.addView(loadingLayout);

                                        JSONObject bodyParam = new JSONObject();
                                        JSONObject Param = new JSONObject();

                                        try {

                                            Param.accumulate("usercd",userCd);
                                            Param.accumulate("menuId","8");
                                            Param.accumulate("selectedDropId",selectedBrand);
                                            Param.accumulate("selectedStatus",selectedSubCategory);
                                            Param.accumulate("extraSpinnerId","8");
                                            Param.accumulate("selectedCompId",selectedSubBrand);

                                            bodyParam.accumulate("option", "getListData");
                                            bodyParam.accumulate("Param", Param);
                                        } catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }

                                        System.out.println(Param);

                                        ApiService apiService1 = new ApiService(GetProductSummarieslist, RequestType.POST,
                                                TEST_DATA_COPY+Param.toString(), new HashMap<String, String>(), bodyParam);

                                        Loading = ProgressDialog.show(BrandReport.this, "Loading Details", "Please Wait...", true, true);

                                    }

                                    final TextView lblFilter1 = findViewById(R.id.lblFilter1);
                                    lblFilter1.setText("Brand: "+selectedBrandStr+"; Product Type: "+selectedSubCategoryStr+"; Sub Brand: "+selectedSubBrandStr);


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {

                                }
                            });

                            spinnerSubBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {

                                    selectedSubBrand = subbrands.get(position).comp_id;
                                    selectedSubBrandStr = subbrands.get(position).comp_nm;

                                    RelativeLayout rlProductSummaries = findViewById(R.id.rlProductSummaries);

                                    if(selectedBrand.equals("0") && selectedSubBrand.equals("0") && selectedSubCategory.equals("0")){

                                        rlProductSummaries.removeAllViews();

                                        TextView lblTotalVendors = findViewById(R.id.lblTotalVendors);
                                        lblTotalVendors.setText("Total Vendor(s): ");

                                        TextView lblTotalBrands = findViewById(R.id.lblTotalBrands);
                                        lblTotalBrands.setText("Total Brand(s): ");

                                        TextView txt_ProdItemTotal = findViewById(R.id.txt_ProdItemTotal);
                                        txt_ProdItemTotal.setText("");

                                        TextView txt_ProdActive = findViewById(R.id.txt_ProdActive);
                                        txt_ProdActive.setText("");

                                        TextView txt_ProdInactive = findViewById(R.id.txt_ProdInactive);
                                        txt_ProdInactive.setText("");

                                        TextView txt_Address = findViewById(R.id.txt_Address);
                                        txt_Address.setText("");

                                        TextView txt_TotalLP = findViewById(R.id.txt_TotalLP);
                                        txt_TotalLP.setText("");

                                        TextView txt_TotalTRP = findViewById(R.id.txt_TotalTRP);
                                        txt_TotalTRP.setText("");

                                        TextView txt_TotalGST = findViewById(R.id.txt_TotalGST);
                                        txt_TotalGST.setText("");

                                        TextView txt_TotalOrdQty = findViewById(R.id.txt_TotalOrdQty);
                                        txt_TotalOrdQty.setText("");

                                        TextView txt_TotalSchemeMGn = findViewById(R.id.txt_TotalSchemeMGn);
                                        txt_TotalSchemeMGn.setText("");

                                        TextView txt_TotalTRdMgn = findViewById(R.id.txt_TotalTRdMgn);
                                        txt_TotalTRdMgn.setText("");

                                        TextView txtSummaryWithArrow = findViewById(R.id.txtSummaryWithArrow);
                                        txtSummaryWithArrow.setText("Summary");

                                        llSummaryToggle.setBackground(null);
                                        Loading.cancel();

                                    }
                                    else if(!previousBrand.equals(selectedBrand) || !previousSubBrand.equals(selectedSubBrand) || !previousSubCategory.equals(selectedSubCategory)){

                                        rlProductSummaries.removeAllViews();

                                        previousSubBrand = selectedSubBrand;

                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        LinearLayout loadingLayout = (LinearLayout) inflater.inflate(R.layout.loading, (ViewGroup) findViewById(android.R.id.content), false);

                                        rlProductSummaries.addView(loadingLayout);

                                        JSONObject bodyParam = new JSONObject();
                                        JSONObject Param = new JSONObject();

                                        try {

                                            Param.accumulate("usercd", userCd);
                                            Param.accumulate("menuId", "8");
                                            Param.accumulate("selectedDropId", selectedBrand);
                                            Param.accumulate("selectedStatus", selectedSubCategory);
                                            Param.accumulate("extraSpinnerId", "8");
                                            Param.accumulate("selectedCompId", selectedSubBrand);

                                            bodyParam.accumulate("option", "getListData");
                                            bodyParam.accumulate("Param", Param);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        System.out.println(Param);

                                        ApiService apiService1 = new ApiService(GetProductSummarieslist, RequestType.POST,
                                                TEST_DATA_COPY + Param.toString(),  new HashMap<String, String>(), bodyParam);

                                        Loading = ProgressDialog.show(BrandReport.this, "Loading Details", "Please Wait...", true, true);

                                    }

                                    final TextView lblFilter1 = findViewById(R.id.lblFilter1);
                                    lblFilter1.setText("Brand: "+selectedBrandStr+"; Product Type: "+selectedSubCategoryStr+"; Sub Brand: "+selectedSubBrandStr);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {

                                }
                            });

                            SpinnerSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {
                                    selectedSubCategory = subcategories.get(position).comp_id;
                                    selectedSubCategoryStr = subcategories.get(position).comp_nm;

                                    RelativeLayout rlProductSummaries = findViewById(R.id.rlProductSummaries);

                                    if(selectedBrand.equals("0") && selectedSubBrand.equals("0") && selectedSubCategory.equals("0")){

                                        rlProductSummaries.removeAllViews();

                                        TextView lblTotalVendors = findViewById(R.id.lblTotalVendors);
                                        lblTotalVendors.setText("Total Vendor(s): ");

                                        TextView lblTotalBrands = findViewById(R.id.lblTotalBrands);
                                        lblTotalBrands.setText("Total Brand(s): ");

                                        TextView txt_ProdItemTotal = findViewById(R.id.txt_ProdItemTotal);
                                        txt_ProdItemTotal.setText("");

                                        TextView txt_ProdActive = findViewById(R.id.txt_ProdActive);
                                        txt_ProdActive.setText("");

                                        TextView txt_ProdInactive = findViewById(R.id.txt_ProdInactive);
                                        txt_ProdInactive.setText("");

                                        TextView txt_Address = findViewById(R.id.txt_Address);
                                        txt_Address.setText("");

                                        TextView txt_TotalLP = findViewById(R.id.txt_TotalLP);
                                        txt_TotalLP.setText("");

                                        TextView txt_TotalTRP = findViewById(R.id.txt_TotalTRP);
                                        txt_TotalTRP.setText("");

                                        TextView txt_TotalGST = findViewById(R.id.txt_TotalGST);
                                        txt_TotalGST.setText("");

                                        TextView txt_TotalOrdQty = findViewById(R.id.txt_TotalOrdQty);
                                        txt_TotalOrdQty.setText("");

                                        TextView txt_TotalSchemeMGn = findViewById(R.id.txt_TotalSchemeMGn);
                                        txt_TotalSchemeMGn.setText("");

                                        TextView txt_TotalTRdMgn = findViewById(R.id.txt_TotalTRdMgn);
                                        txt_TotalTRdMgn.setText("");

                                        TextView txtSummaryWithArrow = findViewById(R.id.txtSummaryWithArrow);
                                        txtSummaryWithArrow.setText("Summary");

                                        if(!loadData) {
                                            JSONObject Param = new JSONObject();
                                            try {
                                                Param.accumulate("usercd", userCd);
                                                Param.accumulate("menuId", "8");
                                                Param.accumulate("statusId", 0);
                                                Param.accumulate("spinnerId", 0);
                                                Param.accumulate("compId", 0);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            System.out.println(Param);

                                            ApiService apiService1 = new ApiService(getBrandMenuData, RequestType.GET,
                                                    TEST_MENU_DATA + Param.toString(), new HashMap<String, String>(), Param);

                                            loadData = true;

                                        }

                                        llSummaryToggle.setBackground(null);
                                        Loading.cancel();

                                    }
                                    else if(!previousBrand.equals(selectedBrand) || !previousSubBrand.equals(selectedSubBrand) || !previousSubCategory.equals(selectedSubCategory)){
                                        rlProductSummaries.removeAllViews();

                                        previousSubCategory = selectedSubCategory;

                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        LinearLayout loadingLayout = (LinearLayout) inflater.inflate(R.layout.loading, (ViewGroup) findViewById(android.R.id.content), false);

                                        rlProductSummaries.addView(loadingLayout);

                                        JSONObject bodyParam = new JSONObject();
                                        JSONObject Param = new JSONObject();

                                        try {

                                            Param.accumulate("usercd", userCd);
                                            Param.accumulate("menuId", "8");
                                            Param.accumulate("selectedDropId", selectedBrand);
                                            Param.accumulate("selectedStatus", selectedSubCategory);
                                            Param.accumulate("extraSpinnerId", "8");
                                            Param.accumulate("selectedCompId", selectedSubBrand);

                                            bodyParam.accumulate("option", "getListData");
                                            bodyParam.accumulate("Param", Param);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        System.out.println(Param);

                                        ApiService apiService1 = new ApiService(GetProductSummarieslist, RequestType.POST,
                                                TEST_DATA_COPY + Param.toString(), new HashMap<String, String>(), bodyParam);

                                        Loading = ProgressDialog.show(BrandReport.this, "Loading Details", "Please Wait...", true, true);

                                    }

                                    final TextView lblFilter1 = findViewById(R.id.lblFilter1);
                                    lblFilter1.setText("Brand: "+selectedBrandStr+"; Product Type: "+selectedSubCategoryStr+"; Sub Brand: "+selectedSubBrandStr);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                            /*try {
                                JSONObject Param = new JSONObject();
                                try {
                                    Param.accumulate("usercd","301418");
                                    Param.accumulate("menuId","7");
                                    Param.accumulate("selectedDropId",selectedVendor);
                                    Param.accumulate("selectedStatus",selectedSubCategory);
                                    Param.accumulate("extraSpinnerId","8");
                                    Param.accumulate("selectedCompId",selectedCompany);
                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                                System.out.println(Param);

                                ApiService apiService1 = new ApiService(GetProductSummarieslist, RequestType.GET,
                                        TEST_DATA_COPY + Param.toString(), new HashMap<String, String>(), Param);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }*/

                        }
                        else
                        {
                            String errorString = responseJSON.getString("obj");
                            Toast.makeText(BrandReport.this, errorString, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }});
        }

        @Override
        public void onError(String error) {

        }
    };

    OnResponseListener GetProductSummarieslist = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response1, HashMap<String, String> headers)
        {
            final String response = response1;
            System.out.println(response);
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        loadData = false;

                        //llSummary.setVisibility(View.VISIBLE);
                        //imgSummaryLeft.setImageDrawable(getDrawable(R.drawable.up_arrow));
                        //imgSummaryRight.setImageDrawable(getDrawable(R.drawable.up_arrow));
                        llSummaryToggle.setBackground(getDrawable(R.drawable.rectangle_with_yellow));

                        JSONObject responseJSON = new JSONObject(response);
                        System.out.println(responseJSON);

                        String loginSuccess = responseJSON.getString("loginSuccess");

                        if (loginSuccess.equals("Y"))
                        {
                            try {
                                final JSONArray getProductSummaries = responseJSON.getJSONArray("categoryReportData");

                                RelativeLayout rlProductSummaries = findViewById(R.id.rlProductSummaries);
                                rlProductSummaries.removeAllViews();
                                int len;
                                len = getProductSummaries.length();

                                int i = 0;
                                JSONObject test1 = (JSONObject) getProductSummaries.get(i);
                                final String CM_NAME_1 = test1.getString("CM_NAME");
                                final String CM_NAME_CNT_1 = test1.getString("CM_NAME_CNT");
                                final String CM_NAME_ACTIVE_1 = test1.getString("CM_NAME_ACTIVE");
                                final String CM_TIME_1 = test1.getString("CM_TIME");
                                final String CM_STATUS_1 = test1.getString("CM_STATUS");
                                final String CM_DESC_1 = test1.getString("CM_DESC");
                                final String CM_ROOM_NO_1 = test1.getString("CM_ROOM_NO");
                                final String CM_ROOM_FY_1 = test1.getString("CM_ROOM_FY");
                                final String OFFER_1 = test1.getString("OFFER");
                                final String CM_QTY_1 = test1.getString("CM_QTY");
                                final String CM_LP_1 = test1.getString("CM_LP");
                                final String CM_TRP_1 = test1.getString("CM_TRP");
                                final String CM_GST_1 = test1.getString("CM_GST");
                                final String CM_TRD_MGN_1 = test1.getString("CM_TRD_MGN");
                                final String CM_SCH_MGN_1 = test1.getString("CM_SCH_MGN");

                                String totalVendors = "0";
                                totalVendors = CM_ROOM_NO_1.split(" Total Brand\\(s\\): ")[0].split(": ")[1];
                                String totalBrands = CM_ROOM_NO_1.split(" Total Brand\\(s\\): ")[1];

                                TextView lblTotalVendors = findViewById(R.id.lblTotalVendors);
                                lblTotalVendors.setText("Total Vendor(s): "+totalVendors);

                                TextView lblTotalBrands = findViewById(R.id.lblTotalBrands);
                                lblTotalBrands.setText("Total Brand(s): "+totalBrands);

                                TextView txt_ProdItemTotal = findViewById(R.id.txt_ProdItemTotal);
                                txt_ProdItemTotal.setText(CM_NAME_CNT_1);

                                TextView txt_ProdActive = findViewById(R.id.txt_ProdActive);
                                txt_ProdActive.setText(CM_NAME_ACTIVE_1);

                                TextView txt_ProdInactive = findViewById(R.id.txt_ProdInactive);
                                String InActive = String.valueOf(Integer.parseInt(CM_NAME_CNT_1) - Integer.parseInt(CM_NAME_ACTIVE_1));
                                txt_ProdInactive.setText(InActive);

                                TextView txt_Address = findViewById(R.id.txt_Address);
                                txt_Address.setText(CM_DESC_1);

                                TextView txt_TotalLP = findViewById(R.id.txt_TotalLP);
                                txt_TotalLP.setText(CM_LP_1);

                                TextView txt_TotalTRP = findViewById(R.id.txt_TotalTRP);
                                txt_TotalTRP.setText(CM_TRP_1);

                                TextView txt_TotalGST = findViewById(R.id.txt_TotalGST);
                                txt_TotalGST.setText(CM_GST_1);

                                TextView txt_TotalOrdQty = findViewById(R.id.txt_TotalOrdQty);
                                txt_TotalOrdQty.setText(CM_QTY_1);

                                TextView txt_TotalSchemeMGn = findViewById(R.id.txt_TotalSchemeMGn);
                                txt_TotalSchemeMGn.setText(CM_SCH_MGN_1);

                                TextView txt_TotalTRdMgn = findViewById(R.id.txt_TotalTRdMgn);
                                txt_TotalTRdMgn.setText(CM_TRD_MGN_1);

                                TextView txtSummaryWithArrow = findViewById(R.id.txtSummaryWithArrow);
                                txtSummaryWithArrow.setText(CM_ROOM_FY_1 + " Summary");

                                if(Integer.parseInt(totalVendors) == 1 || Integer.parseInt(totalVendors) == 0){

                                    for (int j = 1; j < len; j++) {
                                        final JSONObject test = (JSONObject) getProductSummaries.get(j);
                                        final String CM_NO = test.getString("CM_NO");
                                        final String CM_DESC = test.getString("CM_DESC");
                                        final String CM_NAME = test.getString("CM_NAME");
                                        final String CM_STATUS = test.getString("CM_STATUS");
                                        final String CM_ROOM_NO = test.getString("CM_ROOM_NO");
                                        final String CM_TIME = test.getString("CM_TIME");
                                        final String OFFER = test.getString("OFFER");
                                        final String SHARE = test.getString("SHARE");
                                        final String TAP = test.getString("TAP");
                                        //final String flag_color = test.getString("flag_color");
                                        final String CM_PROD_MRP = test.getString("CM_PROD_MRP");
                                        final String CM_PROD_LP = test.getString("CM_PROD_LP");
                                        final String CM_PROD_TRP = test.getString("CM_PROD_TRP");
                                        final String CM_PROD_GST = test.getString("CM_PROD_GST");
                                        final String CM_PROD_MGN = test.getString("CM_PROD_MGN");
                                        final String CM_PROD_MOQ = test.getString("CM_PROD_MOQ");
                                        final String CM_PROD_SCM = test.getString("CM_PROD_SCM");
                                        final String CM_QTY = test.getString("CM_QTY");
                                        final String CM_LP = test.getString("CM_LP");
                                        final String CM_TRP = test.getString("CM_TRP");
                                        final String CM_GST = test.getString("CM_GST");
                                        final String CM_TRD_MGN = test.getString("CM_TRD_MGN");
                                        final String CM_SCH_MGN = test.getString("CM_SCH_MGN");

                                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                        lp.setMargins(0, 25, 0, 0);

                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        LinearLayout poLayout = (LinearLayout) inflater.inflate(R.layout.product_summary_layout, (ViewGroup) findViewById(android.R.id.content), false);
                                        poLayout.setId(200 + j);
                                        poLayout.setLayoutParams(lp);

                                        LinearLayout llTitle = poLayout.findViewById(R.id.llTitle);
                                        final LinearLayout llProductSummary = poLayout.findViewById(R.id.MainLinearLayout);
                                        final ImageView arrow = poLayout.findViewById(R.id.arrow);

                                        TextView txt_Productname = poLayout.findViewById(R.id.txt_Productname);
                                        TextView txt_MOQ = poLayout.findViewById(R.id.txt_MOQ);
                                        TextView txt_Scheme = poLayout.findViewById(R.id.txt_Scheme);
                                        TextView txt_MRP = poLayout.findViewById(R.id.txt_MRP);
                                        TextView txt_OrderQty = poLayout.findViewById(R.id.txt_OrderQty);
                                        TextView txt_TRPUnit = poLayout.findViewById(R.id.txt_TRPUnit);
                                        TextView txt_TRDUnit = poLayout.findViewById(R.id.txt_TRDUnit);
                                        TextView txt_scheme_MGN = poLayout.findViewById(R.id.txt_scheme_MGN);
                                        TextView txt_GST = poLayout.findViewById(R.id.txt_GST);
                                        TextView txt_LPUnit = poLayout.findViewById(R.id.txt_LPUnit);
                                        TextView txt_Contribution = poLayout.findViewById(R.id.txt_Contribution);
                                        TextView txt_ProductCode = poLayout.findViewById(R.id.txt_ProductCode);
                                        TextView txt_TRP_UNIT = poLayout.findViewById(R.id.txt_TRP_UNIT);
                                        TextView txt_LP_UNIT = poLayout.findViewById(R.id.txt_LP_UNIT);
                                        TextView txt_GSTValue = poLayout.findViewById(R.id.txt_GSTValue);
                                        TextView txt_TRD_MGN_UNIT = poLayout.findViewById(R.id.txt_TRD_MGN_UNIT);

                                        LinearLayout llVerticleLine1 = poLayout.findViewById(R.id.llVerticleLine1);
                                        LinearLayout llMRP = poLayout.findViewById(R.id.llMRP);
                                        LinearLayout llMOQ = poLayout.findViewById(R.id.llMOQ);
                                        TextView txtTapText = poLayout.findViewById(R.id.txtTapText);
                                        txtTapText.setText(TAP);
                                        llTitle.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (llProductSummary.getVisibility() == View.VISIBLE) {
                                                    llProductSummary.setVisibility(View.GONE);
                                                    arrow.setImageDrawable(getDrawable(R.drawable.down_arrow_white));
                                                } else {
                                                    llProductSummary.setVisibility(View.VISIBLE);
                                                    arrow.setImageDrawable(getDrawable(R.drawable.up_arrow_white));
                                                }
                                            }
                                        });

                                        txtTapText.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                String scheme1="0", scheme2="0", scheme3="0";
                                                if(!CM_PROD_SCM.contains("Nil")) {
                                                    if(CM_PROD_SCM.contains(" & ")) {
                                                        String[] schemes = CM_PROD_SCM.split(" & ");
                                                        scheme1 = schemes[0].split("/")[0].substring(6);
                                                        scheme2 = schemes[0].split("/")[1];
                                                        scheme3 = schemes[1];
                                                    }
                                                    else if(CM_PROD_SCM.contains("/")){
                                                        scheme1 = CM_PROD_SCM.split("/")[0].substring(6);
                                                        scheme2 = CM_PROD_SCM.split("/")[1];
                                                    }
                                                    else{
                                                        scheme1 = CM_PROD_SCM;
                                                    }
                                                }

                                                String productDetail = CM_DESC+" \n\nMRP/Unit: ₹"+CM_PROD_MRP+" \n\nTrd Mgn/Unit: ₹"+CM_PROD_MGN+" | YTD: ₹"+CM_TRD_MGN+" \n\nLP/Unit: ₹"+CM_PROD_LP+" | YTD: ₹"+CM_LP+" \n\nTRP/Unit: ₹"+CM_PROD_TRP+" | YTD: ₹"+CM_TRP+" \n\nGST: "+CM_PROD_GST+" | YTD: ₹"+CM_GST+" \n\nScheme Mgn: "+CM_SCH_MGN+" \n\nOrdered QTY YTD Units: "+CM_QTY+" \n\n\n"+TAP;
                                                showCustomDialog(CM_NO, productDetail,CM_PROD_MOQ,scheme1,scheme2,scheme3);

                                            }
                                        });

                                        LinearLayout llTap = poLayout.findViewById(R.id.llTap);

                                        if(selectedBrand.equals("999999"))
                                            llTap.setVisibility(View.INVISIBLE);

                                        if(SHARE.equals("")){
                                            llTitle.setBackground(getDrawable(R.drawable.rectangle_with_red_gradient_bg_1));
                                        }

                                        txt_Productname.setText(CM_DESC);
                                        txt_MOQ.setText(CM_PROD_MOQ);
                                        txt_Scheme.setText(CM_PROD_SCM);
                                        txt_MRP.setText(CM_PROD_MRP);
                                        txt_OrderQty.setText("YTD:" + CM_QTY);
                                        txt_TRPUnit.setText("YTD: ₹" + CM_TRP);
                                        txt_TRDUnit.setText("YTD: ₹" + CM_TRD_MGN);
                                        txt_scheme_MGN.setText("YTD:" + CM_SCH_MGN);
                                        txt_GST.setText("YTD: ₹" + CM_GST);
                                        txt_LPUnit.setText("YTD: ₹" + CM_LP);
                                        txt_Contribution.setText(SHARE);
                                        txt_ProductCode.setText(CM_TIME);
                                        txt_TRP_UNIT.setText(CM_PROD_TRP);
                                        txt_LP_UNIT.setText(CM_PROD_LP);
                                        txt_GSTValue.setText(CM_PROD_GST);
                                        txt_TRD_MGN_UNIT.setText("₹"+CM_PROD_MGN);

                                        if(CM_PROD_MRP.equals("")){
                                            llMOQ.setVisibility(View.INVISIBLE);
                                            llMRP.setVisibility(View.GONE);
                                            llVerticleLine1.setVisibility(View.GONE);
                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 30f);
                                            txt_Scheme.setLayoutParams(params);
                                        }


                                        rlProductSummaries.addView(poLayout);
                                    }

                                }
                                else{

                                    txt_Address.setVisibility(View.GONE);

                                    RelativeLayout productsList = null;

                                    int vendor = 0;

                                    for (int j = 1; j < len; j++) {

                                        final JSONObject test = (JSONObject) getProductSummaries.get(j);
                                        final String CM_NO = test.getString("CM_NO");
                                        final String CM_DESC = test.getString("CM_DESC");
                                        final String CM_NAME = test.getString("CM_NAME");
                                        final String CM_STATUS = test.getString("CM_STATUS");
                                        final String CM_ROOM_NO = test.getString("CM_ROOM_NO");
                                        final String CM_TIME = test.getString("CM_TIME");
                                        final String OFFER = test.getString("OFFER");
                                        final String SHARE = test.getString("SHARE");
                                        final String TAP = test.getString("TAP");
                                        final String CM_QTY = test.getString("CM_QTY");
                                        final String CM_LP = test.getString("CM_LP");
                                        final String CM_TRP = test.getString("CM_TRP");
                                        final String CM_GST = test.getString("CM_GST");
                                        final String CM_TRD_MGN = test.getString("CM_TRD_MGN");
                                        final String CM_SCH_MGN = test.getString("CM_SCH_MGN");

                                        if(!SHARE.contains("out of")) {

                                            //final String flag_color = test.getString("flag_color");
                                            final String CM_PROD_MRP = test.getString("CM_PROD_MRP");
                                            final String CM_PROD_LP = test.getString("CM_PROD_LP");
                                            final String CM_PROD_TRP = test.getString("CM_PROD_TRP");
                                            final String CM_PROD_GST = test.getString("CM_PROD_GST");
                                            final String CM_PROD_MGN = test.getString("CM_PROD_MGN");
                                            final String CM_PROD_MOQ = test.getString("CM_PROD_MOQ");
                                            final String CM_PROD_SCM = test.getString("CM_PROD_SCM");

                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                            lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            lp.setMargins(0, 25, 0, 0);

                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            LinearLayout poLayout = (LinearLayout) inflater.inflate(R.layout.product_summary_layout, (ViewGroup) findViewById(android.R.id.content), false);
                                            poLayout.setId(200 + j);
                                            poLayout.setLayoutParams(lp);

                                            LinearLayout llTitle = poLayout.findViewById(R.id.llTitle);
                                            final LinearLayout llProductSummary = poLayout.findViewById(R.id.MainLinearLayout);
                                            final ImageView arrow = poLayout.findViewById(R.id.arrow);

                                            TextView txt_Productname = poLayout.findViewById(R.id.txt_Productname);
                                            TextView txt_MOQ = poLayout.findViewById(R.id.txt_MOQ);
                                            TextView txt_Scheme = poLayout.findViewById(R.id.txt_Scheme);
                                            TextView txt_MRP = poLayout.findViewById(R.id.txt_MRP);
                                            TextView txt_OrderQty = poLayout.findViewById(R.id.txt_OrderQty);
                                            TextView txt_TRPUnit = poLayout.findViewById(R.id.txt_TRPUnit);
                                            TextView txt_TRDUnit = poLayout.findViewById(R.id.txt_TRDUnit);
                                            TextView txt_scheme_MGN = poLayout.findViewById(R.id.txt_scheme_MGN);
                                            TextView txt_GST = poLayout.findViewById(R.id.txt_GST);
                                            TextView txt_LPUnit = poLayout.findViewById(R.id.txt_LPUnit);
                                            TextView txt_Contribution = poLayout.findViewById(R.id.txt_Contribution);
                                            TextView txt_ProductCode = poLayout.findViewById(R.id.txt_ProductCode);
                                            TextView txt_TRP_UNIT = poLayout.findViewById(R.id.txt_TRP_UNIT);
                                            TextView txt_LP_UNIT = poLayout.findViewById(R.id.txt_LP_UNIT);
                                            TextView txt_GSTValue = poLayout.findViewById(R.id.txt_GSTValue);
                                            TextView txt_TRD_MGN_UNIT = poLayout.findViewById(R.id.txt_TRD_MGN_UNIT);
                                            TextView txtTapText = poLayout.findViewById(R.id.txtTapText);
                                            txtTapText.setText(TAP);
                                            llTitle.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (llProductSummary.getVisibility() == View.VISIBLE) {
                                                        llProductSummary.setVisibility(View.GONE);
                                                        arrow.setImageDrawable(getDrawable(R.drawable.down_arrow_white));
                                                    } else {
                                                        llProductSummary.setVisibility(View.VISIBLE);
                                                        arrow.setImageDrawable(getDrawable(R.drawable.up_arrow_white));
                                                    }
                                                }
                                            });

                                            LinearLayout llTap = poLayout.findViewById(R.id.llTap);

                                            if(selectedBrand.equals("999999"))
                                                llTap.setVisibility(View.INVISIBLE);

                                            if(SHARE.equals("")){
                                                llTitle.setBackground(getDrawable(R.drawable.rectangle_with_red_gradient_bg_1));
                                            }

                                            txtTapText.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    String scheme1="0", scheme2="0", scheme3="0";
                                                    if(!CM_PROD_SCM.contains("Nil")) {
                                                        if(CM_PROD_SCM.contains(" & ")) {
                                                            String[] schemes = CM_PROD_SCM.split(" & ");
                                                            scheme1 = schemes[0].split("/")[0].substring(6);
                                                            scheme2 = schemes[0].split("/")[1];
                                                            scheme3 = schemes[1];
                                                        }
                                                        else if(CM_PROD_SCM.contains("/")){
                                                            scheme1 = CM_PROD_SCM.split("/")[0].substring(6);
                                                            scheme2 = CM_PROD_SCM.split("/")[1];
                                                        }
                                                        else{
                                                            scheme1 = CM_PROD_SCM;
                                                        }
                                                    }

                                                    String productDetail = CM_DESC+" \n\nMRP/Unit: ₹"+CM_PROD_MRP+" \n\nTrd Mgn/Unit: ₹"+CM_PROD_MGN+" | YTD: ₹"+CM_TRD_MGN+" \n\nLP/Unit: ₹"+CM_PROD_LP+" | YTD: ₹"+CM_LP+" \n\nTRP/Unit: ₹"+CM_PROD_TRP+" | YTD: ₹"+CM_TRP+" \n\nGST: "+CM_PROD_GST+" | YTD: ₹"+CM_GST+" \n\nScheme Mgn: "+CM_SCH_MGN+" \n\nOrdered QTY YTD Units: "+CM_QTY+" \n\n\n"+TAP;
                                                    showCustomDialog(CM_NO, productDetail,CM_PROD_MOQ,scheme1,scheme2,scheme3);

                                                }
                                            });

                                            txt_Productname.setText(CM_DESC);
                                            txt_MOQ.setText(CM_PROD_MOQ);
                                            txt_Scheme.setText(CM_PROD_SCM);
                                            txt_MRP.setText(CM_PROD_MRP);
                                            txt_OrderQty.setText("YTD:" + CM_QTY);
                                            txt_TRPUnit.setText("YTD: ₹" + CM_TRP);
                                            txt_TRDUnit.setText("YTD: ₹" + CM_TRD_MGN);
                                            txt_scheme_MGN.setText("YTD:" + CM_SCH_MGN);
                                            txt_GST.setText("YTD: ₹" + CM_GST);
                                            txt_LPUnit.setText("YTD: ₹" + CM_LP);
                                            txt_Contribution.setText(SHARE);
                                            txt_ProductCode.setText(CM_TIME);
                                            txt_TRP_UNIT.setText(CM_PROD_TRP);
                                            txt_LP_UNIT.setText(CM_PROD_LP);
                                            txt_GSTValue.setText(CM_PROD_GST);
                                            txt_TRD_MGN_UNIT.setText("₹"+CM_PROD_MGN);

                                            productsList.addView(poLayout);
                                        }
                                        else{

                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                            lp.addRule(RelativeLayout.BELOW, (300 + vendor - 1));
                                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            lp.setMargins(0, 25, 0, 0);

                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            LinearLayout vLayout = (LinearLayout) inflater.inflate(R.layout.vendor_layout, (ViewGroup) findViewById(android.R.id.content), false);
                                            vLayout.setId(300 + vendor);
                                            vLayout.setLayoutParams(lp);

                                            final String CM_ROOM_FY = test.getString("CM_ROOM_FY");

                                            TextView vendorSummaryTitle = vLayout.findViewById(R.id.vendorSummaryTitle);
                                            vendorSummaryTitle.setText("Data of "+CM_ROOM_FY);

                                            TextView txtVendorLP = vLayout.findViewById(R.id.txtVendorLP);
                                            txtVendorLP.setText(CM_LP);

                                            TextView txtVendorTRP = vLayout.findViewById(R.id.txtVendorTRP);
                                            txtVendorTRP.setText(CM_TRP);

                                            TextView txtVendorGST = vLayout.findViewById(R.id.txtVendorGST);
                                            txtVendorGST.setText(CM_GST);

                                            TextView txtVendorQty = vLayout.findViewById(R.id.txtVendorQty);
                                            txtVendorQty.setText(CM_QTY);

                                            TextView txtVendorSchemeMgn = vLayout.findViewById(R.id.txtVendorSchemeMgn);
                                            txtVendorSchemeMgn.setText(CM_SCH_MGN);

                                            TextView txtVendorTrgMgn = vLayout.findViewById(R.id.txtVendorTrdMgn);
                                            txtVendorTrgMgn.setText(CM_TRD_MGN);

                                            productsList = vLayout.findViewById(R.id.rlProductSummaries);
                                            final LinearLayout llDef1 = vLayout.findViewById(R.id.llDef1);
                                            final ImageView arrow = vLayout.findViewById(R.id.arrow);

                                            TextView txtVendorName = vLayout.findViewById(R.id.txtVendorName);
                                            TextView txtVendorProductCount = vLayout.findViewById(R.id.txtVendorProductCount);

                                            txtVendorName.setText((vendor+1)+") "+CM_DESC);
                                            txtVendorProductCount.setText(CM_TIME);

                                            final RelativeLayout finalProductsList = productsList;
                                            vLayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (finalProductsList.getVisibility() == View.VISIBLE)
                                                    {
                                                        finalProductsList.setVisibility(View.GONE);
                                                        llDef1.setVisibility(View.GONE);
                                                        arrow.setImageDrawable(getDrawable(R.drawable.down_arrow_white));
                                                    } else {
                                                        finalProductsList.setVisibility(View.VISIBLE);
                                                        llDef1.setVisibility(View.VISIBLE);
                                                        arrow.setImageDrawable(getDrawable(R.drawable.up_arrow_white));
                                                    }
                                                }
                                            });

                                            rlProductSummaries.addView(vLayout);
                                            vendor++;


                                        }
                                    }

                                }

                                //subcategoryListReport
                                final JSONArray SubcategoryList= responseJSON.getJSONArray("subcategoryListReport");
                                len = SubcategoryList.length();

                                final List<String> spinnerSubcategoryArray = new ArrayList<>();

                                int subcategoryIndex = 0;

                                subcategories.clear();

                                for (int j = 0; j < len; j++)
                                {

                                    JSONObject Subcategory = (JSONObject) SubcategoryList.get(j);

                                    final String sub_cat_nm = Subcategory.getString("sub_cat_nm");
                                    final String sub_cat_agent_id=Subcategory.getString("sub_cat_agent_id");
                                    final String sub_cat_id=Subcategory.getString("sub_cat_id");
                                    spinnerSubcategoryArray.add(sub_cat_nm);

                                    Company companyObj = new Company();
                                    companyObj.comp_id = sub_cat_id;
                                    companyObj.comp_nm = sub_cat_nm;

                                    subcategories.add(companyObj);

                                    if(selectedSubCategory.equals(sub_cat_id))
                                    {
                                        subcategoryIndex = j;
                                    }
                                }

                                ArrayAdapter<String> adapterSubcategory = new ArrayAdapter<String>(
                                        BrandReport.this,
                                        android.R.layout.simple_spinner_item,
                                        spinnerSubcategoryArray
                                );

                                final Spinner SpinnerSubcategory = findViewById(R.id.spinnerStatus);
                                SpinnerSubcategory.setAdapter(adapterSubcategory);
                                SpinnerSubcategory.setSelection(subcategoryIndex);

                                //Company List
                                final JSONArray compList = responseJSON.getJSONArray("compList");
                                len = compList.length();

                                List<String> spinnerCompanyArray = new ArrayList<>();

                                int companiesIndex = 0;
                                subbrands.clear();
                                for (int j = 0; j < len; j++)
                                {
                                    JSONObject company = (JSONObject) compList.get(j);

                                    final String comp_nm = company.getString("comp_nm");
                                    final String comp_id = company.getString("comp_id");
                                    spinnerCompanyArray.add(comp_nm);

                                    Company companyObj = new Company();
                                    companyObj.comp_id = comp_id;
                                    companyObj.comp_nm = comp_nm;

                                    subbrands.add(companyObj);

                                    if(selectedSubBrand.equals(comp_id))
                                        companiesIndex = j;

                                }

                                ArrayAdapter<String> adapterCompany = new ArrayAdapter<String>(
                                        BrandReport.this,
                                        android.R.layout.simple_spinner_item,
                                        spinnerCompanyArray
                                );

                                final Spinner spinnerCompany = findViewById(R.id.spinnerSubBrand);
                                spinnerCompany.setAdapter(adapterCompany);
                                spinnerCompany.setSelection(companiesIndex);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        } else {
                            String errorString = responseJSON.getString("obj");
                            Toast.makeText(BrandReport.this, errorString, Toast.LENGTH_LONG).show();
                        }

                        Loading.cancel();
                    } catch (Exception e) {
                        Loading.cancel();
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

    private void showCustomDialog(final String itemId, String productDetail, final String moq, final String scheme1, final String scheme2, final String scheme3) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.quick_order_popup_layout, viewGroup, false);

        TextView txt_moq = (TextView) dialogView.findViewById(R.id.txt_moq);
        TextView txt_scheme1 = (TextView) dialogView.findViewById(R.id.txt_scheme1);
        TextView txt_scheme2 = (TextView) dialogView.findViewById(R.id.txt_scheme2);
        TextView txt_scheme3 = (TextView) dialogView.findViewById(R.id.txt_scheme3);
        TextView txt_productdetail = (TextView) dialogView.findViewById(R.id.txt_productdetail);

        final RadioButton rbMOQ = (RadioButton) dialogView.findViewById(R.id.rbMOQ);
        final RadioButton rbScheme1 = (RadioButton) dialogView.findViewById(R.id.rbScheme1);
        final RadioButton rbScheme2 = (RadioButton) dialogView.findViewById(R.id.rbScheme2);
        final RadioButton rbScheme3 = (RadioButton) dialogView.findViewById(R.id.rbScheme3);

        rbMOQ.setChecked(true);

        rbMOQ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(rbMOQ.isChecked()){
                    rbScheme1.setChecked(false);
                    rbScheme2.setChecked(false);
                    rbScheme3.setChecked(false);
                }

            }
        });

        rbScheme1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(rbScheme1.isChecked()){
                    rbMOQ.setChecked(false);
                    rbScheme2.setChecked(false);
                    rbScheme3.setChecked(false);
                }

            }
        });

        rbScheme2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(rbScheme2.isChecked()){
                    rbMOQ.setChecked(false);
                    rbScheme1.setChecked(false);
                    rbScheme3.setChecked(false);
                }

            }
        });

        rbScheme3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(rbScheme3.isChecked()){
                    rbMOQ.setChecked(false);
                    rbScheme1.setChecked(false);
                    rbScheme2.setChecked(false);
                }

            }
        });

        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);

        txt_moq.setText("MOQ : "+moq);
        txt_scheme1.setText("Scheme 1: "+scheme1);
        txt_scheme2.setText("Scheme 2: "+scheme2);
        txt_scheme3.setText("Scheme 3: "+scheme3);
        txt_productdetail.setText(productDetail);

        //Now we need an AlertDialog.Builder object
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                String itmQty = moq, distId = "1";

                if(rbScheme1.isChecked()){
                    itmQty = scheme1.split("\\+")[0];
                    distId = "2";
                }
                if(rbScheme2.isChecked()){
                    itmQty = scheme2.split("\\+")[0];
                    distId = "2";
                }
                if(rbScheme3.isChecked()){
                    itmQty = scheme3.split("\\+")[0];
                    distId = "2";
                }

                JSONObject Param = new JSONObject();
                try {
                    Param.accumulate("complaint_no", itemId+":"+itmQty);
                    Param.accumulate("usercd", userCd);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                System.out.println(Param);

                ApiService apiService1 = new ApiService(getQuickOrderData, RequestType.GET,
                        QUICK_ORDER + Param.toString(),new HashMap<String, String>(), Param);

                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    OnResponseListener getQuickOrderData = new OnResponseListener()
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

                        if (loginSuccess.equals("Y"))
                        {
                            Toast.makeText(BrandReport.this,"Quick Order saved in Draft Successfully.",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String errorString = responseJSON.getString("obj");
                            Toast.makeText(BrandReport.this, errorString, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }});
        }

        @Override
        public void onError(String error) {

        }
    };

}