package com.catch32.zumpbeta.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.listener.OrderDetailsSubmitListener;

/**
 * @author Ruchi Mehta
 * @version Jun 08, 2019
 */
public class OrderDetailsDialog extends FullScreenBaseDialogFragment implements View.OnClickListener {

    private RadioGroup mPayTermGrp;
    private OrderDetailsSubmitListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_order_details, container, false);
        initView(rootView);
        return rootView;
    }

    public void setListener(OrderDetailsSubmitListener listener) {
        this.mListener = listener;
    }

    private void initView(View view) {
        mPayTermGrp = view.findViewById(R.id.rg_payterm);

        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            int id = mPayTermGrp.getCheckedRadioButtonId();
            switch (id) {
                case R.id.rb_delivery:
                    mListener.onPayTermSelected(OrderDetailsSubmitListener.PAY_TERM.ON_DELIVERY);
                    break;
                case R.id.rb_next_cycle:
                    mListener.onPayTermSelected(OrderDetailsSubmitListener.PAY_TERM.ON_NEXT_CYCLE);
                    break;
            }
        }

        dismiss();
    }
}
