package com.catch32.zumpbeta.myfragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.SignInActivity;
import com.catch32.zumpbeta.apicall.ApiService;
import com.catch32.zumpbeta.apicall.OnResponseListener;
import com.catch32.zumpbeta.apicall.RequestType;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.dialog.ChangePasswordUpdateDialog;
import com.catch32.zumpbeta.dialogforzump.ProfileUpdateDialog;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.model.Profile;
import com.catch32.zumpbeta.model.ProfileData;
import com.catch32.zumpbeta.myactivity.Dashboard_Activity;
import com.catch32.zumpbeta.myactivity.Month_Wise_Summary;
import com.catch32.zumpbeta.myactivity.MyShop;
import com.catch32.zumpbeta.myactivity.My_Order_Activity;
import com.catch32.zumpbeta.process.AppUserManager;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.catch32.zumpbeta.apicall.ApiEndPoint.GET_PROFILE;

public class Fragment_Profile extends Fragment
{
    private static final String TAG = Fragment_Profile.class.getSimpleName();
    private Context mContext;
    private TextView mNameTxt, mGenderTxt, mAddressTxt, mMobileTxt, mEmailTxt, mGstnTxt, mStreetTxt, mCityTxt, mPoTxt;
    int initialScrollY = 0;
    String userCd,mobile_No;
    Handler handler=new Handler();
    Button btn_updateProfile,btn_ChangePass;
    String po ="";
    String city1 ="";
    String street1 ="";
    String owner_nm1 ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final NestedScrollView nsvDashboard = view.findViewById(R.id.nsvDashboard);
        final LinearLayout llFooter = view.findViewById(R.id.llFooter);
        final int initialViewHeight = llFooter.getLayoutParams().height;
        initialScrollY = nsvDashboard.getScrollY();

        btn_updateProfile=view.findViewById(R.id.btn_terms_Agree);
        btn_ChangePass=view.findViewById(R.id.btn_Terms_decline);

        mContext = getActivity();
        mNameTxt = view.findViewById(R.id.txt_name);
        mGstnTxt = view.findViewById(R.id.txt_gstn); //SB20191216
        mMobileTxt =  view.findViewById(R.id.txt_mobile_no);
        mEmailTxt = view.findViewById(R.id.txt_pin);
        mGenderTxt = view.findViewById(R.id.txt_gender);
        mAddressTxt = view.findViewById(R.id.txt_area);
        mStreetTxt = view.findViewById(R.id.txt_street); //SB20200109
        mCityTxt = view.findViewById(R.id.txt_city); //SB20200109
        mPoTxt = view.findViewById(R.id.txt_po); //SB20200109

        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        mobile_No = sharedPref.getString(BaseSharedPref.MOBILE_NUMBER_KEY);

        btn_updateProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ProfileUpdateDialog dialog1 = new ProfileUpdateDialog();
                dialog1.show(getChildFragmentManager(), "CHANGE_PROFILE");
                ////////////SB20200209/////////////////////////////////////////
                AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                builder2.setIcon(R.drawable.ic_logout_red_48dp);
                builder2.setTitle("Update Profile");
                builder2.setMessage("You have Updated Profile.\nPlease Logout and Login again");
                builder2.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        AppUserManager.logout(mContext, SignInActivity.class);
                    }
                });
                builder2.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog2 = builder2.create();
                alertDialog2.show();

                Button positiveButton2 = alertDialog2.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton2.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

                Button negativeButton2 = alertDialog2.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton2.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
        });

        btn_ChangePass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangePasswordUpdateDialog dialog = new ChangePasswordUpdateDialog();
                dialog.show(getChildFragmentManager(), "CHANGE_PASSWORD");
                String FirstTimePolicyUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.drawable.ic_logout_red_48dp);
                builder.setTitle("Update Password");

                if (FirstTimePolicyUser.equals("Y"))
                {
                    Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_LONG);

                    builder.setMessage("You have Changed Password.\nPlease Logout and Login again");
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            AppUserManager.logout(mContext, SignInActivity.class);
                        }
                    });
                }
                else if (FirstTimePolicyUser.equals("N"))
                {
                    Toast.makeText(getActivity(), "Password Not Updated", Toast.LENGTH_LONG);

                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            //  AppUserManager.logout(mContext, SignInActivity.class);
                        }
                    });
                    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
                }

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }
        });

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
            Param.accumulate("usercd",  SharedPrefFactory.getProfileSharedPreference(getActivity()).getString(BaseSharedPref.USER_CD_KEY));
            Param.accumulate("mobileNo", mobile_No);
            System.out.println(Param);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
       ApiService apiService1 = new ApiService(GetProfile, RequestType.POST,
              GET_PROFILE + Param.toString(), new HashMap<String, String>(), bodyParam);

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

                        if (loginSuccess.equals("Y")) {
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

                            mNameTxt.setText(firstName + " " + lastName);
                            mGstnTxt.setText(gstn); //SB20191216
                            mMobileTxt.setText(mobileNo);
                            mEmailTxt.setText(emailId);
                            mAddressTxt.setText(address);
                            mGenderTxt.setText(gender + " " + owner_nm);
                            mStreetTxt.setText(street);
                            mCityTxt.setText(city);
                            mPoTxt.setText(po_nm);

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
}

