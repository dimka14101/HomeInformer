package com.example.dmytro.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dmytro on 09.03.2017.
 */

public final class ApiModule {

    private static final String BASE_URL = "http://homeinformer2.azurewebsites.net/";
    private static Retrofit retrofit = null;

    public static ManagerApi getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ManagerApi.class);
    }

}
