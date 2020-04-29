package com.formax.pexelsgallery.requests;

import com.formax.pexelsgallery.util.Constans;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Interceptor {

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new okhttp3.Interceptor() {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Authorization", Constans.API_KEY)
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }
    }).build();
}
