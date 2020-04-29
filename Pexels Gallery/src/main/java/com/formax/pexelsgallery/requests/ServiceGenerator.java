package com.formax.pexelsgallery.requests;

import com.formax.pexelsgallery.util.Constans;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Constans.MAIN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(Interceptor.okHttpClient);

    private static Retrofit retrofit = retrofitBuilder.build();

    private static PexelsAPI pexelsAPI = retrofit.create(PexelsAPI.class);

    static PexelsAPI getPexelsAPI() {
        return pexelsAPI;
    }
}
