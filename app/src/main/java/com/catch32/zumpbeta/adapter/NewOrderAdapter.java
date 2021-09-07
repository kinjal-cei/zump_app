package com.catch32.zumpbeta.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.activity.NewOrderActivity;
import com.catch32.zumpbeta.model.SubCategoryProdList;

import java.util.List;

/**
 * @author Ruchi Mehta
 * @version Jul 19, 2019
 */
public class NewOrderAdapter extends ArrayAdapter<SubCategoryProdList>
{

    private NewOrderActivity mActivity;
    private List<SubCategoryProdList> mList;

    public NewOrderAdapter(NewOrderActivity context, List<SubCategoryProdList> values)
    {
        super(context, R.layout.adapter_new_order, values);
        this.mActivity = context;
        this.mList = values;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SubCategoryProdList getItem(int position) {
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
            LayoutInflater mInflater = (LayoutInflater) mActivity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_new_order, parent, false);
            mViewHolder.mTitle = (TextView) convertView.findViewById(R.id.txt_title);
            mViewHolder.mMRP = (TextView) convertView.findViewById(R.id.txt_mrp);
            mViewHolder.mMOQ = (EditText) convertView.findViewById(R.id.txt_moq);

            mViewHolder.mAddTimeoutBtn = convertView.findViewById(R.id.btn_add);
            mViewHolder.mMinTimeoutBtn = convertView.findViewById(R.id.btn_remove);

            mViewHolder.mWatcher = new MutableWatcher();
            mViewHolder.mWatcher.setEditText(mViewHolder.mMOQ);

            mViewHolder.mMOQ.addTextChangedListener(mViewHolder.mWatcher);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        final SubCategoryProdList subCategoryProdList = mList.get(position);

        if (subCategoryProdList.getQty() != null && subCategoryProdList.getQty().isEmpty()) {
            subCategoryProdList.setQty("");
        }
        mViewHolder.mAddTimeoutBtn.setTag(subCategoryProdList);
        mViewHolder.mMinTimeoutBtn.setTag(subCategoryProdList);
        mViewHolder.mTitle.setText(subCategoryProdList.getSubCategoryProdName()+subCategoryProdList.getProdCount());


        //TODO what to take? TRP or OFFER
        if (subCategoryProdList.getColor().equals("B")) //SB20191220
            mViewHolder.mMRP.setText(subCategoryProdList.getOffer()+" (TRP: Rs."+subCategoryProdList.getSubCategoryProdBasic()+subCategoryProdList.getSubCategoryProdGST()+")"+" LP: Rs."+subCategoryProdList.getSubCategoryProdTrp());
        else {
            mViewHolder.mMRP.setText(subCategoryProdList.getOffer()+" (TRP: Rs."+subCategoryProdList.getSubCategoryProdBasic()+subCategoryProdList.getSubCategoryProdGST()+")"+" LP: Rs."+subCategoryProdList.getSubCategoryProdTrp()+ "  [PoM]");
            mViewHolder.mMRP.setTextColor(Color.parseColor("BLUE"));
        }

        if (subCategoryProdList.getColor().equals("B")) {
            mViewHolder.mTitle.setTextColor(Color.parseColor("#000000"));
        } else
            mViewHolder.mTitle.setTextColor(Color.parseColor("#1874CD"));

        mViewHolder.mWatcher.setActive(false);
        mViewHolder.mMOQ.setText(subCategoryProdList.getQty());
        mViewHolder.mWatcher.setSubCategoryProdList(subCategoryProdList);

        mViewHolder.mWatcher.setActive(true);

        final ViewHolder finalMViewHolder = mViewHolder;


        mViewHolder.mAddTimeoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubCategoryProdList subCategoryProdList = (SubCategoryProdList) view.getTag();

                if (subCategoryProdList.getQty() != null && subCategoryProdList.getQty().isEmpty()) {
                    subCategoryProdList.setQty(subCategoryProdList.getMoq());
                } else {
                    String txtQty = finalMViewHolder.mMOQ.getText().toString();
                    int moq = Integer.valueOf(subCategoryProdList.getMoq());
                    int qty = Integer.valueOf(txtQty);

                    if (qty < moq)
                        qty = moq;
                    else qty++;

                    subCategoryProdList.setQty(String.valueOf(qty));
                }

                mActivity.updateCountAndAmount();
                notifyDataSetChanged();
            }
        });


        mViewHolder.mMinTimeoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubCategoryProdList subCategoryProdList = (SubCategoryProdList) view.getTag();
                if (subCategoryProdList.getQty() != null && subCategoryProdList.getQty().isEmpty()) {
                    subCategoryProdList.setQty(subCategoryProdList.getMoq());
                } else {
                    int moq = Integer.valueOf(subCategoryProdList.getMoq());
                    String txtQty = finalMViewHolder.mMOQ.getText().toString();
                    int qty = Integer.valueOf(txtQty);
                    qty--;
                    String qtyStr = String.valueOf(qty);
                    if (qty < moq) qtyStr = "";

                    subCategoryProdList.setQty(qtyStr);
                }

                mActivity.updateCountAndAmount();
                notifyDataSetChanged();
            }


        });

        return convertView;
    }


    public class ViewHolder {
        TextView mTitle;
        TextView mMRP;
        EditText mMOQ;
        View mAddTimeoutBtn;
        View mMinTimeoutBtn;
        MutableWatcher mWatcher;
    }

    class MutableWatcher implements TextWatcher {

        private SubCategoryProdList mSubCategoryProdList;
        private boolean mActive;
        private EditText mEditText;


        public void setSubCategoryProdList(SubCategoryProdList subCategoryProdList) {
            this.mSubCategoryProdList = subCategoryProdList;
        }

        public void setEditText(EditText editText) {
            this.mEditText = editText;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mActive) {
                mSubCategoryProdList.setQty(s.toString());
                mActivity.updateCountAndAmount();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                mEditText.setSelection(mEditText.getText().length());
            }
        }
    }

}

