package com.catch32.zumpbeta.myactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class Order_Product_Summary_Details extends AppCompatActivity implements ResponseListener,
        NavigationView.OnNavigationItemSelectedListener
{

    private static final String TAG = Order_Product_Summary_Details.class.getSimpleName();
    private Context mContext;
    private TextView mRetailer, mCompany, mVendor, mProductType, mBrand, mFinalItem, mFinalQty, mFinalAmt, mTotalText, mBrandProd;
    private TableLayout mTableLyt;
    String menuId ="";
    private TextView mVendorPhNo, mHeadCompany, mHeadRetail;
    LinearLayout Menu,BookMyNewOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_summary_details);
            Menu=findViewById(R.id.Menu);

        BookMyNewOrder=findViewById(R.id.BookMyNewOrder);
        BookMyNewOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Order_Product_Summary_Details.this, Book_My_New_Order_Activity.class);
                startActivity(intent);
            }
        });


        LinearLayout llReports = findViewById(R.id.llReports);
        llReports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Order_Product_Summary_Details.this, MyShop.class);
                startActivity(i);
            }
        });

        LinearLayout llHome = findViewById(R.id.llHome);
        llHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Order_Product_Summary_Details.this, Dashboard_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llMonthwiseSummary = findViewById(R.id.llMonthwiseSummary);
        llMonthwiseSummary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Order_Product_Summary_Details.this, Month_Wise_Summary.class);
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
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int vid = item.getItemId();

        if (vid == R.id.nav_home)
        {
            Intent i=new Intent(Order_Product_Summary_Details.this, Dashboard_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_book_my_order)
        {
            Intent i = new Intent(Order_Product_Summary_Details.this, Book_My_New_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_order_status)
        {
            Intent i = new Intent(Order_Product_Summary_Details.this, Order_Status.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_order)
        {
            Intent i = new Intent(Order_Product_Summary_Details.this, My_Order_Activity.class);
            startActivity(i);
        }
        else if (vid == R.id.nav_my_shop)
        {
            Intent i = new Intent(Order_Product_Summary_Details.this, MyShop.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_add_more_brands)
        {
            Intent i = new Intent(Order_Product_Summary_Details.this, Add_More_Brands.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_reward)
        {
            Intent i = new Intent(Order_Product_Summary_Details.this, My_Rewards.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_summary)
        {
            Intent i = new Intent(Order_Product_Summary_Details.this, Month_Wise_Summary.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_my_draft)
        {
            Intent i = new Intent(Order_Product_Summary_Details.this, Draft_Orders.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_feedback)
        {
            Intent i=new Intent(Order_Product_Summary_Details.this, Feedback_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_profile)
        {
            Intent i=new Intent(Order_Product_Summary_Details.this, Profile_Activity.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_Notification)
        {
            Intent i=new Intent(Order_Product_Summary_Details.this, Notification.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_policy)
        {
            Intent i=new Intent(Order_Product_Summary_Details.this, Privacy_Policy.class);
            startActivity(i);
        }
        else if(vid == R.id.nav_terms_conditions)
        {
            Intent i=new Intent(Order_Product_Summary_Details.this, TermsAndCondition.class);
            startActivity(i);
        }
        else if(vid == R.id.about_us)
        {
            Intent i=new Intent(Order_Product_Summary_Details.this, About_Us.class);
            startActivity(i);
        }
        else if(vid == R.id.call_us)
        {
            Intent i=new Intent(Order_Product_Summary_Details.this, Contact_Us.class);
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
                    AppUserManager.logout(Order_Product_Summary_Details.this, SignInActivity.class);
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
            mHeadCompany = findViewById(R.id.head_company); //SB20200103
            mHeadRetail = findViewById(R.id.head_company); //SB20200103
            mRetailer = findViewById(R.id.txt_retailer);
            mCompany = findViewById(R.id.txt_company); //SB20200103
            mVendorPhNo = findViewById(R.id.txt_ph_no); //SB20191221
            mVendor = findViewById(R.id.txt_vendor);
            mProductType = findViewById(R.id.txt_prod_type);
            mBrand = findViewById(R.id.txt_brand);
            mBrandProd = findViewById(R.id.txt_brand_prod); //SB20191220

            mTotalText = findViewById(R.id.txt_total);

            mFinalItem = findViewById(R.id.txt_item);
            mFinalQty = findViewById(R.id.txt_final_qty);
            mFinalAmt = findViewById(R.id.txt_final_amt);


            mTableLyt = findViewById(R.id.info_details);

            String complainNumber = getIntent().getStringExtra("complaint_number");
            menuId = getIntent().getStringExtra("menuId");
            getOrderDetails(complainNumber);

            mHeadRetail.setText("Outlet"); //SB20191220
            mHeadCompany.setText("Company"); //SB20191220
            String screen = getIntent().getStringExtra("screen");
            TextView info = findViewById(R.id.txt_screen_info);
            info.setText(screen);

            findViewById(R.id.lyt_order_details).setVisibility(View.GONE);
            findViewById(R.id.btn_submit).setVisibility(View.GONE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void getOrderDetails(String complainNumber)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("complaint_no", complainNumber);
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));

        new PostDataToServerTask(mContext, AppConstant.Actions.GET_ORDER_DETAILS)
                .setPath(AppConstant.WebURL.GET_ORDER_DETAILS)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }
    private void addTableRow(ProductList productList)
    {
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

        if(productList.getProd_mrp().equals("")) //SB20200309
            title.setText(productList.getDescription());
        else
            title.setText(productList.getDescription()+"\n"+productList.getProd_mrp()); //SB20200309
        gst.setText(productList.getProd_gst());

        qty.setText(qtyStr);
        amt.setText(amtStr);

        View line = new View(mContext);
        line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        line.setBackgroundColor(Color.rgb(120, 144, 156));
        mTableLyt.addView(tr);
        mTableLyt.addView(line);
    }
    @Override
    public void onResponse(String tag, String response)
    {
        Gson gson = GsonFactory.getInstance();
        OrderDetails orderDetails = gson.fromJson(response, OrderDetails.class);
        mRetailer.setText(orderDetails.getComplainData().getUser_nm());
        mCompany.setText(orderDetails.getComplainData().getCm_no());
        mVendor.setText(orderDetails.getComplainData().getDist_nm());
        mVendorPhNo.setText("Ph# "+orderDetails.getComplainData().getDist_ph_no()); //SB20191221
        mProductType.setText(orderDetails.getComplainData().getSubcategory());
        mBrand.setText(orderDetails.getComplainData().getDescription());
        if(menuId.equals("3"))
            mBrandProd.setText("Product : ");
        else
            mBrandProd.setText("Brand : ");
        mTotalText.setText("Total");
        int totalFreeQty = 0;
        for (ProductList productList : orderDetails.getProductList())
        {
            addTableRow(productList);
            if (productList.getFree_qty() != null && !productList.getFree_qty().isEmpty())
            {
                int freeQty = Integer.parseInt(productList.getFree_qty());
                totalFreeQty += freeQty;
            }
        }
        if (totalFreeQty > 0)
            mTotalText.setText("Total(incl.Free "+totalFreeQty+")");

        mFinalItem.setText(orderDetails.getComplainData().getTotItem());
        mFinalQty.setText(orderDetails.getComplainData().getTotQty() + "");
        mFinalAmt.setText(orderDetails.getComplainData().getTotAmt() + "");
    }

    @Override
    public void onError(String tag, String error)
    {
        {
            WidgetUtil.showErrorToast(mContext, error);
        }
    }
}
