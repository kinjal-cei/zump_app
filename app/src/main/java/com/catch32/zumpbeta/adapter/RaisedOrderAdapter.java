package com.catch32.zumpbeta.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.model.OrderList;
import com.catch32.zumpbeta.model.OrderListClr;

import java.util.List;
;

/**
 * @author Ruchi Mehta
 * @version Jun 19, 2019
 */
public class RaisedOrderAdapter extends ArrayAdapter<OrderList> {

    private Context mContext;
    private List<OrderList> mList;
    private List<OrderListClr> mColorList;

    public RaisedOrderAdapter(Context context, List<OrderList> values, List<OrderListClr> colorList)
    {
        super(context, R.layout.adapter_raised_orders, values);
        this.mContext = context;
        this.mList = values;
        this.mColorList = colorList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public OrderList getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder mViewHolder = new ViewHolder();

        if (convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_raised_orders, parent, false);
            mViewHolder.mOrdno = (TextView) convertView.findViewById(R.id.txt_orno);
            mViewHolder.mTap = (TextView) convertView.findViewById(R.id.txt_tap); //SB20191221
            mViewHolder.mSubcategory = (TextView) convertView.findViewById(R.id.txt_sub_category);
            mViewHolder.mProductNm = (TextView) convertView.findViewById(R.id.txt_product_nm);
            mViewHolder.mDesc = (TextView) convertView.findViewById(R.id.txt_desc);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mOrdno.setSelected(true);
        mViewHolder.mTap.setSelected(true);
        mViewHolder.mSubcategory.setSelected(true);
        mViewHolder.mProductNm.setSelected(true);
        mViewHolder.mDesc.setSelected(true);

        OrderList orderList = mList.get(position);

        mViewHolder.mOrdno.setText(orderList.getOrdno());

        mViewHolder.mSubcategory.setText(orderList.getSubcategory());
        mViewHolder.mProductNm.setText(orderList.getProd_nm());
        mViewHolder.mDesc.setText(orderList.getDesc());
        mViewHolder.mTap.setText(orderList.getTap()); //Sb20191221
        if (orderList.getTap().equals("CANCEL")) {
            mViewHolder.mTap.setBackgroundColor(Color.parseColor("YELLOW"));
        }

        OrderListClr orderListClr = mColorList.get(position);

        mViewHolder.mOrdno.setTextColor(Color.parseColor("#FF7F00"));
        if (orderList.getTap().equals(""))
        {
            mViewHolder.mTap.setBackgroundColor(Color.parseColor("#FF7F00"));
        }
        else
            mViewHolder.mTap.setBackgroundColor(Color.parseColor("GREEN"));
        if (orderListClr != null)
        {
            if (orderListClr.getFlagColor().equals("E"))
            {
              //  mViewHolder.mOrdno.setTextColor(Color.parseColor("#8B0000"));
                mViewHolder.mOrdno.setTextColor(Color.parseColor("BLACK"));
            }
        }

        return convertView;

    }


    private class ViewHolder
    {
        TextView mOrdno;
        TextView mTap; //Sb20191221
        TextView mSubcategory;
        TextView mProductNm;
        TextView mDesc;

    }
}




