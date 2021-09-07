package com.catch32.zumpbeta.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.OrderDetailsActivity;
import com.catch32.zumpbeta.adapter.CategoryListReportAdapter;
import com.catch32.zumpbeta.adapter.CompanyAdapter;
import com.catch32.zumpbeta.adapter.MyOrderAdapter;
import com.catch32.zumpbeta.adapter.StatusAdapter;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.CategoryListReport;
import com.catch32.zumpbeta.model.CategoryReportData;
import com.catch32.zumpbeta.model.Company;
import com.catch32.zumpbeta.model.Reports;
import com.catch32.zumpbeta.model.Status;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.ReportsUtil;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class MyOrdersFragment extends Fragment implements AdapterView.OnItemSelectedListener, ResponseListener, AdapterView.OnItemClickListener {

    private static final String TAG = MyOrdersFragment.class.getSimpleName();
    private Context mContext;
    private CategoryListReportAdapter mCategoryListReportAdapter;
    private StatusAdapter mStatusAdapter;
    private CompanyAdapter mCompanyAdapter;
    private MyOrderAdapter mMyOrderAdapter;
    private List<Company> mCompanyList;
    private List<CategoryListReport> mVendorList;
    private List<Status> mStatusList;
    private List<CategoryReportData> mOrderList;
    private Spinner mVendorSpinner;
    private Spinner mStatusSpinner;
    private Spinner mCompanySpinner;
    private static final String MENU_ID = "5";
    int initialScrollY = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        init(view);
        return view;
    }
    private void init(View view)
    {
        mContext = getActivity();
        //////SB20200225//////////////////////////
        TextView info = view.findViewById(R.id.header_path);
        info.setText("["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home >My Orders");
        //////^SB20200225//////////////////////////
        mVendorList = new ArrayList<>();
        mStatusList = new ArrayList<>();
        mCompanyList = new ArrayList<>();
        mOrderList = new ArrayList<>();

        mCategoryListReportAdapter = new CategoryListReportAdapter(mContext, mVendorList);
        mStatusAdapter = new StatusAdapter(mContext, mStatusList);
        mCompanyAdapter = new CompanyAdapter(mContext, mCompanyList);
        mMyOrderAdapter = new MyOrderAdapter(mContext, mOrderList);

        mVendorSpinner = view.findViewById(R.id.spinner_vendor);
        mStatusSpinner = view.findViewById(R.id.spinner_status);
        mCompanySpinner = view.findViewById(R.id.spinner_company);

        mVendorSpinner.setAdapter(mCategoryListReportAdapter);
        mStatusSpinner.setAdapter(mStatusAdapter);
        mCompanySpinner.setAdapter(mCompanyAdapter);

        mVendorSpinner.setOnItemSelectedListener(this);
        mStatusSpinner.setOnItemSelectedListener(this);
        mCompanySpinner.setOnItemSelectedListener(this);

        ListView listView = view.findViewById(R.id.list_my_orders);
        listView.setEmptyView(view.findViewById(R.id.empty_view));
        listView.setAdapter(mMyOrderAdapter);
        listView.setOnItemClickListener(this);

        ReportsUtil.getMenuData(mContext, MENU_ID, this);
    }

    private void initNavigationView()
    {
        if (getActivity() instanceof IBaseMenuActivity)
        {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            baseMenuActivity.setActionbarTitle("My Orders");
            baseMenuActivity.checkMenuItem(R.id.nav_my_order);
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        initNavigationView();
    }
    @Override
    public void onResponse(String tag, String response)
    {
        Gson gson = GsonFactory.getInstance();
        Reports reports = null;
        switch (tag) {
            case AppConstant.Actions.GET_MENU_DATA:
                mVendorList.clear();
                mStatusList.clear();
                mCompanyList.clear();

                reports = gson.fromJson(response, Reports.class);

                mVendorList.addAll(reports.getCategoryListReport());
                mStatusList.addAll(reports.getStatusList());
                mCompanyList.addAll(reports.getCompList());

                mCategoryListReportAdapter.notifyDataSetChanged();
                mStatusAdapter.notifyDataSetChanged();
                mCompanyAdapter.notifyDataSetChanged();
                break;

            case AppConstant.Actions.GET_LIST_DATA_THIRD_SPINNER:
                mCompanyList.clear();
                mOrderList.clear();

                reports = gson.fromJson(response, Reports.class);
                mCompanyList.addAll(reports.getCompList());
                mCompanyAdapter.notifyDataSetChanged();

                mOrderList.addAll(reports.getCategoryReportData());
                mMyOrderAdapter.notifyDataSetChanged();

                mCompanySpinner.setSelection(0);
                break;

            case AppConstant.Actions.GET_LIST_DATA:
                mOrderList.clear();
                reports = gson.fromJson(response, Reports.class);

                mOrderList.addAll(reports.getCategoryReportData());
                mMyOrderAdapter.notifyDataSetChanged();
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
        String selectedStatus = "0";
        String selectedCompany = "0";
        CategoryListReport categoryListReport;
        Company company;
        switch (viewId) {
            case R.id.spinner_vendor:
                selectedVendor = mCategoryListReportAdapter.getItem(position).getCat_id();
                ReportsUtil.getListData(mContext, AppConstant.Actions.GET_LIST_DATA_THIRD_SPINNER, MENU_ID, selectedVendor, selectedStatus, selectedCompany, this);
                break;
            case R.id.spinner_status:
                categoryListReport = (CategoryListReport) mVendorSpinner.getSelectedItem();
                selectedVendor = categoryListReport.getCat_id();

                selectedStatus = String.valueOf(position);
                company = (Company) mCompanySpinner.getSelectedItem();
                if (company != null)
                    selectedCompany = company.getComp_id();
                ReportsUtil.getListData(mContext, AppConstant.Actions.GET_LIST_DATA, MENU_ID, selectedVendor, selectedStatus, selectedCompany, this);
                break;
            case R.id.spinner_company:
                categoryListReport = (CategoryListReport) mVendorSpinner.getSelectedItem();
                selectedVendor = categoryListReport.getCat_id();

                selectedStatus = String.valueOf(mStatusSpinner.getSelectedItemPosition());
                selectedCompany = mCompanyAdapter.getItem(position).getComp_id();
                ReportsUtil.getListData(mContext, AppConstant.Actions.GET_LIST_DATA, MENU_ID, selectedVendor, selectedStatus, selectedCompany, this);

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
        CategoryReportData report = mMyOrderAdapter.getItem(position);
        Intent intent = new Intent(mContext, OrderDetailsActivity.class);
        intent.putExtra("complaint_number", report.getCM_NO());
        System.out.println(report.getCM_NO());
        intent.putExtra("screen", "My Orders");
        intent.putExtra("title", ""+"["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"]\n"+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.ALIAS_KEY));
        if(!report.getCM_NO().equals("0"))
            mContext.startActivity(intent);
    }
}
