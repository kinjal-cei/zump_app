package com.catch32.zumpbeta.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
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
import com.catch32.zumpbeta.dialog.QtyAddOrUpdateDialog;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.QtyAddListener;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.OrderDetails;
import com.catch32.zumpbeta.model.ProductList;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;

public class DraftReportActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, RadioGroup.OnCheckedChangeListener
{
    private Context mContext;
    private TextView mRetailer, mCompany, mVendor, mProductType, mBrand, mFinalItem, mFinalQty, mFinalAmt, mEdd,mTotalText, mtxt_MBA, mtxt_MOQ;
    private EditText mRemark;
    private TableLayout mTableLyt;
    private Button mSubmitBtn;
    private RadioGroup mPayTermGrp, mStatusGrp;
    private OrderDetails mOrderDetails;
    private String mComplainNumber;
    private int mPosition;
    public double TotalAmt=0, MinOrderAmt=300, TotalBillAmt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_report);
        getSupportActionBar().setTitle("["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY));
        getSupportActionBar().setHomeButtonEnabled(true);
        init();
    }

    DecimalFormat dnf = new DecimalFormat("##########0.00");

    private void init()
    {
        mContext = this;
        TextView info = findViewById(R.id.txt_screen_info);
        info.setText("["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home > Draft Orders > Draft Order Details");

        mRetailer = findViewById(R.id.txt_retailer);
        mCompany = findViewById(R.id.txt_company);
        mVendor = findViewById(R.id.txt_vendor);
        mProductType = findViewById(R.id.txt_prod_type);
        mBrand = findViewById(R.id.txt_brand);

        mTotalText = findViewById(R.id.txt_total);

        mFinalItem = findViewById(R.id.txt_item);
        mFinalQty = findViewById(R.id.txt_final_qty);
        mFinalAmt = findViewById(R.id.txt_final_amt);
        mtxt_MBA = findViewById(R.id.txt_MBA);
        mtxt_MOQ = findViewById(R.id.txt_MOQ);

        mEdd = findViewById(R.id.txt_edd);
        mRemark = findViewById(R.id.txt_remark);

        mTableLyt = findViewById(R.id.info_details);

        mPayTermGrp = findViewById(R.id.rg_payterm);

        mStatusGrp = findViewById(R.id.rg_status);

        mComplainNumber = getIntent().getStringExtra("complaint_number");
        mPosition = getIntent().getIntExtra("position", -1);

        mPayTermGrp.setVisibility(View.GONE);

        mSubmitBtn = findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(this);

        mStatusGrp.setOnCheckedChangeListener(this);
        getOrderDetails(mComplainNumber, mPosition);
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

    private void getOrderDetails(String complainNumber, int position)
    {
        String param = complainNumber;
        if (position != -1)
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

    private void updateOrderDetails(String complainNumber, int position, String product_cd, String prod_qty)
    {
        String param = complainNumber;
        if (position != -1)
        {
            param = complainNumber + ":" + position;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("complaint_no", param);
        jsonObject.addProperty("prod_cd", product_cd);
        jsonObject.addProperty("prod_qty", prod_qty);
        jsonObject.addProperty("report", "Y");
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));

        new PostDataToServerTask(mContext, AppConstant.Actions.GET_ORDER_DETAILS)
                .setPath(AppConstant.WebURL.GET_ORDER_DETAILS)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }

    private TableRow addTableRow(ProductList productList)
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
        /////SB20200211///////////////////////
        if (productList.getProd_trp() != null && !productList.getProd_trp().isEmpty())
            TotalAmt=TotalAmt + Double.parseDouble(amtStr);
        TotalBillAmt=TotalBillAmt+Double.parseDouble(amtStr);
        /////////////////////////////////////

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
        return tr;

    }

    @Override
    public void onResponse(final String tag, String response)
    {
        if (tag.equals(AppConstant.Actions.GET_ORDER_DETAILS))
        {
            mTableLyt.removeAllViews();
            Gson gson = GsonFactory.getInstance();

            mOrderDetails = gson.fromJson(response, OrderDetails.class);
            MinOrderAmt=Double.parseDouble(mOrderDetails.getComplainData().getMBA()); //SB20200211
            mRetailer.setText(mOrderDetails.getComplainData().getUser_nm());
            mCompany.setText(mOrderDetails.getComplainData().getCm_no());
            mVendor.setText(mOrderDetails.getComplainData().getDist_nm());
            mProductType.setText(mOrderDetails.getComplainData().getSubcategory());
            mBrand.setText(mOrderDetails.getComplainData().getDescription());
            mTotalText.setText("Total");  //SB201911214
            int totalFreeQty = 0; //SB201911214

            for (int i = 0; i < mOrderDetails.getProductList().size(); i++)
            {
                final ProductList productList = mOrderDetails.getProductList().get(i);
                final TableRow tr = addTableRow(productList);
                //SB201911214
                if (productList.getFree_qty() != null && !productList.getFree_qty().isEmpty())
                {
                    int freeQty = Integer.parseInt(productList.getFree_qty());
                    totalFreeQty += freeQty;
                }
                if (totalFreeQty > 0) //SB201911214
                    mTotalText.setText("Total(incl.Free "+totalFreeQty+")");
                //^SB201911214
                if (productList.getProd_gst() != null && !productList.getProd_gst().isEmpty())
                {
                    tr.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            QtyAddOrUpdateDialog fragment = new QtyAddOrUpdateDialog();
                            fragment.setAddOrUpdateListener(new QtyAddListener()
                            {
                                @Override
                                public void onQtyAdded(int qty)
                                {
                                    updateOrderDetails(mComplainNumber, mPosition, productList.getProd_cd(), String.valueOf(qty));
                                    TotalAmt=0; TotalBillAmt=0; //SB20200211
                                }
                            });
                            fragment.setProductList(productList);
                            fragment.show(fragmentManager, "AddOrUpdateQty");
                        }
                    });
                }
            }

            mFinalItem.setText(mOrderDetails.getComplainData().getTotItem());
            mFinalQty.setText(mOrderDetails.getComplainData().getTotQty() + "");
            mFinalAmt.setText(mOrderDetails.getComplainData().getTotAmt() + "");
            if(Double.parseDouble(mOrderDetails.getComplainData().getMBA())>0)
                mtxt_MBA.setText("MOA: Rs."+mOrderDetails.getComplainData().getMBA()); //SB20200211
            else
                mtxt_MBA.setText(""); //SB20200211
            if(!mOrderDetails.getComplainData().getMOQ().equals(""))
                mtxt_MOQ.setText(""+mOrderDetails.getComplainData().getMOQ()); //SB20200211
            else
                mtxt_MOQ.setText(""); //SB20200211

            mEdd.setText(mOrderDetails.getComplainData().getEtd()); //SB20200108
          //SB20200108  mEdd.setText(mOrderDetails.getComplainData().getTargetdt());
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
            remark = "-";

        int status = 1;
        int id = mStatusGrp.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rb_cancel:
                status = 1;
                break;
            case R.id.rb_final:
                status = 2;
                break;
        }

        String targetDate = "";

        if (mOrderDetails.getComplainData().getComplainsts().equals("0"))
            targetDate = mOrderDetails.getComplainData().getCurrentDt();
        else if (mOrderDetails.getComplainData().getComplainsts().equals("1"))
            targetDate = mOrderDetails.getComplainData().getTargetdt();
        /////////SB20191223///////////////
        String payterm="";
        if (mOrderDetails.getComplainData().getComplainsts().equals("0"))
            payterm="On Delivery";
        if (mOrderDetails.getComplainData().getComplainsts().equals("1"))
            payterm="On vendor Choice";
        ////////////////////////////////////
        String orderNumber = mOrderDetails.getComplainData().getCm_no().replace("-", "");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("complainsts", mOrderDetails.getComplainData().getComplainsts());
        jsonObject.addProperty("complainsts_new", status);
        jsonObject.addProperty("complaint_no", orderNumber);
        jsonObject.addProperty("targetdt", targetDate);
        jsonObject.addProperty("flag", "A");
        jsonObject.addProperty("color_flag", "");
        jsonObject.addProperty("remark", remark);
        jsonObject.addProperty("payterm", payterm);

        new PostDataToServerTask(mContext, AppConstant.Actions.SUBMIT_ORDER_DETAILS)
                .setPath(AppConstant.WebURL.SUBMIT_ORDER_DETAILS)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
        /////////SB20191223/////////////////////
        Intent intent = null;
        intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        ///////////////////////////////////////////////
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_final)
        {
            mPayTermGrp.setVisibility(View.VISIBLE);
            if(TotalAmt>MinOrderAmt)
            {
                mSubmitBtn.setText("Submit"+" Rs."+dnf.format(TotalBillAmt));
                mSubmitBtn.setBackgroundColor(Color.BLUE);
                mSubmitBtn.setTextColor(Color.WHITE);
            }
            else {
                mSubmitBtn.setText("Basic Amt. Rs."+dnf.format(TotalAmt)+" < MOA Rs." + dnf.format(MinOrderAmt));
                mSubmitBtn.setBackgroundColor(Color.TRANSPARENT);
                mSubmitBtn.setTextColor(Color.RED);
                mSubmitBtn.setEnabled(false);
            }

        } else if (checkedId == R.id.rb_cancel)
        {
            mPayTermGrp.setVisibility(View.GONE);
            mSubmitBtn.setText("Cancel Draft Order");
            mSubmitBtn.setBackgroundColor(Color.RED);
            mSubmitBtn.setTextColor(Color.WHITE);
        }
      /*SB20191223  AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //    builder.setIcon(R.drawable.ic_logout_red_48dp);
        if (checkedId == R.id.rb_final) {
            builder.setTitle("Final Order");
            builder.setMessage("Submit as Final Order");
        }
        if (checkedId == R.id.rb_cancel) {
            builder.setTitle("Cancel Draft Order");
            builder.setMessage("Draft Order will be Cancelled");
        }
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              //  Intent intent = null;
              //  intent = new Intent(mContext, MainActivity.class);
              //  mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        //negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
*/
    }

    @Override
    public void onBackPressed()
    {
       // super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
