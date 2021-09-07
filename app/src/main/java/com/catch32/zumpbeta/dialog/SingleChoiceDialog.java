package com.catch32.zumpbeta.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.model.CategoryReportData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SingleChoiceDialog extends FullWidthBaseDialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TITLE = "TITLE";
    public static final String ITEMS = "ITEMS";
    public static final String TAG = "TAG";

    private OnItemClickListener mItemClickListener;
    private SingleChoiceAdapter mSingleChoiceAdapter;
    private String mTag;
    private Map<String, String> mTitleMap;
    private CategoryReportData mReport;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }


    public void setReport(CategoryReportData categoryReportData) {
        this.mReport = categoryReportData;
    }

    public void setItems(Map<String, String> titleMap) {
        this.mTitleMap = titleMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_single_choice, container, false);
        initComponent(rootView);
        return rootView;
    }


    private void initComponent(View rootView) {
        Bundle bundle = getArguments();
        String title = bundle.getString(TITLE);

        TextView titleTxt = (TextView) rootView.findViewById(R.id.txt_title);
        TextView titleTxtProd = (TextView) rootView.findViewById(R.id.txt_titleProd); //SB20200211
        if (!TextUtils.isEmpty(title)) {
            titleTxtProd.setText(mReport.getCM_NAME()); //SB20200211
            titleTxt.setText(title);
        } else {
            titleTxtProd.setText(mReport.getCM_NAME()); //SB20200211
            titleTxt.setText("Select Action");
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        mSingleChoiceAdapter = new SingleChoiceAdapter(getActivity(), mTitleMap, false);
        listView.setAdapter(mSingleChoiceAdapter);
        listView.setOnItemClickListener(this);

        rootView.findViewById(R.id.btn_submit).setOnClickListener(this);
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(mSingleChoiceAdapter.getSelectedItem(), mTag, mReport);
                }
                break;

        }

        try {
            dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String value = mSingleChoiceAdapter.getItem(position);
        mSingleChoiceAdapter.pickValue(value);
    }

    private class SingleChoiceAdapter extends ArrayAdapter<String> {
        private LayoutInflater mLayoutInflater;
        private boolean mAllowMultiChoice;
        private Set<String> mPickedSet;
        private Map<String, String> mMap;

        private SingleChoiceAdapter(Context context, Map<String, String> object, boolean allowMultiChoice) {
            super(context, R.layout.adapter_single_choice, new ArrayList<String>(object.keySet()));
            mLayoutInflater = LayoutInflater.from(context);
            this.mAllowMultiChoice = allowMultiChoice;
            mPickedSet = new HashSet<>();
            mMap = object;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.adapter_single_choice, parent, false);
                holder.label = (TextView) convertView.findViewById(R.id.label);
                holder.selectRB = (RadioButton) convertView.findViewById(R.id.rb_choice);

                holder.selectRB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String app = (String) v.getTag();
                        pickValue(mMap.get(app));
                    }
                });

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String value = getItem(position);

            holder.selectRB.setTag(value);
            holder.label.setSelected(true);

            holder.selectRB.setChecked(mPickedSet.contains(mMap.get(value)));
            holder.selectRB.setVisibility(mAllowMultiChoice ? View.GONE : View.VISIBLE);

            holder.label.setText(value);

            return convertView;
        }

        private class ViewHolder {
            private TextView label;
            private RadioButton selectRB;
        }

        public void pickValue(String value) {
            if (mPickedSet.contains(value))
                mPickedSet.remove(value);
            else {
                if (!mAllowMultiChoice) clearPicked();
                mPickedSet.add(value);
            }

            notifyDataSetChanged();
        }

        public void clearPicked() {
            mPickedSet.clear();
            notifyDataSetChanged();
        }

        public List<String> getSelectedItem() {
            return new ArrayList<>(mPickedSet);
        }


    }

    public interface OnItemClickListener {
        void OnItemClick(List<String> item, String tag, CategoryReportData report);
    }

}
