package com.catch32.zumpbeta.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.OrderDetails2Activity;
import com.catch32.zumpbeta.adapter.CategoryListReportAdapter;
import com.catch32.zumpbeta.adapter.CompanyAdapter;
import com.catch32.zumpbeta.adapter.ReportAdapter;
import com.catch32.zumpbeta.adapter.SubcategoryListReportAdapter;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.CategoryListReport;
import com.catch32.zumpbeta.model.CategoryReportData;
import com.catch32.zumpbeta.model.Company;
import com.catch32.zumpbeta.model.Reports;
import com.catch32.zumpbeta.model.SubcategoryListReport;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.ReportsUtil;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrderProductSummaryFragment extends Fragment implements ResponseListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String TAG = OrderProductSummaryFragment.class.getSimpleName();
    private Context mContext;
    private CategoryListReportAdapter mCategoryListReportAdapter;
    private SubcategoryListReportAdapter mSubcategoryListReportAdapter;
    private CompanyAdapter mCompanyAdapter;
    private ReportAdapter mReportAdapter;
    private List<Company> mSubBrandList;
    private List<CategoryReportData> mReportList;
    private List<CategoryListReport> mBrandList;
    private List<SubcategoryListReport> mProductTypeList;
    private Spinner mBrandSpinner;
    private Spinner mProductTypeSpinner;
    private Spinner mSubBrandSpinner;
    private static final String MENU_ID = "3";
    private CardView mCompanyInfoView;
    private TextView mNameTv, mTotalTv, mDescTv, mSchemeTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_order_product_summary, container, false);
        init(view);
        return view;
    }

    private void init(View view)
    {
        mContext = getActivity();
        //////SB20200225//////////////////////////
        TextView info = view.findViewById(R.id.header_path);
        info.setText("["+ SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home > Reports >Ordered Product Summary");
        //////^SB20200225//////////////////////////
        mBrandList = new ArrayList<>();
        mProductTypeList = new ArrayList<>();
        mSubBrandList = new ArrayList<>();
        mReportList = new ArrayList<>();

        mCompanyInfoView = view.findViewById(R.id.lyt_info);

        mNameTv = mCompanyInfoView.findViewById(R.id.txt_name);
        mTotalTv = mCompanyInfoView.findViewById(R.id.txt_total);
        mDescTv = mCompanyInfoView.findViewById(R.id.txt_desc);
        mSchemeTv = mCompanyInfoView.findViewById(R.id.txt_scheme);

        mCategoryListReportAdapter = new CategoryListReportAdapter(mContext, mBrandList);
        mSubcategoryListReportAdapter = new SubcategoryListReportAdapter(mContext, mProductTypeList);
        mCompanyAdapter = new CompanyAdapter(mContext, mSubBrandList);
        mReportAdapter = new ReportAdapter(mContext, mReportList);

        mBrandSpinner = view.findViewById(R.id.spinner_brand);
        mProductTypeSpinner = view.findViewById(R.id.spinner_product_type);
        mSubBrandSpinner = view.findViewById(R.id.spinner_sub_brand);

        mBrandSpinner.setAdapter(mCategoryListReportAdapter);
        mProductTypeSpinner.setAdapter(mSubcategoryListReportAdapter);
        mSubBrandSpinner.setAdapter(mCompanyAdapter);


        ListView listView = view.findViewById(R.id.list_order_product);
        listView.setEmptyView(view.findViewById(R.id.empty_view));
        listView.setAdapter(mReportAdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mBrandSpinner.setOnItemSelectedListener(this);
        mProductTypeSpinner.setOnItemSelectedListener(this);
        mSubBrandSpinner.setOnItemSelectedListener(this);

        showCompanyInfo(null);
        ReportsUtil.getMenuData(mContext, MENU_ID, this);

    }

    private void showCompanyInfo(CategoryReportData categoryReportData) {
        if (categoryReportData == null) {
            mCompanyInfoView.setVisibility(View.GONE);
        } else {
            mCompanyInfoView.setVisibility(View.VISIBLE);

            mNameTv.setText(categoryReportData.getCM_DESC());
            mTotalTv.setText(categoryReportData.getCM_NAME());
            mSchemeTv.setText(categoryReportData.getCM_TIME());
            mDescTv.setText(categoryReportData.getCM_ROOM_NO());

            mSchemeTv.setVisibility(categoryReportData.getCM_TIME().isEmpty() ? View.GONE : View.VISIBLE);
            mDescTv.setVisibility(categoryReportData.getCM_ROOM_NO().isEmpty() ? View.GONE : View.VISIBLE);
        }
    }
    private void initNavigationView() {
        if (getActivity() instanceof IBaseMenuActivity) {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            baseMenuActivity.setActionbarTitle("Reports");
            baseMenuActivity.checkMenuItem(R.id.nav_home);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initNavigationView();
    }

    @Override
    public void onResponse(String tag, String response) {
        Gson gson = GsonFactory.getInstance();
        Reports reports = null;
        CategoryReportData companyInfo = null; //SB20200708
        switch (tag) {
            case AppConstant.Actions.GET_MENU_DATA:
                mBrandList.clear();
                mProductTypeList.clear();
                mSubBrandList.clear();
                reports = gson.fromJson(response, Reports.class);

                mBrandList.addAll(reports.getCategoryListReport());
                mProductTypeList.addAll(reports.getSubcategoryListReport());
                mSubBrandList.addAll(reports.getCompList());

                mCategoryListReportAdapter.notifyDataSetChanged();
                mSubcategoryListReportAdapter.notifyDataSetChanged();
                mCompanyAdapter.notifyDataSetChanged();


                break;

            case AppConstant.Actions.GET_LIST_DATA_SECOND_SPINNER:
                mProductTypeList.clear();
                mSubBrandList.clear();
                mReportList.clear();

                reports = gson.fromJson(response, Reports.class);
                companyInfo = reports.getCategoryReportData().remove(0);
                showCompanyInfo(companyInfo);

                mProductTypeList.addAll(reports.getSubcategoryListReport());
                mSubBrandList.addAll(reports.getCompList());
                mReportList.addAll(reports.getCategoryReportData());

                mSubcategoryListReportAdapter.notifyDataSetChanged();
                mCompanyAdapter.notifyDataSetChanged();
                mReportAdapter.notifyDataSetChanged();


                mProductTypeSpinner.setSelection(0);
                mSubBrandSpinner.setSelection(0);
                break;

            case AppConstant.Actions.GET_LIST_DATA_THIRD_SPINNER:
                mSubBrandList.clear();
                mReportList.clear();
                reports = gson.fromJson(response, Reports.class);
                companyInfo = reports.getCategoryReportData().remove(0);
                showCompanyInfo(companyInfo);

                mSubBrandList.addAll(reports.getCompList());
                mReportList.addAll(reports.getCategoryReportData());

                mCompanyAdapter.notifyDataSetChanged();
                mReportAdapter.notifyDataSetChanged();

                mSubBrandSpinner.setSelection(0);
                break;

            case AppConstant.Actions.GET_LIST_DATA:
                mReportList.clear();
                reports = gson.fromJson(response, Reports.class);
                companyInfo = reports.getCategoryReportData().remove(0);
                showCompanyInfo(companyInfo);
             //SB20200708   Collections.sort(reports.getCategoryReportData());
                mReportList.addAll(reports.getCategoryReportData());
                mReportAdapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);

        int viewId = parent.getId();
        String selectedBrand = "0";
        String selectedProductType = "0";
        String selectedSubBrand = "0";
        CategoryListReport categoryListReport;
        SubcategoryListReport subcategoryListReport;
        switch (viewId) {
            case R.id.spinner_brand:
                if (position > 0) {
                    selectedBrand = mCategoryListReportAdapter.getItem(position).getCat_id();
                    ReportsUtil.getListData(mContext, AppConstant.Actions.GET_LIST_DATA_SECOND_SPINNER, MENU_ID, selectedBrand, selectedProductType, selectedSubBrand, this);
                }
                break;
            case R.id.spinner_product_type:
                if (position > 0) {
                    categoryListReport = (CategoryListReport) mBrandSpinner.getSelectedItem();
                    selectedBrand = categoryListReport.getCat_id();
                    selectedProductType = mSubcategoryListReportAdapter.getItem(position).getSub_cat_id();
                    ReportsUtil.getListData(mContext, AppConstant.Actions.GET_LIST_DATA_THIRD_SPINNER, MENU_ID, selectedBrand, selectedProductType, selectedSubBrand, this);
                }
                break;
            case R.id.spinner_sub_brand:
                if (position > 0) {
                    categoryListReport = (CategoryListReport) mBrandSpinner.getSelectedItem();
                    selectedBrand = categoryListReport.getCat_id();

                    subcategoryListReport = (SubcategoryListReport) mProductTypeSpinner.getSelectedItem();
                    selectedProductType = subcategoryListReport.getSub_cat_id();

                    selectedSubBrand = mCompanyAdapter.getItem(position).getComp_id();
                    ReportsUtil.getListData(mContext, AppConstant.Actions.GET_LIST_DATA, MENU_ID, selectedBrand, selectedProductType, selectedSubBrand, this);
                }
                break;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        CategoryReportData report = mReportAdapter.getItem(position);
        Intent intent = new Intent(mContext, OrderDetails2Activity.class);
        intent.putExtra("complaint_number", report.getCM_NO());
        intent.putExtra("screen", "Ordered Product Summary");
        intent.putExtra("menuId", MENU_ID); //SB20191220
        if(!report.getTAP().equals(""))  //SB20191220
            mContext.startActivity(intent);
    }
}
