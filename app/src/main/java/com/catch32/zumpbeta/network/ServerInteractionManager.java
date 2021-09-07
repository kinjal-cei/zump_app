package com.catch32.zumpbeta.network;


import com.catch32.zumpbeta.constant.AppConstant;
import com.catch32.zumpbeta.utils.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class ServerInteractionManager
{
    private static final String TAG = ServerInteractionManager.class.getSimpleName();

    public static String postDataToServer(String path, LinkedHashMap<String, String> params) {
        HttpURLConnection connection = null;
        String mResponse = null;
        try {
            mResponse = AppConstant.RESPONSE_APP_ERROR;
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(AppConstant.REQUEST_TIMEOUT);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = connection.getResponseCode();
            Logger.d(TAG, "Result Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                mResponse = "";
                String line;
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null)
                {
                    mResponse += line;
                }
                is.close();
                br.close();
            } else {
                connection.disconnect();
                mResponse = AppConstant.RESPONSE_HTTP_ERROR;
            }
        } catch (SocketTimeoutException | ConnectException e)
        {
            Logger.e(TAG, "postDataToServer: " + e.getMessage());
            mResponse = AppConstant.RESPONSE_TIMEOUT_ERROR;
        } catch (Exception e)
        {
            e.printStackTrace();
            Logger.e(TAG, "postDataToServer: " + e.getClass());
            mResponse = AppConstant.RESPONSE_APP_ERROR;
        } finally {
            Logger.d(TAG, "Response: " + mResponse);
            Logger.d(TAG, "Disconnected..");
            if (connection != null)
                connection.disconnect();
        }

        return mResponse;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        Logger.d(TAG, "Request : " + result);

        return result.toString();
    }

}