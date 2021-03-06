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
import com.catch32.zumpbeta.model.TermsConditions; //SB20191217
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
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class TermsConditionsFragment extends Fragment implements View.OnClickListener, ResponseListener {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private Context mContext;
    private TextView mNameTxt, mGenderTxt, mAddressTxt, mMobileTxt, mEmailTxt;
    private TextView mTerm5Txt, mTerm6Txt, mTerm7Txt, mTerm8Txt, mTerm9Txt, mTerm10Txt, mTerm11Txt, mTerm12Txt, mTerm13Txt, mTerm14Txt,mTerm15Txt, mTerm16Txt, mTerm17Txt;
    private TextView mTerm18Txt, mTerm19Txt, mTerm20Txt, mTerm21Txt, mTerm22Txt, mTerm23Txt, mTerm24Txt,mTerm25Txt,mTerm26Txt,mTermTxt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_terms_conditions, container, false);
        init(view);
        return view;
    }
    private void init(View view)
    {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String TermStatus = sharedPref.getString(BaseSharedPref.USER_TERM_KEY); //SB20191216

        mContext = getActivity();
        view.findViewById(R.id.btn_Terms_decline).setOnClickListener(this);
        if(TermStatus.equals("N"))
            view.findViewById(R.id.btn_terms_Agree).setOnClickListener(this);
        if(TermStatus.equals("Y"))
        {
            view.findViewById(R.id.btn_terms_Agree).setVisibility(View.GONE);
            view.findViewById(R.id.btn_Terms_decline).setVisibility(View.GONE);
        }

        mNameTxt = (TextView) view.findViewById(R.id.txt_name);
        mMobileTxt = (TextView) view.findViewById(R.id.txt_mobile_no);
        mEmailTxt = (TextView) view.findViewById(R.id.txt_email);
        mGenderTxt = (TextView) view.findViewById(R.id.txt_gender);
        mAddressTxt = (TextView) view.findViewById(R.id.txt_area);
        mTerm5Txt = (TextView) view.findViewById(R.id.txt_term5); //SB20191216
        mTerm6Txt = (TextView) view.findViewById(R.id.txt_term6); //SB20191216
        mTerm7Txt = (TextView) view.findViewById(R.id.txt_term7); //SB20191216
        mTerm8Txt = (TextView) view.findViewById(R.id.txt_term8); //SB20191216
        mTerm9Txt = (TextView) view.findViewById(R.id.txt_term9); //SB20191216
        mTerm10Txt = (TextView) view.findViewById(R.id.txt_term10); //SB20191216
        mTerm11Txt = (TextView) view.findViewById(R.id.txt_term11); //SB20191216
        mTerm12Txt = (TextView) view.findViewById(R.id.txt_term12); //SB20191216
        mTerm13Txt = (TextView) view.findViewById(R.id.txt_term13); //SB20191216
        mTerm14Txt = (TextView) view.findViewById(R.id.txt_term14); //SB20191216
        mTerm15Txt = (TextView) view.findViewById(R.id.txt_term15); //SB20191216
        mTerm16Txt = (TextView) view.findViewById(R.id.txt_term16); //SB20191216
        mTerm17Txt = (TextView) view.findViewById(R.id.txt_term17); //SB20191216
        mTerm18Txt = (TextView) view.findViewById(R.id.txt_term18); //SB20191216
        mTerm19Txt = (TextView) view.findViewById(R.id.txt_term19); //SB20191216
        mTerm20Txt = (TextView) view.findViewById(R.id.txt_term20); //SB20191216
        mTerm21Txt = (TextView) view.findViewById(R.id.txt_term21); //SB20191216
        mTerm22Txt = (TextView) view.findViewById(R.id.txt_term22); //SB20191216
        mTerm23Txt = (TextView) view.findViewById(R.id.txt_term23); //SB20191216
        mTerm24Txt = (TextView) view.findViewById(R.id.txt_term24); //SB20191216
        mTerm25Txt = (TextView) view.findViewById(R.id.txt_term25); //SB20191216
        mTerm26Txt = (TextView) view.findViewById(R.id.txt_term26); //SB20191216

        mTermTxt = (TextView) view.findViewById(R.id.txt_term); //SB20191216


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", userCd);
        jsonObject.addProperty("state", "24");
        jsonObject.addProperty("accept", "Y");


        new PostDataToServerTask(mContext, AppConstant.Actions.GET_TERMS_CONDITIONS)
                .setPath(AppConstant.WebURL.GET_TERMS_CONDITIONS)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }

    private void initNavigationView()
    {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String terms = sharedPref.getString(BaseSharedPref.USER_TERM_KEY, "unknown");
        String terms_dt = sharedPref.getString(BaseSharedPref.USER_TERM_DT, "unknown");

        if (getActivity() instanceof IBaseMenuActivity)
        {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            if(terms.equals("Y"))
                baseMenuActivity.setActionbarTitle("Terms & Conditions(Agreed)");
            else
                baseMenuActivity.setActionbarTitle("Terms & Conditions");
         //   baseMenuActivity.checkMenuItem(R.id.nav_profile);
        }
    }

    @Override
    public void onClick(View v)
    {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String userCd = sharedPref.getString(BaseSharedPref.USER_CD_KEY);
        String PolicyStatus = sharedPref.getString(BaseSharedPref.USER_POLICY_KEY); //SB20191216
        switch (v.getId())
        {
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

                new PostDataToServerTask(mContext, AppConstant.Actions.TERMS_CONDITIONS)
                        .setPath(AppConstant.WebURL.TERMS_CONDITIONS)
                        .setResponseListener(this)
                        .setRequestParams(jsonObject)
                        .makeCall();
                ////////////////////////////////////////////////////
                // AppUserManager.logout(mContext, SignInActivity.class);
                /*Intent intent = null;
                intent = new Intent(getActivity(), MainActivity.class);
                mContext.startActivity(intent);
*/
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.drawable.ic_logout_red_48dp);
                builder.setTitle("Terms and Conditions");
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
    public void onResume()
    {
        super.onResume();
        initNavigationView();
    }

    @Override
    public void onResponse(String tag, String response)
    {
        Gson gson = GsonFactory.getInstance();
        TermsConditions TermsConditions = gson.fromJson(response, TermsConditions.class);
        fillTermsDetails();
    }

    private void fillTermsDetails()
    {
        SharedPrefUtil sharedPref = SharedPrefFactory.getProfileSharedPreference(mContext);
        String terms = sharedPref.getString(BaseSharedPref.USER_TERM_KEY, "unknown");
        String terms_dt = sharedPref.getString(BaseSharedPref.USER_TERM_DT, "unknown");
        String Term ="Agreed on "+terms_dt;

        String Term0 ="Please read these Terms and Conditions (\"Terms\", \"Terms and Conditions\") carefully before using the http://www.billpower.in website (the \"Service\") operated by BILLPOWER (\"us\", \"we\", or \"our\"). By installing the Application (as defined below), you agree to be bound by these terms of use (\"app terms\"). Please review them carefully before installation and/or acceptance.\n" +
                "Your access to and use of the Service is conditioned on your acceptance of and compliance with these Terms. These Terms apply to all visitors, users and others who access or use the Service. By accessing or using the Service you agree to be bound by these Terms. If you disagree with any part of the terms then you may not access the Service.\n" +
                "The \"Application\" shall mean the software provided by ??BILLPOWER?? to offer services related to BILLPOWER, BILLPOWER's services and its partners' services, to be used on Android OS devices and any upgrades from time to time and any other software or documentation which enables the use of the Application. Any personal information you supply to BILLPOWER when using the Application will be used by BILLPOWER in accordance with its Privacy Policy.";
        String Term1  ="1. Your Account\n" +
                "If you use the website, you are responsible for maintaining the confidentiality of your account and password and for restricting access to your handheld device to prevent unauthorized access to your account. You agree to accept responsibility for all activities that occur under your account or password. You should take all necessary steps to ensure that the password is kept confidential and secure and should inform us immediately if you have any reason to believe that your password has become known to anyone else, or if the password is being, or is likely to be, used in an unauthorized manner. Please ensure that the details you provide us with are correct and complete and inform us immediately of any changes to the information that you provided when registering. You can access and update much of the information you provided us with in the Your Account area of the website and App. You agree and acknowledge that you will use your account on the website to purchase products only for your business purposes.  BillPower reserves the right to refuse access to the website, terminate accounts, remove or edit content at any time without notice to you.";
        String Term2 ="2. Privacy\n" +
                "Please review our Privacy Notice, which also governs your visit to BillPower, to understand our practices. The personal information / data provided to us by you during the course of usage of BillPower will be treated as strictly confidential and in accordance with the Privacy Notice and applicable laws and regulations. If you object to your information being transferred or used, please do not use the website and related App.";
        String Term3 ="3. E-Platform for Communication\n" +
                "You agree, understand and acknowledge that the website is an online platform that enables you to purchase products listed on the website at the price indicated therein at any time from the vendors linked to your account. You further agree and acknowledge that BILLPOWER is only a facilitator and is not and cannot be a party to or control in any manner any transactions on the website. Accordingly, the contract of sale of products on the website shall be a strictly bipartite contract between you and the sellers on BillPower.";
        String Term4 ="4. Access to BillPower\n" +
                "We will do our utmost to ensure that availability of the website will be uninterrupted and that transmissions will be error-free. However, due to the nature of the Internet, this cannot be guaranteed. Also, your access to the website may also be occasionally suspended or restricted to allow for repairs, maintenance, or the introduction of new facilities or services at any time without prior notice. We will attempt to limit the frequency and duration of any such suspension or restriction.";
        String Term5 ="5. Obligations of BILLPOWER\n" +
                "In the BILLPOWER Application, Customer shall mean not only the Retailers, Vendors, Distributors but also the Wholesaler, Stockist, C&F or anyone accessing the Bill power Application.\n" +
                "The Distributors data, which is personal in nature and specially pertaining to the name of the Distributor, its stores, warehouses, Branches and also indicative figures pertaining to the business that such Distributor conducts, shall not be shared with any third party without the permission of the Distributor management.";
        String Term6 ="6. Obligations of Customer (Vendors, Distributors, C&F, Stockist)\n" +
                "It will be necessary for the Distributor to give consent on sharing its retailers?? data, product data, stock data, stock quantity and other data as mutually agreed between BILLPOWER and the Distributor. This will enable the BILLPOWER Application to work as intended and fulfill the objectives of the Distributor.\n" +
                "It is incumbent on the Distributor to give consent and authorize BILLPOWER to install its Application at the Distributors?? server or other relevant accessible Infrastructure / location (with connectivity to server or relevant database or patch required for BILLPOWER) to sync relevant data as a prerequisite for using the BILLPOWER Application. The Distributor will be required to mandatorily confirm all the fields that can be synced by BILLPOWER on regular basis.\n" +
                "The BILLPOWER business methodology and also access to the Bill power Website or the Application or any other Bill power interface shall not be shared by the Distributor with any third party without taking prior consent from Bill power.";
        String Term7 ="7. License for website access\n" +
                "Subject to your compliance with these Conditions of Use and payment of applicable fees, if any, BILLPOWER Seller Services Private Limited grants you a limited license to access and make personal use of this website, but not to download (other than page caching) or modify it, or any portion of it, except with express written consent of BILLPOWER Seller Services Private Limited and / or its affiliates, as may be applicable. This license does not include any resale or commercial use of this website or its contents; any collection and use of any product listings, descriptions, or prices; any derivative use of this website or its contents; any downloading or copying of account information for the benefit of another seller; or any use of data mining, robots, or similar data gathering and extraction tools.\n" +
                "This website or any portion of this website (including but not limited to any copyrighted material, trademarks, or other proprietary information) may not be reproduced, duplicated, copied, sold, resold, visited, distributed or otherwise exploited for any commercial purpose without express written consent of BILLPOWER Seller Services Private Limited and / or its affiliates, as may be applicable.\n" +
                "You may not frame or use framing techniques to enclose any trademark, logo, or other proprietary information (including images, text, page layout, or form) of BillPower and its affiliates without express written consent. You may not use any meta tags or any other \"hidden text\" utilizing BILLPOWER Seller Services Private Limited's or its affiliates' names or trademarks without the express written consent of BILLPOWER Seller Services Private Limited and / or its affiliates, as applicable. Any unauthorized use terminates the permission or license granted by BILLPOWER Seller Services Private Limited and / or its affiliates, as applicable.\n" +
                "You are granted a limited, revocable, and non-exclusive right to create a hyperlink to the Welcome page of BillPower as long as the link does not portray BILLPOWER Seller Services Private Limited, BillPower, their affiliates, or their products or services in a false, misleading, derogatory, or otherwise offensive matter. You may not use any BillPower logo or other proprietary graphic or trademark as part of the link without express written consent of BILLPOWER Seller Services Private Limited and / or its affiliates, as may be applicable.";
        String Term8 ="8. Your Conduct\n" +
                "You must not use the website in any way that causes, or is likely to cause, the website or access to it to be interrupted, damaged or impaired in any way You understand that you, and not BillPower, are responsible for all electronic communications and content sent from your computer to us and you must use the website for lawful purposes only. You must not use the website for any of the following:\n" +
                "-for fraudulent purposes, or in connection with a criminal offense or other unlawful activity;\n" +
                "-to send, use or reuse any material that does not belong to you; or is illegal, offensive (including but not limited to material that is sexually explicit content or which promotes racism, bigotry, hatred or physical harm), deceptive, misleading, abusive, indecent, harassing, blasphemous, defamatory, libelous, obscene, pornographic, pedophilic or menacing; ethnically objectionable, disparaging or in breach of copyright, trademark, confidentiality, privacy or any other proprietary information or right; or is otherwise injurious to third parties; or relates to or promotes money laundering or gambling; or is harmful to minors in any way; or impersonates another person; or threatens the unity, integrity, security or sovereignty of India or friendly relations with foreign States; or objectionable or otherwise unlawful in any manner whatsoever; or which consists of or contains software viruses, political campaigning, commercial solicitation, chain letters, mass mailings or any \"spam\n" +
                "-to cause annoyance, inconvenience or needless anxiety.";
        String Term9 ="9. Reviews, comments, communications and other content\n" +
                "Users of this website may post reviews, comments and other content; send communications; and submit suggestions, ideas, comments, questions, or other information, as long as the content is not illegal, obscene, abusive, threatening, defamatory, invasive of privacy, infringing of intellectual property rights, or otherwise injurious to third parties, or objectionable and does not consist of or contain software viruses, political campaigning, commercial solicitation, chain letters, mass mailings, or any form of \"spam.\" In the event a user uses a false e-mail address, impersonates any person or entity, or otherwise misleads as to the origin of any content. BillPower reserves the right (but not the obligation) to remove, refuse, delete or edit any content that in the sole judgment of BillPower violates these Conditions of use and, or terminate your permission to access or use this website.\n" +
                "If you do post content or submit material, and unless we indicate otherwise, you\n" +
                "(a) grant BILLPOWER Seller Services Private Limited and its affiliates a non-exclusive, royalty-free, irrevocable, perpetual and fully sub licensable rights to use, reproduce, modify, adapt, publish, translate, create derivative works from, distribute, and display such content throughout the world in any media; and\n" +
                "(b) BILLPOWER Seller Services Private Limited and its affiliates and sub licensees the right to use the name that you submit in connection with such content, if they choose.\n" +
                "You agree that the rights you grant above are irrevocable during the entire period of protection of your intellectual property rights associated with such content and material. You agree to waive your right to be identified as the author of such content and your right to object to derogatory treatment of such content. You agree to perform all further acts necessary to perfect any of the above rights granted by you to BILLPOWER Seller Services Private Limited, including the execution of deeds and documents, at its request.\n" +
                "You represent and warrant that you own or otherwise control all of the rights to the content that you post or that you otherwise provide on or through the website; that, as at the date that the content or material is submitted to BillPower: (i) the content and material is accurate; (ii) use of the content and material you supply does not breach any applicable BillPower policies or guidelines and will not cause injury to any person or entity (including that the content or material is not defamatory); (iii) the content is lawful. You agree to indemnify BILLPOWER Seller Services Private Limited and its affiliates for all claims brought by a third party against it or its affiliates arising out of or in connection with a breach of any of these warranties.";
        String Term10 ="10. Claims against Objectionable Content\n" +
                "Because BillPower lists millions of products for sale offered by sellers on the website and hosts many thousands of comments, it is not possible for us to be aware of the contents of each product listed for sale, or each comment or review that is displayed. Accordingly, BillPower operates on a \"notice and takedown\" basis. If you believe that any content on the website is illegal, offensive (including but not limited to material that is sexually explicit content or which promotes racism, bigotry, hatred or physical harm), deceptive, misleading, abusive, indecent, harassing, blasphemous, defamatory, libelous, obscene, pornographic, pedophilic or menacing; ethnically objectionable, disparaging; or is otherwise injurious to third parties; or relates to or promotes money laundering or gambling; or is harmful to minors in any way; or impersonates another person; or threatens the unity, integrity, security or sovereignty of India or friendly relations with foreign States; or objectionable or otherwise unlawful in any manner whatsoever; or which consists of or contains software viruses, (\" Objectionable Content \"), please notify us immediately by following our Notice and Procedure for Making Claims of Right Infringements. Once this procedure has been followed, BillPower will make all reasonable endeavours to remove such Objectionable Content complained about within a reasonable time.";
        String Term11 ="11. Database rights\n" +
                "All content included on the website, such as text, graphics, logos, button icons, images, audio clips, digital downloads, data compilations, and software, is the property of BILLPOWER Seller Services Private Limited, its affiliates or its content suppliers and is protected by India and international copyright, authors' rights and database right laws. The compilation of all content on this website is the exclusive property of BILLPOWER Seller Services Private Limited and its affiliates and is protected by laws of India and international copyright and database right laws. All software used on this website is the property of BILLPOWER Seller Services Private Limited, its affiliates or its software suppliers and is protected by India and international copyright and author' rights laws.\n" +
                "You may not systematically extract/ or re-utilize parts of the contents of the website without BILLPOWER Seller Services Private Limited and / or its affiliates (as may be applicable) express written consent. In particular, you may not utilize any data mining, robots, or similar data gathering and extraction tools to extract (whether once or many times) for re-utilization of any substantial parts of this website, without BILLPOWER Seller Services Private Limited and / or its affiliates (as may be applicable) express written consent. You may also not create and/ or publish your own database that features substantial (e.g.: prices and product listings) parts of this website without BILLPOWER Seller Services Private Limited and / or its affiliates (as may be applicable) express written consent.";
        String Term12 ="12. Intellectual Property Claims\n" +
                "BILLPOWER Seller Services Private Limited and its affiliates respect the intellectual property of others. If you believe that your intellectual property rights have been used in a way that gives rise to concerns of infringement, please follow our Notice and Procedure for Making Claims of Right Infringements.";
        String Term13 ="13. Trademarks\n" +
                "BILLPOWER, BILLPOWER, THE BILLPOWER LOGO, and other marks indicated on our website are trademarks or registered trademarks of BillPower, Inc. or its subsidiaries (collectively \"BILLPOWER\"), in India Union and/or other jurisdictions. BillPower's graphics, logos, page headers, button icons, scripts and service names are the trademarks of BILLPOWER. BILLPOWER's trademarks may not be used in connection with any product or service that is not BILLPOWER's, in any manner that is likely to cause confusion among customers, or in any manner that disparages or discredits BILLPOWER. All other trademarks not owned by BILLPOWER that appear on this website are the property of their respective owners, who may or may not be affiliated with, connected to, or sponsored by BILLPOWER.";
        String Term14 ="14. Disclaimer\n" +
                "You acknowledge and undertake that you are accessing the services on the website and transacting at your own risk and are using your best and prudent judgment before entering into any transactions through the website. You further acknowledge and undertake that you will use the website to order products only for your business purposes. We shall neither be liable nor responsible for any actions or inactions of sellers nor any breach of conditions, representations or warranties by the sellers or manufacturers of the products and hereby expressly disclaim any and all responsibility and liability in that regard. We shall not mediate or resolve any dispute or disagreement between you and the sellers or manufacturers of the products.\n" +
                "We further expressly disclaim any warranties or representations (express or implied) in respect of quality, suitability, accuracy, reliability, completeness, timeliness, performance, safety, merchantability, fitness for a particular purpose, or legality of the products listed or displayed or transacted or the content (including product or pricing information and/or specifications) on the website. While we have taken precautions to avoid inaccuracies in content, this website, all content, information (including the price of products), software, products, services and related graphics are provided as is, without warranty of any kind. We do not implicitly or explicitly support or endorse the sale or purchase of any products on the website. At no time shall any right, title or interest in the products sold through or displayed on the website vest with BILLPOWER nor shall BILLPOWER have any obligations or liabilities in respect of any transactions on the website.";
        String Term15 ="15. Indemnity and Release\n" +
                "You shall indemnify and hold harmless BILLPOWER Seller Services Private Limited, its subsidiaries, affiliates and their respective officers, directors, agents and employees, from any claim or demand, or actions including reasonable attorney's fees, made by any third party or penalty imposed due to or arising out of your breach of these Conditions of Use or any document incorporated by reference, or your violation of any law, rules, regulations or the rights of a third party.\n" +
                "You hereby expressly release BILLPOWER Seller Services Private Limited and/or its affiliates and/or any of its officers and representatives from any cost, damage, liability or other consequence of any of the actions/inactions of the vendors and specifically waiver any claims or demands that you may have in this behalf under any statute, contract or otherwise.";
        String Term16 ="16. Legal Age\n" +
                "Use of BillPower is available only to persons who can form a legally binding contract under the Indian Contract Act, 1872. If you are a minor i.e. under the age of 18 years, you may use BillPower only with the involvement of a parent or guardian.";
        String Term17 ="17. Communications\n" +
                "When you visit BillPower, you are communicating with us electronically. You will be required to provide a valid phone number while placing an order with us. We may communicate with you by e-mail, SMS, phone call or by posting notices on the website or by any other mode of communication. For contractual purposes, you consent to receive communications (including transactional, promotional and/or commercial messages), from us with respect to your use of the website and/or your order placed on the website.";
        String Term18 ="18. Losses\n" +
                "We will not be responsible for any business loss (including loss of profits, revenue, contracts, anticipated savings, data, goodwill or wasted expenditure) or any other indirect or consequential loss that is not reasonably foreseeable to both you and us when you commenced using the website.";
        String Term19 ="19. Alteration of Service or Amendments to the Conditions\n" +
                "We reserve the right to make changes to our website, policies, and these Conditions of Use at any time. You will be subject to the policies and Conditions of Use in force at the time that you use the website or that you order goods from us, unless any change to those policies or these conditions is required to be made by law or government authority (in which case it will apply to orders previously placed by you). If any of these conditions is deemed invalid, void, or for any reason unenforceable, that condition will be deemed severable and will not affect the validity and enforceability of any remaining condition.";
        String Term20 ="20. Events beyond our reasonable control\n" +
                "We will not be held responsible for any delay or failure to comply with our obligations under these conditions if the delay or failure arises from any cause which is beyond our reasonable control. This condition does not affect your statutory rights.";
        String Term21 ="21. Waiver\n" +
                "If you breach these conditions and we take no action, we will still be entitled to use our rights and remedies in any other situation where you breach these conditions.";
        String Term22 ="22. Governing law and Jurisdiction\n" +
                "These conditions are governed by and construed in accordance with the laws of India. You agree, as we do, to submit to the exclusive jurisdiction of the courts at Ahmedabad.";
        String Term23 ="23. Our Details\n" +
                "This website is operated by BILLPOWER Seller Services Private Limited.\n" +
                "For the BillPower website, you could contact us by visiting: www.billpower.in";
        String Term24 ="24. BILLPOWER Software Terms\n" +
                "In addition to these Conditions of Use, the terms found here apply to any software (including any updates or upgrades to the software and any related documentation) that we make available to you from time to time for your use in connection with BILLPOWER Services (the \"BILLPOWER Software\").";
        String Term25 ="25. Availability\n" +
                "This Application is available to handheld mobile devices running Android OS Operating Systems. BILLPOWER will use reasonable efforts to make the Application available at all times. However you acknowledge the Application is provided over the internet and mobile networks and so the quality and availability of the Application may be affected by factors outside BILLPOWER??s reasonable control.\n" +
                "BILLPOWER, its group of companies and sub-contractors do not accept any responsibility whatsoever for unavailability of the Application, or any difficulty or inability to download or access content or any other communication system failure which may result in the Application being unavailable.\n" +
                "BILLPOWER will not be responsible for any support or maintenance for the Application.";
        String Term26 ="26. System Requirements\n" +
                "In order to use the Application, you are required to have a compatible mobile telephone or handheld device, internet access, and the necessary minimum specifications ('Software Requirements').\n" +
                "The Software Requirements are as follows: Android OS devices running Android OS 4.0.2 onwards; Language: English.\n" +
                "The version of the Application software may be upgraded from time to time to add support for new functions and services.";
        //Term18 =Term18+"\n"+Term19+"\n"+Term20+"\n"+Term21+"\n"+Term22+"\n"+Term23+"\n"+Term24;
        mNameTxt.setText(Term0);
        mGenderTxt.setText(Term1);
        mAddressTxt.setText(Term2);
        mEmailTxt.setText(Term3);
        mMobileTxt.setText(Term4);
        mTerm5Txt.setText(Term5);
        mTerm6Txt.setText(Term6);
        mTerm7Txt.setText(Term7);
        mTerm8Txt.setText(Term8);
        mTerm9Txt.setText(Term9);
        mTerm10Txt.setText(Term10);
        mTerm11Txt.setText(Term11);
        mTerm12Txt.setText(Term12);
        mTerm13Txt.setText(Term13);
        mTerm14Txt.setText(Term14);
        mTerm15Txt.setText(Term15);
        mTerm16Txt.setText(Term16);
        mTerm17Txt.setText(Term17);
        mTerm18Txt.setText(Term18);
        mTerm19Txt.setText(Term19);
        mTerm20Txt.setText(Term20);
        mTerm21Txt.setText(Term21);
        mTerm22Txt.setText(Term22);
        mTerm23Txt.setText(Term23);
        mTerm24Txt.setText(Term24);
        mTerm25Txt.setText(Term25);
        mTerm26Txt.setText(Term26);
        if(terms.equals("Y"))
            mTermTxt.setText(Term);
    }

    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }
}

