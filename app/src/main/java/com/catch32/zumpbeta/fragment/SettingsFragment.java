package com.catch32.zumpbeta.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.utils.IBaseMenuActivity;

import androidx.fragment.app.Fragment;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public class SettingsFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

    }

    private void initNavigationView() {
        if (getActivity() instanceof IBaseMenuActivity) {
            IBaseMenuActivity baseMenuActivity = (IBaseMenuActivity) getActivity();
            baseMenuActivity.setActionbarTitle("Settings");
            baseMenuActivity.checkMenuItem(R.id.nav_terms_conditions);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initNavigationView();
    }

}

