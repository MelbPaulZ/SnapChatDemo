package com.example.paul.snapchatdemo.utils;

import com.example.paul.snapchatdemo.bean.C;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


/**
 * Created by Paul on 20/09/2016.
 */
public class HttpUtil {
    // this is generic method for access to server
    public static <S> S accessServer(Class<S> serviceClass){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(C.api.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

}
