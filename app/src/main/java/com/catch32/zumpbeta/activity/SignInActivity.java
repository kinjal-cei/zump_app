package com.catch32.zumpbeta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.apicall.ApiService;
import com.catch32.zumpbeta.apicall.OnResponseListener;
import com.catch32.zumpbeta.apicall.RequestType;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.model.Login;
import com.catch32.zumpbeta.myactivity.Dashboard_Activity;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.utils.KeyboardHelper;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.catch32.zumpbeta.apicall.ApiEndPoint.LOGIN;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener
{
    private final String TAG = SignInActivity.class.getSimpleName();
    private TextInputEditText mUsernameEt, mPasswordEt;
    private TextInputLayout mUsernameTIL, mPasswordTIL;
    private String mUsername, mPassword;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView()
    {
        setTitle(getString(R.string.app_name));

        mUsernameEt = (TextInputEditText) findViewById(R.id.et_mobile);
        mPasswordEt = (TextInputEditText) findViewById(R.id.et_password);

        mPasswordEt.setOnEditorActionListener(this);

        mUsernameTIL = (TextInputLayout) findViewById(R.id.til_mobile);
        mPasswordTIL = (TextInputLayout) findViewById(R.id.til_password);

        findViewById(R.id.btn_forgot_password).setOnClickListener(this);
        findViewById(R.id.btn_new_regd).setOnClickListener(this); //SB20200102
        findViewById(R.id.btn_sign_in).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_forgot_password:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.btn_new_regd:
                startActivity(new Intent(this, UnregdUserActivity.class));
                break;
            case R.id.btn_sign_in:
                signIn();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
    {
        if (actionId == EditorInfo.IME_ACTION_DONE)
        {
            signIn();
        }
        return false;
    }


    private boolean validate()
    {
        boolean isValidUserName = true;
        boolean isValidPassword = true;

        if (TextUtils.isEmpty(mUsername))
        {
            WidgetUtil.showTextInputLayoutError(mUsernameTIL, "Please enter your mobile number");
            isValidUserName = false;
        }

        if (TextUtils.isEmpty(mPassword))
        {
            WidgetUtil.showTextInputLayoutError(mPasswordTIL, "Please enter your password");
            isValidPassword = false;
        }

        if (isValidUserName) WidgetUtil.hideTextInputLayoutError(mUsernameTIL);
        if (isValidPassword) WidgetUtil.hideTextInputLayoutError(mPasswordTIL);

        return isValidUserName && isValidPassword;
    }


    private void signIn()
    {
        KeyboardHelper.hideSoftKeyboard(SignInActivity.this);

        mUsername = mUsernameEt.getText().toString().trim();
        mPassword = mPasswordEt.getText().toString().trim();

        JSONObject bodyParam = new JSONObject();
        JSONObject Param = new JSONObject();
        try {
            Param.accumulate("uname",  mUsername);
            Param.accumulate("upass", mPassword);
            Param.accumulate("state", "24");
            System.out.println(Param);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        ApiService apiService1 = new ApiService(Login_User, RequestType.POST,
                LOGIN + Param.toString(), new HashMap<String, String>(), bodyParam);
        if (!validate()) return;
    }

    OnResponseListener Login_User = new OnResponseListener()
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
                        Gson gson = GsonFactory.getInstance();
                        Login login = gson.fromJson(response, Login.class);
                        System.out.println(response);
                        try {
                            if (login.getLoginSuccess() != null && login.getLoginSuccess().equals("Y"))
                            {
                                SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(SignInActivity.this);
                                sharedPrefUtil.putBoolean(BaseSharedPref.LOGIN_STATUS_KEY, true);
                                sharedPrefUtil.putInt(BaseSharedPref.LOGIN_TYPE_KEY, login.getLogin_type());
                                sharedPrefUtil.putString(BaseSharedPref.USER_CD_KEY, login.getUsercd());
                                sharedPrefUtil.putString(BaseSharedPref.EMP_NAME_KEY, login.getEmpName());
                                sharedPrefUtil.putString(BaseSharedPref.MOBILE_NUMBER_KEY, login.getMobileNo());
                                sharedPrefUtil.putString(BaseSharedPref.ALIAS_KEY, login.getALIAS());
                                sharedPrefUtil.putString(BaseSharedPref.SUPER_FLAG_KEY, login.getSuperflag());
                                sharedPrefUtil.putString(BaseSharedPref.USER_TERM_KEY, login.getAgreeFlag()); //SB20191216
                                sharedPrefUtil.putString(BaseSharedPref.USER_TERM_DT, login.getAgreeDt()); //SB20191217
                                sharedPrefUtil.putString(BaseSharedPref.USER_POLICY_KEY, login.getPolicyAgreeFlag()); //SB20191216
                                sharedPrefUtil.putString(BaseSharedPref.USER_POLICY_DT, login.getPolicyAgreeDt()); //SB20191217
                                sharedPrefUtil.putString(BaseSharedPref.LAST_LOGIN, login.getLast_login()); //SB20200608
                                sharedPrefUtil.putString(BaseSharedPref.LAST_LOGIN_MSG, login.getLast_login_msg()); //SB20200608
                                sharedPrefUtil.putString(BaseSharedPref.UNIQUE_KEY, login.getUnique_key()); //SB20200608
                                sharedPrefUtil.putString(BaseSharedPref.USER_CD_KEY, login.getUsercd()); //SB20200608

                                startActivity(new Intent(SignInActivity.this, Dashboard_Activity.class));
                                finish();
                            }
                            else if (login.getLoginSuccess() != null && login.getLoginSuccess().equals("X"))
                            {
                                SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(SignInActivity.this);
                                sharedPrefUtil.putBoolean(BaseSharedPref.LOGIN_STATUS_KEY, true);
                                sharedPrefUtil.putInt(BaseSharedPref.LOGIN_TYPE_KEY, login.getLogin_type());
                                //  sharedPrefUtil.putString(BaseSharedPref.USER_CD_KEY, "300001");
                                sharedPrefUtil.putString(BaseSharedPref.USER_CD_KEY, "000001");
                                sharedPrefUtil.putString(BaseSharedPref.EMP_NAME_KEY, "New User");
                                sharedPrefUtil.putString(BaseSharedPref.MOBILE_NUMBER_KEY, login.getMobileNo());
                                sharedPrefUtil.putString(BaseSharedPref.ALIAS_KEY, login.getALIAS());
                                sharedPrefUtil.putString(BaseSharedPref.SUPER_FLAG_KEY, login.getSuperflag());
                                sharedPrefUtil.putString(BaseSharedPref.USER_TERM_KEY, login.getAgreeFlag()); //SB20191216
                                sharedPrefUtil.putString(BaseSharedPref.USER_TERM_DT, login.getAgreeDt()); //SB20191217
                                sharedPrefUtil.putString(BaseSharedPref.USER_POLICY_KEY, login.getPolicyAgreeFlag()); //SB20191216
                                sharedPrefUtil.putString(BaseSharedPref.USER_POLICY_DT, login.getPolicyAgreeDt()); //SB20191217
                                sharedPrefUtil.putString(BaseSharedPref.LAST_LOGIN, login.getLast_login()); //SB20200608
                                sharedPrefUtil.putString(BaseSharedPref.LAST_LOGIN_MSG, login.getLast_login_msg()); //SB20200608
                                sharedPrefUtil.putString(BaseSharedPref.UNIQUE_KEY, login.getUnique_key()); //SB20200608
                            }
                            else {

                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }});
        }
        @Override
        public void onError(String error)
        {
            WidgetUtil.showErrorToast(SignInActivity.this, error);
        }
    };

    /*  JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uname", mUsername);
        jsonObject.addProperty("upass", mPassword);
        jsonObject.addProperty("state", "24");


        new PostDataToServerTask(SignInActivity.this, AppConstant.Actions.LOGIN)
                .setPath(ApiEndPoint.WebURL.LOGIN_PATH)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();*/
  /* @Override
    public void onResponse(String tag, String response)
    {
        Gson gson = GsonFactory.getInstance();
        Login login = gson.fromJson(response, Login.class);

        if (login.getLoginSuccess() != null && login.getLoginSuccess().equals("Y"))
        {
            SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(SignInActivity.this);
            sharedPrefUtil.putBoolean(BaseSharedPref.LOGIN_STATUS_KEY, true);
            sharedPrefUtil.putInt(BaseSharedPref.LOGIN_TYPE_KEY, login.getLogin_type());
            sharedPrefUtil.putString(BaseSharedPref.USER_CD_KEY, login.getUsercd());
            sharedPrefUtil.putString(BaseSharedPref.EMP_NAME_KEY, login.getEmpName());
            sharedPrefUtil.putString(BaseSharedPref.MOBILE_NUMBER_KEY, login.getMobileNo());
            sharedPrefUtil.putString(BaseSharedPref.ALIAS_KEY, login.getALIAS());
            sharedPrefUtil.putString(BaseSharedPref.SUPER_FLAG_KEY, login.getSuperflag());
            sharedPrefUtil.putString(BaseSharedPref.USER_TERM_KEY, login.getAgreeFlag()); //SB20191216
            sharedPrefUtil.putString(BaseSharedPref.USER_TERM_DT, login.getAgreeDt()); //SB20191217
            sharedPrefUtil.putString(BaseSharedPref.USER_POLICY_KEY, login.getPolicyAgreeFlag()); //SB20191216
            sharedPrefUtil.putString(BaseSharedPref.USER_POLICY_DT, login.getPolicyAgreeDt()); //SB20191217
            sharedPrefUtil.putString(BaseSharedPref.LAST_LOGIN, login.getLast_login()); //SB20200608
            sharedPrefUtil.putString(BaseSharedPref.LAST_LOGIN_MSG, login.getLast_login_msg()); //SB20200608
            sharedPrefUtil.putString(BaseSharedPref.UNIQUE_KEY, login.getUnique_key()); //SB20200608
            sharedPrefUtil.putString(BaseSharedPref.USER_CD_KEY, login.getUsercd()); //SB20200608

            startActivity(new Intent(this, Dashboard_Activity.class));
            finish();
        }
        else if (login.getLoginSuccess() != null && login.getLoginSuccess().equals("X"))
        {
            SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(SignInActivity.this);
            sharedPrefUtil.putBoolean(BaseSharedPref.LOGIN_STATUS_KEY, true);
            sharedPrefUtil.putInt(BaseSharedPref.LOGIN_TYPE_KEY, login.getLogin_type());
          //  sharedPrefUtil.putString(BaseSharedPref.USER_CD_KEY, "300001");
            sharedPrefUtil.putString(BaseSharedPref.USER_CD_KEY, "000001");
            sharedPrefUtil.putString(BaseSharedPref.EMP_NAME_KEY, "New User");
            sharedPrefUtil.putString(BaseSharedPref.MOBILE_NUMBER_KEY, login.getMobileNo());
            sharedPrefUtil.putString(BaseSharedPref.ALIAS_KEY, login.getALIAS());
            sharedPrefUtil.putString(BaseSharedPref.SUPER_FLAG_KEY, login.getSuperflag());
            sharedPrefUtil.putString(BaseSharedPref.USER_TERM_KEY, login.getAgreeFlag()); //SB20191216
            sharedPrefUtil.putString(BaseSharedPref.USER_TERM_DT, login.getAgreeDt()); //SB20191217
            sharedPrefUtil.putString(BaseSharedPref.USER_POLICY_KEY, login.getPolicyAgreeFlag()); //SB20191216
            sharedPrefUtil.putString(BaseSharedPref.USER_POLICY_DT, login.getPolicyAgreeDt()); //SB20191217
            sharedPrefUtil.putString(BaseSharedPref.LAST_LOGIN, login.getLast_login()); //SB20200608
            sharedPrefUtil.putString(BaseSharedPref.LAST_LOGIN_MSG, login.getLast_login_msg()); //SB20200608
            sharedPrefUtil.putString(BaseSharedPref.UNIQUE_KEY, login.getUnique_key()); //SB20200608

          /*  if (login.getAgreeFlag().equals("N")) //SB20191216
            {
                sharedPrefUtil.putString(BaseSharedPref.USER_TERM_KEY, "First Time User"); //SB20191216
                startActivity(new Intent(this, MainActivity.class));
            }
            else*/
         /*   startActivity(new Intent(this, UnregdUserActivity.class));
            finish();
        }
        else {
            WidgetUtil.showErrorToast(this, login.getData());
        }
    }*/

}

