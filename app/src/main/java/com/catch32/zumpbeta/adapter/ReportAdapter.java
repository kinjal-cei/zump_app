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
 * @version Jul 10, 2019
 */
public class ReportAdapter extends ArrayAdapter<CategoryReportData> {

    private Context mContext;
    private List<CategoryReportData> mList;

    public ReportAdapter(Context context,
                         List<CategoryReportData> values) {
        super(context, R.layout.adapter_report, values);
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
            convertView = mInflater.inflate(R.layout.adapter_report, parent, false);
            mViewHolder.mBarcode = (TextView) convertView.findViewById(R.id.txt_barcode);
            mViewHolder.mTitle = (TextView) convertView.findViewById(R.id.txt_title);
            mViewHolder.mScheme = (TextView) convertView.findViewById(R.id.txt_scheme);
            mViewHolder.mDesc = (TextView) convertView.findViewById(R.id.txt_desc);
            mViewHolder.mContribution = (TextView) convertView.findViewById(R.id.txt_contribution);//SB20191219
            mViewHolder.mTap = (TextView) convertView.findViewById(R.id.txt_tap);//SB20191219

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mBarcode.setSelected(true);
        mViewHolder.mDesc.setSelected(true);

        CategoryReportData categoryReportData = mList.get(position);

        mViewHolder.mBarcode.setText(categoryReportData.getCM_DESC());
        mViewHolder.mTitle.setText(categoryReportData.getCM_NAME());
        mViewHolder.mScheme.setText(categoryReportData.getCM_TIME());
        mViewHolder.mDesc.setText(categoryReportData.getCM_ROOM_NO());
        mViewHolder.mContribution.setText(categoryReportData.getSHARE()); //SB20191219
        mViewHolder.mContribution.setTextColor(Color.parseColor("BLACK"));
        if(!categoryReportData.getSHARE().equals(""))
            mViewHolder.mContribution.setBackgroundColor(Color.parseColor("GREEN"));
        else
            mViewHolder.mContribution.setBackgroundColor(Color.parseColor("RED"));
        mViewHolder.mTap.setText(categoryReportData.getTAP()); //SB20191219
        mViewHolder.mBarcode.setTextColor(Color.parseColor("#000000"));
        String status = categoryReportData.getCM_STATUS();

        if (status != null)
            switch (status) {
                case "0":
                    mViewHolder.mBarcode.setTextColor(Color.parseColor("#8B3A3A"));
                    break;
                case "1":
                case "N":
                    mViewHolder.mBarcode.setTextColor(Color.parseColor("#FF7F00"));
                    break;
                case "2":
                case "Y":
                    mViewHolder.mBarcode.setTextColor(Color.parseColor("#228B22"));
                    break;
                case "3":
                case "X":
                    mViewHolder.mBarcode.setTextColor(Color.parseColor("#CD0000"));
                    break;
                case "4":
                case "W":
                    mViewHolder.mBarcode.setTextColor(Color.parseColor("#458B74"));
                    break;
                case "5":
                case "B":
                    mViewHolder.mBarcode.setTextColor(Color.parseColor("#DBDB70"));
                    break;
                default:
                    mViewHolder.mBarcode.setTextColor(Color.parseColor("#000000"));
                    break;
            }

        return convertView;

    }


    private class ViewHolder {
        TextView mBarcode;
        TextView mTitle;
        TextView mDesc;
        TextView mScheme;
        TextView mContribution; //SB20191219
        TextView mTap; //SB20191219
    }
}
