package com.catch32.zumpbeta.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.SignInActivity;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.Notification; //SB20191227
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.process.AppUserManager;
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
public class NotificationFragment extends Fragment implements View.OnClickListener, ResponseListener
{
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private Context mContext;
    private TextView mHeader, mNotifyTxt, mNotify2Txt, mNotify3Txt, mNotifyReadTxt, mNotify4Txt, mNotify5Txt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String TermStatus = sharedPref.getString(BaseSharedPref.USER_TERM_KEY); //SB20191216

        mContext = getActivity();
        //////SB20200225//////////////////////////
        TextView info = view.findViewById(R.id.header_path);
        info.setText("["+ SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home > Notification");
        //////^SB20200225//////////////////////////
        view.findViewById(R.id.btn_Terms_decline).setOnClickListener(this);
        if(TermStatus.equals("N"))
            view.findViewById(R.id.btn_terms_Agree).setOnClickListener(this);

        mHeader = (TextView) view.findViewById(R.id.txt_info);
        mNotifyTxt = (TextView) view.findViewById(R.id.txt_name);
        mNotify2Txt = (TextView) view.findViewById(R.id.txt_name2);
        mNotify3Txt = (TextView) view.findViewById(R.id.txt_name3);
        mNotifyReadTxt = (TextView) view.findViewById(R.id.txt_read);
        mNotify4Txt = (TextView) view.findViewById(R.id.txt_name4);
        mNotify5Txt = (TextView) view.findViewById(R.id.txt_name5);



        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", userCd);
        jsonObject.addProperty("state", "24");
        jsonObject.addProperty("accept", "Y");


        new PostDataToServerTask(mContext, AppConstant.Actions.NOTIFICATION)
                .setPath(AppConstant.WebURL.NOTIFICATION)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();

    }

    private void initNavigationView() {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String terms = sharedPref.getString(BaseSharedPref.USER_TERM_KEY, "unknown");
        String terms_dt = sharedPref.getString(BaseSharedPref.USER_TERM_DT, "unknown");

        if (getActivity() instanceof IBaseMenuActivity) {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            if(terms.equals("Y"))
                baseMenuActivity.setActionbarTitle("Notification(Read)");
            else
                baseMenuActivity.setActionbarTitle("Notification");

            baseMenuActivity.checkMenuItem(R.id.nav_profile);
        }
    }

    @Override
    public void onClick(View v) {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String PolicyStatus = sharedPref.getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
        switch (v.getId()) {
            case R.id.btn_Terms_decline:
              //  ChangePasswordUpdateDialog dialog = new ChangePasswordUpdateDialog();
              //  dialog.show(getChildFragmentManager(), "Cancel");
                break;

            case R.id.btn_terms_Agree:
                ////////Update Status as Agreed///////////////////
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("usercd", userCd);
                jsonObject.addProperty("state", "24");
                jsonObject.addProperty("accept", "Y");

                new PostDataToServerTask(mContext, AppConstant.Actions.TERMS_CONDITIONS)
                        .setPath(AppConstant.WebURL.TERMS_CONDITIONS)
                        .setResponseListener(this)
                        .setRequestParams(jsonObject)
                        .makeCall();
                ////////////////////////////////////////////////////
                // AppUserManager.logout(mContext, SignInActivity.class);
                /*Intent intent = null;
                intent = new Intent(getActivity(), MainActivity.class);
                mContext.startActivity(intent);
*/
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.drawable.ic_logout_red_48dp);
                builder.setTitle("Notification");
                builder.setMessage("You have Read.\nPlease Logout and Login again");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppUserManager.logout(mContext, SignInActivity.class);
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
                positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));


                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initNavigationView();
    }

    @Override
    public void onResponse(String tag, String response)
    {
        Gson gson = GsonFactory.getInstance();

        Notification notification = gson.fromJson(response, Notification.class); //SB20191227
        //NotificationList notifyList = gson.fromJson(response, NotificationList.class);
       /// mHeader.setText(notification.getTermsText());
            mHeader.setText("Message from "+notification.getNotifyBy());
            mNotifyTxt.setText("Subject: "+notification.getTermsHeader());
            mNotify2Txt.setText("Date: "+notification.getAgreeDt());
            mNotify3Txt.setText(notification.getTermsText());
            if(notification.getAgreeDt().equals("Y"))
                mNotifyReadTxt.setText("New Message ");
            else
                mNotifyReadTxt.setText("Read Date: "+notification.getReadDt());
        if (notification.getNotificationList() != null && !notification.getNotificationList().isEmpty())
        {
            String abc="";
            mNotify4Txt.setText("Total Available Notification "+notification.getNotificationList().size());
            for (int i = 0; i < notification.getNotificationList().size(); i++)
            {
                abc=abc+notification.getNotificationList().get(i);
                mNotify5Txt.setText(abc);
            }

        }
        fillTermsDetails();

        /*if (notifyList.getNotificationList() != null && !notifyList.getNotificationList().isEmpty()) {
            for (int i = 0; i < notifyList.getNotificationList().size(); i++) {

                System.out.println("Notifications Date : "+  notifyList.getNotificationList().size()+"");
                System.out.println("Notifications : "+  notifyList.getNotification()+"");
                System.out.println("Notifications Flag : "+  notifyList.getNotifyFlag()+"");
                System.out.println("Notifications Date : "+  notifyList.getNotifyDt()+"");

            }
        }*/



    }

    private void fillTermsDetails() {

        String terms = "";
        String terms_dt = "";

        String Term ="Agreed on "+terms_dt;

        String Term0 ="Under Dev. "+terms;
        String Term1 ="Under Dev. "+terms_dt;
        String Term2 ="";
        String Term3 ="";
        String Term4 ="";

    }

    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }
}

