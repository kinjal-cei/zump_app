package com.catch32.zumpbeta.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.adapter.CategoryListReportAdapter;
import com.catch32.zumpbeta.adapter.DepartmentListAdapter;
import com.catch32.zumpbeta.adapter.NewOrderAdapter;
import com.catch32.zumpbeta.adapter.SubCategoryBrandListAdapter;
import com.catch32.zumpbeta.adapter.SubCategoryListAdapter;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.dialog.OrderDetailsDialog;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.OrderDetailsSubmitListener;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.CategoryListReport;
import com.catch32.zumpbeta.model.DepartmentList;
import com.catch32.zumpbeta.model.NewOrder;
import com.catch32.zumpbeta.model.SubCategoryBrandList;
import com.catch32.zumpbeta.model.SubCategoryList;
import com.catch32.zumpbeta.model.SubCategoryProdList;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class NewOrderActivity extends AppCompatActivity implements ResponseListener, AdapterView.OnItemSelectedListener,
        View.OnClickListener, OrderDetailsSubmitListener
{
    private static final String TAG = NewOrderActivity.class.getSimpleName();
    private Context mContext;

    private CategoryListReportAdapter mCategoryListReportAdapter;
    private SubCategoryListAdapter mSubcategoryListAdapter;
    private DepartmentListAdapter mDepartmentAdapter;
    private SubCategoryBrandListAdapter mSubCategoryBrandListAdapter;
    private NewOrderAdapter mNewOrderAdapter;

    private List<DepartmentList> mVendorList;
    private List<CategoryListReport> mCategoryList;
    private List<SubCategoryList> mProductTypeList;
    private List<SubCategoryBrandList> mBrandList;
    private List<SubCategoryProdList> mNewOrderList;

    private Spinner mVendorSpinner;
    private Spinner mProductTypeSpinner;
    private Spinner mCategorySpinner;
    private Spinner mBrandSpinner;

    private TextView mDlvSch;
    private TextView mTxtCount;
    private TextView mTxtAmount;

    private EditText mRemarks;
    private EditText mSearchEt;

    double min_bill_amount = 300, bill_amount = 0, amountBasic=0; //SB20191213
    private TextView mHeader; //SB20200108
    String Comment=""; //SB20200110
    private AppBarLayout mAppBarLayout;
    private Button mDraftBtn, mSubmitBtn; //SB20200316
    private TextView mTxtNote2, mTxtNote2A; //SB20200620
    String Note2="MoA"; //SB20200620
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        initView();
    }

    DecimalFormat dnf = new DecimalFormat("##########0.00");

    private void initView()
    {
        mContext = this;
        TextView header = findViewById(R.id.header);
        header.setText(SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY));
        mVendorList = new ArrayList<>();
        mProductTypeList = new ArrayList<>();
        mCategoryList = new ArrayList<>();
        mBrandList = new ArrayList<>();
        mNewOrderList = new ArrayList<>();

        mDepartmentAdapter = new DepartmentListAdapter(mContext, mVendorList);
        mCategoryListReportAdapter = new CategoryListReportAdapter(mContext, mCategoryList);
        mSubcategoryListAdapter = new SubCategoryListAdapter(mContext, mProductTypeList);
        mSubCategoryBrandListAdapter = new SubCategoryBrandListAdapter(mContext, mBrandList);
        mNewOrderAdapter = new NewOrderAdapter(this, mNewOrderList);
        TextView info = findViewById(R.id.header_path);
        info.setText("["+ SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home > Book My Order");
        mVendorSpinner = findViewById(R.id.spinner_vendor);
        mCategorySpinner = findViewById(R.id.spinner_category);
        mProductTypeSpinner = findViewById(R.id.spinner_product_type);
        mBrandSpinner = findViewById(R.id.spinner_brand);

        mDlvSch = findViewById(R.id.dlv_sch);
        mRemarks = findViewById(R.id.et_remarks);

        mTxtCount = findViewById(R.id.txt_count);
        mTxtAmount = findViewById(R.id.txt_amt);
        mTxtNote2 = findViewById(R.id.txt_price_note2); //SB20200620
        mTxtNote2A = findViewById(R.id.txt_moa_note2); //SB20200620

        mSearchEt = findViewById(R.id.searchBar);

        mVendorSpinner.setAdapter(mDepartmentAdapter);
        mCategorySpinner.setAdapter(mCategoryListReportAdapter);
        mProductTypeSpinner.setAdapter(mSubcategoryListAdapter);
        mBrandSpinner.setAdapter(mSubCategoryBrandListAdapter);

        ListView listView = findViewById(R.id.list_new_order);
        listView.setEmptyView(findViewById(R.id.empty_view));
        listView.setAdapter(mNewOrderAdapter);

        mVendorSpinner.setOnItemSelectedListener(this);
        mProductTypeSpinner.setOnItemSelectedListener(this);
        mCategorySpinner.setOnItemSelectedListener(this);
        mBrandSpinner.setOnItemSelectedListener(this);


        findViewById(R.id.btn_draft).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);
        mDraftBtn = findViewById(R.id.btn_draft); //SB20200318
        //SB20200624 mDraftBtn.setEnabled(false); //SB20200318
        mSubmitBtn = findViewById(R.id.btn_submit); //SB20200318
       //SB20200624 mSubmitBtn.setEnabled(false);//SB20200318

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("categoryId", "-1");
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("distId", "-1");

        new PostDataToServerTask(mContext, AppConstant.Actions.GET_DATA)
                .setPath(AppConstant.WebURL.GET_DATA)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();

    }

    public void updateCountAndAmount()
    {
        int count = 0; int qty =0;
        double amount = 0; double amountBasic = 0;
        for (SubCategoryProdList product : mNewOrderList)
        {
            if (product.getQty() != null && !product.getQty().isEmpty())
            {
                qty = Integer.parseInt(product.getQty());
                double trp = Double.parseDouble(product.getSubCategoryProdTrp());
                double basePrice = Double.parseDouble(product.getSubCategoryProdBasic()); //SB20200211
                ////////SB20200110: Check for Min Product Amt/////////////////////////
                double prodAmt =0; double prodbaseAmt =0;
                double moa = Double.parseDouble(product.getMoa()); //SB20200110
              //  double mba = Double.parseDouble(product.getMba()); //SB20200110
                prodbaseAmt =qty * basePrice;
                prodAmt =qty * trp;
                if (prodAmt<moa)
                {
                    Comment=dnf.format(prodAmt)+ " < Min.Product Order Amt(Rs."+moa+")";
                }
                if (qty > 0)
                {
                    count++;
                    amount = amount + (qty * trp);
                    amountBasic = amountBasic + (qty * basePrice); //SB20200211
                   //SB bill_amount=amount; //SB20191213
                    bill_amount=amountBasic; //SB20200211
                }
                else
                    bill_amount=0;
            }
        }
        mTxtNote2A.setText("LP (Rs.)  ");
        if(bill_amount>=min_bill_amount)
        {
            mTxtCount.setText(count + "");
            mTxtAmount.setText(dnf.format(amount));
            mTxtAmount.setBackgroundColor(Color.BLUE);
            mDraftBtn.setEnabled(true); //SB20200318
            mSubmitBtn.setEnabled(true); //SB20200318
        }
       /* else if(amount<min_bill_amount)
        {
            mTxtCount.setText(count + "");
            mTxtAmount.setText(dnf.format(amount));
            mTxtAmount.setBackgroundColor(Color.YELLOW);
            mDraftBtn.setEnabled(true); //SB20200318
            mSubmitBtn.setEnabled(true); //SB20200318
        }*/
        else
        {
           // mTxtCount.setText("0");
          //  mTxtAmount.setText("Rs."+dnf.format(min_bill_amount));
          //  mTxtAmount.setText("Rs. 0");
            mTxtCount.setText(count + "");
            mTxtAmount.setText(dnf.format(amount));
            mTxtAmount.setBackgroundColor(Color.RED);
         //SB20200624   mDraftBtn.setEnabled(false); //SB20200318
            // SB20200624    mSubmitBtn.setEnabled(false); //SB20200318
        }
    }
    @Override
    public void onResponse(String tag, String response)
    {
        Gson gson = GsonFactory.getInstance();
        NewOrder newOrder = null;
        mNewOrderList.clear();
        min_bill_amount = 300; bill_amount = 0; //SB20191213

        mTxtCount.setText("MoA"); //SB20191213
        mTxtAmount.setText("Rs."+dnf.format(min_bill_amount)); //SB20191213
        //SB20200623    mTxtAmount.setText("Rs.0"); //SB20191213
       // mHeader.setText(""+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY)); //SB20200108
        mTxtNote2.setText("");
        mTxtNote2A.setText("");

        switch (tag) {
            case AppConstant.Actions.GET_DATA:
                mVendorList.clear();
                mProductTypeList.clear();
                mCategoryList.clear();
                mBrandList.clear();


                newOrder = gson.fromJson(response, NewOrder.class);

                mVendorList.addAll(newOrder.getDepartmentList());
                mProductTypeList.addAll(newOrder.getSubCategoryList());
                mCategoryList.addAll(newOrder.getCategoryList());
                mBrandList.addAll(newOrder.getSubCategoryBrandList());

                mSubCategoryBrandListAdapter.notifyDataSetChanged();
                mDepartmentAdapter.notifyDataSetChanged();
                mSubcategoryListAdapter.notifyDataSetChanged();
                mCategoryListReportAdapter.notifyDataSetChanged();
                break;

            case AppConstant.Actions.GET_CATEGORY_DATA:
                mCategoryList.clear();
                newOrder = gson.fromJson(response, NewOrder.class);
                mCategoryList.addAll(newOrder.getCategoryList());
                mCategoryListReportAdapter.notifyDataSetChanged();
                mDlvSch.setText(newOrder.getDelvSch());
                mCategorySpinner.setSelection(0);
                mProductTypeSpinner.setSelection(0);
                mBrandSpinner.setSelection(0);
                break;

            case AppConstant.Actions.GET_SUB_CATEGORY_DATA:
                mProductTypeList.clear();
                newOrder = gson.fromJson(response, NewOrder.class);
                mProductTypeList.addAll(newOrder.getSubCategoryList());
                mDlvSch.setText(newOrder.getDelvSch());
                mSubcategoryListAdapter.notifyDataSetChanged();

                mProductTypeSpinner.setSelection(0);
                mBrandSpinner.setSelection(0);
                break;

            case AppConstant.Actions.GET_SUB_CATEGORY_BRAND_DATA:
                mBrandList.clear();
                newOrder = gson.fromJson(response, NewOrder.class);
                mBrandList.addAll(newOrder.getSubCategoryBrandList());
                mSubCategoryBrandListAdapter.notifyDataSetChanged();
                mDlvSch.setText(newOrder.getDelvSch());
                break;

            case AppConstant.Actions.GET_SUB_CATEGORY_PRODUCT_DATA:
                newOrder = gson.fromJson(response, NewOrder.class);
                mNewOrderList.addAll(newOrder.getSubCategoryProdList());
                mDlvSch.setText(newOrder.getDelvSch());
                ////////////SB20200110//////////////////
                if(Double.parseDouble(newOrder.getMinBill())>0) {
                    //  mTxtAmount.setText("Rs."+dnf.format(newOrder.getMinBill())); //SB20200110
                    mTxtAmount.setText("Rs." + newOrder.getMinBill()); //SB20200110
                    min_bill_amount=Double.parseDouble(newOrder.getMinBill());
                }
                break;

            case AppConstant.Actions.SEARCH_PRODUCT_NEW_ORDER:
                newOrder = gson.fromJson(response, NewOrder.class);
                mNewOrderList.addAll(newOrder.getSubCategoryProdList());
                mDlvSch.setText(newOrder.getDelvSch());
                ////////////SB20200110//////////////////
               // System.out.println("STEP-1: "+newOrder.getMinBill());
                if(Double.parseDouble(newOrder.getMinBill())>0) {
                    //  mTxtAmount.setText("Rs."+dnf.format(newOrder.getMinBill())); //SB20200110
                    mTxtAmount.setText("Rs." + newOrder.getMinBill()); //SB20200110
                    min_bill_amount=Double.parseDouble(newOrder.getMinBill());
                }
               // System.out.println("STEP-2: "+newOrder.getMinBill());
                break;

            case AppConstant.Actions.SUBMIT_NEW_ORDER:
            case AppConstant.Actions.SUBMIT_DRAFT_ORDER:
                finish();
        }
        mNewOrderAdapter.notifyDataSetChanged();

    }

    @Override
    public void onError(String tag, String error)
    {
        WidgetUtil.showErrorToast(mContext, error);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        int viewId = parent.getId();
        String selectedVendor = "0";
        String selectedCategory = "0";
        String selectedProductType = "0";
        String selectedBrand = "0";
        CategoryListReport categoryListReport;
        SubCategoryList subcategoryList;
        switch (viewId) {
            case R.id.spinner_vendor:
                if (position > 0)
                {
                    selectedVendor = mDepartmentAdapter.getItem(position).getId();
                    getCategoryData(selectedVendor);
                }
                break;
            case R.id.spinner_category:
                if (position > 0)
                {
                    DepartmentList departmentList = (DepartmentList) mVendorSpinner.getSelectedItem();
                    selectedVendor = departmentList.getId();

                    selectedCategory = mCategoryListReportAdapter.getItem(position).getCat_id();
                    getSubCategoryData(selectedVendor, selectedCategory);
                }
                break;
            case R.id.spinner_product_type:
                if (position > 0)
                {
                    DepartmentList departmentList = (DepartmentList) mVendorSpinner.getSelectedItem();
                    selectedVendor = departmentList.getId();

                    categoryListReport = (CategoryListReport) mCategorySpinner.getSelectedItem();
                    selectedCategory = categoryListReport.getCat_id();

                    selectedProductType = mSubcategoryListAdapter.getItem(position).getSubCategoryId();
                    getSubCategoryBrandData(selectedVendor, selectedCategory, selectedProductType);
                }
                break;
            case R.id.spinner_brand:
                if (position > 0)
                {
                    DepartmentList departmentList = (DepartmentList) mVendorSpinner.getSelectedItem();
                    selectedVendor = departmentList.getId();

                    categoryListReport = (CategoryListReport) mCategorySpinner.getSelectedItem();
                    selectedCategory = categoryListReport.getCat_id();

                    subcategoryList = (SubCategoryList) mProductTypeSpinner.getSelectedItem();
                    selectedProductType = subcategoryList.getSubCategoryId();

                    selectedBrand = mSubCategoryBrandListAdapter.getItem(position).getSubCategoryBrandId();
                    getSubCategoryProductData(selectedVendor, selectedCategory, selectedProductType, selectedBrand);
                }
                break;
        }
    }


    private void getCategoryData(String distcd)
    {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("distcd", distcd);
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));

        new PostDataToServerTask(mContext, AppConstant.Actions.GET_CATEGORY_DATA)
                .setPath(AppConstant.WebURL.GET_CATEGORY_DATA)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }
    private void getSubCategoryData(String distId, String categoryId)
    {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("distcd", distId);
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("categoryId", categoryId);

        new PostDataToServerTask(mContext, AppConstant.Actions.GET_SUB_CATEGORY_DATA)
                .setPath(AppConstant.WebURL.GET_SUB_CATEGORY_DATA)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }
    private void getSubCategoryBrandData(String distId, String categoryId, String subCategoryCd)
    {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("distcd", distId);
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("subCategoryCd", subCategoryCd);


        new PostDataToServerTask(mContext, AppConstant.Actions.GET_SUB_CATEGORY_BRAND_DATA)
                .setPath(AppConstant.WebURL.GET_SUB_CATEGORY_BRAND_DATA)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }
    private void getSubCategoryProductData(String distId, String categoryId, String subCategoryCd, String brandcd)
    {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("distcd", distId);
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("subCategoryCd", subCategoryCd);
        jsonObject.addProperty("brandcd", brandcd);


        new PostDataToServerTask(mContext, AppConstant.Actions.GET_SUB_CATEGORY_PRODUCT_DATA)
                .setPath(AppConstant.WebURL.GET_SUB_CATEGORY_PRODUCT_DATA)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_draft:
                submitOrder(true, "");
                break;
            case R.id.btn_submit:
                ////////SB20191213: Check for Min Bill Amt/////////////////////////
                List<String> itemIdList = new ArrayList<>();
                List<String> itemQtyList = new ArrayList<>();
                List<String> itemDistIdList = new ArrayList<>();
                List<String> errorList = new ArrayList<>();

                String warning = "ALERT…!!!\n";

                for (SubCategoryProdList product : mNewOrderList) {
                    if (product.getQty() != null && !product.getQty().isEmpty()) {
                        if (Integer.valueOf(product.getQty()) > 0) {
                            itemIdList.add(product.getSubCategoryProdId());
                            itemQtyList.add(product.getQty());
                            itemDistIdList.add(product.getSubCategoryParentDistId());

                            if (Integer.valueOf(product.getQty()) < Integer.valueOf(product.getMoq())) {
                                errorList.add(String.valueOf(mNewOrderList.indexOf(product) + 1));
                            }
                        }
                    }
                }

                if (!errorList.isEmpty()) {
                    warning = warning+"Rows " + getCommaSeparatedString(errorList) + " < MOQ";
                    WidgetUtil.showErrorToast(mContext, warning);
                    return;
                }
                warning = "ALERT…!!!!\n";
                if (bill_amount<min_bill_amount) {
                    warning = warning+"TRP Rs." +dnf.format(bill_amount)+ " < Min. Order Amt(Rs."+dnf.format(min_bill_amount)+")";
                    WidgetUtil.showErrorToast(mContext, warning);
                    break;
                }
                else {
                    ////////////////////////////////////////////////////////////////////
                    OrderDetailsDialog dialog = new OrderDetailsDialog();
                    dialog.setListener(this);
                    dialog.show(getSupportFragmentManager(), "ORDER_DETAILS");
                    break;
                }

            case R.id.btn_search:
                String query = mSearchEt.getText().toString();
                if (!query.isEmpty())
                    searchProduct(query);
                break;

        }
    }

    private void searchProduct(String productNumber) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("prodcd", productNumber);
       // System.out.println("STEP-3: ");
        new PostDataToServerTask(mContext, AppConstant.Actions.SEARCH_PRODUCT_NEW_ORDER)
                .setPath(AppConstant.WebURL.SEARCH_PRODUCT_NEW_ORDER)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
       // System.out.println("STEP-4: ");
    }

    private void submitOrder(boolean isDraft, String payterm) {
        DepartmentList departmentList = (DepartmentList) mVendorSpinner.getSelectedItem();
        String selectedVendor = departmentList.getId();

        CategoryListReport categoryListReport = (CategoryListReport) mCategorySpinner.getSelectedItem();
        String selectedCategory = categoryListReport.getCat_id();

        SubCategoryList subcategoryList = (SubCategoryList) mProductTypeSpinner.getSelectedItem();
        String selectedSubCategory = subcategoryList.getSubCategoryId();

        List<String> itemIdList = new ArrayList<>();
        List<String> itemQtyList = new ArrayList<>();
        List<String> itemDistIdList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();

        String warning = "ALERT…!!!\n";

        for (SubCategoryProdList product : mNewOrderList) {
            if (product.getQty() != null && !product.getQty().isEmpty()) {
                if (Integer.valueOf(product.getQty()) > 0) {
                    itemIdList.add(product.getSubCategoryProdId());
                    itemQtyList.add(product.getQty());
                    itemDistIdList.add(product.getSubCategoryParentDistId());

                    if (Integer.valueOf(product.getQty()) < Integer.valueOf(product.getMoq())) {
                        errorList.add(String.valueOf(mNewOrderList.indexOf(product) + 1));
                    }
                }
            }
        }

        if (!errorList.isEmpty()) {
            warning = warning+"Rows " + getCommaSeparatedString(errorList) + " < MOQ";
            WidgetUtil.showErrorToast(mContext, warning);
            return;
        }

        ////////////////////////////////////////////////////////////////////
        ////////SB20191213: Check for Min Bill Amt/////////////////////////
        if (bill_amount<min_bill_amount) {
            warning = warning+"TRP Rs." +dnf.format(bill_amount)+ " < Min. Order Amt(Rs."+dnf.format(min_bill_amount)+")";
            WidgetUtil.showErrorToast(mContext, warning);
            return;
        }
        ////////////////////////////////////////////////////////////////////
        String remarks = mRemarks.getText().toString();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deptSpinnerId", selectedVendor);
        jsonObject.addProperty("categorySpinnerId", selectedCategory);
        jsonObject.addProperty("subCategorySpinnerId", selectedSubCategory);
        jsonObject.addProperty("subjectDesc", "0");
        jsonObject.addProperty("filePath", "");
        jsonObject.addProperty("mobileNo", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.MOBILE_NUMBER_KEY));
        jsonObject.addProperty("roomNo", "0");
        jsonObject.addProperty("imei_no", "");
        jsonObject.addProperty("itmId", getCommaSeparatedString(itemIdList));
        jsonObject.addProperty("itmQty", getCommaSeparatedString(itemQtyList));
        jsonObject.addProperty("distId", getCommaSeparatedString(itemDistIdList));
        jsonObject.addProperty("remarknew", remarks);
        jsonObject.addProperty("supplyknew", "");

        String URL = AppConstant.WebURL.SUBMIT_DRAFT_ORDER;
        String tag = AppConstant.Actions.SUBMIT_DRAFT_ORDER;
        if (!isDraft) {
            URL = AppConstant.WebURL.SUBMIT_NEW_ORDER;
            tag = AppConstant.Actions.SUBMIT_NEW_ORDER;

            jsonObject.addProperty("payterm", payterm);
        }
        if (isDraft) {
            String warning2 = "NOTE: \n";
            warning2 = warning2 + "You have submitted Draft Order";
            WidgetUtil.showErrorToast(mContext, warning2);
        }
        new PostDataToServerTask(mContext, tag)
                .setPath(URL)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }

    public String getCommaSeparatedString(List<String> list) {
        StringBuilder string = new StringBuilder();
        for (String name : list) {
            string = string.length() > 0 ? string.append(",").append(name) : string.append(name);
        }
        return string.toString();
    }

    @Override
    public void onPayTermSelected(PAY_TERM payTerm) {
        submitOrder(false, payTerm.getPayTerm());
    }

}

