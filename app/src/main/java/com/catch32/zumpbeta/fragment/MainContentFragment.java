package com.catch32.zumpbeta.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.apicall.ApiService;
import com.catch32.zumpbeta.apicall.OnResponseListener;
import com.catch32.zumpbeta.apicall.RequestType;
import com.catch32.zumpbeta.listener.OnBackPressedListener;
import com.catch32.zumpbeta.myactivity.Month_Wise_Summary;
import com.catch32.zumpbeta.myactivity.MyShop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.catch32.zumpbeta.apicall.ApiEndPoint.TEST_DATA_COPY;
import static com.catch32.zumpbeta.apicall.ApiEndPoint.TEST_MENU;

public class MainContentFragment extends Fragment implements OnBackPressedListener
{

    private Context mContext;
    int initialScrollY = 0;
    Handler handler = new Handler();
    LinearLayout llMyShop,llMonthwiseSummary;
    public class Company
    {
        String comp_nm, comp_id;
    }

    ArrayList<MainContentFragment.Company> companies = new ArrayList<>();
    ArrayList<MainContentFragment.Company> vendors = new ArrayList<>();

    String selectedVendor = "0", selectedStatus = "0", selectedCompany = "0";
    String selectedVendorStr = "", selectedStatusStr = "", selectedCompanyStr = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_main, container, false);

        llMyShop=view.findViewById(R.id.llMyShop);
        llMonthwiseSummary=view.findViewById(R.id.llMonthwiseSummary);
        llMyShop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), MyShop.class);
                startActivity(i);
            }
        });
        llMonthwiseSummary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(), Month_Wise_Summary.class);
                startActivity(i);
            }
        });
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
                /* get the maximum height which we have scroll before performing any action */
                int maxDistance = llFooter.getHeight();
                /* how much we have scrolled */
                float movement = nsvDashboard.getScrollY();
                /*finally calculate the alpha factor and set on the view */
                //float alphaFactor = ((movement * 1.0f) / (maxDistance + llFooter.getHeight()));
                if (movement > initialScrollY)
                {
                    if (!animator.isRunning())
                    {
                        //Setting animation from actual value to the initial yourViewToHide height)
                        animator.setIntValues(params.height, 0);
                        //Animation duration
                        animator.setDuration(100);
                        //In this listener we update the view
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
                        //Starting the animation
                        animator.start();
                    }
                    /*for image parallax with scroll */
                    //llFooter.setLayoutParams();
                    /* set visibility */
                    //llFooter.setAlpha(0);
                }
                else
                {
                    if (!animator.isRunning())
                    {
                        //Setting animation from actual value to the target value (here 0, because we're hiding the view)
                        animator.setIntValues(params.height, initialViewHeight);
                        //Animation duration
                        animator.setDuration(500);
                        //In this listener we update the view
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                ViewGroup.LayoutParams params = llFooter.getLayoutParams();
                                params.height = (int) animation.getAnimatedValue();
                                llFooter.setLayoutParams(params);
                            }
                        });
                        //Starting the animation
                        animator.start();
                    }
                }

                initialScrollY = nsvDashboard.getScrollY();
            }
        });

        init(view);
        return view;
    }

    public void init(View view)
    {
        mContext = getActivity();
        final ImageView imgDownArrow = view.findViewById(R.id.imgDownArrow);
        final ImageView imgUpArrow = view.findViewById(R.id.imgUpArrow);
        final Spinner spinnerVendor = view.findViewById(R.id.spinnerVendor);
        final Spinner spinnerStatus = view.findViewById(R.id.spinnerStatus);
        final Spinner spinnerCompany = view.findViewById(R.id.spinnerCompany);
        final TextView lblFilter1 = view.findViewById(R.id.lblFilter1);
        final TextView lblVendor = view.findViewById(R.id.lblVendor);
        final TextView lblStatus = view.findViewById(R.id.lblStatus);
        final TextView lblCompany = view.findViewById(R.id.lblCompany);

        JSONObject bodyParam = new JSONObject();
        try {
            bodyParam.accumulate("createdById", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.initialize(getContext().getApplicationContext());

        ApiService apiService = new ApiService(getMenuData, RequestType.POST,
                TEST_MENU, new HashMap<String, String>(), bodyParam);

        JSONObject Param = new JSONObject();

        try {
            Param.accumulate("id", "2VoTM29S");
            Param.accumulate("usercd", "301418");
            Param.accumulate("menuId", "5");
            Param.accumulate("selectedDropId", selectedVendor);
            Param.accumulate("selectedStatus", selectedStatus);
            Param.accumulate("extraSpinnerId", "0");
            Param.accumulate("selectedCompId", selectedCompany);

            bodyParam.accumulate("option", "getListData");
            bodyParam.accumulate("Param", Param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(getContext().getApplicationContext());

        System.out.println(Param);

        ApiService apiService1 = new ApiService(getData, RequestType.POST,
                TEST_DATA_COPY + Param.toString(), new HashMap<String, String>(), bodyParam);

        imgDownArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                spinnerVendor.setVisibility(View.VISIBLE);
                spinnerStatus.setVisibility(View.VISIBLE);
                spinnerCompany.setVisibility(View.VISIBLE);
                lblVendor.setVisibility(View.VISIBLE);
                lblStatus.setVisibility(View.VISIBLE);
                lblCompany.setVisibility(View.VISIBLE);
                imgUpArrow.setVisibility(View.VISIBLE);
                imgDownArrow.setVisibility(View.GONE);
                lblFilter1.setVisibility(View.GONE);
            }
        });

        imgUpArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                spinnerVendor.setVisibility(View.GONE);
                spinnerStatus.setVisibility(View.GONE);
                spinnerCompany.setVisibility(View.GONE);
                lblVendor.setVisibility(View.GONE);
                lblStatus.setVisibility(View.GONE);
                lblCompany.setVisibility(View.GONE);
                imgUpArrow.setVisibility(View.GONE);
                imgDownArrow.setVisibility(View.VISIBLE);
                lblFilter1.setVisibility(View.VISIBLE);
            }
        });
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
                        String menuData = responseJSON.getString("menuData");
                        String spinnerId = responseJSON.getString("spinnerId");
                        String statusId = responseJSON.getString("statusId");

                        if (loginSuccess.equals("Y"))
                        {
                            int len;

                            //Status List
                            final JSONArray statusList = responseJSON.getJSONArray("statusList");
                            len = statusList.length();

                            final List<String> spinnerStatusArray = new ArrayList<>();

                            for (int j = 0; j < len; j++) {

                                JSONObject status = (JSONObject) statusList.get(j);

                                final String statusName = status.getString("statusName");
                                spinnerStatusArray.add(statusName);

                            }

                            ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_spinner_item,
                                    spinnerStatusArray
                            );

                            final Spinner spinnerStatus = getActivity().findViewById(R.id.spinnerStatus);
                            spinnerStatus.setAdapter(adapterStatus);
                            spinnerStatus.setSelection(Integer.parseInt(spinnerId));

                            //Company List
                            final JSONArray compList = responseJSON.getJSONArray("compList");
                            len = compList.length();

                            List<String> spinnerCompanyArray = new ArrayList<>();

                            for (int j = 0; j < len; j++) {

                                JSONObject company = (JSONObject) compList.get(j);

                                final String comp_nm = company.getString("comp_nm");
                                final String comp_id = company.getString("comp_id");
                                spinnerCompanyArray.add(comp_nm);

                                MainContentFragment.Company companyObj = new MainContentFragment.Company();
                                companyObj.comp_id = comp_id;
                                companyObj.comp_nm = comp_nm;

                                companies.add(companyObj);

                            }

                            ArrayAdapter<String> adapterCompany = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_spinner_item,
                                    spinnerCompanyArray
                            );

                            final Spinner spinnerCompany = getActivity().findViewById(R.id.spinnerCompany);
                            spinnerCompany.setAdapter(adapterCompany);
                            //spinnerCompany.setSelection(Integer.parseInt(compId));

                            //Company List
                            final JSONArray categoryList = responseJSON.getJSONArray("categoryListReport");
                            len = categoryList.length();

                            List<String> spinnerVendorArray = new ArrayList<>();

                            for (int j = 0; j < len; j++) {

                                JSONObject category = (JSONObject) categoryList.get(j);

                                final String cat_nm = category.getString("cat_nm");
                                final String cat_id = category.getString("cat_id");
                                spinnerVendorArray.add(cat_nm);

                                MainContentFragment.Company companyObj = new MainContentFragment.Company();
                                companyObj.comp_id = cat_id;
                                companyObj.comp_nm = cat_nm;

                                vendors.add(companyObj);

                            }

                            ArrayAdapter<String> adapterVendor = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_spinner_item,
                                    spinnerVendorArray
                            );

                            final Spinner spinnerVendor = getActivity().findViewById(R.id.spinnerVendor);
                            spinnerVendor.setAdapter(adapterVendor);
                            //spinnerCompany.setSelection(Integer.parseInt(compId));

                            spinnerVendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    selectedVendor = vendors.get(position).comp_id;
                                    selectedVendorStr = vendors.get(position).comp_nm;

                                    JSONObject bodyParam = new JSONObject();
                                    JSONObject Param = new JSONObject();

                                    try {

                                        Param.accumulate("id", "2VoTM29S");
                                        Param.accumulate("usercd", "301418");
                                        Param.accumulate("menuId", "5");
                                        Param.accumulate("selectedDropId", selectedVendor);
                                        Param.accumulate("selectedStatus", selectedStatus);
                                        Param.accumulate("extraSpinnerId", "0");
                                        Param.accumulate("selectedCompId", selectedCompany);

                                        bodyParam.accumulate("option", "getListData");
                                        bodyParam.accumulate("Param", Param);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    AndroidNetworking.initialize(getContext().getApplicationContext());

                                    System.out.println(Param);

                                    ApiService apiService1 = new ApiService(getData, RequestType.POST,
                                            TEST_DATA_COPY + Param.toString(), new HashMap<String, String>(), bodyParam);

                                    final TextView lblFilter1 = getActivity().findViewById(R.id.lblFilter1);
                                    lblFilter1.setText("Vendor: " + selectedVendorStr + "; Status: " + selectedStatusStr + "; Company: " + selectedCompanyStr);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    selectedCompany = companies.get(position).comp_id;
                                    selectedCompanyStr = companies.get(position).comp_nm;

                                    JSONObject bodyParam = new JSONObject();
                                    JSONObject Param = new JSONObject();

                                    try {

                                        Param.accumulate("id", "2VoTM29S");
                                        Param.accumulate("usercd", "301418");
                                        Param.accumulate("menuId", "5");
                                        Param.accumulate("selectedDropId", selectedVendor);
                                        Param.accumulate("selectedStatus", selectedStatus);
                                        Param.accumulate("extraSpinnerId", "0");
                                        Param.accumulate("selectedCompId", selectedCompany);

                                        bodyParam.accumulate("option", "getListData");
                                        bodyParam.accumulate("Param", Param);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    AndroidNetworking.initialize(getContext().getApplicationContext());

                                    System.out.println(Param);

                                    ApiService apiService1 = new ApiService(getData, RequestType.POST,
                                            TEST_DATA_COPY + Param.toString(), new HashMap<String, String>(), bodyParam);

                                    final TextView lblFilter1 = getActivity().findViewById(R.id.lblFilter1);
                                    lblFilter1.setText("Vendor: " + selectedVendorStr + "; Status: " + selectedStatusStr + "; Company: " + selectedCompanyStr);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    selectedStatus = (position) + "";
                                    selectedStatusStr = spinnerStatusArray.get(position);

                                    JSONObject bodyParam = new JSONObject();
                                    JSONObject Param = new JSONObject();

                                    try {

                                        Param.accumulate("id", "2VoTM29S");
                                        Param.accumulate("usercd", "301418");
                                        Param.accumulate("menuId", "5");
                                        Param.accumulate("selectedDropId", selectedVendor);
                                        Param.accumulate("selectedStatus", selectedStatus);
                                        Param.accumulate("extraSpinnerId", "0");
                                        Param.accumulate("selectedCompId", selectedCompany);

                                        bodyParam.accumulate("option", "getListData");
                                        bodyParam.accumulate("Param", Param);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    AndroidNetworking.initialize(getContext().getApplicationContext());

                                    System.out.println(Param);

                                    ApiService apiService1 = new ApiService(getData, RequestType.POST,
                                            TEST_DATA_COPY + Param.toString(), new HashMap<String, String>(), bodyParam);

                                    final TextView lblFilter1 = getActivity().findViewById(R.id.lblFilter1);
                                    lblFilter1.setText("Vendor: " + selectedVendorStr + "; Status: " + selectedStatusStr + "; Company: " + selectedCompanyStr);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {

                                }
                            });
                        } else {
                            String errorString = responseJSON.getString("obj");
                            Toast.makeText(getContext(), errorString, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(String error) {

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

                            if (len > 0)
                            {
                                JSONObject summary = (JSONObject) categoryReportData.get(0);
                                String TAP = summary.getString("TAP");
                                String CM_NAME = summary.getString("CM_NAME");
                                String CM_ROOM_NO = summary.getString("CM_ROOM_NO");
                                String CM_DESC = summary.getString("CM_DESC");
                                String CM_TIME = summary.getString("CM_TIME");

                                String[] temps = CM_NAME.split("=");
                                String OrdersOnHand = temps[0].split(": ")[1].split("\\(")[0];
                                String OrdersOnHandLP = temps[0].split(": ")[1].split("\\(LP:Rs.")[1].split("\\)")[0];

                                String[] temps1 = temps[1].split("\\+ ");
                                String PendingOrders = temps1[0].split(": ")[1].split(" \\(")[0];
                                String PendingOrdersLP = temps1[0].split(": ")[1].split(" \\(LP:Rs.")[1].split("\\)")[0];

                                String OnHoldOrders = temps1[1].split(": ")[1].split(" \\(")[0];
                                String OnHoldOrdersLP = temps1[1].split(": ")[1].split(" \\(LP:Rs.")[1].split("\\)")[0];

                                String InProcessOrders = temps1[2].split(": ")[1].split(" \\(")[0];
                                String InProcessOrdersLP = temps1[2].split(": ")[1].split(" \\(LP:Rs.")[1].split("\\)")[0];

                                temps = CM_ROOM_NO.split(" LP Rs.");
                                String TotalDeliveredOrders = temps[0].split("Orders ")[1];
                                String TotalDeliveredOrdersLP = temps[1];

                                //temps = CM_TIME.split("");
                                String TotalRegdOrders = CM_TIME.substring(CM_TIME.indexOf(":") + 2, CM_TIME.indexOf("MRP"));
                                String TotalRegdOrdersMRP = CM_TIME.substring(CM_TIME.indexOf("MRP Rs.") + 7, CM_TIME.indexOf("LP") - 1);
                                String TotalRegdOrdersLP = CM_TIME.split("\\(TRP")[0].substring(CM_TIME.indexOf("LP Rs.") + 6);
                                String TradeMargin = CM_TIME.substring(CM_TIME.indexOf("Trd Mgn Rs.") + 11, CM_TIME.indexOf("TimeOut") - 2).split("\\(")[0];
                                String TradeMarginPercentage = CM_TIME.substring(CM_TIME.indexOf("Trd Mgn Rs.") + 11, CM_TIME.indexOf("(TimeOut")).split("\\(")[1].split("\\)")[0];
                                String TradeMarginTRP = CM_TIME.substring(CM_TIME.indexOf("TRP Rs.") + 7, CM_TIME.indexOf("+"));
                                String TradeMarginGST = CM_TIME.substring(CM_TIME.indexOf("+") + 8, CM_TIME.indexOf(") Trd Mgn"));

                                String TimeOutOrders = CM_TIME.split("TimeOut Orders ")[1].split(" LP")[0];
                                String TimeOutOrdersLP = CM_TIME.split("TimeOut Orders ")[1].split(" LP Rs.")[1].split("\\)")[0];

                                TextView lblSummaryTitle = getActivity().findViewById(R.id.lblSummaryTitle);
                                lblSummaryTitle.setText("Summary of " + TAP);

                                TextView lblOnHandOrders = getActivity().findViewById(R.id.lblOnHandOrders);
                                TextView lblOnHandOrdersLP = getActivity().findViewById(R.id.lblOnHandOrdersLP);
                                lblOnHandOrders.setText("On Hand Orders: " + OrdersOnHand);
                                lblOnHandOrdersLP.setText(" | LP ₹ " + OrdersOnHandLP);

                                TextView lblPendingOrders = getActivity().findViewById(R.id.lblPendingOrders);
                                TextView lblPendingOrdersLP = getActivity().findViewById(R.id.lblPendingOrdersLP);
                                lblPendingOrders.setText("Pending Orders: " + PendingOrders);
                                lblPendingOrdersLP.setText("LP ₹ " + PendingOrdersLP);

                                TextView lblInProgressOrders = getActivity().findViewById(R.id.lblInProgressOrders);
                                TextView lblInProgressOrdersLP = getActivity().findViewById(R.id.lblInProgressOrdersLP);
                                lblInProgressOrders.setText("In Process Orders: " + InProcessOrders);
                                lblInProgressOrdersLP.setText("LP ₹ " + InProcessOrdersLP);

                                TextView lblOnHoldOrders = getActivity().findViewById(R.id.lblOnHoldOrders);
                                TextView lblOnHoldOrdersLP = getActivity().findViewById(R.id.lblOnHoldOrdersLP);
                                lblOnHoldOrders.setText("On Hold Orders: " + OnHoldOrders);
                                lblOnHoldOrdersLP.setText("LP ₹ " + OnHoldOrdersLP);

                                TextView lblTotalRegdOrders = getActivity().findViewById(R.id.lblTotalRegdOrders);
                                TextView lblTotalRegdOrdersMRP = getActivity().findViewById(R.id.lblTotalRegdOrdersMRP);
                                TextView lblTotalRegdOrdersLP = getActivity().findViewById(R.id.lblTotalRegdOrdersLP);
                                lblTotalRegdOrders.setText(TotalRegdOrders);
                                lblTotalRegdOrdersMRP.setText("₹ " + TotalRegdOrdersMRP);
                                lblTotalRegdOrdersLP.setText("₹ " + TotalRegdOrdersLP);

                                TextView lblTradeMargin = getActivity().findViewById(R.id.lblTradeMargin);
                                TextView lblTradeMarginPercentage = getActivity().findViewById(R.id.lblTradeMarginPercentage);
                                TextView lblTradeMarginTRP = getActivity().findViewById(R.id.lblTradeMarginTRP);
                                TextView lblTradeMarginGST = getActivity().findViewById(R.id.lblTradeMarginGST);
                                lblTradeMargin.setText("₹ " + TradeMargin);
                                lblTradeMarginPercentage.setText(TradeMarginPercentage);
                                lblTradeMarginTRP.setText("₹ " + TradeMarginTRP);
                                lblTradeMarginGST.setText("₹ " + TradeMarginGST);

                                TextView lblDeliveredOrders = getActivity().findViewById(R.id.lblDeliveredOrders);
                                TextView lblDeliveredOrdersLP = getActivity().findViewById(R.id.lblDeliveredOrdersLP);
                                lblDeliveredOrders.setText("Delivered Orders: " + TotalDeliveredOrders);
                                lblDeliveredOrdersLP.setText("LP ₹ " + TotalDeliveredOrdersLP);

                                TextView lblTimeoutOrders = getActivity().findViewById(R.id.lblTimeoutOrders);
                                TextView lblTimeoutOrdersLP = getActivity().findViewById(R.id.lblTimeoutOrdersLP);
                                lblTimeoutOrders.setText("Timeout Orders: " + TimeOutOrders);
                                lblTimeoutOrdersLP.setText("LP ₹ " + TimeOutOrdersLP);

                                RelativeLayout nv = (RelativeLayout) getActivity().findViewById(R.id.rlPOList);
                                nv.removeAllViews();

                                for (int j = 1; j < len; j++)
                                {
                                    JSONObject poData = (JSONObject) categoryReportData.get(j);

                                    String PO_CM_NAME = poData.getString("CM_NAME");
                                    String PO_CM_ROOM_NO = poData.getString("CM_ROOM_NO");
                                    String PO_CM_NO = poData.getString("CM_NO");
                                    String PO_status = poData.getString("status");
                                    String PO_CM_DESC = poData.getString("CM_DESC");
                                    String PO_CM_TIME = poData.getString("CM_TIME");

                                    String CompanyName = "", CompanyUID = "", VendorName = "", VendorType = "";

                                    if (selectedVendor.equals("0")) {
                                        temps = PO_CM_NAME.split(" \\(");
                                        CompanyName = temps[0];
                                        CompanyUID = "(" + temps[1];

                                        temps = PO_CM_ROOM_NO.split(" \\(");
                                        VendorName = temps[0];
                                        VendorType = "(" + temps[1];
                                    } else {
                                        temps = PO_CM_NAME.split(" \\(");
                                        VendorName = temps[0];
                                        VendorType = "(" + temps[1];

                                    }

                                    String Qty = PO_status.split("Total Qty:")[1];
                                    String Status = PO_status.split(" By")[0];

                                    temps = PO_CM_DESC.split(" {5}");
                                    String PONumber = temps[0];
                                    String PODate = temps[1];

                                    String DaysRaised = PO_CM_TIME.split(" Total MRP")[0].substring(PO_CM_TIME.indexOf(":") + 2);
                                    String TotalMRP = PO_CM_TIME.split("Total MRP Rs.")[1].substring(0, PO_CM_TIME.split("Total MRP Rs.")[1].indexOf("Total LP Rs."));
                                    String TotalLP = PO_CM_TIME.split("Total LP Rs.")[1].substring(0, PO_CM_TIME.split("Total LP Rs.")[1].indexOf("  Qty:"));
                                    String POTradeMargin = PO_CM_TIME.split("Trd. Mgn Rs.")[1];

                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                    lp.setMargins(0, 25, 0, 0);

                                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    LinearLayout poLayout = (LinearLayout) inflater.inflate(R.layout.po_layout, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                    poLayout.setId(200 + j);
                                    poLayout.setLayoutParams(lp);

                                    TextView lblPOUID = poLayout.findViewById(R.id.lblPOUID);
                                    TextView lblPODate = poLayout.findViewById(R.id.lblPODate);
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

                                    lblPOUID.setText(PONumber + "  ");
                                    lblPODate.setText(PODate);
                                    lblQty.setText("Qty: " + Qty + " pcs");
                                    lblCompanyName.setText(CompanyName);
                                    lblCompanyUID.setText(CompanyUID);
                                    lblVendor.setText(VendorName);
                                    lblVendorType.setText(VendorType);
                                    lblDays.setText(DaysRaised);
                                    //lblDate.setText(DaysRaised);
                                    lblTotalMRP.setText(TotalMRP);
                                    lblTotalLP.setText(TotalLP);
                                    lblPOTradeMargin.setText(POTradeMargin);

                                    if (selectedStatus.equals("4"))
                                        poLayout.setBackground(getContext().getDrawable(R.drawable.rectangle_with_red_gradient_bg2));
                                    else if (selectedStatus.equals("2"))
                                        poLayout.setBackground(getContext().getDrawable(R.drawable.rectangle_with_orange_gradient_bg));
                                    else if (selectedStatus.equals("5"))
                                        poLayout.setBackground(getContext().getDrawable(R.drawable.rectangle_with_greenish_gradient_bg));
                                    else if (selectedStatus.equals("3"))
                                        poLayout.setBackground(getContext().getDrawable(R.drawable.rectangle_with_green_gradient_bg2));
                                    else if (selectedStatus.equals("1"))
                                        poLayout.setBackground(getContext().getDrawable(R.drawable.rectangle_with_brown_gradient_bg));
                                    else if (selectedStatus.equals("0"))
                                        poLayout.setBackground(getContext().getDrawable(R.drawable.rectangle_with_brown_gradient_bg));

                                    LinearLayout tap1 = poLayout.findViewById(R.id.tap1);
                                    LinearLayout tap2 = poLayout.findViewById(R.id.tap2);

                                    if (selectedStatus.equals("3"))
                                        tap1.setVisibility(View.GONE);
                                    else
                                        tap2.setVisibility(View.GONE);

                                    nv.addView(poLayout);
                                }
                                LinearLayout llLoading = getActivity().findViewById(R.id.llLoading);
                                llLoading.setVisibility(View.GONE);
                            }
                            /*
                            for (int j = 0; j < len; j++) {

                                JSONObject status = (JSONObject) statusList.get(j);

                                final String statusName = status.getString("statusName");
                                spinnerStatusArray.add(statusName);

                            }

                            ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(
                                    ActivityMain.this,
                                    android.R.layout.simple_spinner_item,
                                    spinnerStatusArray
                            );

                            final Spinner spinnerStatus = findViewById(R.id.spinnerStatus);
                            spinnerStatus.setAdapter(adapterStatus);
                            spinnerStatus.setSelection(Integer.parseInt(spinnerId));
                            */

                        }
                        else
                            {
                            String errorString = responseJSON.getString("obj");
                            Toast.makeText(getContext(), errorString, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        public void onError(String error)
        {
        }
    };

    @Override
    public boolean onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //SB20191213 builder.setIcon(R.drawable.ic_logout_red_48dp);
        //SB20191213 builder.setTitle("Warning");
        builder.setMessage("You are in Home Page !!!");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                //SB20191213  AppUserManager.logout(mContext, SignInActivity.class);
            }
        });

      /*  builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });*/

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));


        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        return true;

    }


}
