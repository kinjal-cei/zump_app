package com.catch32.zumpbeta.myactivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.SignInActivity;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.OrderDetails;
import com.catch32.zumpbeta.model.ProductList;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.process.AppUserManager;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Order_Details_Activity extends AppCompatActivity implements ResponseListener, View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = Order_Details_Activity.class.getSimpleName();
    private Context mContext;
    private TextView mRetailer, mCompany, mVendor, mVendorPhNo, mProductType, mBrand, mFinalItem, mFinalQty, mFinalAmt, mStatus, mEdd, mPayTerm, mRemarkTv,mTotalText;
    private EditText mRemark;
    int initialScrollY = 0;
    private TableLayout mTableLyt;
    private OrderDetails mOrderDetails;
    private TextView mBrandProd; //SB20200103
    LinearLayout Menu,BookMyNewOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Menu=findViewById(R.id.Menu);

        BookMyNewOrder=findViewById(R.id.BookMyNewOrder);
        BookMyNewOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Order_Details_Activity.this, Book_My_New_Order_Activity.class);
                startActivity(intent);
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


        LinearLayout llReports = findViewById(R.id.llReports);
        llReports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Order_Details_Activity.this, MyShop.class);
                startActivity(i);
            }
        });

        LinearLayout llHome = findViewById(R.id.llHome);
        llHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Order_Details_Activity.this, Dashboard_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llMonthwiseSummary = findViewById(R.id.llMonthwiseSummary);
        llMonthwiseSummary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Order_Details_Activity.this, Month_Wise_Summary.class);
                startActivity(i);
            }
        });
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
        // TextView header = findViewById(R.id.txt_user);
        navigationView.setNavigationItemSelectedListener(this);
        init();
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
        int vid = item.getItemId();

        if (vid == R.id.nav_home)
        {
            Intent i=new Intent(Order_Details_Activity.this, Dashboard_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_book_my_order)
        {
            Intent i = new Intent(Order_Details_Activity.this, Book_My_New_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_order_status)
        {
            Intent i = new Intent(Order_Details_Activity.this, Order_Status.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_order)
        {
            Intent i = new Intent(Order_Details_Activity.this, My_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_shop)
        {
            Intent i = new Intent(Order_Details_Activity.this, MyShop.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_add_more_brands)
        {
            Intent i = new Intent(Order_Details_Activity.this, Add_More_Brands.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_reward)
        {
            Intent i = new Intent(Order_Details_Activity.this, My_Rewards.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_summary)
        {
            Intent i = new Intent(Order_Details_Activity.this, Month_Wise_Summary.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_draft)
        {
            Intent i = new Intent(Order_Details_Activity.this, Draft_Orders.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_feedback)
        {
            Intent i=new Intent(Order_Details_Activity.this, Feedback_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_profile)
        {
            Intent i=new Intent(Order_Details_Activity.this, Profile_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_Notification)
        {
            Intent i=new Intent(Order_Details_Activity.this, Notification.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_policy)
        {
            Intent i=new Intent(Order_Details_Activity.this, Privacy_Policy.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_terms_conditions)
        {
            Intent i=new Intent(Order_Details_Activity.this, TermsAndCondition.class);
            startActivity(i);
        }
        else if(vid == R.id.about_us)
        {
            Intent i=new Intent(Order_Details_Activity.this, About_Us.class);
            startActivity(i);
        }
        else if(vid == R.id.call_us)
        {
            Intent i=new Intent(Order_Details_Activity.this, Contact_Us.class);
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
                    AppUserManager.logout(Order_Details_Activity.this, SignInActivity.class);
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

    private void init()
    {
        try {
            mContext = this;
            mRetailer = findViewById(R.id.txt_retailer);
            mCompany = findViewById(R.id.txt_company);
            mVendor = findViewById(R.id.txt_vendor);
            mVendorPhNo = findViewById(R.id.txt_ph_no);
            mProductType = findViewById(R.id.txt_prod_type);
            mBrand = findViewById(R.id.txt_brand);
            mBrandProd = findViewById(R.id.txt_brand_prod);
            mTotalText = findViewById(R.id.txt_total);

            mFinalItem = findViewById(R.id.txt_item);
            mFinalQty = findViewById(R.id.txt_final_qty);
            mFinalAmt = findViewById(R.id.txt_final_amt);

            mStatus = findViewById(R.id.txt_status);
            mEdd = findViewById(R.id.txt_edd);
            mPayTerm = findViewById(R.id.txt_payterm);
            mRemark = findViewById(R.id.txt_remark);

            mRemarkTv = findViewById(R.id.txt_remark_tv);
            mTableLyt = findViewById(R.id.info_details);

            String complainNumber = getIntent().getStringExtra("complaint_number");

            findViewById(R.id.btn_submit).setOnClickListener(this);
            findViewById(R.id.btn_submit).setVisibility(getIntent().getBooleanExtra("submit", false) ? View.VISIBLE : View.GONE);
            mRemark.setVisibility(getIntent().getBooleanExtra("submit", false) ? View.VISIBLE : View.GONE);
            mRemarkTv.setVisibility(getIntent().getBooleanExtra("submit", false) ? View.GONE : View.VISIBLE);

            TextView description = findViewById(R.id.txt_title);
            description.setText(getIntent().getBooleanExtra("hsn", true) ? "HSN: Description" : "Description");

            String screen = getIntent().getStringExtra("screen");
            TextView info = findViewById(R.id.txt_screen_info);
            info.setText(screen);
            getOrderDetails(complainNumber);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getOrderDetails(String complainNumber)
    {
        try {
            String param = complainNumber;
      /*  if (position != null)
        {
            param = complainNumber + ":" + position;
        }*/
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("complaint_no", param);
            jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));

            new PostDataToServerTask(mContext, AppConstant.Actions.GET_ORDER_DETAILS)
                    .setPath(AppConstant.WebURL.GET_ORDER_DETAILS)
                    .setResponseListener(this)
                    .setRequestParams(jsonObject)
                    .makeCall();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addTableRow(ProductList productList)
    {
        try {
            TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.order_details_row, null);

            TextView title = (TextView) tr.findViewById(R.id.txt_description);
            TextView gst = (TextView) tr.findViewById(R.id.txt_gst);
            TextView qty = (TextView) tr.findViewById(R.id.txt_qty);
            TextView amt = (TextView) tr.findViewById(R.id.txt_amt);

            String qtyStr = productList.getProd_qty();

            if (productList.getFree_qty() != null && !productList.getFree_qty().isEmpty())
            {
                qtyStr = qtyStr + "\n" + "(" + productList.getFree_qty() + ")";
            }
            String amtStr = productList.getProd_value();

            if (productList.getProd_trp() != null && !productList.getProd_trp().isEmpty())
            {
                amtStr = amtStr + "\n" + "(@" + productList.getProd_trp() + ")";
            }

            title.setText(productList.getProd_nm());
            gst.setText(productList.getProd_gst());
            qty.setText(qtyStr);
            amt.setText(amtStr);

            View line = new View(mContext);
            line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            line.setBackgroundColor(Color.rgb(120, 144, 156));

            mTableLyt.addView(tr);
            mTableLyt.addView(line);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String tag, String response)
    {
        try {
            Gson gson = GsonFactory.getInstance();
            mOrderDetails = gson.fromJson(response, OrderDetails.class);
            mRetailer.setText(mOrderDetails.getComplainData().getUser_nm());
            mCompany.setText(mOrderDetails.getComplainData().getCm_no());
            mVendor.setText(mOrderDetails.getComplainData().getDist_nm());
            mVendorPhNo.setText("M# " + mOrderDetails.getComplainData().getDist_ph_no()); //SB20191221
            mProductType.setText(mOrderDetails.getComplainData().getSubcategory());
            mBrand.setText(mOrderDetails.getComplainData().getDescription());

            mBrandProd.setText("Brand : "); //SB20200103
            mTotalText.setText("Total");  //SB201911214
            int totalFreeQty = 0; //SB201911214
            for (ProductList productList : mOrderDetails.getProductList())
            {
                addTableRow(productList);
                if (productList.getFree_qty() != null && !productList.getFree_qty().isEmpty())
                {
                    int freeQty = Integer.parseInt(productList.getFree_qty());
                    totalFreeQty += freeQty;
                }
            }
            if (totalFreeQty > 0)//SB201911214

            mTotalText.setText("Total(incl.Free " + totalFreeQty + ")");
            mFinalItem.setText(mOrderDetails.getComplainData().getTotItem());
            mFinalQty.setText(mOrderDetails.getComplainData().getTotQty() + "");
            mFinalAmt.setText(mOrderDetails.getComplainData().getTotAmt() + "");

            String statusIntString = mOrderDetails.getComplainData().getComplainsts();
            String status = "";
            switch (statusIntString)
            {
                case "0":
                    status = " Pending";
                    mEdd.setText(mOrderDetails.getComplainData().getEtd()); //SB20200108
                    break;
                case "1":
                    status = " In Process";
                    mEdd.setText(mOrderDetails.getComplainData().getTargetdt());
                    break;
                case "2":
                    status = " Delivered";
                    mEdd.setText(mOrderDetails.getComplainData().getTargetdt());
                    break;
                case "5":
                    status = " HOLD";
                    mEdd.setText(mOrderDetails.getComplainData().getTargetdt());
                    break;
                case "9":
                    status = " CANCELLED";
                    mEdd.setText(mOrderDetails.getComplainData().getTargetdt());
                    break;
            }

            mStatus.setText(status);
            //SB20200108  mEdd.setText(mOrderDetails.getComplainData().getTargetdt());
            mPayTerm.setText(mOrderDetails.getComplainData().getPayterm());
            mRemark.setText(mOrderDetails.getComplainData().getRemark());
            mRemarkTv.setText(mOrderDetails.getComplainData().getRemark());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String tag, String error)
    {
        {
            WidgetUtil.showErrorToast(mContext, error);
        }
    }
    @Override
    public void onClick(View v)
    {
        String remark = mRemark.getText().toString();

        if (remark.isEmpty())
            remark = "-";
        String targetDate = "";

        if (mOrderDetails.getComplainData().getComplainsts().equals("0"))
            targetDate = mOrderDetails.getComplainData().getCurrentDt();
        else if (mOrderDetails.getComplainData().getComplainsts().equals("1"))
            targetDate = mOrderDetails.getComplainData().getTargetdt();

        String orderNumber = mOrderDetails.getComplainData().getCm_no().replace("-", "");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("complainsts", mOrderDetails.getComplainData().getComplainsts());
        jsonObject.addProperty("complainsts_new", "2");
        jsonObject.addProperty("complaint_no", orderNumber);
        jsonObject.addProperty("targetdt", targetDate);
        jsonObject.addProperty("flag", "A");
        jsonObject.addProperty("color_flag", "");
        jsonObject.addProperty("remark", remark);

        new PostDataToServerTask(mContext, AppConstant.Actions.SUBMIT_ORDER_DETAILS)
                .setPath(AppConstant.WebURL.SUBMIT_ORDER_DETAILS)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }
}
