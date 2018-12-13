package com.vu.hp.sunshine.app.Retrofit;

import com.vu.hp.sunshine.app.Common.Common;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl(Common.HOME_PAGE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }
}
