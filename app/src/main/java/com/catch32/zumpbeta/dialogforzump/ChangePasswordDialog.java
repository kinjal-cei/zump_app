package com.catch32.zumpbeta.dialogforzump;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.apicall.ApiService;
import com.catch32.zumpbeta.apicall.OnResponseListener;
import com.catch32.zumpbeta.apicall.RequestType;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.dialog.FullScreenBaseDialogFragment;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.ChangePasswd;
import com.catch32.zumpbeta.utils.KeyboardHelper;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.catch32.zumpbeta.apicall.ApiEndPoint;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ChangePasswordDialog extends FullScreenBaseDialogFragment
        implements View.OnClickListener, ResponseListener
{
    private EditText mPasswordEt, mNewPasswordEt, mConfirmPasswordEt;
    private TextInputLayout mPasswordTIL, mNewPasswordTIL, mConfirmPasswordTIL;
    private String mNewPassword;
    private Context mContext;
    Handler handler=new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.dialog_change_password, container, false);
        initComponent(rootView);
        return rootView;
    }

    private void initComponent(View rootView)
    {
        mPasswordEt = (EditText) rootView.findViewById(R.id.et_password);
        mNewPasswordEt = (EditText) rootView.findViewById(R.id.et_new_password);
        mConfirmPasswordEt = (EditText) rootView.findViewById(R.id.et_retype_new_password);

        mPasswordTIL = (TextInputLayout) rootView.findViewById(R.id.til_password);
        mNewPasswordTIL = (TextInputLayout) rootView.findViewById(R.id.til_new_password);
        mConfirmPasswordTIL = (TextInputLayout) rootView.findViewById(R.id.til_retype_new_password);

        mPasswordTIL.setErrorEnabled(false);
        mNewPasswordTIL.setErrorEnabled(false);
        mConfirmPasswordTIL.setErrorEnabled(false);

        rootView.findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if (id == R.id.btn_submit)
        {
            changePasswordClick();
        }
    }

    private void changePasswordClick()
    {
        String password = mPasswordEt.getText().toString().trim();
        mNewPassword = mNewPasswordEt.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEt.getText().toString().trim();

        KeyboardHelper.hideSoftKeyboard(getActivity(), mPasswordEt);
        KeyboardHelper.hideSoftKeyboard(getActivity(), mNewPasswordEt);
        KeyboardHelper.hideSoftKeyboard(getActivity(), mConfirmPasswordEt);

        boolean status = true;
        if (TextUtils.isEmpty(password))
        {
            status = false;
            WidgetUtil.showTextInputLayoutError(mPasswordTIL, "Please enter current password");
        } else {
            WidgetUtil.hideTextInputLayoutError(mPasswordTIL);
        }

        if (TextUtils.isEmpty(mNewPassword))
        {
            status = false;
            WidgetUtil.showTextInputLayoutError(mNewPasswordTIL, "Please enter new password");
        } else {
            int minPasswordLength = 6;
            if (mNewPassword.length() < minPasswordLength) {
                WidgetUtil.showTextInputLayoutError(mNewPasswordTIL, "Password must be at least " + minPasswordLength + " characters in length");
                status = false;
            } else {
                WidgetUtil.hideTextInputLayoutError(mNewPasswordTIL);
            }
        }

        if (!mNewPassword.equals(confirmPassword))
        {
            status = false;
            WidgetUtil.showTextInputLayoutError(mConfirmPasswordTIL, "This password doesn't match");
        } else {
            WidgetUtil.hideTextInputLayoutError(mConfirmPasswordTIL);
        }

        if (!status) return;

        JSONObject bodyParam = new JSONObject();
        JSONObject Param = new JSONObject();
        try {
            Param.accumulate("usercd",  SharedPrefFactory.getProfileSharedPreference(getActivity()).getString(BaseSharedPref.USER_CD_KEY));
            Param.accumulate("newPassword", mNewPassword);
            Param.accumulate("usercd",password);
            Param.accumulate("mobileNo",SharedPrefFactory.getProfileSharedPreference(getActivity()).getString(BaseSharedPref.MOBILE_NUMBER_KEY));
            System.out.println(Param);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        ApiService apiService1 = new ApiService(ChangePassword, RequestType.POST,
                ApiEndPoint.UPDATE_PROFILE + Param.toString(), new HashMap<String, String>(), bodyParam);
    }

    OnResponseListener ChangePassword = new OnResponseListener()
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
                        String FirstTimePolicyUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
                        if (FirstTimePolicyUser.equals("Y"))
                        {
                            Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_LONG);
                        }
                        Gson gson = GsonFactory.getInstance();
                        ChangePasswd changePasswd = gson.fromJson(response, ChangePasswd.class);
                        if (changePasswd.getChng_pwd().equals("Y") && changePasswd.getLoginSuccess().equals("Y"))
                        {
                            Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_LONG);
                        }
                        else
                            Toast.makeText(getActivity(), "Wrong Old Password ", Toast.LENGTH_LONG);
                        dismiss();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }});
        }
        @Override
        public void onError(String error)
        {
            WidgetUtil.showErrorToast(getActivity(), error);
        }
    };

    @Override
    public void onResponse(String tag, String response)
    {
        String FirstTimePolicyUser=SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
        if (FirstTimePolicyUser.equals("Y"))
        {
            Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_LONG);
        }
        Gson gson = GsonFactory.getInstance();
        ChangePasswd changePasswd = gson.fromJson(response, ChangePasswd.class);
        if (changePasswd.getChng_pwd().equals("Y") && changePasswd.getLoginSuccess().equals("Y")) {
            Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_LONG);
        }
        else
            Toast.makeText(getActivity(), "Wrong Old Password ", Toast.LENGTH_LONG);
        dismiss();
    }

    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(getActivity(), error);
    }
}
