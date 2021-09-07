package com.catch32.zumpbeta.utils;


import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.catch32.zumpapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.content.ContextCompat;


public class WidgetUtil {

    public static void showTextInputLayoutError(TextInputLayout til, int resId) {
        showTextInputLayoutError(til, til.getContext().getString(resId));
    }

    public static void showErrorToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showTextInputLayoutError(TextInputLayout til, String message) {
        til.setErrorEnabled(true);
        til.setError(message);
    }

    public static void hideTextInputLayoutError(TextInputLayout til) {
        til.setError(null);
        til.setErrorEnabled(false);
    }

    public static void showSnackbar(View view, int resId) {
        showSnackbar(view, view.getContext().getString(resId));
    }

    public static void showSnackbar(View view, String message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.red_500));
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
    }

}
