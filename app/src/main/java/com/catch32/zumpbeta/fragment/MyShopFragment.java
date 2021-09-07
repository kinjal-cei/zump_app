package com.catch32.zumpbeta.fragment;


import android.content.Context;
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

public class MyShopFragment extends Fragment implements ResponseListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = MyShopFragment.class.getSimpleName();
    private Context mContext;
    private CategoryListReportAdapter mCategoryListReportAdapter;
    private SubcategoryListReportAdapter mSubcategoryListReportAdapter;
    private CompanyAdapter mCompanyAdapter;
    private ReportAdapter mReportAdapter;
    private List<Company> mCompanyList;
    private List<CategoryReportData> mReportList;
    private List<CategoryListReport> mVendorList;
    private List<SubcategoryListReport> mProductTypeList;
    private Spinner mVendorSpinner;
    private Spinner mProductTypeSpinner;
    private Spinner mSubBrandSpinner;
    private CardView mCompanyInfoView;
    private TextView mNameTv, mTotalTv, mDescTv, mSchemeTv;
    private static final String MENU_ID = "0";
    int initialScrollY = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_shop, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        //////SB20200225//////////////////////////
        TextView info = view.findViewById(R.id.header_path);
        info.setText("["+ SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home > My Shop");
        //////^SB20200225//////////////////////////
        mVendorList = new ArrayList<>();
        mProductTypeList = new ArrayList<>();
        mCompanyList = new ArrayList<>();
        mReportList = new ArrayList<>();
        ///////////////Blue Band/////////////////
        mCompanyInfoView = view.findViewById(R.id.lyt_info);
        mNameTv = mCompanyInfoView.findViewById(R.id.txt_name);
        mTotalTv = mCompanyInfoView.findViewById(R.id.txt_total);
        mDescTv = mCompanyInfoView.findViewById(R.id.txt_desc);
        mSchemeTv = mCompanyInfoView.findViewById(R.id.txt_scheme);
        ///////////////^Blue Band/////////////////
        mCategoryListReportAdapter = new CategoryListReportAdapter(mContext, mVendorList);
        mSubcategoryListReportAdapter = new SubcategoryListReportAdapter(mContext, mProductTypeList);
        mCompanyAdapter = new CompanyAdapter(mContext, mCompanyList);
        mReportAdapter = new ReportAdapter(mContext, mReportList);

        mVendorSpinner = view.findViewById(R.id.spinner_vendor);
        mProductTypeSpinner = view.findViewById(R.id.spinner_product_type);
        mSubBrandSpinner = view.findViewById(R.id.spinner_sub_brand);

        mVendorSpinner.setAdapter(mCategoryListReportAdapter);
        mProductTypeSpinner.setAdapter(mSubcategoryListReportAdapter);
        mSubBrandSpinner.setAdapter(mCompanyAdapter);


        ListView listView = view.findViewById(R.id.list_shop);
        listView.setEmptyView(view.findViewById(R.id.empty_view));
        listView.setAdapter(mReportAdapter);

        mVendorSpinner.setOnItemSelectedListener(this);
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
            baseMenuActivity.setActionbarTitle("My Shop");
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
        CategoryReportData companyInfo = null;

        switch (tag) {
            case AppConstant.Actions.GET_MENU_DATA:

                mVendorList.clear();
                mProductTypeList.clear();
                mCompanyList.clear();
                reports = gson.fromJson(response, Reports.class);

                mVendorList.addAll(reports.getCategoryListReport());
                mProductTypeList.addAll(reports.getSubcategoryListReport());
                mCompanyList.addAll(reports.getCompList());

                mCategoryListReportAdapter.notifyDataSetChanged();
                mSubcategoryListReportAdapter.notifyDataSetChanged();
                mCompanyAdapter.notifyDataSetChanged();
                break;

            case AppConstant.Actions.GET_LIST_DATA_SECOND_SPINNER:
                mProductTypeList.clear();
                mCompanyList.clear();
                mReportList.clear();

                reports = gson.fromJson(response, Reports.class);
                companyInfo = reports.getCategoryReportData().remove(0);
                showCompanyInfo(companyInfo);

                mProductTypeList.addAll(reports.getSubcategoryListReport());
                mCompanyList.addAll(reports.getCompList());
                mReportList.addAll(reports.getCategoryReportData());

                mSubcategoryListReportAdapter.notifyDataSetChanged();
                mCompanyAdapter.notifyDataSetChanged();
                mReportAdapter.notifyDataSetChanged();

                mProductTypeSpinner.setSelection(0);
                mSubBrandSpinner.setSelection(0);
                break;

            case AppConstant.Actions.GET_LIST_DATA_THIRD_SPINNER:
                mCompanyList.clear();
                mReportList.clear();
                reports = gson.fromJson(response, Reports.class);
                companyInfo = reports.getCategoryReportData().remove(0);
                showCompanyInfo(companyInfo);

                mCompanyList.addAll(reports.getCompList());
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
        int viewId = parent.getId();
        String selectedVendor = "0";
        String selectedProductType = "0";
        String selectedCompany = "0";
        CategoryListReport categoryListReport;
        SubcategoryListReport subcategoryListReport;
        switch (viewId) {
            case R.id.spinner_vendor:
                if (position > 0) {
                    selectedVendor = mCategoryListReportAdapter.getItem(position).getCat_id();
                    ReportsUtil.getListData(mContext, AppConstant.Actions.GET_LIST_DATA_SECOND_SPINNER, MENU_ID, selectedVendor, selectedProductType, selectedCompany, this);

                }
                break;
            case R.id.spinner_product_type:
                if (position > 0) {
                    categoryListReport = (CategoryListReport) mVendorSpinner.getSelectedItem();
                    selectedVendor = categoryListReport.getCat_id();
                    selectedProductType = mSubcategoryListReportAdapter.getItem(position).getSub_cat_id();
                    ReportsUtil.getListData(mContext, AppConstant.Actions.GET_LIST_DATA_THIRD_SPINNER, MENU_ID, selectedVendor, selectedProductType, selectedCompany, this);
                }
                break;
            case R.id.spinner_sub_brand:
                if (position > 0) {
                    categoryListReport = (CategoryListReport) mVendorSpinner.getSelectedItem();
                    selectedVendor = categoryListReport.getCat_id();

                    subcategoryListReport = (SubcategoryListReport) mProductTypeSpinner.getSelectedItem();
                    selectedProductType = subcategoryListReport.getSub_cat_id();

                    selectedCompany = mCompanyAdapter.getItem(position).getComp_id();
                    ReportsUtil.getListData(mContext, AppConstant.Actions.GET_LIST_DATA, MENU_ID, selectedVendor, selectedProductType, selectedCompany, this);
                }
                break;

        }


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

