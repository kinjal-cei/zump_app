package com.catch32.zumpbeta.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.listener.QtyAddListener;
import com.catch32.zumpbeta.model.ProductList;
import com.catch32.zumpbeta.utils.KeyboardHelper;
import com.catch32.zumpbeta.utils.WidgetUtil;
import com.google.android.material.textfield.TextInputLayout;

public class QtyAddOrUpdateDialog extends FullWidthBaseDialogFragment implements View.OnClickListener {

    private Context mContext;
    private TextInputLayout mQtyTIL;
    private EditText mQtyNameEt;
    private QtyAddListener mListener;
    private ProductList mProductList;

    public void setAddOrUpdateListener(QtyAddListener mListener) {
        this.mListener = mListener;
    }


    public void setProductList(ProductList mProductList) {
        this.mProductList = mProductList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_qty, container, false);
        configureComponent(view);
        return view;
    }

    private void configureComponent(View view) {
        mContext = getActivity();

        TextView titleTxt = (TextView) view.findViewById(R.id.txt_title);
        TextView infoTxt = (TextView) view.findViewById(R.id.txt_info);

        view.findViewById(R.id.btn_ok).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mQtyTIL = (TextInputLayout) view.findViewById(R.id.til_name);
        mQtyNameEt = (EditText) view.findViewById(R.id.et_name);

        titleTxt.setText("Quantity : " + mProductList.getProd_qty());
        infoTxt.setText(mProductList.getProd_nm());

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_cancel) {
            dismiss();
        } else if (id == R.id.btn_ok) {
            KeyboardHelper.hideSoftKeyboard(mContext, mQtyNameEt);
            String qty = mQtyNameEt.getEditableText().toString().trim();

            if (TextUtils.isEmpty(qty)) {
                WidgetUtil.showTextInputLayoutError(mQtyTIL, "Quantity cannot be empty");
                return;
            }

            WidgetUtil.hideTextInputLayoutError(mQtyTIL);
            mListener.onQtyAdded(Integer.parseInt(qty));
            dismiss();
        }
    }


}