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
import com.catch32.zumpbeta.utils.KeyboardHelper;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.catch32.zumpbeta.apicall.ApiEndPoint;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ProfileUpdateDialog extends FullScreenBaseDialogFragment implements View.OnClickListener
{
    private EditText mGstnEt, mNewOwnerEt, mConfirmAddrEt, mConfirmStreetEt, mConfirmCityEt, mConfirmPoEt, mConfirmPinEt, mConfirmMobEt;
    private TextInputLayout mGstnTIL, mNewOwnerTIL, mConfirmAddrTIL, mConfirmStreetTIL, mConfirmCityTIL, mConfirmPoTIL, mConfirmPinTIL, mConfirmMobTIL;
    private String mNewOwner, NewAddr, NewStreet, NewCity, NewPo, NewPin, NewMob;
    private Context mContext;
    String po ="";
    String city ="";
    String street ="";
    String owner_nm ="";
    Handler handler=new Handler();

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
        String ownerName = sharedPref.getString(BaseSharedPref.OWNER_NM, "unknown");
        System.out.println(ownerName);

        mNewOwnerEt.setText(ownerName);
        mGstnEt.setText(gstn); //SB20191216
        mConfirmMobEt.setText(mobileNo);
        mConfirmPinEt.setText(email);
        mConfirmAddrEt.setText(address);
       // mNewOwnerEt.setText(gender+" "+owner_nm);
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
        if (TextUtils.isEmpty(mNewOwner))
        {
            status = false;
            WidgetUtil.showTextInputLayoutError(mNewOwnerTIL, "Please enter new Owner Name");
        } else {
            WidgetUtil.hideTextInputLayoutError(mNewOwnerTIL);
        }

        if (TextUtils.isEmpty(gstn))
        {
            status = true;
           // WidgetUtil.showTextInputLayoutError(mGstnTIL, "Please enter GSTN");
        } else
            {
            int minPasswordLength = 15;
            if (gstn.length() < minPasswordLength)
            {
                WidgetUtil.showTextInputLayoutError(mGstnTIL, "GSTN must be " + minPasswordLength + " characters in length");
                status = false;
            } else {
                WidgetUtil.hideTextInputLayoutError(mGstnTIL);
            }
        }
        JSONObject bodyParam = new JSONObject();
        JSONObject Param = new JSONObject();
        try {
            Param.accumulate("usercd",  SharedPrefFactory.getProfileSharedPreference(getActivity()).getString(BaseSharedPref.USER_CD_KEY));
            Param.accumulate("gstn",gstn);
            Param.accumulate("owner",mNewOwner);
            Param.accumulate("addr",NewAddr);
            Param.accumulate("street",NewStreet);
            Param.accumulate("city",NewCity);
            Param.accumulate("po",NewPo);
            Param.accumulate("pin",NewPin);
            Param.accumulate("mobile",NewMob);
            System.out.println(Param);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        ApiService apiService1 = new ApiService(updateProfile, RequestType.POST,
                ApiEndPoint.UPDATE_PROFILE + Param.toString(), new HashMap<String, String>(), bodyParam);
    }
    OnResponseListener updateProfile = new OnResponseListener()
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
                         JSONObject responseJSON = new JSONObject(response);
                         System.out.println(responseJSON);

                        String loginSuccess =response;

                        if (loginSuccess.equals("Y"))
                        {
                            Toast.makeText(getActivity(), "Profile Update successfully", Toast.LENGTH_LONG);
                            System.out.println("Profile Update successfully");
                            dismiss();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Profile Update unsuccessfully", Toast.LENGTH_LONG).show();
                            System.out.println("Profile Update unsuccessfully");
                            dismiss();
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
