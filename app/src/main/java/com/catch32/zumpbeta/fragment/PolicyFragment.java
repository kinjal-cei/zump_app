package com.catch32.zumpbeta.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.SignInActivity;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.Policy;
//import com.catch32.rms.model.PolicyData;
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
import androidx.fragment.app.Fragment;

/**
 * @author SB
 * @version Dec 11, 2019
 */
public class PolicyFragment extends Fragment implements View.OnClickListener, ResponseListener {

    private static final String TAG = PolicyFragment.class.getSimpleName();
    private Context mContext;
    int initialScrollY = 0;

    private TextView mPolicyTxt, mPolicy1Txt, mPolicy2Txt, mPolicy3Txt, mPolicy4Txt, mPolicy5Txt;
    private TextView mPolicy6Txt, mPolicy7Txt, mPolicy8Txt, mPolicy9Txt, mPolicy10Txt, mPolicy11Txt, mPolicy12Txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_policy, container, false);

        init(view);
        return view;
    }

    private void init(View view)
    {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String PolicyStatus = sharedPref.getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216

        mContext = getActivity();
        view.findViewById(R.id.btn_Terms_decline).setOnClickListener(this);
        if(PolicyStatus.equals("N"))
            view.findViewById(R.id.btn_terms_Agree).setOnClickListener(this);
        if(PolicyStatus.equals("Y"))
        {
            view.findViewById(R.id.btn_terms_Agree).setVisibility(View.GONE);
            view.findViewById(R.id.btn_Terms_decline).setVisibility(View.GONE);
        }

        mPolicy1Txt = (TextView) view.findViewById(R.id.txt_policy1);
        mPolicy2Txt = (TextView) view.findViewById(R.id.txt_policy2);
        mPolicy3Txt = (TextView) view.findViewById(R.id.txt_policy3);
        mPolicy4Txt = (TextView) view.findViewById(R.id.txt_policy4);
        mPolicy5Txt = (TextView) view.findViewById(R.id.txt_policy5);

        mPolicy6Txt = (TextView) view.findViewById(R.id.txt_policy6);
        mPolicy7Txt = (TextView) view.findViewById(R.id.txt_policy7);
        mPolicy8Txt = (TextView) view.findViewById(R.id.txt_policy8);
        mPolicy9Txt = (TextView) view.findViewById(R.id.txt_policy9);
        mPolicy10Txt = (TextView) view.findViewById(R.id.txt_policy10);
        mPolicy11Txt = (TextView) view.findViewById(R.id.txt_policy11);
        mPolicy12Txt = (TextView) view.findViewById(R.id.txt_policy12);
        mPolicyTxt = (TextView) view.findViewById(R.id.txt_policy);



        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", userCd);
        jsonObject.addProperty("state", "24");
        jsonObject.addProperty("accept", "Y");


        new PostDataToServerTask(mContext, AppConstant.Actions.GET_PRIVACY_POLICY)
                .setPath(AppConstant.WebURL.GET_PRIVACY_POLICY)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();

    }

    private void initNavigationView()
    {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String terms = sharedPref.getString(BaseSharedPref.USER_POLICY_KEY, "unknown");
        String terms_dt = sharedPref.getString(BaseSharedPref.USER_POLICY_DT, "unknown");

        if (getActivity() instanceof IBaseMenuActivity)
        {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            if(terms.equals("Y"))
                baseMenuActivity.setActionbarTitle("Privacy Policy (Agreed)");
            else
                baseMenuActivity.setActionbarTitle("Privacy Policy");
            baseMenuActivity.checkMenuItem(R.id.nav_profile);
        }
    }

    @Override
    public void onClick(View v)
    {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String PolicyStatus = sharedPref.getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
        switch (v.getId()) {
            case R.id.btn_Terms_decline:
              //  ChangePasswordUpdateDialog dialog = new ChangePasswordUpdateDialog();
              //  dialog.show(getChildFragmentManager(), "Cancel");
                AppUserManager.logout(mContext, SignInActivity.class);
                break;

            case R.id.btn_terms_Agree:
                        ////////Update Status as Agreed///////////////////
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("usercd", userCd);
                        jsonObject.addProperty("state", "24");
                        jsonObject.addProperty("accept", "Y");

                        new PostDataToServerTask(mContext, AppConstant.Actions.PRIVACY_POLICY)
                                .setPath(AppConstant.WebURL.PRIVACY_POLICY)
                                .setResponseListener(this)
                                .setRequestParams(jsonObject)
                                .makeCall();
                        ////////////////////////////////////////////////////
                      //  AppUserManager.logout(mContext, SignInActivity.class);
                     /*   Intent intent = null;
                        intent = new Intent(getActivity(), MainActivity.class);
                        mContext.startActivity(intent);*/

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                //    builder.setIcon(R.drawable.ic_logout_red_48dp);
                builder.setTitle("Privacy Policy");
                builder.setMessage("You have Agreed.\nPlease Logout and Login again");
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
    public void onResponse(String tag, String response) {
        Gson gson = GsonFactory.getInstance();
        Policy policy = gson.fromJson(response, Policy.class);


      //  ProfileData profileData = profile.getProfileData();

      //  SharedPrefUtil sharedPrefUtil = SharedPrefFactory.getProfileSharedPreference(mContext);

      //  sharedPrefUtil.putString(BaseSharedPref.USER_TERM_KEY, profileData.getMobileNo()); //SB20191216

        fillPolicyDetails();


    }

    private void fillPolicyDetails() {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);

        String policy = sharedPref.getString(BaseSharedPref.USER_POLICY_KEY, "unknown");
        String policy_dt = sharedPref.getString(BaseSharedPref.USER_POLICY_DT, "unknown");
        String Policy ="Agreed on "+policy_dt;

        String Policy1 ="Bill Power(us, we, or our) operates the http://www.billpower.in website (the Service). This privacy policy governs your use of the software application BillPower (Application) for mobile devices that was created by Bill Power.This page informs you of our policies regarding the collection, use and disclosure of Personal Information when you use our Service. Registration with us is mandatory in order to be able to use the basic features of the Application. We will not use or share your information with anyone except as described in this Privacy Policy.We use your Personal Information for providing and improving the Service. By using the Service, you agree to the collection and use of information in accordance with this policy. Unless otherwise defined in this Privacy Policy, terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, accessible at http://www.billpower.in";
        String Policy2 ="Information Collection and Use\n" +
                "While using our Service, we may ask you to provide us with certain personally identifiable information that can be used to contact or identify you. Personally identifiable information may include, but is not limited to, your name, phone number, postal address and other information (Personal Information). When you register with us and use the Application, you generally provide (a) your name, email address, age, user name, password and other registration information; (b) transaction-related information, such as when you make purchases, respond to any offers, or download or use applications from us; (c) information you provide us when you contact us for help; (d) credit card information for purchase and use of the Application, and; (e) information you enter into our system when using the Application, such as contact information and project management information";
        String Policy3 ="Log Data\n" +
                "In addition, the Application may collect certain information automatically, including, but not limited to, the type of mobile device you use, your mobile devices unique device ID, the IP address of your mobile device, your mobile operating system, the type of mobile Internet browsers you use, and information about the way you use the Application. This Application does not collect precise information about the location of your mobile device";
        String Policy4 ="Cookies\n" +
                "Cookies are files with small amount of data, which may include an anonymous unique identifier. Cookies are sent to your browser from a web site and stored on your computer's hard drive. We use cookies to collect information. You can instruct your browser to refuse all cookies or to indicate when a cookie is being sent. However, if you do not accept cookies, you may not be able to use some portions of our Service.";
        String Policy5 ="Service Providers\n" +
                "We may employ third party companies and individuals to facilitate our Service, to provide the Service on our behalf, to perform Service-related services or to assist us in analyzing how our Service is used. These third parties have access to your Personal Information only to perform these tasks on our behalf and are obligated not to disclose or use it for any other purpose.\n\n" +
                "Only aggregated, anonymized data is periodically transmitted to external services to help us improve the Application and our service. We will share your information with third parties only in the ways that are described in this privacy statement.\n\n";
        String Policy6 ="We may disclose User Provided and Automatically Collected Information:\n" +
                "\n(a) as required by law, such as to comply with a subpoena, or similar legal process; \n\n(b) when we believe in good faith that disclosure is necessary to protect our rights, protect your safety or the safety of others, investigate fraud, or respond to a government request; \n\n(c) with our trusted services providers who work on our behalf, do not have an independent use of the information we disclose to them, and have agreed to adhere to the rules set forth in this privacy statement. \n\n(d) if Bill Power is involved in a merger, acquisition, or sale of all or a portion of its assets, you will be notified via email and/or a prominent notice on our Web site of any change in ownership or uses of this information, as well as any choices you may have regarding this information.\n";
        String Policy7 ="Data Retention Policy, Managing Your Information\n" +
                "We will retain User Provided data for as long as you use the Application and for a reasonable time thereafter. We will retain Automatically Collected information for up to 24 months and thereafter may store it in aggregate. If you would like us to delete User Provided Data that you have provided via the Application, please contact us at info@billpower.in and we will respond in a reasonable time. Please note that some or all of the User Provided Data may be required in order for the Application to function properly. You further authorize us to share the secondary sales and stock data with the respective company and on such company making a written request for the said data. The data so disclosed will be at a consolidated level, and shall not personally identify any of your customer\n";
        String Policy8 ="Links To Other Sites\n" +
                "Our Service may contain links to other sites that are not operated by us. If you click on a third party link, you will be directed to that third party's site. We strongly advise you to review the Privacy Policy of every site you visit. We have no control over, and assume no responsibility for the content, privacy policies or practices of any third party sites or services.\n";
        String Policy9 ="Security\n" +
                "We are concerned about safeguarding the confidentiality of your information. We provide physical, electronic, and procedural safeguards to protect information we process and maintain. For example, we limit access to this information to authorized employees and contractors who need to know that information in order to operate, develop or improve our Application. Please be aware that, although we endeavor provide reasonable security for information we process and maintain, no security system can prevent all potential security breaches.\n";
        String Policy10 ="Changes to This Privacy Policy\n" +
                "We may update our Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page and informing you via email or text message. You are advised to review this Privacy Policy periodically for any changes, as continued use is deemed approval of all changes. Changes to this Privacy Policy are effective when they are posted on this page.\n";
        String Policy11 ="Your Consent\n" +
                "By using the Application, you are consenting to our processing of your information as set forth in this Privacy Policy now and as amended by us. Processing, means using cookies on a computer/hand held device or using or touching information in any way, including, but not limited to, collecting, storing, deleting, using, combining and disclosing information, all of which activities will take place in India. We shall not disclose the users data with any third parties, except to the extent provided under this privacy policy, without your prior written consent.\n";
        String Policy12 ="Contact Us:\n" +
                "If you have any questions regarding privacy while using the Application, or have questions about our practices, please contact us via email at info@billpower.in\n";

        mPolicy1Txt.setText(Policy1);
        mPolicy2Txt.setText(Policy2);
        mPolicy3Txt.setText(Policy3);
        mPolicy4Txt.setText(Policy4);
        mPolicy5Txt.setText(Policy5);
        mPolicy6Txt.setText(Policy6);
        mPolicy7Txt.setText(Policy7);
        mPolicy8Txt.setText(Policy8);
        mPolicy9Txt.setText(Policy9);
        mPolicy10Txt.setText(Policy10);
        mPolicy11Txt.setText(Policy11);
        mPolicy12Txt.setText(Policy12);
        if(policy.equals("Y"))
            mPolicyTxt.setText(Policy);
    }

    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }
}

