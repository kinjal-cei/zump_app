package com.catch32.zumpbeta.myfragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.apicall.ApiService;
import com.catch32.zumpbeta.apicall.OnResponseListener;
import com.catch32.zumpbeta.apicall.RequestType;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.model.Profile;
import com.catch32.zumpbeta.model.ProfileData;
import com.catch32.zumpbeta.myactivity.Dashboard_Activity;
import com.catch32.zumpbeta.myactivity.Month_Wise_Summary;
import com.catch32.zumpbeta.myactivity.MyShop;
import com.catch32.zumpbeta.myactivity.My_Order_Activity;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.catch32.zumpbeta.apicall.ApiEndPoint;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class Fragment_Contact_Us extends Fragment
{

    private static final String TAG = Fragment_Contact_Us.class.getSimpleName();
    private Context mContext;
    private TextView mNameTxt, txt_website, mAddressTxt, mMobileTxt, mEmailTxt;
    int initialScrollY = 0;
    Handler handler=new Handler();
    String userCd,mobile_No;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        mContext = getActivity();

        mNameTxt = (TextView) view.findViewById(R.id.txt_name);
        mMobileTxt = (TextView) view.findViewById(R.id.txt_mobile_no);
        mEmailTxt = (TextView) view.findViewById(R.id.txt_email);
        txt_website = (TextView) view.findViewById(R.id.txt_website);
        mAddressTxt = (TextView) view.findViewById(R.id.txt_area);

        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        mobile_No = sharedPref.getString(BaseSharedPref.MOBILE_NUMBER_KEY);
        String firstName = sharedPref.getString(BaseSharedPref.FIRST_NAME_KEY, "unknown");
        String lastName = sharedPref.getString(BaseSharedPref.LAST_NAME_KEY, "unknown");
        String name = firstName + " " + lastName;
        String email = sharedPref.getString(BaseSharedPref.EMAIL_ID_KEY, "unknown");
        String address = sharedPref.getString(BaseSharedPref.ADDRESS_KEY, "unknown");
        String gender = sharedPref.getString(BaseSharedPref.GENDER_KEY, "unknown");
     //   name="BillPower.In"; mobile_No ="9769403868"; email ="info@billpower.in"; address =""; gender ="Mr. Ravi K. Darbha";
        mNameTxt.setText(name);
        mMobileTxt.setText(mobile_No);
        mEmailTxt.setText(email);
        mAddressTxt.setText(address);
        String url="https://www.zump.co.in/contact-us";
        txt_website.setText(url);

        txt_website.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

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


        JSONObject bodyParam = new JSONObject();
        JSONObject Param = new JSONObject();
        try {
            Param.accumulate("usercd",  userCd);
            Param.accumulate("mobileNo", mobile_No);
            System.out.println(Param);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        ApiService apiService1 = new ApiService(GetProfile, RequestType.POST,
                ApiEndPoint.GET_PROFILE + Param.toString(), new HashMap<String, String>(), bodyParam);
        return view;
    }


    OnResponseListener GetProfile = new OnResponseListener()
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

                        String loginSuccess =responseJSON.getString("loginSuccess");

                        if (loginSuccess.equals("Y"))
                        {
                            JSONObject obj = responseJSON.getJSONObject("profileData");
                            String firstName = obj.getString("firstName");
                            String lastName = obj.getString("lastName");
                            String address = obj.getString("address");
                            String gender = obj.getString("gender");
                            String genderVal = obj.getString("genderVal");
                            String name = obj.getString("name");
                            String emailId = obj.getString("emailId");
                            String middleName = obj.getString("middleName");
                            String mobileNo = obj.getString("mobileNo");
                            String gstn = obj.getString("gstn");
                            String street = obj.getString("street");
                            String city = obj.getString("city");
                            String po_nm = obj.getString("po_nm");
                            String pin = obj.getString("pin");
                            String owner_nm = obj.getString("owner_nm");

                            mNameTxt.setText(name);
                            mMobileTxt.setText(mobileNo);
                            mEmailTxt.setText(emailId);
                            mAddressTxt.setText(address);
                        //    txt_website.setText(owner_nm);

                            Gson gson = GsonFactory.getInstance();
                            Profile profile = gson.fromJson(response, Profile.class);
                            ProfileData profileData = profile.getProfileData();

                            SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(mContext);
                            sharedPrefUtil.putString(BaseSharedPref.FIRST_NAME_KEY, profileData.getFirstName());
                            sharedPrefUtil.putString(BaseSharedPref.LAST_NAME_KEY, profileData.getLastName());
                            sharedPrefUtil.putString(BaseSharedPref.ADDRESS_KEY, profileData.getAddress());
                            sharedPrefUtil.putString(BaseSharedPref.GENDER_KEY, profileData.getGender());
                            sharedPrefUtil.putString(BaseSharedPref.EMAIL_ID_KEY, profileData.getEmailId());
                            sharedPrefUtil.putString(BaseSharedPref.GSTN_KEY, profileData.getGstn()); //SB20191216
                            sharedPrefUtil.putString(BaseSharedPref.MOBILE_NUMBER_KEY, profileData.getMobileNo());
                            sharedPrefUtil.putString(BaseSharedPref.MIDDLE_NAME_KEY, profileData.getMiddleName());
                            sharedPrefUtil.putString(BaseSharedPref.CITY, profileData.getCity());
                            sharedPrefUtil.putString(BaseSharedPref.STREET, profileData.getStreet());
                            sharedPrefUtil.putString(BaseSharedPref.PO_NM, profileData.getPoNm());
                            sharedPrefUtil.putString(BaseSharedPref.OWNER_NM, profileData.getOwnerNm());
                        }
                        else
                        {

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
            System.out.println(error);
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
    }
}

