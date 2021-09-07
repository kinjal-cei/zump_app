package com.catch32.zumpbeta.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.model.CategoryReportData;

import java.util.List;

/**
 * @author Ruchi Mehta
 * @version Jul 01, 2019
 */
public class MyOrderAdapter extends ArrayAdapter<CategoryReportData> {

    private Context mContext;
    private List<CategoryReportData> mList;

    public MyOrderAdapter(Context context,
                          List<CategoryReportData> values) {
        super(context, R.layout.adapter_my_order, values);
        this.mContext = context;
        this.mList = values;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CategoryReportData getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.adapter_my_order, parent, false);
            mViewHolder.mTotal = (TextView) convertView.findViewById(R.id.txt_time);
            mViewHolder.mCompany = (TextView) convertView.findViewById(R.id.txt_name);
            mViewHolder.mProductNm = (TextView) convertView.findViewById(R.id.txt_room_no);
            mViewHolder.mDesc = (TextView) convertView.findViewById(R.id.txt_desc);
          //  mViewHolder.mContribution = (TextView) convertView.findViewById(R.id.txt_contribution); //SB20191221
            mViewHolder.mTap = (TextView) convertView.findViewById(R.id.txt_tap); //SB20191221
            mViewHolder.mRating = (TextView) convertView.findViewById(R.id.txt_rating); //SB20191223

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mTotal.setSelected(true);
        mViewHolder.mCompany.setSelected(true);
        mViewHolder.mProductNm.setSelected(true);
        mViewHolder.mDesc.setSelected(true);
       // mViewHolder.mContribution.setSelected(true); //SB20191221
        mViewHolder.mTap.setSelected(true); //SB20191221
        mViewHolder.mRating.setSelected(true); //SB20191223

        CategoryReportData report = mList.get(position);

        mViewHolder.mTotal.setText(report.getCM_TIME());
        mViewHolder.mCompany.setText(report.getCM_NAME());
        mViewHolder.mProductNm.setText(report.getCM_ROOM_NO());
        mViewHolder.mDesc.setText(report.getCM_DESC());
       // mViewHolder.mContribution.setText(report.getSHARE()); //SB20191221
        mViewHolder.mTap.setText(report.getTAP()); //SB20191219
        mViewHolder.mRating.setText(report.getRATING()); //SB20191223
        //////////////////SB20191221/////////////////////
      //  mViewHolder.mContribution.setTextColor(Color.parseColor("BLACK"));
        //if(!report.getSHARE().equals(""))
          //  mViewHolder.mContribution.setBackgroundColor(Color.parseColor("GREEN"));
       // else
          //  mViewHolder.mContribution.setBackgroundColor(Color.parseColor("RED"));
        mViewHolder.mTap = (TextView) convertView.findViewById(R.id.txt_tap); //SB20191221
        mViewHolder.mRating = (TextView) convertView.findViewById(R.id.txt_rating); //SB20191221
        ////////////////////////////////////////////////
        String status = report.getCM_STATUS();
        switch (status) {
            case "0":
                mViewHolder.mDesc.setTextColor(Color.parseColor("#8B3A3A"));
                mViewHolder.mTap.setBackgroundColor(Color.parseColor("#8B3A3A")); //SB20191221
                mViewHolder.mRating.setBackgroundColor(Color.parseColor("#ffffff")); //SB20191221
                break;
            case "1":
            case "N":
                mViewHolder.mDesc.setTextColor(Color.parseColor("#FF7F00"));
                mViewHolder.mTap.setBackgroundColor(Color.parseColor("#FF7F00")); //SB20191221
                mViewHolder.mRating.setBackgroundColor(Color.parseColor("#ffffff")); //SB20191221
                break;
            case "2":
            case "Y":
                mViewHolder.mDesc.setTextColor(Color.parseColor("#228B22"));
                mViewHolder.mTap.setBackgroundColor(Color.parseColor("#228B22")); //SB20191221
                mViewHolder.mRating.setBackgroundColor(Color.parseColor("#000000")); //SB20191221
                break;
            case "3":
            case "X":
                mViewHolder.mDesc.setTextColor(Color.parseColor("#CD0000"));
                mViewHolder.mTap.setBackgroundColor(Color.parseColor("#CD0000")); //SB20191221
                mViewHolder.mRating.setBackgroundColor(Color.parseColor("#ffffff")); //SB20191221
                break;
            case "4":
            case "W":
                mViewHolder.mDesc.setTextColor(Color.parseColor("#458B74"));
                mViewHolder.mTap.setBackgroundColor(Color.parseColor("#458B74")); //SB20191221
                mViewHolder.mRating.setBackgroundColor(Color.parseColor("#ffffff")); //SB20191221
                break;
            case "5":
            case "B":
                mViewHolder.mDesc.setTextColor(Color.parseColor("#DBDB70"));
                mViewHolder.mTap.setBackgroundColor(Color.parseColor("#DBDB70")); //SB20191221
                break;
            default:
                mViewHolder.mDesc.setTextColor(Color.parseColor("#000000"));
                mViewHolder.mTap.setBackgroundColor(Color.parseColor("#000000")); //SB20191221
                mViewHolder.mRating.setBackgroundColor(Color.parseColor("#000000")); //SB20191221
                break;
        }

        return convertView;
    }


    private class ViewHolder {
        TextView mTotal;
        TextView mCompany;
        TextView mProductNm;
        TextView mDesc;
       // TextView mContribution; //SB20191219
        TextView mTap; //SB20191221
        TextView mRating; //SB20191223

    }
}