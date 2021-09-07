package com.catch32.zumpbeta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.model.SubCategoryBrandList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Ruchi Mehta
 * @version Jul 19, 2019
 */
public class SubCategoryBrandListAdapter extends ArrayAdapter<SubCategoryBrandList> {

    private Context mContext;
    private List<SubCategoryBrandList> mList;

    public SubCategoryBrandListAdapter(Context context,
                                  List<SubCategoryBrandList> values) {
        super(context, R.layout.spinner_item, values);
        this.mContext = context;
        this.mList = values;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SubCategoryBrandList getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.spinner_item, parent, false);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.txt_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mName.setText(mList.get(position).getSubCategoryBrandName());


        return convertView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private class ViewHolder {
        TextView mName;
    }
}
