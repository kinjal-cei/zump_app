package com.catch32.zumpbeta.apicall;

import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Executors;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ApiService
{
    private OnResponseListener onResponseListener;

    public ApiService()
    {
    }

    public ApiService(OnResponseListener onResponseListener, RequestType requestType, String endPoint,
                      HashMap<String, String> header, JSONObject bodyParam)
    {

        this.onResponseListener = onResponseListener;

        switch (requestType) {
            case GET:
                GET_REQUEST(endPoint, header);
                break;
            case POST:
                POST_REQUEST(endPoint, header, bodyParam);
                break;
            case PUT:
                PUT_REQUEST(endPoint, header, bodyParam);
                break;
            case DELETE:
                DELETE_REQUEST(endPoint, header, bodyParam);
                break;
            default:
                break;
        }
    }

    private void GET_REQUEST(String endPoint, HashMap<String, String> header)
    {
        System.out.println(endPoint);
        String TempbaseUrl= ApiEndPoint.BASE_URL;
        if(endPoint.contains("CheckLogin.php?")) TempbaseUrl="http://billpower.in:80/BP10/";
        AndroidNetworking.get(TempbaseUrl + endPoint)
                .addHeaders(header)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener()
                {
                    @Override
                    public void onResponse(Response okHttpResponse, String response)
                    {
                        if (!TextUtils.isEmpty(response)) {
                            if (onResponseListener != null) {
                                onResponseListener.onSuccess(response,
                                        convertHeadersToHashMap(okHttpResponse.headers()));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (onResponseListener != null)
                            onResponseListener.onError(anError.getErrorBody());
                    }
                });
    }

    private void POST_REQUEST(String endPoint, HashMap<String, String> header, JSONObject bodyParam) {
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        AndroidNetworking.post(ApiEndPoint.BASE_URL + endPoint)
                .addHeaders(header)
                .addJSONObjectBody(bodyParam)
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            if (onResponseListener != null) {
                                onResponseListener.onSuccess(response,
                                        convertHeadersToHashMap(okHttpResponse.headers()));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (onResponseListener != null)
                            onResponseListener.onError(anError.getErrorBody());
                    }
                });
    }

    private HashMap<String, String> convertHeadersToHashMap(Headers headers) {
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            result.put(headers.name(i), headers.value(i));
        }
        return result;
    }

    private void PUT_REQUEST(String endPoint, HashMap<String, String> header, JSONObject bodyParam) {
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        AndroidNetworking.put(ApiEndPoint.BASE_URL + endPoint)
                .addHeaders(header)
                .addJSONObjectBody(bodyParam)
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener()
                {
                    @Override
                    public void onResponse(Response okHttpResponse, String response)
                    {
                        if (!TextUtils.isEmpty(response))
                        {
                            if (onResponseListener != null)
                            {
                                onResponseListener.onSuccess(response,
                                        convertHeadersToHashMap(okHttpResponse.headers()));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        if (onResponseListener != null)
                            onResponseListener.onError(anError.getErrorBody());
                    }
                });
    }

    private void DELETE_REQUEST(String endPoint, HashMap<String, String> header, JSONObject bodyParam) {
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        AndroidNetworking.delete(ApiEndPoint.BASE_URL + endPoint)
                .addHeaders(header)
                .addJSONObjectBody(bodyParam)
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            if (onResponseListener != null) {
                                onResponseListener.onSuccess(response,
                                        convertHeadersToHashMap(okHttpResponse.headers()));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (onResponseListener != null)
                            onResponseListener.onError(anError.getErrorBody());
                    }
                });
    }

    public void UPLOAD_REQUEST(final OnResponseListener onResponseListener,
                               String endPoint, HashMap<String, String> header,
                               File fileUpload) {
        //header.put("Accept", "application/json");
        header.put("Content-Type", "multipart/form-data");

        AndroidNetworking.upload(ApiEndPoint.UPLOAD_BASE_URL + endPoint)
                .addHeaders(header)
                .addMultipartFile("profile_video", fileUpload)
                .setOkHttpClient(getConfigOkHttpClient())
                .setTag("uploadArtistData")
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            if (onResponseListener != null) {
                                onResponseListener.onSuccess(response,
                                        convertHeadersToHashMap(okHttpResponse.headers()));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getErrorDetail());
                        if (onResponseListener != null)
                            onResponseListener.onError(error.getErrorBody());
                    }
                });
    }

    public static OkHttpClient getConfigOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .build();
    }
}
