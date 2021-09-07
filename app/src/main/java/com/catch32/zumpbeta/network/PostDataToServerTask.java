package com.catch32.zumpbeta.network;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;

import com.catch32.zumpapp.R;
import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.dialog.ProgressDialog;
import com.catch32.zumpbeta.listener.ResponseListener;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;


public class PostDataToServerTask extends AsyncTask<Void, Void, String> implements DialogInterface.OnCancelListener
{
    public static final String TAG = PostDataToServerTask.class.getSimpleName();
    private String mPath;
    private LinkedHashMap<String, String> mPostParams;
    private Context mContext;
    private ResponseListener mResponseListener;
    private ProgressDialog mProgressBar;
    private boolean mShowProgress;
    private String mTag;
    private boolean mStopTask;

    public PostDataToServerTask(Context context, String action)
    {
        mContext = context;
        mShowProgress = true;
        mTag = action;
    }
    public PostDataToServerTask setRequestParams(JsonObject params)
    {
        mPostParams = new LinkedHashMap<>();
        mPostParams.put("Param", params.toString());
        return this;
    }
    public PostDataToServerTask setPath(String name)
    {
        mPath = name;
        return this;
    }

    public PostDataToServerTask setResponseListener(ResponseListener responseListener)
    {
        mResponseListener = responseListener;
        return this;
    }
    public PostDataToServerTask showProgress(boolean showProgress)
    {
        mShowProgress = showProgress;
        return this;
    }

    public void makeCall()
    {
        if (mShowProgress)
        showProgressDialog(mContext);
        this.execute();
    }

    @Override
    protected String doInBackground(Void... params)
    {
        return ServerInteractionManager.postDataToServer(mPath, mPostParams);
    }

    @Override
    protected void onPostExecute(final String result)
    {
        super.onPostExecute(result);
        new Handler().
                postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mProgressBar.isShowing())
                            dismissProgressDialog();
                        if (!mStopTask)
                        {
                            switch (result)
                            {
                                case AppConstant.RESPONSE_HTTP_ERROR:
                                    mResponseListener.onError(mTag,mContext.getString(R.string.http_error));
                                    break;
                                case AppConstant.RESPONSE_TIMEOUT_ERROR:
                                    mResponseListener.onError(mTag,mContext.getString(R.string.server_error));
                                    break;
                                case AppConstant.RESPONSE_APP_ERROR:
                                    mResponseListener.onError(mTag,mContext.getString(R.string.default_error));
                                    break;
                                default:
                                    mResponseListener.onResponse(mTag,result);
                                    break;
                            }

                        }


                    }
                }, 1000);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface)
    {
        mStopTask = true;
    }
    private void showProgressDialog(Context context)
    {
        if (context instanceof Activity)
        {
            mProgressBar = ProgressDialog.getProgressDialog(context, false);
            mProgressBar.show();
        } else {
            throw new RuntimeException("Require Activity Instance Instead of Context");
        }

    }

    private void dismissProgressDialog()
    {
        if (mProgressBar != null) mProgressBar.dismiss();
    }
}
