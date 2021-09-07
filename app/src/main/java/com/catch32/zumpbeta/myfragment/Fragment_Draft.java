package com.catch32.zumpbeta.myfragment;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
import com.catch32.zumpbeta.utils.SharedPrefFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.catch32.zumpbeta.apicall.ApiEndPoint.GET_DRAFT_ORDER_LIST;
import static com.catch32.zumpbeta.apicall.ApiEndPoint.GET_MENU_DATA;

public class Fragment_Draft extends Fragment
{
    private Activity mContext;
    private Spinner mVendorSpinner;
    private Spinner mStatusSpinner;
    private Spinner mCompanySpinner;
    String menuId = "2";
    int initialScrollY = 0;
    int selectedVendorId,selectedCompanyId;
    String selectedStatus;
    Handler handler=new Handler();
    ArrayList<String> listOfCompanies =new ArrayList<>();
    ArrayList<String> listOfCompaniesId =new ArrayList<String>();
    ArrayList<String> listOfStatus =new ArrayList<>();
    ArrayList<String> listOfVendors =new ArrayList<>();
    ArrayList<String> listOfVendorsId =new ArrayList<String>();
    LinearLayout empty_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_draft_for_zump, container, false);

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

        init(view);
        return view;
    }
    private void init(View view)
    {
        mContext = getActivity();
        empty_view=view.findViewById(R.id.empty_view);

        TextView info = view.findViewById(R.id.header_path);
        info.setText("["+ SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home >Draft Orders");

        mVendorSpinner = view.findViewById(R.id.spinner_vendor);
        mStatusSpinner = view.findViewById(R.id.spinner_status);
        mCompanySpinner = view.findViewById(R.id.spinner_company);

        JSONObject bodyParam = new JSONObject();
        JSONObject Param = new JSONObject();
        try {
            Param.accumulate("usercd",SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
            Param.accumulate("menuId",menuId);
            Param.accumulate("statusId",selectedStatus);
            Param.accumulate("compId",selectedCompanyId);
            Param.accumulate("spinnerId",selectedVendorId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        System.out.println(Param);
        AndroidNetworking.initialize(getActivity().getApplicationContext());

        ApiService apiService1 = new ApiService(getMenuData, RequestType.POST,
                GET_MENU_DATA+Param.toString(), new HashMap<String, String>(),bodyParam);
    }

    OnResponseListener getMenuData = new OnResponseListener()
    {
        @Override
        public void onSuccess(String response1, HashMap<String, String> headers)
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
                        empty_view.setVisibility(View.GONE);

                        if (loginSuccess.equals("Y"))
                            {
                            int len;
                            //Vendor List
                            final JSONArray VendorListArray = responseJSON.getJSONArray("categoryListReport");
                            len = VendorListArray.length();

                            listOfVendors = new ArrayList<>();
                            listOfVendorsId = new ArrayList<String>();

                            for (int j = 0; j < len; j++)
                            {
                                JSONObject category = (JSONObject) VendorListArray.get(j);

                                final String cat_nm = category.getString("cat_nm");
                                final String cat_id = category.getString("cat_id");

                                listOfVendors.add(cat_nm);
                                listOfVendorsId.add(cat_id);
                            }
                            ArrayAdapter<String> adapterVendor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listOfVendors);
                            mVendorSpinner.setAdapter(adapterVendor);

                            mVendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {
                                    if (true)
                                    {
                                        try {
                                            String VendorId = ((JSONObject) VendorListArray.get(mVendorSpinner.getSelectedItemPosition())).getString("cat_id");
                                            selectedVendorId=Integer.parseInt(VendorId);
                                            try {
                                                JSONObject bodyParam = new JSONObject();
                                                JSONObject Param = new JSONObject();
                                                try {
                                                    Param.accumulate("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
                                                    Param.accumulate("menuId", "2");
                                                    Param.accumulate("selectedDropId", selectedVendorId);
                                                    Param.accumulate("selectedStatus", selectedStatus);
                                                    Param.accumulate("selectedCompId", selectedCompanyId);
                                                    Param.accumulate("extraSpinnerId",0);
                                                    Param.accumulate("logintyp",0);
                                                } catch (JSONException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                System.out.println(Param);
                                                ApiService apiService1 = new ApiService(getData, RequestType.GET,
                                                        GET_DRAFT_ORDER_LIST + Param.toString(), new HashMap<String, String>(), bodyParam);
                                            } catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        } catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {
                                }
                            });
                            //StatusList
                            final JSONArray statusListArray = responseJSON.getJSONArray("statusList");
                            len = statusListArray.length();

                            listOfStatus = new ArrayList<>();

                            for (int j = 0; j < len; j++)
                            {

                                JSONObject company = (JSONObject) statusListArray.get(j);

                                final String statusName = company.getString("statusName");

                                listOfStatus.add(statusName);
                            }

                            ArrayAdapter<String> adapterStatus= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listOfStatus);
                            mStatusSpinner.setAdapter(adapterStatus);

                            mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {
                                    if (true)
                                    {
                                            try {
                                                selectedStatus=mStatusSpinner.getSelectedItemPosition()+"";
                                                System.out.println(selectedStatus);
                                                JSONObject bodyParam = new JSONObject();
                                                JSONObject Param = new JSONObject();
                                                try {
                                                    Param.accumulate("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
                                                    Param.accumulate("menuId", "2");
                                                    Param.accumulate("selectedDropId", selectedVendorId);
                                                    Param.accumulate("selectedStatus", selectedStatus);
                                                    Param.accumulate("selectedCompId", selectedCompanyId);
                                                    Param.accumulate("extraSpinnerId",0);
                                                    Param.accumulate("logintyp",0);
                                                }
                                                catch (JSONException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                System.out.println(Param);
                                                ApiService apiService1 = new ApiService(getData, RequestType.GET,
                                                        GET_DRAFT_ORDER_LIST + Param.toString(), new HashMap<String, String>(), bodyParam);
                                            } catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {

                                }
                            });

                            //CompanyList
                            final JSONArray compListArray = responseJSON.getJSONArray("compList");
                            len = compListArray.length();

                            listOfCompanies = new ArrayList<>();
                            listOfCompaniesId = new ArrayList<String>();

                            for (int j = 0; j < len; j++)
                            {
                                JSONObject company = (JSONObject) compListArray.get(j);
                                final String comp_nm = company.getString("comp_nm");
                                final String comp_id = company.getString("comp_id");

                                listOfCompanies.add(comp_nm);
                                listOfCompaniesId.add(comp_id);
                            }

                            ArrayAdapter<String> adapterCompany = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listOfCompanies);
                            mCompanySpinner.setAdapter(adapterCompany);

                            mCompanySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {
                                    if (true)
                                    {
                                        try {
                                            String CompanyId = ((JSONObject) compListArray.get(mCompanySpinner.getSelectedItemPosition())).getString("comp_id");
                                            selectedCompanyId=Integer.parseInt(CompanyId);
                                            JSONObject bodyParam = new JSONObject();
                                            JSONObject Param = new JSONObject();
                                            try {
                                                Param.accumulate("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
                                                Param.accumulate("menuId", "2");
                                                Param.accumulate("selectedDropId", selectedVendorId);
                                                Param.accumulate("selectedStatus", selectedStatus);
                                                Param.accumulate("selectedCompId", selectedCompanyId);
                                                Param.accumulate("extraSpinnerId",0);
                                                Param.accumulate("logintyp",0);
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }
                                            System.out.println(Param);
                                            ApiService apiService1 = new ApiService(getData, RequestType.GET,
                                                    GET_DRAFT_ORDER_LIST + Param.toString(), new HashMap<String, String>(), bodyParam);
                                        } catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {
                                }
                            });
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
        }
    };

    OnResponseListener getData = new OnResponseListener()
    {
        @Override
        public void onSuccess(String response1, HashMap<String, String> headers)
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
                            int len;
                            //Status List
                            final JSONArray categoryReportData = responseJSON.getJSONArray("categoryReportData");
                            len = categoryReportData.length();

                                RelativeLayout nv = (RelativeLayout) getActivity().findViewById(R.id.rl_DraftOrder);
                                nv.removeAllViews();
                                for (int j = 0; j < len; j++)
                                {

                                    JSONObject poData = (JSONObject) categoryReportData.get(j);
                                    String CM_NO = poData.getString("CM_NO");
                                    String CM_NAME = poData.getString("CM_NAME");
                                    String CM_ROOM_NO = poData.getString("CM_ROOM_NO");
                                    String CM_TIME = poData.getString("CM_TIME");
                                    String OFFER = poData.getString("OFFER");
                                    String SHARE = poData.getString("SHARE");
                                    String TAP = poData.getString("TAP");

                                    String CM_DESC = poData.getString("CM_DESC");
                                    String status = poData.getString("status");
                                    String RATING = poData.getString("RATING");

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 25, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout poLayout = (LinearLayout) inflater.inflate(R.layout.order_status_layout_alt, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    poLayout.setId(200 + j);
                                    poLayout.setLayoutParams(lp);

                                    LinearLayout TapToView=(LinearLayout) poLayout.findViewById(R.id.TapToView);
                                    TextView lblPOUID = poLayout.findViewById(R.id.lblPOUID);
                                    TextView lblQty = poLayout.findViewById(R.id.lblQTY);
                                    TextView lblCompanyName = poLayout.findViewById(R.id.lblCompanyName);
                                    TextView lblCompanyUID = poLayout.findViewById(R.id.lblCompanyUID);
                                    TextView lblVendorName = poLayout.findViewById(R.id.lblVendorName);
                                    TextView lblVendorType = poLayout.findViewById(R.id.lblVendorType);
                                    TextView lblDays = poLayout.findViewById(R.id.lblDays);
                                    TextView lblDate = poLayout.findViewById(R.id.lblDate);
                                    TextView lblTotalMRP = poLayout.findViewById(R.id.lblTotalMRP);
                                    TextView lblTotalLP = poLayout.findViewById(R.id.lblTotalLP);
                                    TextView lblPOTradeMargin = poLayout.findViewById(R.id.lblPOTradeMargin);
                                    TextView lblTap = poLayout.findViewById(R.id.lblTap);

                                    String[] Qty = CM_TIME.split("Qty");
                                    String[] QtyStr = Qty[1].split("Trd");
                                    String QtyString= QtyStr[0];

                                    String[] TradeMargin = CM_TIME.split("Trd. Mgn Rs.");
                                    String[] TradeMarginStr = TradeMargin[1].split("\\(");
                                    String POTradeMarginstr= TradeMarginStr[0];

                                    String[] TotalLPstr = CM_TIME.split("LP");
                                    String[] TotalLPstrstr1 = TotalLPstr[1].split("Qty");
                                    String TotalLP= TotalLPstrstr1[0];

                                    String[] DaysRaised = CM_TIME.split(" ");
                                    String Days = DaysRaised[0]+" Days Raised";

                                //    String[] Cm_Desc = CM_DESC.split("-");
                                  //  String CmDesc = Cm_Desc[0];

                                    String TotalMRP = CM_TIME.split("Total MRP Rs.")[1].substring(0,CM_TIME.split("Total MRP Rs.")[1].indexOf("LP"));

                                    lblQty.setText("Qty: " + QtyString + " pcs");
                               /*     try {
                                        String[] temps = CM_NAME.split(" \\(");
                                        String CompanyName = temps[0];
                                        String CompanyUID = "(" + temps[1];

                                        lblCompanyName.setText(CompanyName);
                                        lblCompanyUID.setText(CompanyUID);
                                    }
                                    catch (Exception e)
                                    {
                                    }*/
                                    String Cm_NameStr = "",Cm_NameStr1="";

                                    String[] Cm_Name = CM_NAME.split("\\(");
                                    Cm_NameStr = Cm_Name[0];
                                    Cm_NameStr1 = Cm_Name[1];

                                    lblPOUID.setText(CM_DESC);
                                    lblCompanyName.setText(Cm_NameStr);
                                    lblDays.setText(Days);
                                    lblCompanyUID.setText("("+Cm_NameStr1);
                                    lblVendorName.setText(CM_ROOM_NO);
                                    //lblVendorNa.setText("("+Cm_NameStr1);
                                    lblTotalMRP.setText(TotalMRP);
                                    lblTotalLP.setText(TotalLP);
                                    lblPOTradeMargin.setText(POTradeMarginstr);
                                    lblTap.setText(TAP);

                                    TapToView.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Bundle Draftbundle=new Bundle();
                                            Draftbundle.putString("complaint_number",CM_NO);
                                            Draftbundle.putInt("position",-1);
                                            Bundle bundle=new Bundle();
                                            bundle.putBundle("Draftbundle",Draftbundle);
                                            NavHostFragment.findNavController(Fragment_Draft.this)
                                                    .navigate(R.id.action_Draft_Orders_to_Draft_Reports,bundle);
                                        }
                                    });
                                    nv.addView(poLayout);
                                }
                            }

                        else {
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
        }
    };
}
