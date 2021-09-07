package com.catch32.zumpbeta.dialog;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.utils.KeyboardHelper;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import android.content.Context; //SB20200209


public class ChangeProfileUpdateDialog extends FullScreenBaseDialogFragment implements View.OnClickListener, ResponseListener
{
    private EditText mGstnEt, mNewOwnerEt, mConfirmAddrEt, mConfirmStreetEt, mConfirmCityEt, mConfirmPoEt, mConfirmPinEt, mConfirmMobEt;
    private TextInputLayout mGstnTIL, mNewOwnerTIL, mConfirmAddrTIL, mConfirmStreetTIL, mConfirmCityTIL, mConfirmPoTIL, mConfirmPinTIL, mConfirmMobTIL;
    private String mNewOwner, NewAddr, NewStreet, NewCity, NewPo, NewPin, NewMob;
    private Context mContext;
    String po ="";
    String city ="";
    String street ="";
    String owner_nm ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.dialog_change_profile, container, false);
       //SB View rootView = inflater.inflate(R.layout.fragment_update_profile, container, false);
        initComponent(rootView);
        return rootView;
    }

    private void initComponent(View rootView)
    {
        mGstnEt = (EditText) rootView.findViewById(R.id.et_gstn);
        mNewOwnerEt = (EditText) rootView.findViewById(R.id.et_new_owner);
        mConfirmAddrEt = (EditText) rootView.findViewById(R.id.et_retype_new_addr);
        mConfirmStreetEt = (EditText) rootView.findViewById(R.id.et_retype_new_street);
        mConfirmCityEt = (EditText) rootView.findViewById(R.id.et_retype_new_city);
        mConfirmPoEt = (EditText) rootView.findViewById(R.id.et_retype_new_po);
        mConfirmPinEt = (EditText) rootView.findViewById(R.id.et_retype_new_pin);
        mConfirmMobEt = (EditText) rootView.findViewById(R.id.et_retype_new_mobile);

        mGstnTIL = (TextInputLayout) rootView.findViewById(R.id.til_gstn);
        mNewOwnerTIL = (TextInputLayout) rootView.findViewById(R.id.til_new_owner);
        mConfirmAddrTIL = (TextInputLayout) rootView.findViewById(R.id.til_retype_new_addr);
        mConfirmStreetTIL = (TextInputLayout) rootView.findViewById(R.id.til_retype_new_addr);
        mConfirmCityTIL = (TextInputLayout) rootView.findViewById(R.id.til_retype_new_addr);
        mConfirmPoTIL = (TextInputLayout) rootView.findViewById(R.id.til_retype_new_po);
        mConfirmPinTIL = (TextInputLayout) rootView.findViewById(R.id.til_retype_new_pin);
        mConfirmMobTIL = (TextInputLayout) rootView.findViewById(R.id.til_retype_new_mobile);

        mGstnTIL.setErrorEnabled(false);
        mNewOwnerTIL.setErrorEnabled(false);
        mConfirmAddrTIL.setErrorEnabled(false);
        mConfirmStreetTIL.setErrorEnabled(false);
        mConfirmCityTIL.setErrorEnabled(false);
        mConfirmPoTIL.setErrorEnabled(false);
        mConfirmPinTIL.setErrorEnabled(false);
        mConfirmMobTIL.setErrorEnabled(false);

        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String firstName = sharedPref.getString(BaseSharedPref.FIRST_NAME_KEY, "unknown");
        String lastName = sharedPref.getString(BaseSharedPref.LAST_NAME_KEY, "unknown");
        String name = firstName + " " + lastName;
        String gstn = sharedPref.getString(BaseSharedPref.GSTN_KEY, "unknown"); //SB20191216
        String mobileNo = sharedPref.getString(BaseSharedPref.MOBILE_NUMBER_KEY, "unknown");
        String email = sharedPref.getString(BaseSharedPref.EMAIL_ID_KEY, "unknown");
        String address = sharedPref.getString(BaseSharedPref.ADDRESS_KEY, "unknown");
        String gender = sharedPref.getString(BaseSharedPref.GENDER_KEY, "unknown");
        String street = sharedPref.getString(BaseSharedPref.STREET, "unknown");
        String city = sharedPref.getString(BaseSharedPref.CITY, "unknown");
        String po = sharedPref.getString(BaseSharedPref.PO_NM, "unknown");

        mNewOwnerEt.setText(name);
        mGstnEt.setText(gstn); //SB20191216
        mConfirmMobEt.setText(mobileNo);
        mConfirmPinEt.setText(email);
        mConfirmAddrEt.setText(address);
        mNewOwnerEt.setText(gender+" "+owner_nm);
        mConfirmStreetEt.setText(street);
        mConfirmCityEt.setText(city);
        mConfirmPoEt.setText(po);

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
        String gstn = mGstnEt.getText().toString().trim();
        mNewOwner = mNewOwnerEt.getText().toString().trim();
        NewAddr = mConfirmAddrEt.getText().toString().trim();
        NewStreet = mConfirmStreetEt.getText().toString().trim();
        NewCity = mConfirmCityEt.getText().toString().trim();
        NewPo = mConfirmPoEt.getText().toString().trim();
        NewPin = mConfirmPinEt.getText().toString().trim();
        NewMob = mConfirmMobEt.getText().toString().trim();

        KeyboardHelper.hideSoftKeyboard(getActivity(), mGstnEt);
        KeyboardHelper.hideSoftKeyboard(getActivity(), mNewOwnerEt);
        KeyboardHelper.hideSoftKeyboard(getActivity(), mConfirmAddrEt);
        KeyboardHelper.hideSoftKeyboard(getActivity(), mConfirmStreetEt);
        KeyboardHelper.hideSoftKeyboard(getActivity(), mConfirmCityEt);
        KeyboardHelper.hideSoftKeyboard(getActivity(), mConfirmPoEt);
        KeyboardHelper.hideSoftKeyboard(getActivity(), mConfirmPinEt);
        KeyboardHelper.hideSoftKeyboard(getActivity(), mConfirmMobEt);

        boolean status = true;
        if (TextUtils.isEmpty(mNewOwner)) {
            status = false;
            WidgetUtil.showTextInputLayoutError(mNewOwnerTIL, "Please enter new Owner Name");
        } else {
            WidgetUtil.hideTextInputLayoutError(mNewOwnerTIL);
        }

        if (TextUtils.isEmpty(gstn)) {
            status = true;
           // WidgetUtil.showTextInputLayoutError(mGstnTIL, "Please enter GSTN");
        } else {
            int minPasswordLength = 15;
            if (gstn.length() < minPasswordLength)
            {
                WidgetUtil.showTextInputLayoutError(mGstnTIL, "GSTN must be " + minPasswordLength + " characters in length");
                status = false;
            } else {
                WidgetUtil.hideTextInputLayoutError(mGstnTIL);
            }
        }
/*SB
        if (!mNewOwner.equals(confirmPassword)) {
            status = false;
            WidgetUtil.showTextInputLayoutError(mConfirmPasswordTIL, "This password doesn't match");
        } else {
            WidgetUtil.hideTextInputLayoutError(mConfirmPasswordTIL);
        }
*/
        if (!status) return;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(getActivity()).getString(BaseSharedPref.USER_CD_KEY));
        jsonObject.addProperty("gstn", gstn);
        jsonObject.addProperty("owner", mNewOwner);
        jsonObject.addProperty("addr", NewAddr);
        jsonObject.addProperty("street", NewStreet);
        jsonObject.addProperty("city", NewCity);
        jsonObject.addProperty("po", NewPo);
        jsonObject.addProperty("pin", NewPin);
        jsonObject.addProperty("mobile", NewMob);
       // jsonObject.addProperty("mobileNo", SharedPrefFactory.getProfileSharedPreference(getActivity()).getString(BaseSharedPref.MOBILE_NUMBER_KEY));

        new PostDataToServerTask(getActivity(), AppConstant.Actions.CHANGE_PROFILE)
                .setPath(AppConstant.WebURL.CHANGE_PROFILE)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }


    @Override
    public void onResponse(String tag, String response)
    {
        Toast.makeText(getActivity(), "Profile changed successfully", Toast.LENGTH_LONG);
        dismiss();
    }

    @Override
    public void onError(String tag, String error)
    {
        WidgetUtil.showErrorToast(getActivity(), error);
    }
}
