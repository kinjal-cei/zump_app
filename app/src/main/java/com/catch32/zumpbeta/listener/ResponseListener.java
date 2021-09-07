package com.catch32.zumpbeta.listener;


/**
 * @author Ruchi Mehta
 * @version Jun 18, 2019
 */
public interface ResponseListener {
    void onResponse(String tag,String response);

    void onError(String tag,String error);
}
