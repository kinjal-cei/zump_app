package com.catch32.zumpbeta.myfragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.apicall.ApiService;
import com.catch32.zumpbeta.apicall.OnResponseListener;
import com.catch32.zumpbeta.apicall.RequestType;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.fragment.NotificationFragment;
import com.catch32.zumpbeta.model.Notification;
import com.catch32.zumpbeta.myactivity.Dashboard_Activity;
import com.catch32.zumpbeta.myactivity.Month_Wise_Summary;
import com.catch32.zumpbeta.myactivity.MyShop;
import com.catch32.zumpbeta.myactivity.My_Order_Activity;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.catch32.zumpbeta.apicall.ApiEndPoint.NOTIFICATION;

public class Fragment_Notification  extends Fragment
{
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private Context mContext;
    Handler handler=new Handler();
    String userCd,TermStatus;
    Button btnAgree,btnDecline;
    int initialScrollY = 0;
    private TextView mHeader, mNotifyTxt, mNotify2Txt, mNotify3Txt, mNotifyReadTxt, mNotify4Txt, mNotify5Txt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mContext = getActivity();

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
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyShop.class);
                startActivity(i);
            }
        });

        LinearLayout llOrderStatus = view.findViewById(R.id.llOrderStatus);
        llOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), My_Order_Activity.class);
                startActivity(i);
            }
        });

        LinearLayout llMonthwiseSummary = view.findViewById(R.id.llMonthwiseSummary);
        llMonthwiseSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Month_Wise_Summary.class);
                startActivity(i);
            }
        });

            SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
            userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
            TermStatus = sharedPref.getString(BaseSharedPref.USER_TERM_KEY); //SB20191216
            String terms = sharedPref.getString(BaseSharedPref.USER_TERM_KEY, "unknown");
            String terms_dt = sharedPref.getString(BaseSharedPref.USER_TERM_DT, "unknown");
            String PolicyStatus = sharedPref.getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216

            btnAgree = view.findViewById(R.id.btn_terms_Agree);
            btnDecline = view.findViewById(R.id.btn_Terms_decline);

            mHeader = (TextView) view.findViewById(R.id.txt_info);
            mNotifyTxt = (TextView) view.findViewById(R.id.txt_name);
            mNotify2Txt = (TextView) view.findViewById(R.id.txt_name2);
            mNotify3Txt = (TextView) view.findViewById(R.id.txt_name3);
            mNotifyReadTxt = (TextView) view.findViewById(R.id.txt_read);
            mNotify4Txt = (TextView) view.findViewById(R.id.txt_name4);
            mNotify5Txt = (TextView) view.findViewById(R.id.txt_name5);

            if (getActivity() instanceof IBaseMenuActivity) {
                IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
                if (terms.equals("Y"))
                    baseMenuActivity.setActionbarTitle("Notification(Read)");
                else
                    baseMenuActivity.setActionbarTitle("Notification");

                baseMenuActivity.checkMenuItem(R.id.nav_profile);
            }

            JSONObject bodyParam = new JSONObject();
            JSONObject Param = new JSONObject();
            try {
                Param.accumulate("usercd", userCd);
                Param.accumulate("state", "24");
                System.out.println(Param);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(getActivity());
            ApiService apiService1 = new ApiService(GetNotification, RequestType.POST,
                    NOTIFICATION + Param.toString(), new HashMap<String, String>(), bodyParam);

            if (TermStatus.equals("N")) {
                btnAgree.setVisibility(View.VISIBLE);
                btnDecline.setVisibility(View.VISIBLE);
            }
            if (TermStatus.equals("Y")) {
                btnAgree.setVisibility(View.GONE);
                btnDecline.setVisibility(View.GONE);
            }

        return view;
    }

    OnResponseListener GetNotification = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response1, HashMap<String, String> headers)
        {
            final String response = response1;
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
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
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }});
        }

        @Override
        public void onError(String error)
        {
            WidgetUtil.showErrorToast(mContext, error);
        }
    };
}