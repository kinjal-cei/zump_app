package com.catch32.zumpbeta.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.OrderDetailsActivity;
import com.catch32.zumpbeta.adapter.CategoryListReportAdapter;
import com.catch32.zumpbeta.adapter.MonthAdapter;
import com.catch32.zumpbeta.adapter.YearAdapter;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.CategoryListReport;
import com.catch32.zumpbeta.model.MonthlySummaryCategoryReportData;
import com.catch32.zumpbeta.model.MonthlySummaryDistReportData;
import com.catch32.zumpbeta.model.MontlySummaryReport;
import com.catch32.zumpbeta.model.Reports;
import com.catch32.zumpbeta.model.Year;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.ReportsUtil;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * @author Ruchi Mehta
 * @version Jul 11, 2019
 */
public class MonthlyProductSummaryFragment extends Fragment implements AdapterView.OnItemSelectedListener, ResponseListener {

    private static final String TAG = MonthlySummaryFragment.class.getSimpleName();
    private Context mContext;
    private YearAdapter mYearAdapter;
    private CategoryListReportAdapter mCategoryListReportAdapter;
    private List<Year> mYearList;
    private List<CategoryListReport> mVendorList;
    private Spinner mYearSpinner;
    private Spinner mMonthSpinner;
    private Spinner mVendorSpinner;
    private TextView mRaisedNos, mDeliveredNos, mProcessNos, mPendingNos;
    private TextView mRaisedMrp, mDeliveredMrp, mProcessMrp, mPendingMrp;
    private TextView mRaisedTrp, mDeliveredTrp, mProcessTrp, mPendingTrp;
    private TextView mDelivTxt, mDelivTotal;
    private static final String MENU_ID = "12";
    private TableLayout mTableLyt;
    private TextView mFyMthTxt; //SB20200618

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month_product_summary, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
//////SB20200225//////////////////////////
        TextView info = view.findViewById(R.id.header_path);
        info.setText("["+ SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home > Reports > Month Wise Product Summary");
        //////^SB20200225//////////////////////////
        mYearList = new ArrayList<>();
        mVendorList = new ArrayList<>();
        List<String> monthList = new ArrayList<>(Arrays.asList(AppConstant.MONTHS));

        mTableLyt = view.findViewById(R.id.info_report);

        mRaisedNos = view.findViewById(R.id.txt_raised_nos);
        mDeliveredNos = view.findViewById(R.id.txt_deliv_nos);
        mProcessNos = view.findViewById(R.id.txt_process_nos);
        mPendingNos = view.findViewById(R.id.txt_pending_nos);

        mRaisedMrp = view.findViewById(R.id.txt_raised_mrp);
        mDeliveredMrp = view.findViewById(R.id.txt_deliv_mrp);
        mProcessMrp = view.findViewById(R.id.txt_process_mrp);
        mPendingMrp = view.findViewById(R.id.txt_pending_mrp);

        mRaisedTrp = view.findViewById(R.id.txt_raised_trp);
        mDeliveredTrp = view.findViewById(R.id.txt_deliv_trp);
        mProcessTrp = view.findViewById(R.id.txt_process_trp);
        mPendingTrp = view.findViewById(R.id.txt_pending_trp);

        mDelivTxt = view.findViewById(R.id.txt_deliv);
        mDelivTotal = view.findViewById(R.id.txt_deliv_total);
        mFyMthTxt = view.findViewById(R.id.txt_yr_mth); //SB20200618

        mCategoryListReportAdapter = new CategoryListReportAdapter(mContext, mVendorList);
        mYearAdapter = new YearAdapter(mContext, mYearList);
        MonthAdapter monthAdapter = new MonthAdapter(mContext, monthList);


        mYearSpinner = view.findViewById(R.id.spinner_year);
        mVendorSpinner = view.findViewById(R.id.spinner_vendor);
        mMonthSpinner = view.findViewById(R.id.spinner_month);


        mYearSpinner.setAdapter(mYearAdapter);
        mVendorSpinner.setAdapter(mCategoryListReportAdapter);
        mMonthSpinner.setAdapter(monthAdapter);


        mYearSpinner.setOnItemSelectedListener(this);
        mMonthSpinner.setOnItemSelectedListener(this);
        mVendorSpinner.setOnItemSelectedListener(this);

        ReportsUtil.getMenuData(mContext, MENU_ID, this);
    }

    private void initNavigationView() {
        if (getActivity() instanceof IBaseMenuActivity) {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            baseMenuActivity.setActionbarTitle("Month Wise Summary");
            baseMenuActivity.checkMenuItem(R.id.nav_home);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initNavigationView();
    }


    private void initTable(MonthlySummaryCategoryReportData monthlySummaryCategoryReportData) {
        if (monthlySummaryCategoryReportData != null) {
            mRaisedNos.setText(monthlySummaryCategoryReportData.getTOTAL_CM());
            mRaisedMrp.setText(monthlySummaryCategoryReportData.getTOTAL_CM_MRP());
            mRaisedTrp.setText(monthlySummaryCategoryReportData.getTOTAL_CM_TRP());

            mDeliveredNos.setText(monthlySummaryCategoryReportData.getTOTAL_RESOLVED());
            mDeliveredMrp.setText(monthlySummaryCategoryReportData.getTOTAL_RESOLVED_MRP());
            mDeliveredTrp.setText(monthlySummaryCategoryReportData.getTOTAL_RESOLVED_TRP());

            mPendingNos.setText(monthlySummaryCategoryReportData.getTOTAL_PENDING());
            mPendingMrp.setText(monthlySummaryCategoryReportData.getTOTAL_PENDING_MRP());
            mPendingTrp.setText(monthlySummaryCategoryReportData.getTOTAL_PENDING_TRP());

            mProcessNos.setText(monthlySummaryCategoryReportData.getTOTAL_INPROCESS());
            mProcessMrp.setText(monthlySummaryCategoryReportData.getTOTAL_INPROCESS_MRP());
            mProcessTrp.setText(monthlySummaryCategoryReportData.getTOTAL_INPROCESS_TRP());

            mDelivTxt.setText(monthlySummaryCategoryReportData.getFY_VAL());
            mDelivTotal.setText(monthlySummaryCategoryReportData.getYR_VAL());

            mFyMthTxt.setText(monthlySummaryCategoryReportData.getFY_MTH()); //SB20200618

        } else {
            mRaisedNos.setText("0");
            mRaisedMrp.setText("0");
            mRaisedTrp.setText("0");

            mDeliveredNos.setText("0");
            mDeliveredMrp.setText("0");
            mDeliveredTrp.setText("0");

            mPendingNos.setText("0");
            mPendingMrp.setText("0");
            mPendingTrp.setText("0");

            mProcessNos.setText("(0)");
            mProcessMrp.setText("(0)");
            mProcessTrp.setText("(0)");

            mDelivTxt.setText("0");
            mDelivTotal.setText("0");
            mFyMthTxt.setText(""); //SB20200618
        }
    }

    @Override
    public void onResponse(String tag, String response) {
        Gson gson = GsonFactory.getInstance();

        switch (tag) {
            case AppConstant.Actions.GET_MENU_DATA:
                mVendorList.clear();
                mYearList.clear();

                Reports reports = gson.fromJson(response, Reports.class);
                mVendorList.addAll(reports.getCategoryListReport());
                mYearList.addAll(reports.getYearList());

                mCategoryListReportAdapter.notifyDataSetChanged();
                mYearAdapter.notifyDataSetChanged();
                break;

            case AppConstant.Actions.GET_LIST_DATA:
                mTableLyt.removeAllViews();
                mTableLyt.invalidate();
                initTable(null);

                MontlySummaryReport montlySummaryReport = gson.fromJson(response, MontlySummaryReport.class);

                if (!montlySummaryReport.getCategoryReportData().isEmpty()) {
                    initTable(montlySummaryReport.getCategoryReportData().get(0));
                }

                for (MonthlySummaryDistReportData monthlySummaryDistReportData : montlySummaryReport.getDistReportData()) {
                    addTableRow(monthlySummaryDistReportData);
                }

                break;
        }

    }

    private void addTableRow(MonthlySummaryDistReportData monthlySummaryDistReportData) {
        TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.summary_row_2, null);

        TextView title = (TextView) tr.findViewById(R.id.txt_title);
        TextView mrp = (TextView) tr.findViewById(R.id.txt_mrp);
        TextView gross_margin = (TextView) tr.findViewById(R.id.txt_margin);

      //SB20200623  title.setText(monthlySummaryDistReportData.getDIST_NM()+"\n"+monthlySummaryDistReportData.getDIST_SHR());
        title.setText(monthlySummaryDistReportData.getDIST_NM());
        mrp.setText(monthlySummaryDistReportData.getDIST_TOT_MRP()+"\n"+monthlySummaryDistReportData.getDIST_TOT_TRP()+"\n");
        gross_margin.setText(monthlySummaryDistReportData.getDIST_MARGIN()+"\n"+monthlySummaryDistReportData.getDIST_AVG_VAL()+"\n");

        View line = new View(mContext);
        line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        line.setBackgroundColor(Color.rgb(120, 144, 156));


        mTableLyt.addView(tr);
        mTableLyt.addView(line);
        String orderNumber = monthlySummaryDistReportData.getDIST_TOT_ORD();

        if (orderNumber.matches("\\d+"))
        {
            tr.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                    intent.putExtra("complaint_number", orderNumber);
                    intent.putExtra("screen", "Month Wise Summary");
                    intent.putExtra("title", "");
                    intent.putExtra("submit", false);
                    mContext.startActivity(intent);
                }
            });
        }

    }


    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int viewId = parent.getId();
        String selectedYear = "0";
        String selectedMonth = "0";
        String selectedVendor = "0";
        Year year;
        CategoryListReport categoryListReport;
        switch (viewId) {
            case R.id.spinner_year:
                selectedYear = mYearAdapter.getItem(position).getYEAR();

              //SB20200629  if (selectedYear.equals("--All--")) selectedYear = "0";
                if (selectedYear.equals("--All--") || selectedYear.contains("FY")) selectedYear = "0";

                selectedMonth = String.valueOf(mMonthSpinner.getSelectedItemPosition());

                categoryListReport = (CategoryListReport) mVendorSpinner.getSelectedItem();
                if (categoryListReport != null)
                    selectedVendor = categoryListReport.getCat_id();
                ReportsUtil.getSummaryListData(mContext, AppConstant.Actions.GET_LIST_DATA, MENU_ID, selectedYear, selectedMonth, selectedVendor, this);

                break;
            case R.id.spinner_month:
                year = (Year) mYearSpinner.getSelectedItem();
                if (year != null)
                    selectedYear = year.getYEAR();

              //SB20200629  if (selectedYear.equals("--All--")) selectedYear = "0";
                if (selectedYear.equals("--All--") || selectedYear.contains("FY")) selectedYear = "0";

                selectedMonth = String.valueOf(mMonthSpinner.getSelectedItemPosition());

                categoryListReport = (CategoryListReport) mVendorSpinner.getSelectedItem();
                if (categoryListReport != null)
                    selectedVendor = categoryListReport.getCat_id();
                ReportsUtil.getSummaryListData(mContext, AppConstant.Actions.GET_LIST_DATA, MENU_ID, selectedYear, selectedMonth, selectedVendor, this);
                break;
            case R.id.spinner_vendor:
                year = (Year) mYearSpinner.getSelectedItem();
                if (year != null)
                    selectedYear = year.getYEAR();

              //SB20200629  if (selectedYear.equals("--All--")) selectedYear = "0";
                if (selectedYear.equals("--All--") || selectedYear.contains("FY")) selectedYear = "0";

                selectedMonth = String.valueOf(mMonthSpinner.getSelectedItemPosition());
                selectedVendor = mCategoryListReportAdapter.getItem(position).getCat_id();
                ReportsUtil.getSummaryListData(mContext, AppConstant.Actions.GET_LIST_DATA, MENU_ID, selectedYear, selectedMonth, selectedVendor, this);

                break;

        }


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
