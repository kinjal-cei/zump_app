package com.catch32.zumpbeta.myfragment;

import android.animation.ValueAnimator;
import android.content.Context;
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

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.apicall.ApiService;
import com.catch32.zumpbeta.apicall.OnResponseListener;
import com.catch32.zumpbeta.apicall.RequestType;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.myactivity.Dashboard_Activity;
import com.catch32.zumpbeta.myactivity.Month_Wise_Summary;
import com.catch32.zumpbeta.myactivity.MyShop;
import com.catch32.zumpbeta.myactivity.My_Order_Activity;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.SharedPrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.catch32.zumpbeta.apicall.ApiEndPoint.PRIVACY_POLICY;

public class Fragment_Privacy_Policy extends Fragment
{
    private static final String TAG = Fragment_Privacy_Policy.class.getSimpleName();
    private Context mContext;
    int initialScrollY = 0;
    Button btn_terms_Agree,btn_Terms_decline;
    private TextView mPolicyTxt, mPolicy1Txt, mPolicy2Txt, mPolicy3Txt, mPolicy4Txt, mPolicy5Txt;
    private TextView mPolicy6Txt, mPolicy7Txt, mPolicy8Txt, mPolicy9Txt, mPolicy10Txt, mPolicy11Txt, mPolicy12Txt;
    String userCd,PolicyStatus;
    Handler handler=new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_policy, container, false);

        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        PolicyStatus = sharedPref.getString(BaseSharedPref.USER_POLICY_KEY); //SB2

        btn_terms_Agree=view.findViewById(R.id.btn_terms_Agree);
        btn_Terms_decline=view.findViewById(R.id.btn_Terms_decline);

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

        if(PolicyStatus.equals("N"))
        {
            btn_terms_Agree.setVisibility(View.VISIBLE);
            btn_Terms_decline.setVisibility(View.VISIBLE);
        }

        if(PolicyStatus.equals("Y"))
        {
            btn_terms_Agree.setVisibility(View.GONE);
            btn_Terms_decline.setVisibility(View.GONE);
        }

        String policy = sharedPref.getString(BaseSharedPref.USER_POLICY_KEY, "unknown");
        String policy_dt = sharedPref.getString(BaseSharedPref.USER_POLICY_DT, "unknown");
        String Policy ="Agreed on "+policy_dt;

        if (getActivity() instanceof IBaseMenuActivity)
        {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            if(policy.equals("Y"))
                baseMenuActivity.setActionbarTitle("Privacy Policy (Agreed)");
            else
                baseMenuActivity.setActionbarTitle("Privacy Policy");
            //baseMenuActivity.checkMenuItem(R.id.nav_profile);
        }

        String Policy1 ="Ra Lifecare Pvt, Ltd..(us, we, or our) operates the https://www.zump.co.in website (the Service). This privacy policy governs your use of the software application ZUMP (Application) for mobile devices that was created by ZUMP.This page informs you of our policies regarding the collection, use and disclosure of Personal Information when you use our Service. Registration with us is mandatory in order to be able to use the basic features of the Application. We will not use or share your information with anyone except as described in this Privacy Policy.We use your Personal Information for providing and improving the Service. By using the Service, you agree to the collection and use of information in accordance with this policy. Unless otherwise defined in this Privacy Policy, terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, accessible at https://www.zump.co.in";
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
               "If you have any questions regarding privacy while using the Application, or have questions about our practices, please contact us via email at connect@zump.co.in\n";

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
        if(policy.equals("Y"))
            mPolicyTxt.setText(Policy);

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
            Param.accumulate("usercd",userCd);
            Param.accumulate("state","24");
            System.out.println(Param);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(getActivity());
        ApiService apiService1 = new ApiService(GetPrivacyPolicy, RequestType.POST,
                PRIVACY_POLICY+Param.toString(), new HashMap<String, String>(), bodyParam);

        btn_terms_Agree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                JSONObject bodyParam = new JSONObject();
                JSONObject Param = new JSONObject();
                try {
                    Param.accumulate("usercd", userCd);
                    Param.accumulate("accept", "Y");
                    System.out.println(Param);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                 ApiService apiService1 = new ApiService(PrivacyPolicy, RequestType.POST,
                        PRIVACY_POLICY + Param.toString(), new HashMap<String, String>(), bodyParam);
            }
        });

        btn_Terms_decline.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                JSONObject bodyParam = new JSONObject();
                JSONObject Param = new JSONObject();
                try {
                    Param.accumulate("usercd", userCd);
                    //   Param.accumulate("state", "24");
                    Param.accumulate("accept", "N");
                    System.out.println(Param);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                 ApiService apiService1 = new ApiService(PrivacyPolicy, RequestType.POST,
                        PRIVACY_POLICY + Param.toString(), new HashMap<String, String>(), bodyParam);
            }
        });
        return view;
    }

    OnResponseListener GetPrivacyPolicy = new OnResponseListener()
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

                       String loginSuccess = responseJSON.getString("loginSuccess");

                        if (loginSuccess.equals("Y"))
                        {
                          String TermsText=responseJSON.getString("TermsText");
                          mPolicy12Txt.setText(TermsText);
                        }
                        else
                        {
                            String errorString = responseJSON.getString("obj");
                            Toast.makeText(getActivity(), errorString, Toast.LENGTH_LONG).show();
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

    OnResponseListener PrivacyPolicy = new OnResponseListener()
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

                        String loginSuccess = responseJSON.getString("loginSuccess");

                        if (loginSuccess.equals("Y"))
                        {
                            String TermsText=responseJSON.getString("TermsText");
                            String PolicyagreeFlag=responseJSON.getString("PolicyagreeFlag");

                            if(PolicyagreeFlag.equals("Y"))
                            {
                                Toast.makeText(getActivity(), "You have Agreed", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "You have Declined", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            String errorString = responseJSON.getString("obj");
                            Toast.makeText(getActivity(), errorString, Toast.LENGTH_LONG).show();
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

