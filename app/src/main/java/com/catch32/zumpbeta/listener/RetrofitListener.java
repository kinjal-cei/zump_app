package com.catch32.zumpbeta.listener;

import retrofit2.Response;

/**
 * @author Ruchi Mehta
 * @version Jun 14, 2019
 */
public interface RetrofitListener<T> {
    void onResponse(Response<T> response);

    void onError(Throwable throwable);
}
