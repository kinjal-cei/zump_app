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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.apicall.ApiService;
import com.catch32.zumpbeta.apicall.OnResponseListener;
import com.catch32.zumpbeta.apicall.RequestType;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.myactivity.Dashboard_Activity;
import com.catch32.zumpbeta.myactivity.Month_Wise_Summary;
import com.catch32.zumpbeta.myactivity.MyShop;
import com.catch32.zumpbeta.myactivity.My_Order_Activity;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.catch32.zumpbeta.utils.WidgetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.catch32.zumpbeta.apicall.ApiEndPoint.GETFEEDBACK;
import static com.catch32.zumpbeta.apicall.ApiEndPoint.SAVE_FEEDBACK;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class Fragment_Feedback extends Fragment
{
    private static final String TAG = Fragment_Feedback.class.getSimpleName();
    private Context mContext;
    private TextView mHeader; //SB20200103
    String userCd;
    int initialScrollY = 0;
    Handler handler=new Handler();
    LinearLayout empty_view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_for_zump, container, false);
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
        mContext = getActivity();
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);

        empty_view= view.findViewById(R.id.empty_view);
        mHeader= view.findViewById(R.id.feedback_head);
        mHeader.setText("Zumpapp welcomes your valuable feedback to improve our Ordering Process");
        getFeedbackItems();
    }

    private void getFeedbackItems()
    {

        JSONObject bodyParam = new JSONObject();
        JSONObject Param = new JSONObject();
        try {
            Param.accumulate("usercd", userCd);
            System.out.println(Param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(getActivity());
        ApiService apiService1 = new ApiService(GetFeedback, RequestType.POST,
                GETFEEDBACK + Param.toString(), new HashMap<String, String>(), bodyParam);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    OnResponseListener GetFeedback = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        empty_view.setVisibility(View.GONE);
                        System.out.println(responseJSON);
                        String loginSuccess = responseJSON.getString("loginSuccess");

                        if (loginSuccess.equals("Y"))
                        {
                            int len;
                            final JSONArray feedbackDataArray = responseJSON.getJSONArray("feedbackData");
                            len = feedbackDataArray.length();
                            RelativeLayout nv = (RelativeLayout) getActivity().findViewById(R.id.rl_Feedback);

                            for (int j = 0; j < len; j++)
                            {
                                JSONObject object = (JSONObject) feedbackDataArray.get(j);
                                String CM_NAME = object.getString("CM_NAME");
                                String CM_NO = object.getString("CM_NO");

                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 25, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                LinearLayout Layout = (LinearLayout) inflater.inflate(R.layout.layout_feedback, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                Layout.setId(200 + j);
                                Layout.setLayoutParams(lp);

                                TextView txt_title = Layout.findViewById(R.id.txt_title);
                                RatingBar rt_feedback=Layout.findViewById(R.id.rt_feedback);
                                Button btn_submit=Layout.findViewById(R.id.btn_submit);

                                rt_feedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
                                {
                                    @Override
                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
                                    {

                                    }
                                });

                                btn_submit.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        float rating=rt_feedback.getRating();
                                        JSONObject bodyParam = new JSONObject();
                                        JSONObject Param = new JSONObject();
                                        try {
                                            Param.accumulate("usercd", userCd);
                                            Param.accumulate("id", CM_NO);
                                            Param.accumulate("rating", rating);

                                            System.out.println(Param);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        AndroidNetworking.initialize(getActivity());
                                        ApiService apiService1 = new ApiService(SaveFeedback, RequestType.POST,
                                                SAVE_FEEDBACK + Param.toString(), new HashMap<String, String>(), bodyParam);
                                    }
                                });

                                txt_title.setText(CM_NAME);

                                nv.addView(Layout);
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

    OnResponseListener SaveFeedback = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        System.out.println(responseJSON);
                        String loginSuccess = responseJSON.getString("loginSuccess");

                        if (loginSuccess.equals("Y"))
                        {
                            String submitFlag = responseJSON.getString("submitFlag");
                            if(submitFlag.equals("Y"))
                            {
                                WidgetUtil.showErrorToast(mContext,"Feedback Saved Successfully");
                            }
                            else
                            {
                                WidgetUtil.showErrorToast(mContext,"Feedback Saved Unsuccessfully");
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