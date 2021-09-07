package com.catch32.zumpbeta.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Ruchi Mehta
 * @version Jun 19, 2019
 */
public class GsonFactory {

    private static Gson mInstance;

    public static Gson getInstance() {
        if (mInstance == null) {
            mInstance = new GsonBuilder().setPrettyPrinting().create();
        }
        return mInstance;
    }
}
