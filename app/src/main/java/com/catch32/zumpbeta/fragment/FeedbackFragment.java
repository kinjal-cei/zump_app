package com.catch32.zumpbeta.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.adapter.FeedbackAdapter;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.factory.GsonFactory;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.Feedback;
import com.catch32.zumpbeta.model.FeedbackDatum;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class FeedbackFragment extends Fragment implements ResponseListener
{
    private static final String TAG = FeedbackFragment.class.getSimpleName();
    private Context mContext;
    private FeedbackAdapter mAdapter;
    private List<FeedbackDatum> mFeedbackList;
    private TextView mHeader; //SB20200103

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        init(view);
        return view;
    }

    private void init(View view)
    {
        mContext = getActivity();
        mFeedbackList = new ArrayList<>();

        mAdapter = new FeedbackAdapter(mContext, mFeedbackList);
        mHeader= view.findViewById(R.id.feedback_head);
        mHeader.setText("BILL POWER welcomes your valuable feedback to improve our Ordering Process");
        ListView listView = view.findViewById(R.id.list_feedback);
        listView.setEmptyView(view.findViewById(R.id.empty_view));
        listView.setAdapter(mAdapter);

        getFeedbackItems();
    }
    private void getFeedbackItems()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
        String URL = AppConstant.Actions.GET_FEEDBACK_DATA + "&Param={\"usercd\":"+
        SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY) + "}";

        new PostDataToServerTask(mContext, AppConstant.Actions.GET_FEEDBACK_DATA)
                .setPath(AppConstant.WebURL.GET_FEEDBACK_DATA)
                .setResponseListener(this)
                .setRequestParams(jsonObject)
                .makeCall();
    }
    private void initNavigationView()
    {
        if (getActivity() instanceof IBaseMenuActivity)
        {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            baseMenuActivity.setActionbarTitle("Feedback");
            baseMenuActivity.checkMenuItem(R.id.nav_home);
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

        switch (tag)
        {
            case AppConstant.Actions.GET_FEEDBACK_DATA:
                mFeedbackList.clear();
                Feedback feedback = gson.fromJson(response, Feedback.class);
                mFeedbackList.addAll(feedback.getFeedbackData());
                mAdapter.notifyDataSetChanged();
                break;
            case AppConstant.Actions.GET_LIST_DATA_SECOND_SPINNER:
                break;
        }
    }

    @Override
    public void onError(String tag, String error) {
        WidgetUtil.showErrorToast(mContext, error);
    }
}