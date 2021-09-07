package com.catch32.zumpbeta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.constant.BaseSharedPref;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.catch32.zumpbeta.model.FeedbackDatum;
import com.catch32.zumpbeta.network.PostDataToServerTask;
import com.catch32.zumpbeta.utils.SharedPrefFactory;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * @author Ruchi Mehta
 * @version Oct 16, 2019
 */
public class FeedbackAdapter extends ArrayAdapter<FeedbackDatum>
{

    private Context mContext;
    private List<FeedbackDatum> mList;

    public FeedbackAdapter(Context context,List<FeedbackDatum> values)
    {
        super(context, R.layout.adapter_feedback, values);
        this.mContext = context;
        this.mList = values;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public FeedbackDatum getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_feedback, parent, false);
            mViewHolder.mTitle = (TextView) convertView.findViewById(R.id.txt_title);
            mViewHolder.mFeedback = (RatingBar) convertView.findViewById(R.id.rt_feedback);
            mViewHolder.mSubmit = convertView.findViewById(R.id.btn_submit);

            ViewHolder finalMViewHolder = mViewHolder;
            mViewHolder.mSubmit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FeedbackDatum feedbackDatum = (FeedbackDatum) v.getTag();

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("usercd", SharedPrefFactory.getProfileSharedPreference(mContext).getString(BaseSharedPref.USER_CD_KEY));
                    jsonObject.addProperty("id",feedbackDatum.getCM_NO());
                    jsonObject.addProperty("rating", finalMViewHolder.mFeedback.getRating());
                    System.out.println(jsonObject);
                    new PostDataToServerTask(mContext, AppConstant.Actions.SUBMIT_FEEDBACK_DATA)
                            .setPath(AppConstant.WebURL.SUBMIT_FEEDBACK_DATA)
                            .setResponseListener(new ResponseListener()
                            {
                                @Override
                                public void onResponse(String tag, String response)
                                {
                                    WidgetUtil.showErrorToast(mContext,"Saved");
                                }
                                @Override
                                public void onError(String tag, String error)
                                {
                                    WidgetUtil.showErrorToast(mContext,error);
                                }
                            })
                            .setRequestParams(jsonObject)
                            .makeCall();
                }
            });
            mViewHolder.mFeedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
            {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
                {

                }
            });

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mTitle.setSelected(true);

        FeedbackDatum feedbackDatum = mList.get(position);

        mViewHolder.mTitle.setText(feedbackDatum.getCM_NAME());
        mViewHolder.mSubmit.setTag(feedbackDatum);

        return convertView;

    }


    private class ViewHolder {
        TextView mTitle;
        RatingBar mFeedback;
        Button mSubmit;
    }

}
