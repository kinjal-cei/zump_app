package com.catch32.zumpbeta.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.MainActivity;
import com.catch32.zumpbeta.activity.NewOrderActivity;
import com.catch32.zumpbeta.activity.ReportsActivity;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.listener.OnBackPressedListener;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.RaisedOrder;
import com.catch32.zumpbeta.model.Notification; //SB20191227
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class DashboardFragment extends Fragment implements View.OnClickListener, ResponseListener, OnBackPressedListener
{
    private static final String TAG = DashboardFragment.class.getSimpleName();
    private Context mContext;
    private TextView mRaisedOrderTv;
    private TextView mPolicyTerms; //SB20191218
    private TextView mRaisedNotificationTv; //SB20191226
    private TextView mTextMsgTv; //SB20200708
    private TextView mLblVendor;
    private TextView mLblStatus;
    private TextView mLblCompany;
    private TextView mLblFilter1;
    private Spinner mSpinnerVendor;
    private Spinner mSpinnerStatus;
    private Spinner mSpinnerCompany;
    private ImageView mImgDownArrow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_dashboard, container, false);
        initView(view);
      ///  View view1 = inflater.inflate(R.layout.nav_header, container, false);
      ///  TextView greetingTv1 = view1.findViewById(R.id.mobile);
      ///  greetingTv1.setText("Hello ["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"]");
        return view;
    }

    private void initView(View view)
    {
        String FirstTimeUserDtl="Please Click "; //SB20191216
        String FirstTimePolicyUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
        String FirstTimeTermUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_TERM_KEY); //SB20191218
        String UniqueKey=""+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.UNIQUE_KEY); //SB20200606

        if(FirstTimePolicyUser.equals("N"))
            FirstTimeUserDtl=FirstTimeUserDtl+"\"Privacy Policy\"";
        if(FirstTimeTermUser.equals("N"))
            FirstTimeUserDtl=FirstTimeUserDtl+" \"Terms & Conditions\"";
        mContext = getActivity();

        TextView greetingTv = view.findViewById(R.id.txt_greeting);
        if(FirstTimePolicyUser.equals("N") || FirstTimeTermUser.equals("N")) //SB20191216
           //SB greetingTv.setText("["+FirstTimeUserDtl+"]\n"+"["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"]\n"+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY));
            greetingTv.setText("["+FirstTimeUserDtl+"]\n"+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY));
        else
          //SB20200608  greetingTv.setText("Hello ["+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"]\n"+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY));
            greetingTv.setText("Last Login: "+SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.LAST_LOGIN) + "\n" +"Hello [" + SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] " +  SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.EMP_NAME_KEY));
        //////////////SB20200317///////////////////////
        mTextMsgTv = view.findViewById(R.id.txt_contribution);
        String msg=""; String msgCondition="";
        if(UniqueKey.equals(""))
            mTextMsgTv.setBackgroundColor(000000);
        msg=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.LAST_LOGIN_MSG);
        mTextMsgTv.setText(msg);
        ///////////////////////////////////////////////
        mRaisedOrderTv = view.findViewById(R.id.txt_raised_orders_count);
        mRaisedOrderTv.setText("0");

        mRaisedNotificationTv = view.findViewById(R.id.txt_notification_count);
        mRaisedNotificationTv.setText("0");
        ////////////SB20191218:Policy//////////////////////////////
        mPolicyTerms = view.findViewById(R.id.txt_notification_count);
        mPolicyTerms.setText("0");
        if(FirstTimePolicyUser.equals("N")) mPolicyTerms.setText("0");
        if(FirstTimeTermUser.equals("N")) mPolicyTerms.setText("0");
        ///////////////////////////////////////////////////////////
        view.findViewById(R.id.lyt_notification).setOnClickListener(this); //SB20191226
        view.findViewById(R.id.lyt_new_order).setOnClickListener(this);
        view.findViewById(R.id.lyt_reports).setOnClickListener(this);
        view.findViewById(R.id.lyt_raised_orders).setOnClickListener(this);
        view.findViewById(R.id.lyt_drafts).setOnClickListener(this);
        view.findViewById(R.id.lyt_shop).setOnClickListener(this);
        view.findViewById(R.id.lyt_new_prod_area).setOnClickListener(this); //SB20200611
        view.findViewById(R.id.lyt_summary).setOnClickListener(this);
        view.findViewById(R.id.lyt_myOrders).setOnClickListener(this);
        view.findViewById(R.id.lyt_summary_gst).setOnClickListener(this);
        view.findViewById(R.id.lyt_my_reward).setOnClickListener(this);

        getRaisedOrders();

        mLblVendor = view.findViewById(R.id.lblVendor);
        mSpinnerVendor = view.findViewById(R.id.spinnerVendor);
        mLblStatus = view.findViewById(R.id.lblStatus);
        mSpinnerStatus = view.findViewById(R.id.spinnerStatus);
        mLblCompany = view.findViewById(R.id.lblCompany);
        mSpinnerCompany = view.findViewById(R.id.spinnerCompany);
        mLblFilter1 = view.findViewById(R.id.lblFilter1);
        mImgDownArrow = view.findViewById(R.id.imgDownArrow);
    }
  @Override
    public void onClick(View v)
  {
        String FirstTimePolicyUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
        String FirstTimeTermUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_TERM_KEY); //SB20191218

        Intent intent = null;
        switch (v.getId()) {
          /*SB20191226  case R.id.lyt_feedback:
                replaceFragment(new FeedbackFragment());
                break;*/
            case R.id.lyt_notification:
                if (Integer.parseInt(mRaisedNotificationTv.getText().toString()) > 0)
                    replaceFragment(new RaisedNotificationFragment()); //SB20191226
                //replaceFragment(new RaisedNotificationFragment()); //SB20200109
                else
                    WidgetUtil.showErrorToast(mContext, "You don't have any Notification");
                break;
            case R.id.lyt_new_order:
                ///////////////SB20191218:Policy////
                if(FirstTimePolicyUser.equals("N"))
                    WidgetUtil.showErrorToast(mContext, "You have not Agreed Privacy Policy");
                else if(FirstTimeTermUser.equals("N"))
                    WidgetUtil.showErrorToast(mContext, "You have not Agreed Terms & Conditions");
                //////////////////////////
                else {
                    intent = new Intent(getActivity(), NewOrderActivity.class);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.lyt_reports:
                intent = new Intent(getActivity(), ReportsActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.lyt_raised_orders:
                if (Integer.parseInt(mRaisedOrderTv.getText().toString()) > 0)
                    replaceFragment(new RaisedOrderFragment());
                else
                    WidgetUtil.showErrorToast(mContext, "You don't have any booked orders");
                break;
            case R.id.lyt_drafts:
                replaceFragment(new DraftFragment());
                break;
            case R.id.lyt_shop: replaceFragment(new MyShopFragment());
                replaceFragment(new MyShopFragment());
                break;
            case R.id.lyt_new_prod_area: replaceFragment(new NewProductMyAreaFragment()); //SB20200611
                replaceFragment(new NewProductMyAreaFragment());
                break;
            case R.id.lyt_summary:
                replaceFragment(new MonthlySummaryFragment());
                break;
            case R.id.lyt_myOrders:
                replaceFragment(new MyOrdersFragment());
                break;
            case R.id.lyt_summary_gst:
                replaceFragment(new MonthlySummaryGstFragment());
                break;
            case R.id.lyt_my_reward:
                replaceFragment(new MyRewardsFragment());
                break;
        }

    }

    private void getRaisedOrders()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("distcd", "0");
        jsonObject.addProperty("compcd", "0");

        new PostDataToServerTask(mContext, AppConstant.Actions.GET_RAISED_ORDERS)
                .setPath(AppConstant.WebURL.GET_RAISED_ORDERS_PATH)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }

    @Override
    public void onResponse(String tag, String response)
    {
        Gson gson = GsonFactory.getInstance();
        SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(mContext);
        RaisedOrder raisedOrder = gson.fromJson(response, RaisedOrder.class);

        if (raisedOrder.getOrderList() != null && !raisedOrder.getOrderList().isEmpty())
            mRaisedOrderTv.setText(raisedOrder.getOrderList().size() + "");
        else
            mRaisedOrderTv.setText("0");

        Notification notification = gson.fromJson(response, Notification.class); //SB20191227
        if (notification.getNotificationList() != null && !notification.getNotificationList().isEmpty())
            mRaisedNotificationTv.setText(notification.getNotificationList().size() + "");
        else
            mRaisedNotificationTv.setText("0");

        initNavigationView();
        /////////////////SB20191220//////////////////////////////////////////////
        String FirstTimeUserDtl="Please Click "; //SB20191216
        String FirstTimePolicyUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
        String FirstTimeTermUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_TERM_KEY); //SB20191218

        if (FirstTimePolicyUser.equals("N")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Privacy Policy Detail");
            builder.setMessage("Please Submit Privacy Policy");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    replaceFragment(new PolicyFragment());
                }
            });
           /* builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });*/
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            positiveButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));

            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
            negativeButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));

            sharedPrefUtil.putString(BaseSharedPref.STORE_NAME_KEY, raisedOrder.getName());
        }
        else if (FirstTimeTermUser.equals("N")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Terms & Conditions");
            builder.setMessage("Please Submit Terms & Conditions");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    replaceFragment(new TermsConditionsFragment());
                }
            });
            /*builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });*/
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            positiveButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));

            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
            negativeButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));

            sharedPrefUtil.putString(BaseSharedPref.STORE_NAME_KEY, raisedOrder.getName());
        }
        ////////////////^SB20191220/////////////////////////////////////////////////////
        /*SB20191223: Blocked based on RD
        if (raisedOrder.getFeedbackApply().equals("Y") && sharedPrefUtil.getString(BaseSharedPref.STORE_NAME_KEY) == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Order Feedback Detail");
            builder.setMessage("Please Submit Feedback");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    replaceFragment(new FeedbackFragment());
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

            sharedPrefUtil.putString(BaseSharedPref.STORE_NAME_KEY, raisedOrder.getName());
        }*/
    }

    @Override
    public void onError(String tag, String error)
    {
        WidgetUtil.showErrorToast(mContext, error);
    }

    private void initNavigationView()
    {
        if (getActivity() instanceof IBaseMenuActivity)
        {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            baseMenuActivity.setActionbarTitle("Home");
            baseMenuActivity.checkMenuItem(R.id.nav_home);
            baseMenuActivity.setActionbarSubtitleTitle("");
            baseMenuActivity.registerActivityBackPressListener(this);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        initNavigationView();
    }

    private void replaceFragment(Fragment fragment)
    {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragmentAfterDelay(fragment);
    }

    @Override
    public boolean onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //SB20191213 builder.setIcon(R.drawable.ic_logout_red_48dp);
        //SB20191213 builder.setTitle("Warning");
        builder.setMessage("You are in Home Page !!!");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
              //SB20191213  AppUserManager.logout(mContext, SignInActivity.class);
            }
        });
      /*  builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });*/

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));


        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        return true;
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if (getActivity() instanceof IBaseMenuActivity)
        {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            baseMenuActivity.unregisterActivityBackPressListener(this);
        }
    }
}