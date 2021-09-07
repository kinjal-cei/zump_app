package com.catch32.zumpbeta.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.catch32.zumpbeta.process.AppUserManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
/////SB20200102//////////


public class UnregdUserActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener
{
    private Context mContext;
    private EditText mMobileNumberEt, mOTPEt, mNewPasswordEt, mConfirmPasswordEt;
    private TextInputLayout mMobileNumberTIL, mOTPTIL, mNewPasswordTIL, mConfirmPasswordTIL;
    private EditText mNameTxtEt, mGstnTxtEt, mMobileTxtEt, mEmailTxtEt, mOwnerTxtEt, mAddressTxtEt, mCityTxtEt, mStreetTxtEt, mPOTxtEt;
    private Button mCancel, mRegd;
    private TextInputLayout mNameTxt, mGstnTxt, mMobileTxt, mEmailTxt, mOwnerTxt, mAddressTxt, mStreetTxt, mPOTxt;
    private String mSentOTP;
    private Button mOTP, mVerify, mSubmit;
    String FirstTimeUnregdUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.MOBILE_NUMBER_KEY); //SB20191216
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_unregd_user);
        setContentView(R.layout.activity_unregd_user);
        initView();
    }
   /* public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_regd, container, false);
        init(view);
        return view;
    }*/
    @SuppressLint("WrongViewCast")
    private void init(View view)
    {
        mContext = this;
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_new_regd).setOnClickListener(this);

        mNameTxt = (TextInputLayout)view.findViewById(R.id.txt_name);
        mGstnTxt = (TextInputLayout) view.findViewById(R.id.txt_gstn); //SB20191216
        mMobileTxt = (TextInputLayout) view.findViewById(R.id.txt_mobile_no);
        mEmailTxt = (TextInputLayout) view.findViewById(R.id.txt_email);
        mOwnerTxt = (TextInputLayout) view.findViewById(R.id.txt_gender);
        mAddressTxt = (TextInputLayout) view.findViewById(R.id.txt_area);
        mPOTxt = (TextInputLayout) view.findViewById(R.id.txt_po);
        mStreetTxt = (TextInputLayout) view.findViewById(R.id.txt_street);
        mAddressTxt = (TextInputLayout) view.findViewById(R.id.txt_po);

    /*  mNameTxtEt = (EditText)view.findViewById(R.id.txt_name);
        mGstnTxtEt = (EditText) view.findViewById(R.id.txt_gstn); //SB20191216
        mMobileTxtEt = (EditText) view.findViewById(R.id.txt_mobile_no);
        mEmailTxtEt = (EditText) view.findViewById(R.id.txt_email);
        mGenderTxtEt = (EditText) view.findViewById(R.id.txt_gender);
        mAddressTxtEt = (EditText) view.findViewById(R.id.txt_area); */
    }
    private void initView()
    {
        mContext = this;
/*
        getSupportActionBar().setTitle("New Registration");

        mMobileNumberEt = findViewById(R.id.et_mobile);
        mMobileNumberEt.setText(FirstTimeUnregdUser); //SB20200101

        mOTPEt = findViewById(R.id.et_otp);
        mNewPasswordEt = findViewById(R.id.et_new_password);
        mConfirmPasswordEt = findViewById(R.id.et_confirm_password);

        mMobileNumberTIL = findViewById(R.id.til_mobile);
        mOTPTIL = findViewById(R.id.til_otp);
        mNewPasswordTIL = findViewById(R.id.til_new_password);
        mConfirmPasswordTIL = findViewById(R.id.til_confirm_password);


        mOTP = findViewById(R.id.btn_send_otp);
        mVerify = findViewById(R.id.btn_verify_otp);
        mSubmit = findViewById(R.id.btn_submit);

        mOTP.setOnClickListener(this);
        mVerify.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        mOTPTIL.setVisibility(View.GONE);
        mNewPasswordTIL.setVisibility(View.GONE);
        mConfirmPasswordTIL.setVisibility(View.GONE);
        mVerify.setVisibility(View.GONE);
        mSubmit.setVisibility(View.GONE);
        */

        mRegd=findViewById(R.id.btn_new_regd);
        mCancel=findViewById(R.id.btn_cancel);

        mNameTxtEt = findViewById(R.id.txt_name);
        mGstnTxtEt = findViewById(R.id.txt_gstn); //SB20191216
        mMobileTxtEt = findViewById(R.id.txt_mobile_no);
        mEmailTxtEt = findViewById(R.id.txt_email);
        mOwnerTxtEt = findViewById(R.id.txt_owner);
        mAddressTxtEt = findViewById(R.id.txt_area);
        mStreetTxtEt = findViewById(R.id.txt_street);
        mCityTxtEt = findViewById(R.id.txt_city);
        mPOTxtEt = findViewById(R.id.txt_po);

        mRegd.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        String retail_nm = mNameTxtEt.getText().toString();
        String gstn = mGstnTxtEt.getText().toString();
        String pin = mEmailTxtEt.getText().toString();
        String owner = mOwnerTxtEt.getText().toString();
        String addr = mAddressTxtEt.getText().toString();
        String mobile = mMobileTxtEt.getText().toString();
        String city = mCityTxtEt.getText().toString();
        String street = mStreetTxtEt.getText().toString();
        String po_nm = mPOTxtEt.getText().toString();
        //  String mobile = FirstTimeUnregdUser; //SB20200101
        // String otp_no = mOTPEt.getText().toString();
        switch (v.getId()) {
            case R.id.btn_new_regd:
                if ((mobile.length() >= 10 && retail_nm.length() >= 6 && (gstn.length() == 0 || gstn.length() == 15) && owner.length() >= 5 && city.length() >= 3)) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("categoryId", "-1");
                      //  jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
                        jsonObject.addProperty("distId", "-1");
                        jsonObject.addProperty("retail_nm", retail_nm); //SB20200101
                        jsonObject.addProperty("gstn", gstn); //SB20200101
                        jsonObject.addProperty("owner_nm", owner); //SB20200101
                        jsonObject.addProperty("addr", addr); //SB20200101
                        jsonObject.addProperty("street_nm", street); //SB20200107
                        jsonObject.addProperty("po_nm", po_nm); //SB20200107
                        jsonObject.addProperty("pin", pin); //SB20200101
                        jsonObject.addProperty("city", city);
                        jsonObject.addProperty("mobile_no", mobile);

                        new PostDataToServerTask(mContext, AppConstant.Actions.USER_UNREGD)
                                .setPath(AppConstant.WebURL.USER_UNREGD)
                                .setResponseListener(this)
                                .setRequestParams(jsonObject)
                                .makeCall();

                        onAlert(retail_nm); //SB20200104

                    }else {
                    if (retail_nm.length() < 6) {
                        Toast.makeText(getBaseContext(), "Outlet must be 6 Characters", Toast.LENGTH_SHORT).show();
                    }
                    if (gstn.length() < 15 && gstn.length() > 0) {
                        Toast.makeText(getBaseContext(), "GSTN must be 15 Characters", Toast.LENGTH_SHORT).show();
                    }
                    if (owner.length() < 5) {
                        Toast.makeText(getBaseContext(), "Owner Name must be 5 Characters", Toast.LENGTH_SHORT).show();
                    }
                    if (addr.length() < 10) {
                        Toast.makeText(getBaseContext(), "Address must be 10 Characters", Toast.LENGTH_SHORT).show();
                    }
                    if (city.length() < 3) {
                        Toast.makeText(getBaseContext(), "City must be 3 Characters", Toast.LENGTH_SHORT).show();
                    }
                    if (po_nm.length() < 3) {
                        Toast.makeText(getBaseContext(), "Post Office must be 3 Characters", Toast.LENGTH_SHORT).show();
                    }
                    if (pin.length() < 6) {
                        Toast.makeText(getBaseContext(), "PIN Code must be 6 Digits", Toast.LENGTH_SHORT).show();
                    }
                    if (mobile.length() < 10) {
                        Toast.makeText(getBaseContext(), "Mobile Number must be 10 Digits", Toast.LENGTH_SHORT).show();
                    }
                    else {
                       // Toast.makeText(getBaseContext(), "Should be Same", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.btn_cancel:
                AppUserManager.logout(mContext, SignInActivity.class);
                break;

        }
       // onAlert(retail_nm); //SB20200104
    }



    public void sendSMS() {
        String mobile_num = mMobileNumberEt.getText().toString();
       // String mobile_num = FirstTimeUnregdUser; //SB20200101
        if (mobile_num.length() == 10) {
            Random r = new Random();
            int otp = r.nextInt(9999);
            String otpL = "" + otp;
            if (otpL.length() < 4) {
                if (otpL.length() == 3) {
                    otpL = "0" + otp;
                } else if (otpL.length() == 2) {
                    otpL = "00" + otp;
                } else {
                    otpL = "000" + otp;
                }
            }
            String serviceCenterAddress = null;
           // String serviceCenterAddress = "+919427602898";
            String message = "Your OTP is " + otpL + ". ";
            message += " Valid only for 5 Mins.";
            SmsManager sms = SmsManager.getDefault();
            String mobileNoVal = "+91" + mobile_num;

            sms.sendTextMessage(mobileNoVal, serviceCenterAddress, message, null, null);

            mSentOTP = otpL;

            Toast.makeText(getBaseContext(), "OTP Sent To Entered Mobile No..", Toast.LENGTH_SHORT).show();
            mOTPTIL.setVisibility(View.VISIBLE);
            mVerify.setVisibility(View.VISIBLE);

            mOTP.setVisibility(View.GONE);
        } else
            {
            Toast.makeText(getBaseContext(), "Incorrect Mobile Number", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case 2294: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    sendSMS();

                } else
                    {
                    Toast.makeText(getBaseContext(), "Please Allow SMS Permissions from Settings> AppPermissions> SMS", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    public void onResponse(String tag, String response)
    {

        try {
            JSONObject jsonResult = new JSONObject(response);
            if (jsonResult.get("updatepassword").equals("Y"))
            {

                Toast.makeText(getBaseContext(), "New Registration Done Succesfully!!\nYour Regd. Mobile Number is default Password\nLogin will be ACTIVE by 24hrs", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UnregdUserActivity.this, SignInActivity.class);
                startActivity(intent);
            } else if (jsonResult.get("updatepassword").equals("C")) {
                Toast.makeText(getBaseContext(), "Failed To Update Please try after sometime!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UnregdUserActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            } else if (jsonResult.get("updatepassword").equals("NotExist")) {
                Toast.makeText(getBaseContext(), "Mobile No Does Not Exist!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UnregdUserActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
   //////////SB20200104///////////////////
    public void onAlert(String retail_nm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.drawable.ic_logout_red_48dp);
        builder.setTitle("Alert");
        builder.setMessage("You have Completed New Registration for\n" + retail_nm+"\n\nLogin will be activated by next 24 hrs !!!!");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                AppUserManager.logout(mContext, SignInActivity.class);
            }
        });
       /* builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
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
    }
    /////////////////////////////////////////////
    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }
}

