package com.catch32.zumpbeta.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class OrderDetailsActivity extends AppCompatActivity implements ResponseListener, View.OnClickListener
{
    private static final String TAG = OrderDetailsActivity.class.getSimpleName();
    private Context mContext;
    private TextView mRetailer, mCompany, mVendor, mVendorPhNo, mProductType, mBrand, mFinalItem, mFinalQty, mFinalAmt, mStatus, mEdd, mPayTerm, mRemarkTv,mTotalText;
    private EditText mRemark;
    private TableLayout mTableLyt;
    private OrderDetails mOrderDetails;
    private TextView mBrandProd; //SB20200103
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getSupportActionBar().setTitle("["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"]\n"+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY));
      //  getSupportActionBar().setTitle("Go to Draft Order for Qty Change..");
        getSupportActionBar().setHomeButtonEnabled(true);
        init();
    }

    private void init()
    {
        mContext = this;
        mRetailer = findViewById(R.id.txt_retailer);
        mCompany = findViewById(R.id.txt_company);
        mVendor = findViewById(R.id.txt_vendor);
        mVendorPhNo = findViewById(R.id.txt_ph_no); //SB20191221
        mProductType = findViewById(R.id.txt_prod_type);
        mBrand = findViewById(R.id.txt_brand);
        mBrandProd = findViewById(R.id.txt_brand_prod); //SB20200103
        mTotalText = findViewById(R.id.txt_total); //SB20191214
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
        String position = getIntent().getStringExtra("position");
        String title = getIntent().getStringExtra("title");

        if (title != null)
            getSupportActionBar().setTitle(title);

        findViewById(R.id.btn_submit).setOnClickListener(this);

        findViewById(R.id.btn_submit).setVisibility(getIntent().getBooleanExtra("submit", false) ? View.VISIBLE : View.GONE);
        mRemark.setVisibility(getIntent().getBooleanExtra("submit", false)?View.VISIBLE:View.GONE);
        mRemarkTv.setVisibility(getIntent().getBooleanExtra("submit", false)?View.GONE:View.VISIBLE);

        TextView description = findViewById(R.id.txt_title);
        description.setText(getIntent().getBooleanExtra("hsn", true) ? "HSN: Description" : "Description");

        String screen = getIntent().getStringExtra("screen");
        TextView info = findViewById(R.id.txt_screen_info);
        if(screen.contains("My Orders") || screen.contains("Summary"))
            info.setText("["+ SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home > " + screen + " >  Order Details");
        else
            info.setText("["+ SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home > " + screen + " > Draft Order Details");
        getOrderDetails(complainNumber, position);
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
    private void getOrderDetails(String complainNumber, String position)
    {
        String param = complainNumber;
        if (position != null)
        {
            param = complainNumber + ":" + position;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("complaint_no", param);
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));

        new PostDataToServerTask(mContext, AppConstant.Actions.GET_ORDER_DETAILS)
                .setPath(AppConstant.WebURL.GET_ORDER_DETAILS)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }


    private void addTableRow(ProductList productList) {
        TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.order_details_row, null);

        TextView title = (TextView) tr.findViewById(R.id.txt_description);
        TextView gst = (TextView) tr.findViewById(R.id.txt_gst);
        TextView qty = (TextView) tr.findViewById(R.id.txt_qty);
        TextView amt = (TextView) tr.findViewById(R.id.txt_amt);

        String qtyStr = productList.getProd_qty();

        if (productList.getFree_qty() != null && !productList.getFree_qty().isEmpty()) {
            qtyStr = qtyStr + "\n" + "(" + productList.getFree_qty() + ")";
        }


        String amtStr = productList.getProd_value();

        if (productList.getProd_trp() != null && !productList.getProd_trp().isEmpty()) {
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

    }


    @Override
    public void onResponse(String tag, String response)
    {
        Gson gson = GsonFactory.getInstance();
        mOrderDetails = gson.fromJson(response, OrderDetails.class);
        mRetailer.setText(mOrderDetails.getComplainData().getUser_nm());
        mCompany.setText(mOrderDetails.getComplainData().getCm_no());
        mVendor.setText(mOrderDetails.getComplainData().getDist_nm());
        mVendorPhNo.setText("M# "+mOrderDetails.getComplainData().getDist_ph_no()); //SB20191221
        mProductType.setText(mOrderDetails.getComplainData().getSubcategory());
        mBrand.setText(mOrderDetails.getComplainData().getDescription());

        mBrandProd.setText("Brand : "); //SB20200103
        mTotalText.setText("Total");  //SB201911214
        int totalFreeQty = 0; //SB201911214
        for (ProductList productList : mOrderDetails.getProductList())
        {
            addTableRow(productList);
            //SB201911214
            if (productList.getFree_qty() != null && !productList.getFree_qty().isEmpty()) {
                int freeQty = Integer.parseInt(productList.getFree_qty());
                totalFreeQty += freeQty;
            }
            //^SB201911214
        }

        if (totalFreeQty > 0) //SB201911214
            mTotalText.setText("Total(incl.Free "+totalFreeQty+")");

        mFinalItem.setText(mOrderDetails.getComplainData().getTotItem());
        mFinalQty.setText(mOrderDetails.getComplainData().getTotQty() + "");
        mFinalAmt.setText(mOrderDetails.getComplainData().getTotAmt() + "");

        String statusIntString = mOrderDetails.getComplainData().getComplainsts();
        String status = "";
        switch (statusIntString) {
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
    }

    @Override
    public void onError(String tag, String error) {
        {
            WidgetUtil.showErrorToast(mContext, error);
        }
    }

    @Override
    public void onClick(View v) {

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
