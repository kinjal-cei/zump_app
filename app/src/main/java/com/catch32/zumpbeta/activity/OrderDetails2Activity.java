package com.catch32.zumpbeta.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.OrderDetails;
import com.catch32.zumpbeta.model.ProductList;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class OrderDetails2Activity extends AppCompatActivity implements ResponseListener
{

    private static final String TAG = OrderDetails2Activity.class.getSimpleName();
    private Context mContext;
    private TextView mRetailer, mCompany, mVendor, mProductType, mBrand, mFinalItem, mFinalQty, mFinalAmt, mTotalText, mBrandProd;
    private TableLayout mTableLyt;
    String menuId ="";
    private TextView mVendorPhNo, mHeadCompany, mHeadRetail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getSupportActionBar().setTitle("["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"]\n"+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY));
       // getSupportActionBar().setTitle("["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"]");
     //   getSupportActionBar().setTitle("Purchase Order Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        init();
    }

    private void init() {
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
        info.setText("Home > Reports > " + screen + " > Linked Purchase Orders");

        findViewById(R.id.lyt_order_details).setVisibility(View.GONE);
        findViewById(R.id.btn_submit).setVisibility(View.GONE);

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
