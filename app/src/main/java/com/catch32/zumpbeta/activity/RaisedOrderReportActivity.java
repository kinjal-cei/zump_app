package com.catch32.zumpbeta.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class RaisedOrderReportActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = RaisedOrderReportActivity.class.getSimpleName();
    private Context mContext;
    private TextView mRetailer, mCompany, mVendor, mProductType, mBrand, mFinalItem, mFinalQty, mFinalAmt, mStatus, mEdd, mPayTerm,mTotalText;
    private EditText mRemark;
    private TableLayout mTableLyt;
    private OrderDetails mOrderDetails;
    private RadioGroup mStatusGrp; //SB20200316
    private Button mSubmitBtn; //SB20200316

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raised_order_report_activty);
      //  System.out.println("****Raised Order**Testing:");
       // getSupportActionBar().setTitle("["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"]\n"+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY));
        getSupportActionBar().setTitle("["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"]\n"+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY));
        getSupportActionBar().setHomeButtonEnabled(true);
        init();

    }

    private void init()
    {
        mContext = this;
//////SB20200225//////////////////////////
        TextView info = findViewById(R.id.txt_screen_info);
        info.setText("["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home > Order Book > Order Details");
        //////^SB20200225//////////////////////////
        mRetailer = findViewById(R.id.txt_retailer);
        mCompany = findViewById(R.id.txt_company);
        mVendor = findViewById(R.id.txt_vendor);
        mProductType = findViewById(R.id.txt_prod_type);
        mBrand = findViewById(R.id.txt_brand);

        mTotalText = findViewById(R.id.txt_total); //SB20191214

        mFinalItem = findViewById(R.id.txt_item);
        mFinalQty = findViewById(R.id.txt_final_qty);
        mFinalAmt = findViewById(R.id.txt_final_amt);

        mStatus = findViewById(R.id.txt_status);
        mEdd = findViewById(R.id.txt_edd);
        mPayTerm = findViewById(R.id.txt_payterm);
        mRemark = findViewById(R.id.txt_remark);

        mTableLyt = findViewById(R.id.info_details);

        String complainNumber = getIntent().getStringExtra("complaint_number");
       //SB20200316 findViewById(R.id.btn_submit).setOnClickListener(this);

        mSubmitBtn = findViewById(R.id.btn_submit); //SB20200316
        mSubmitBtn.setEnabled(false);
        mSubmitBtn.setOnClickListener(this); //SB20200316
        mStatusGrp = findViewById(R.id.rg_status); //SB20200316
        mStatusGrp.setOnCheckedChangeListener(this); //SB20200316
        getOrderDetails(complainNumber);

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
        if (tag.equals(AppConstant.Actions.GET_ORDER_DETAILS))
        {
            mOrderDetails = gson.fromJson(response, OrderDetails.class);
            mRetailer.setText(mOrderDetails.getComplainData().getUser_nm());
            mCompany.setText(mOrderDetails.getComplainData().getCm_no());
            mVendor.setText(mOrderDetails.getComplainData().getDist_nm());
            mProductType.setText(mOrderDetails.getComplainData().getSubcategory());
            mBrand.setText(mOrderDetails.getComplainData().getDescription());

            mTotalText.setText("Total");  //SB201911214
            int totalFreeQty = 0; //SB201911214
            for (ProductList productList : mOrderDetails.getProductList())
            {
                addTableRow(productList);
                //SB201911214
                if (productList.getFree_qty() != null && !productList.getFree_qty().isEmpty())
                {
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
           if(statusIntString.equals("0")) {
               findViewById(R.id.rb_final).setVisibility(View.INVISIBLE);
               findViewById(R.id.rb_cancel).setVisibility(View.VISIBLE);
               findViewById(R.id.rb_cancel).setActivated(true);
               findViewById(R.id.rb_final).setActivated(false);
           }
           else if(statusIntString.equals("1"))
           {
               findViewById(R.id.rb_final).setVisibility(View.VISIBLE);
               findViewById(R.id.rb_cancel).setVisibility(View.INVISIBLE);
               findViewById(R.id.rb_cancel).setActivated(false);
               findViewById(R.id.rb_final).setActivated(true);
           }
           else if(statusIntString.equals("2"))
           {
               findViewById(R.id.rb_final).setVisibility(View.INVISIBLE);
               findViewById(R.id.rb_cancel).setVisibility(View.INVISIBLE);
               mSubmitBtn.setVisibility(View.INVISIBLE);
           }
           else if(statusIntString.equals("9"))
               {
                findViewById(R.id.rb_final).setVisibility(View.INVISIBLE);
                findViewById(R.id.rb_cancel).setVisibility(View.INVISIBLE);
                mSubmitBtn.setVisibility(View.INVISIBLE);
            }
            else
            {
                findViewById(R.id.rb_final).setVisibility(View.INVISIBLE);
                findViewById(R.id.rb_cancel).setVisibility(View.VISIBLE);
                findViewById(R.id.rb_cancel).setActivated(true);
                findViewById(R.id.rb_final).setActivated(false);
            }
            String status = "";
            switch (statusIntString)
            {
                case "0":
                    status = "Pending";
                    break;

                case "1":
                    status = "In Process";
                    break;

                case "2":
                    status = "Delivered";
                    break;

                case "5":
                    status = "HOLD";
                    break;

                case "9":
                    status = "CANCEL";
                    break;
            }
            mStatus.setText(status);
            mEdd.setText(mOrderDetails.getComplainData().getTargetdt());
            mPayTerm.setText(mOrderDetails.getComplainData().getPayterm());
            mRemark.setText(mOrderDetails.getComplainData().getRemark());
        } else {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
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
           // remark = "-";
        {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.drawable.img_feedback);
        builder.setTitle("Reward Ratings");
        builder.setMessage("Please Enter Remark(min 5 Chs)");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
              //  Intent intent = null;
              //  intent = new Intent(getActivity(), MainActivity.class);
               // mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener()
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

        // sharedPrefUtil.putString(BaseSharedPref.STORE_NAME_KEY, raisedOrder.getName());
    }
        ///////SB20200316///////////////////////////////////////////
        int status = 1;
        int id = mStatusGrp.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rb_final:
                status = 2;
                break;
            case R.id.rb_cancel:
                status = 9;
                break;
        }
        ///////////////////////////////////////////////////////////////
        String targetDate = "";

        if (mOrderDetails.getComplainData().getComplainsts().equals("0"))
            targetDate = mOrderDetails.getComplainData().getCurrentDt();
        else if (mOrderDetails.getComplainData().getComplainsts().equals("1"))
            targetDate = mOrderDetails.getComplainData().getTargetdt();


        String orderNumber = mOrderDetails.getComplainData().getCm_no().split(" ")[0];
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("complainsts", mOrderDetails.getComplainData().getComplainsts());
       //SB jsonObject.addProperty("complainsts_new", "2");
        jsonObject.addProperty("complainsts_new", status); //SB20200316
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
    //@Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_final)
        {
                mSubmitBtn.setText("Delivered");
                mSubmitBtn.setEnabled(true);
                mSubmitBtn.setBackgroundColor(Color.BLUE);
                mSubmitBtn.setTextColor(Color.WHITE);
        } else if (checkedId == R.id.rb_cancel)
        {
            mSubmitBtn.setText("Cancel Order");
            mSubmitBtn.setEnabled(true);
            mSubmitBtn.setBackgroundColor(Color.RED);
            mSubmitBtn.setTextColor(Color.WHITE);
        }
    }
}
