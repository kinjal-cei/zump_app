package com.catch32.zumpbeta.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.SignInActivity;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.dialog.ChangePasswordUpdateDialog;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.Profile;
import com.catch32.zumpbeta.model.ProfileData;
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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class CallUsFragment extends Fragment implements View.OnClickListener, ResponseListener {

    private static final String TAG = CallUsFragment.class.getSimpleName();
    private Context mContext;
    private TextView mNameTxt, mGenderTxt, mAddressTxt, mMobileTxt, mEmailTxt;
    int initialScrollY = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

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
                /* get the maximum height which we have scroll before performing any action */
                int maxDistance = llFooter.getHeight();
                /* how much we have scrolled */
                float movement = nsvDashboard.getScrollY();
                /*finally calculate the alpha factor and set on the view */
                //float alphaFactor = ((movement * 1.0f) / (maxDistance + llFooter.getHeight()));
                if (movement > initialScrollY)
                {
                    if (!animator.isRunning())
                    {
                        //Setting animation from actual value to the initial yourViewToHide height)
                        animator.setIntValues(params.height, 0);
                        //Animation duration
                        animator.setDuration(100);
                        //In this listener we update the view
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
                        //Starting the animation
                        animator.start();
                    }
                    /*for image parallax with scroll */
                    //llFooter.setLayoutParams();
                    /* set visibility */
                    //llFooter.setAlpha(0);
                }
                else
                {
                    if (!animator.isRunning())
                    {
                        //Setting animation from actual value to the target value (here 0, because we're hiding the view)
                        animator.setIntValues(params.height, initialViewHeight);
                        //Animation duration
                        animator.setDuration(500);
                        //In this listener we update the view
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                                params.height = (int) animation.getAnimatedValue();
                                llFooter.setLayoutParams(params);
                            }
                        });
                        //Starting the animation
                        animator.start();
                    }
                }

                initialScrollY = nsvDashboard.getScrollY();
            }
        });
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        view.findViewById(R.id.btn_Terms_decline).setOnClickListener(this);
        view.findViewById(R.id.btn_terms_Agree).setOnClickListener(this);

        mNameTxt = (TextView) view.findViewById(R.id.txt_name);
        mMobileTxt = (TextView) view.findViewById(R.id.txt_mobile_no);
        mEmailTxt = (TextView) view.findViewById(R.id.txt_email);
        mGenderTxt = (TextView) view.findViewById(R.id.txt_gender);
        mAddressTxt = (TextView) view.findViewById(R.id.txt_area);

        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String mobileNo = sharedPref.getString(BaseSharedPref.MOBILE_NUMBER_KEY);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", userCd);
        jsonObject.addProperty("mobileNo", mobileNo);


        new PostDataToServerTask(mContext, AppConstant.Actions.GET_PROFILE)
                .setPath(AppConstant.WebURL.GET_PROFILE)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();

    }

    private void initNavigationView() {
        if (getActivity() instanceof IBaseMenuActivity) {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            baseMenuActivity.setActionbarTitle("Call Us");
            baseMenuActivity.checkMenuItem(R.id.call_us);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Terms_decline:
                ChangePasswordUpdateDialog dialog = new ChangePasswordUpdateDialog();
                dialog.show(getChildFragmentManager(), "Cancel");
                break;

            case R.id.btn_terms_Agree:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.drawable.ic_logout_red_48dp);
                builder.setTitle("Privacy Policy");
                builder.setMessage("I Agree ?");
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
        Profile profile = gson.fromJson(response, Profile.class);
        ProfileData profileData = profile.getProfileData();

        SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(mContext);
        sharedPrefUtil.putString(BaseSharedPref.FIRST_NAME_KEY, profileData.getFirstName());
        sharedPrefUtil.putString(BaseSharedPref.LAST_NAME_KEY, profileData.getLastName());
        sharedPrefUtil.putString(BaseSharedPref.ADDRESS_KEY, profileData.getAddress());
        sharedPrefUtil.putString(BaseSharedPref.GENDER_KEY, profileData.getGender());
        sharedPrefUtil.putString(BaseSharedPref.EMAIL_ID_KEY, profileData.getEmailId());
        sharedPrefUtil.putString(BaseSharedPref.MOBILE_NUMBER_KEY, profileData.getMobileNo());
        sharedPrefUtil.putString(BaseSharedPref.MIDDLE_NAME_KEY, profileData.getMiddleName());

        fillCallDetails();
    }

    private void fillCallDetails()
    {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String firstName = sharedPref.getString(BaseSharedPref.FIRST_NAME_KEY, "unknown");
        String lastName = sharedPref.getString(BaseSharedPref.LAST_NAME_KEY, "unknown");
        String name = firstName + " " + lastName;

        String mobileNo = sharedPref.getString(BaseSharedPref.MOBILE_NUMBER_KEY, "unknown");
        String email = sharedPref.getString(BaseSharedPref.EMAIL_ID_KEY, "unknown");
        String address = sharedPref.getString(BaseSharedPref.ADDRESS_KEY, "unknown");
        String gender = sharedPref.getString(BaseSharedPref.GENDER_KEY, "unknown");
        name="BillPower.In"; mobileNo ="9769403868"; email ="info@billpower.in"; address =""; gender ="Mr. Ravi K. Darbha";
        mNameTxt.setText(name);
        mMobileTxt.setText(mobileNo);
        mEmailTxt.setText(email);
        mAddressTxt.setText(address);
        mGenderTxt.setText(gender);
    }

    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }
}

