package com.catch32.zumpbeta.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.adapter.CompanyAdapter;
import com.catch32.zumpbeta.adapter.DistributorAdapter;
import com.catch32.zumpbeta.adapter.RaisedOrderAdapter;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.Company;
import com.catch32.zumpbeta.model.DistReportData;
import com.catch32.zumpbeta.model.OrderList;
import com.catch32.zumpbeta.model.OrderListClr;
import com.catch32.zumpbeta.model.RaisedOrder;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;


/**
 * @author Ruchi Mehta
 * @version Jun 17, 2019
 */
public class RaisedNotificationFragment extends Fragment implements AdapterView.OnItemSelectedListener, ResponseListener, AdapterView.OnItemClickListener {

    private Activity mContext;
    private DistributorAdapter mDistributorAdapter;
    private CompanyAdapter mCompanyAdapter;
    private ArrayList<Company> mCompanyList;
    private ArrayList<OrderList> mOrderList;
    private ArrayList<OrderListClr> mColorList;
    private ArrayList<DistReportData> mDistributorList;
    private RaisedOrderAdapter mRaisedOrderAdapter;
    private Spinner mDistributorSpinner;
    private Spinner mCompanySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raised_notification, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        mOrderList = new ArrayList<>();
        mColorList = new ArrayList<>();
        mCompanyList = new ArrayList<>();
        mDistributorList = new ArrayList<>();

        mRaisedOrderAdapter = new RaisedOrderAdapter(mContext, mOrderList, mColorList);

        ListView listView = view.findViewById(R.id.list_raised_orders);
        listView.setEmptyView(view.findViewById(R.id.empty_view));
        listView.setAdapter(mRaisedOrderAdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mDistributorSpinner = (Spinner) view.findViewById(R.id.spinner_distributor);
        mDistributorSpinner.setOnItemSelectedListener(this);

        mCompanySpinner = (Spinner) view.findViewById(R.id.spinner_company);
        mCompanySpinner.setOnItemSelectedListener(this);


        mDistributorAdapter = new DistributorAdapter(mContext, mDistributorList);
        mDistributorSpinner.setAdapter(mDistributorAdapter);

        mCompanyAdapter = new CompanyAdapter(mContext, mCompanyList);
        mCompanySpinner.setAdapter(mCompanyAdapter);

        fetchRaisedOrders(AppConstant.Actions.GET_RAISED_ORDERS_DISTRIBUTOR, "0", "0");
    }

    private void initNavigationView() {
        if (getActivity() instanceof IBaseMenuActivity) {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            baseMenuActivity.setActionbarTitle("Notification");
            baseMenuActivity.checkMenuItem(R.id.nav_home);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initNavigationView();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (parent.getId() == R.id.spinner_distributor)
        {
            DistReportData distReportData = mDistributorAdapter.getItem(position);
            fetchRaisedOrders(AppConstant.Actions.GET_RAISED_ORDERS_COMPANY, distReportData.getDist_id() + "", "0");
        } else if (parent.getId() == R.id.spinner_company) {
            DistReportData distReportData = (DistReportData) mDistributorSpinner.getSelectedItem();
            String companyId = mCompanyAdapter.getItem(position).getComp_id();
            fetchRaisedOrders(AppConstant.Actions.GET_RAISED_ORDERS, distReportData.getDist_id() + "", companyId);
        }


    }

    private void fetchRaisedOrders(String tag, String distId, String compcd) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("distcd", distId);
        jsonObject.addProperty("compcd", compcd);

        new PostDataToServerTask(mContext, tag)
                .setPath(AppConstant.WebURL.GET_NOTIFICATION)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResponse(String tag, String response) {
        Gson gson = GsonFactory.getInstance();
        RaisedOrder raisedOrder = null;
        switch (tag) {
            case AppConstant.Actions.GET_RAISED_ORDERS_DISTRIBUTOR:
                mOrderList.clear();
                mColorList.clear();
                mCompanyList.clear();
                mDistributorList.clear();

                raisedOrder = gson.fromJson(response, RaisedOrder.class);
                mDistributorList.addAll(raisedOrder.getDistReportData());
                mDistributorAdapter.notifyDataSetChanged();
                mRaisedOrderAdapter.notifyDataSetChanged();
                mCompanyAdapter.notifyDataSetChanged();
                break;
            case AppConstant.Actions.GET_RAISED_ORDERS:
                mOrderList.clear();
                mColorList.clear();
                raisedOrder = gson.fromJson(response, RaisedOrder.class);
                mOrderList.addAll(raisedOrder.getOrderList());
                mColorList.addAll(raisedOrder.getOrderList_clr());
                mRaisedOrderAdapter.notifyDataSetChanged();
                break;
            case AppConstant.Actions.GET_RAISED_ORDERS_COMPANY:
                mOrderList.clear();
                mColorList.clear();
                mCompanyList.clear();

                raisedOrder = gson.fromJson(response, RaisedOrder.class);
                mOrderList.addAll(raisedOrder.getOrderList());
                mColorList.addAll(raisedOrder.getOrderList_clr());
                mCompanyList.addAll(raisedOrder.getCompList());
                mRaisedOrderAdapter.notifyDataSetChanged();
                mCompanyAdapter.notifyDataSetChanged();

                mCompanySpinner.setSelection(0);

        }


    }

    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);

        OrderList report = mRaisedOrderAdapter.getItem(position);
        OrderListClr orderListClr = mColorList.get(position);
       /* if (!orderListClr.getFlagColor().equals("E")) {
            Intent intent = new Intent(mContext, RaisedOrderReportActivity.class);
            intent.putExtra("complaint_number", report.getId());
            mContext.startActivityForResult(intent, 2294);
            ////////////////^SB20191220/////////////////////////////////////////////////////
            //*SB20191223: Blocked based on RD

            Gson gson = GsonFactory.getInstance();
            SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(mContext);
            //RaisedOrder raisedOrder = gson.fromJson(response, RaisedOrder.class);
            //  if (raisedOrder.getFeedbackApply().equals("Y") && sharedPrefUtil.getString(BaseSharedPref.STORE_NAME_KEY) == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setIcon(R.drawable.img_feedback);
            builder.setTitle("Reward Ratings");
            builder.setMessage("Please Go To Order Feedback");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = null;
                    intent = new Intent(getActivity(), MainActivity.class);
                    mContext.startActivity(intent);
                }
            });
            builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
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
            //}
            //////////////////////////////////////
        }*/

    }

}