package com.catch32.zumpbeta.myfragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
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
import com.catch32.zumpbeta.myactivity.Dashboard_Activity;
import com.catch32.zumpbeta.myactivity.Month_Wise_Summary;
import com.catch32.zumpbeta.myactivity.MyShop;
import com.catch32.zumpbeta.myactivity.My_Order_Activity;
import com.catch32.zumpbeta.myactivity.Order_Details_Activity;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.ReportsUtil;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_MyRewards extends Fragment implements AdapterView.OnItemSelectedListener, ResponseListener
{
    int initialScrollY = 0;
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
    private TextView mDelivTOT_RMK, mDelivTOT_CGST, mDelivTOT_SGST; //SB20191211
    private static final String MENU_ID = "21";
    private TableLayout mTableLyt;
    private TextView mFyMthTxt; //SB20200618

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);

        final NestedScrollView nsvDashboard = view.findViewById(R.id.nsvDashboard);
        final LinearLayout llFooter = view.findViewById(R.id.llFooter);
        final int initialViewHeight = llFooter.getLayoutParams().height;
        initialScrollY = nsvDashboard.getScrollY();

        nsvDashboard.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
        {
            @Override
            public void onScrollChanged()
            {
                ValueAnimator animator = ValueAnimator.ofInt(0, 1);
                ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                int maxDistance = llFooter.getHeight();
                float movement = nsvDashboard.getScrollY();
                if (movement > initialScrollY)
                {
                    if (!animator.isRunning())
                    {
                        animator.setIntValues(params.height, 0);
                        animator.setDuration(100);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation)
                            {
                                ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                                params.height = (int) animation.getAnimatedValue();
                                llFooter.setLayoutParams(params);
                            }
                        });
                        animator.start();
                    }
                } else {
                    if (!animator.isRunning())
                    {
                        animator.setIntValues(params.height, initialViewHeight);
                        animator.setDuration(500);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation)
                            {
                                ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                                params.height = (int) animation.getAnimatedValue();
                                llFooter.setLayoutParams(params);
                            }
                        });
                        animator.start();
                    }
                }
                initialScrollY = nsvDashboard.getScrollY();
            }
        });

        LinearLayout llHome = view.findViewById(R.id.llHome);
        llHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Dashboard_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llReports = view.findViewById(R.id.llReports);
        llReports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), MyShop.class);
                startActivity(i);
            }
        });

        LinearLayout llOrderStatus = view.findViewById(R.id.llOrderStatus);
        llOrderStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), My_Order_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llMonthwiseSummary = view.findViewById(R.id.llMonthwiseSummary);
        llMonthwiseSummary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Month_Wise_Summary.class);
                startActivity(i);
            }
        });

        init(view);
        return view;
    }
    private void init(View view)
    {
        try {
            mContext = getActivity();
            TextView info = view.findViewById(R.id.header_path);
            info.setText("[" + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY) + "] " + "Home >My Rewards");
            mYearList = new ArrayList<>();
            mVendorList = new ArrayList<>();
            List<String> monthList = new ArrayList<>(Arrays.asList(AppConstant.MONTHS));

            mTableLyt = view.findViewById(R.id.info_report);

           /* mRaisedNos = view.findViewById(R.id.txt_raised_nos);
            mDeliveredNos = view.findViewById(R.id.txt_deliv_nos);
            mRaisedMrp = view.findViewById(R.id.txt_raised_mrp);
            mDeliveredMrp = view.findViewById(R.id.txt_deliv_mrp);
            mRaisedTrp = view.findViewById(R.id.txt_raised_trp);
            mDeliveredTrp = view.findViewById(R.id.txt_deliv_trp);

            mDelivTxt = view.findViewById(R.id.txt_deliv);
            mDelivTotal = view.findViewById(R.id.txt_deliv_total);*/

            mFyMthTxt = view.findViewById(R.id.txt_yr_mth); //SB20200618

           /* mDelivTOT_RMK = view.findViewById(R.id.txt_deliv); //SB20191211
            mDelivTOT_CGST = view.findViewById(R.id.txt_deliv_cgst); //SB20191211
            mDelivTOT_SGST = view.findViewById(R.id.txt_deliv_sgst); //SB20191211*/

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
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
       // initNavigationView();
    }

    private void initTable(MonthlySummaryCategoryReportData monthlySummaryGstCategoryReportData)
    {
        try {
            if (monthlySummaryGstCategoryReportData != null)
            {
               /* mRaisedNos.setText(monthlySummaryGstCategoryReportData.getTOTAL_CM());
                mRaisedMrp.setText(monthlySummaryGstCategoryReportData.getTOTAL_CM_MRP());
                mRaisedTrp.setText(monthlySummaryGstCategoryReportData.getTOTAL_CM_TRP());

                mDeliveredNos.setText(monthlySummaryGstCategoryReportData.getTOTAL_RESOLVED());
                mDeliveredMrp.setText(monthlySummaryGstCategoryReportData.getTOTAL_RESOLVED_MRP());
                mDeliveredTrp.setText(monthlySummaryGstCategoryReportData.getTOTAL_RESOLVED_TRP());
                mDelivTOT_RMK.setText(monthlySummaryGstCategoryReportData.getFY_VAL()); //SB20191211
                mDelivTOT_CGST.setText(monthlySummaryGstCategoryReportData.getReward_Point()); //SB20191211
                mDelivTOT_SGST.setText(monthlySummaryGstCategoryReportData.getReward_Value()); //SB20191211*/

                mFyMthTxt.setText(monthlySummaryGstCategoryReportData.getFY_MTH()); //SB20200618

            } else {
               /* mRaisedNos.setText("0");
                mRaisedMrp.setText("0");
                mRaisedTrp.setText("0");

                mDeliveredNos.setText("0");
                mDeliveredMrp.setText("0");
                mDeliveredTrp.setText("0");
                mDelivTOT_RMK.setText(""); //SB20191211
                mDelivTOT_CGST.setText("0"); //SB20191211
                mDelivTOT_SGST.setText("0"); //SB20191211*/
               mFyMthTxt.setText(""); //SB20200618
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String tag, String response)
    {
        try {
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
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void addTableRow(MonthlySummaryDistReportData monthlySummaryDistReportData)
    {
        try {
            TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.summary_row_1, null);

            TextView title = (TextView) tr.findViewById(R.id.txt_title);
            TextView mrp = (TextView) tr.findViewById(R.id.txt_mrp);
            TextView gross_margin = (TextView) tr.findViewById(R.id.txt_margin);

            if (monthlySummaryDistReportData.getDIST_NM().contains("Orders"))
                title.setText(monthlySummaryDistReportData.getDIST_TOT_ORD() + "\n" + monthlySummaryDistReportData.getDIST_NM() + "\n" + monthlySummaryDistReportData.getDIST_SHR());
            else
                title.setText(monthlySummaryDistReportData.getORD_PREFIX() + monthlySummaryDistReportData.getDIST_TOT_ORD() + "\n" + monthlySummaryDistReportData.getDIST_NM() + "\n" + monthlySummaryDistReportData.getDIST_SHR());
            mrp.setText(monthlySummaryDistReportData.getDIST_MARGIN() + "\n\n");
            gross_margin.setText(monthlySummaryDistReportData.getDIST_AVG_VAL());

            View line = new View(mContext);
            line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            line.setBackgroundColor(Color.rgb(120, 144, 156));


            mTableLyt.addView(tr);
            mTableLyt.addView(line);
            String orderNumber = monthlySummaryDistReportData.getDIST_TOT_ORD();
            String orderPrefix = monthlySummaryDistReportData.getORD_PREFIX();

            System.out.println(orderNumber);

            if(orderPrefix.contains("PO#"))
            {
                tr.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try {
                            Intent intent = new Intent(mContext, Order_Details_Activity.class);
                            intent.putExtra("complaint_number", orderNumber);
                            intent.putExtra("submit", false);
                            intent.putExtra("screen", "My Rewards > Order Details");
                            mContext.startActivity(intent);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            /*if (orderNumber.matches("\\d+"))
            {
                tr.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(mContext, Order_Details_Activity.class);
                        intent.putExtra("screen", "My Rewards");
                        intent.putExtra("submit", false);
                        mContext.startActivity(intent);
                    }
                });*/
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
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
        String selectedYear = "0";
        String selectedMonth = "0";
        String selectedVendor = "0";
        Year year;
        CategoryListReport categoryListReport;
        switch (viewId) {
            case R.id.spinner_year:
                selectedYear = mYearAdapter.getItem(position).getYEAR();
                if (selectedYear.equals("--All--") || selectedYear.contains("FY"))
                {
                    selectedYear = "0";
                    selectedMonth = "";
                    mMonthSpinner.setEnabled(false);
                }
                else {
                    mMonthSpinner.setEnabled(true);
                }

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

                if (selectedYear.equals("--All--") || selectedYear.contains("FY")) selectedYear = "0";

                selectedMonth = String.valueOf(mMonthSpinner.getSelectedItemPosition());
                if (!selectedMonth.equals("--All--"))  {
                    selectedYear = String.valueOf(mYearSpinner.getSelectedItemPosition());
                }

                categoryListReport = (CategoryListReport) mVendorSpinner.getSelectedItem();
                if (categoryListReport != null)
                    selectedVendor = categoryListReport.getCat_id();
                ReportsUtil.getSummaryListData(mContext, AppConstant.Actions.GET_LIST_DATA, MENU_ID, selectedYear, selectedMonth, selectedVendor, this);
                break;
            case R.id.spinner_vendor:
                year = (Year) mYearSpinner.getSelectedItem();
                if (year != null)
                    selectedYear = year.getYEAR();

                if (selectedYear.equals("--All--") || selectedYear.contains("FY")) selectedYear = "0";

                selectedMonth = String.valueOf(mMonthSpinner.getSelectedItemPosition());
                selectedVendor = mCategoryListReportAdapter.getItem(position).getCat_id();
                ReportsUtil.getSummaryListData(mContext, AppConstant.Actions.GET_LIST_DATA, MENU_ID, selectedYear, selectedMonth, selectedVendor, this);

                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }
}
