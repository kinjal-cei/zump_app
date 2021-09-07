package com.catch32.zumpbeta.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {

    private Context mContext;
    private EditText mMobileNumberEt, mOTPEt, mNewPasswordEt, mConfirmPasswordEt;
    private TextInputLayout mMobileNumberTIL, mOTPTIL, mNewPasswordTIL, mConfirmPasswordTIL;
    private String mSentOTP;
    private Button mOTP, mVerify, mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
    }
    private void initView()
    {
        mContext = this;
        mMobileNumberEt = findViewById(R.id.et_mobile);
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


    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btn_send_otp)
        {
            if (ContextCompat.checkSelfPermission(ForgotPasswordActivity.this,
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 2294);
            } else {
                sendSMS();
            }
        } else if (v.getId() == R.id.btn_verify_otp)
        {
            String otptext = mOTPEt.getText().toString();

            if (mSentOTP.matches(otptext)) {
                mNewPasswordTIL.setVisibility(View.VISIBLE);
                mConfirmPasswordTIL.setVisibility(View.VISIBLE);
                mSubmit.setVisibility(View.VISIBLE);
                mVerify.setVisibility(View.GONE);
            } else {
                Toast.makeText(getBaseContext(), "Incorrect OTP", Toast.LENGTH_SHORT).show();
            }


        } else if (v.getId() == R.id.btn_submit) {
            String new_passwd = mNewPasswordEt.getText().toString();
            String cnf_passwd = mConfirmPasswordEt.getText().toString();
            String mobile = mMobileNumberEt.getText().toString();
            String otp_no = mOTPEt.getText().toString();

            if ((!new_passwd.equals("")) && (!cnf_passwd.equals("")) && (!mobile.equals("")) && (!otp_no.equals(""))) {
                if (new_passwd.matches(cnf_passwd) && (new_passwd.length() >= 6 && cnf_passwd.length() >= 6)) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("categoryId", "-1");
                    jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
                    jsonObject.addProperty("distId", "-1");

                    new PostDataToServerTask(mContext, AppConstant.Actions.FORGOT_PASSWORD)
                            .setPath(AppConstant.WebURL.FORGOT_PASSWORD)
                            .setResponseListener(this)
                            .setRequestParams(jsonObject)
                            .makeCall();

                } else {
                    if (new_passwd.length() < 6) {
                        Toast.makeText(getBaseContext(), "New Password Must Be of Minimum 6 Characters", Toast.LENGTH_SHORT).show();
                    } else if (cnf_passwd.length() < 6) {
                        Toast.makeText(getBaseContext(), "Confirm Password Must Be of Minimum 6 Characters", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "New Password and Confirm Password Should be Same", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getBaseContext(), "Please Enter all details!!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void sendSMS() {
        String mobile_num = mMobileNumberEt.getText().toString();
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
            String message = "Your OTP is " + otpL + ". ";
            message += "OTP is Valid For Only 5 Mins.";
            SmsManager sms = SmsManager.getDefault();
            String mobileNoVal = "+91" + mobile_num;

            sms.sendTextMessage(mobileNoVal, serviceCenterAddress, message, null, null);

            mSentOTP = otpL;

            Toast.makeText(getBaseContext(), "OTP Sent To Entered Mobile No..", Toast.LENGTH_SHORT).show();
            mOTPTIL.setVisibility(View.VISIBLE);
            mVerify.setVisibility(View.VISIBLE);

            mOTP.setVisibility(View.GONE);


        } else {
            Toast.makeText(getBaseContext(), "Incorrect Mobile Number", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2294: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();

                } else {
                    Toast.makeText(getBaseContext(), "Please Allow SMS Permissions from Settings> AppPermissions> SMS", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }

    @Override
    public void onResponse(String tag, String response) {

        try {
            JSONObject jsonResult = new JSONObject(response);
            if (jsonResult.get("updatepassword").equals("Y")) {
                Toast.makeText(getBaseContext(), "Password Changed Succesfully!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
            } else if (jsonResult.get("updatepassword").equals("C")) {
                Toast.makeText(getBaseContext(), "Failed To Update Please try after sometime!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            } else if (jsonResult.get("updatepassword").equals("NotExist")) {
                Toast.makeText(getBaseContext(), "Mobile No Does Not Exist!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }
}

