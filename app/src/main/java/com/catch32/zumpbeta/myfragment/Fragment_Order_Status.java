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

import static com.catch32.zumpbeta.apicall.ApiEndPoint.GET_RAISED_ORDERS_PATH;

public class Fragment_Order_Status extends Fragment
{
        private Activity mContext;
        Handler handler=new Handler();
        String selectedVendorId,selectedCompanyId;
        ArrayList<String> listOfCompanies =new ArrayList<>();
        ArrayList<String> listOfCompaniesId =new ArrayList<String>();
        int initialScrollY = 0;
        ArrayList<String> listOfVendors =new ArrayList<>();
        ArrayList<String> listOfVendorsId =new ArrayList<String>();
        Spinner spinner_vendor,spinner_company;
        LinearLayout empty_view;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.fragment_orders_status, container, false);
            mContext = getActivity();
            spinner_vendor=view.findViewById(R.id.spinner_distributor);
            spinner_company=view.findViewById(R.id.spinner_company);
            empty_view=view.findViewById(R.id.empty_view);

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

            TextView info = view.findViewById(R.id.header_path);
            info.setText("["+ SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY)+"] "+"Home >Order Status");

            JSONObject bodyParam = new JSONObject();
            JSONObject Param = new JSONObject();
            try {
                Param.accumulate("usercd",SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
                Param.accumulate("mobileNo",SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.MOBILE_NUMBER_KEY));
                Param.accumulate("distcd", selectedVendorId);
                Param.accumulate("compcd", selectedCompanyId);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            System.out.println(Param+"1");
            AndroidNetworking.initialize(getActivity().getApplicationContext());

            ApiService apiService1 = new ApiService(getMenuData, RequestType.GET,
                    GET_RAISED_ORDERS_PATH+Param.toString(), new HashMap<String, String>(),bodyParam);
            return view;
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

                            if (loginSuccess.equals("Y"))
                            {
                                empty_view.setVisibility(View.GONE);
                                int len;
                                //Vendor List
                                final JSONArray VendorListArray = responseJSON.getJSONArray("distReportData");
                                len = VendorListArray.length();

                                listOfVendors = new ArrayList<>();
                                listOfVendorsId = new ArrayList<String>();

                                for (int j = 0; j < len; j++) {
                                    JSONObject category = (JSONObject) VendorListArray.get(j);

                                    final String cat_nm = category.getString("dist_nm");
                                    final String cat_id = category.getString("dist_id");

                                    listOfVendors.add(cat_nm);
                                    listOfVendorsId.add(cat_id);
                                }
                                ArrayAdapter<String> adapterVendor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listOfVendors);
                                spinner_vendor.setAdapter(adapterVendor);

                                spinner_vendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                    {
                                        if (true)
                                        {
                                            try {
                                                selectedVendorId = ((JSONObject) VendorListArray.get(spinner_vendor.getSelectedItemPosition())).getString("dist_id");
                                                try {
                                                    JSONObject bodyParam = new JSONObject();
                                                    JSONObject Param = new JSONObject();
                                                    try {
                                                        Param.accumulate("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
                                                        Param.accumulate("distcd", selectedVendorId);
                                                        Param.accumulate("compcd", selectedCompanyId);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    System.out.println(Param);
                                                    AndroidNetworking.initialize(getActivity().getApplicationContext());

                                                    ApiService apiService1 = new ApiService(getMenu, RequestType.GET,
                                                            GET_RAISED_ORDERS_PATH + Param.toString(), new HashMap<String, String>(), bodyParam);
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
                                spinner_company.setAdapter(adapterCompany);

                                spinner_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                    {
                                        if (true)
                                        {
                                            try {
                                                selectedCompanyId = ((JSONObject) compListArray.get(spinner_company.getSelectedItemPosition())).getString("comp_id");
                                                try {
                                                    JSONObject bodyParam = new JSONObject();
                                                    JSONObject Param = new JSONObject();
                                                    try {
                                                        Param.accumulate("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
                                                        Param.accumulate("distcd", selectedVendorId);
                                                        Param.accumulate("compcd", selectedCompanyId);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    System.out.println(Param);
                                                    AndroidNetworking.initialize(getActivity().getApplicationContext());

                                                    ApiService apiService1 = new ApiService(getMenu, RequestType.GET,
                                                            GET_RAISED_ORDERS_PATH + Param.toString(), new HashMap<String, String>(), bodyParam);
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

        OnResponseListener getMenu= new OnResponseListener()
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
                                    //orderList
                                    final JSONArray OrderList = responseJSON.getJSONArray("orderList");
                                    len = OrderList.length();
                                    RelativeLayout nv = (RelativeLayout) getActivity().findViewById(R.id.rl_BookOrderStatus);
                                    nv.removeAllViews();
                                    for (int j = 0; j < len; j++) {
                                        JSONObject object = (JSONObject) OrderList.get(j);
                                        String uid = object.getString("id");
                                        String user_nm = object.getString("user_nm");
                                        String time = object.getString("time");
                                        String flag = object.getString("flag");
                                        String ordno = object.getString("ordno");
                                        String TAP = object.getString("TAP");
                                        String desc = object.getString("desc");
                                        String description = object.getString("description");
                                        String subcategory = object.getString("subcategory");
                                        String prod_nm = object.getString("prod_nm");
                                        String prod_mrp = object.getString("prod_mrp");
                                        String prod_trp = object.getString("prod_trp");
                                        String prod_qty = object.getString("prod_qty");
                                        String CM_TIME = object.getString("CM_TIME");

                                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                        lp.setMargins(0, 25, 0, 0);

                                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        LinearLayout poLayout = (LinearLayout) inflater.inflate(R.layout.order_status_layout_alt, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                        poLayout.setId(200 + j);
                                        poLayout.setLayoutParams(lp);

                                        LinearLayout TapToView = (LinearLayout) poLayout.findViewById(R.id.TapToView);
                                        TextView lblPOUID = poLayout.findViewById(R.id.lblPOUID);
                                        //TextView lblPODate = poLayout.findViewById(R.id.lblPODate);
                                        TextView lblQty = poLayout.findViewById(R.id.lblQTY);
                                        TextView lblCompanyName = poLayout.findViewById(R.id.lblCompanyName);
                                        TextView lblCompanyUID = poLayout.findViewById(R.id.lblCompanyUID);
                                        TextView lblVendor = poLayout.findViewById(R.id.lblVendorName);
                                        TextView lblVendorType = poLayout.findViewById(R.id.lblVendorType);
                                        TextView lblDays = poLayout.findViewById(R.id.lblDays);
                                        TextView lblDate = poLayout.findViewById(R.id.lblDate);
                                        TextView lblTotalMRP = poLayout.findViewById(R.id.lblTotalMRP);
                                        TextView lblTotalLP = poLayout.findViewById(R.id.lblTotalLP);
                                        TextView lblPOTradeMargin = poLayout.findViewById(R.id.lblPOTradeMargin);
                                        TextView lblTap = poLayout.findViewById(R.id.lblTap);

                                        String[] strDaysRaised = subcategory.split("Days Raised:");
                                        String[] strDaysRaisedstr2 = strDaysRaised[1].split("Total MRP Rs.");
                                        String DaysRaised = strDaysRaisedstr2[0];

                                        String Days = subcategory.split(" Total MRP Rs.")[0];
                                        /*if (DaysRaised.contains(")")) {
                                            Days = DaysRaised.replace(")", "");
                                        } else {
                                            Days = DaysRaised;
                                        }*/
                                        // String DaysRaised = "";

                                  /*  if(ordno.contains("IN PROCESS"))
                                        DaysRaised= strDaysRaisedstr2[0];
                                    else
                                        DaysRaised= strDaysRaised[1].split("  Total MRP")[0].trim();*/

                                        String[] TotalLPstr = subcategory.split("Total LP Rs.");
                                        String[] TotalLPstrstr1 = TotalLPstr[1].split("Qty");
                                        String TotalLP = TotalLPstrstr1[0];

                                        String[] TradeMargin = subcategory.split("Trd Mgn Rs.");
                                        String[] TradeMarginStr = TradeMargin[1].split("Qty");
                                        String POTradeMarginstr = TradeMarginStr[0];

                                        String[] Qty = subcategory.split("Qty");
                                        String[] QtyStr = Qty[1].split("Trd");
                                        String QtyString = QtyStr[0];

                                        String[] splitArray = desc.split(" ");
                                        String desc1 = splitArray[0];
                                        String VendorType = splitArray[1];

                                        try {
                                            String[] temps = prod_nm.split(" \\(");
                                            String CompanyName = temps[0];
                                            String CompanyUID = "(" + temps[1];

                                            lblCompanyName.setText(CompanyName);
                                            lblCompanyUID.setText(CompanyUID);
                                        } catch (Exception e) {

                                        }

                                        String TotalMRP = subcategory.split("Total MRP Rs.")[1].substring(0, subcategory.split("Total MRP Rs.")[1].indexOf("Total LP Rs."));

                                        String[] PONumberStr = ordno.split(" ");
                                        String PONumber = PONumberStr[0];
                                        String PODesc = PONumberStr[1];
                                        String PODate = PONumberStr[3];
                                        String POTime = PONumberStr[4];
                                        String POTime2 = PONumberStr[5];

                                        lblPOUID.setText(ordno);
                                        lblQty.setText("Qty: " + QtyString + " pcs");
                                        lblVendor.setText(desc);
                                        //lblVendorType.setText(VendorType);
                                        lblVendorType.setVisibility(View.GONE);
                                        lblDays.setText(Days);
                                        //lblDate.setText(DaysRaised);
                                        lblTotalMRP.setText(TotalMRP);
                                        lblTotalLP.setText(TotalLP);
                                        lblTap.setText(TAP);
                                        lblPOTradeMargin.setText(POTradeMarginstr);

                                        if (ordno.contains("TIMEOUT"))
                                            poLayout.setBackground(getActivity().getDrawable(R.drawable.rectangle_with_red_gradient_bg2));
                                        else if (ordno.contains("IN PROCESS"))
                                            poLayout.setBackground(getActivity().getDrawable(R.drawable.rectangle_with_greenish_gradient_bg));
                                        else if (ordno.contains("ON HOLD"))
                                            poLayout.setBackground(getActivity().getDrawable(R.drawable.rectangle_with_orange_gradient_bg));
                                        else if (ordno.contains("DELIVERED"))
                                            poLayout.setBackground(getActivity().getDrawable(R.drawable.rectangle_with_green_gradient_bg2));
                                        else if (ordno.contains("PENDING"))
                                            poLayout.setBackground(getActivity().getDrawable(R.drawable.rectangle_with_brown_gradient_bg));
                                        else
                                            poLayout.setBackground(getActivity().getDrawable(R.drawable.rectangle_with_brown_gradient_bg));


                                        if (ordno.contains("TIMEOUT"))
                                        {
                                        } else {
                                            TapToView.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    Bundle OrderStatusbundle = new Bundle();
                                                    OrderStatusbundle.putString("complaint_number", uid);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putBundle("OrderStatusbundle", OrderStatusbundle);
                                                    NavHostFragment.findNavController(Fragment_Order_Status.this)
                                                            .navigate(R.id.action_Order_Status_to_Order_Status_Details, bundle);
                                                }
                                            });
                                        }
                                        nv.addView(poLayout);
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
            }
        };
}
