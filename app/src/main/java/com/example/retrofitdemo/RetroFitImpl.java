package com.example.retrofitdemo;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitImpl {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static Retrofit retrofit = null;

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    static {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .build();


    public static Retrofit getRetrofit() {

        if (retrofit == null) {
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(BASE_URL);
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            retrofitBuilder.client(client);
            retrofit = retrofitBuilder.build();
        }
        return retrofit;
    }
}
