package com.example.greenpulse.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static Retrofit weatherFit = new Retrofit.Builder().
            baseUrl("https://api.weatherbit.io/v2.0/").addConverterFactory(GsonConverterFactory.create()).
            build();

    public static Retrofit seraFit = new Retrofit.Builder().
            baseUrl("https://serpapi.com/").addConverterFactory(GsonConverterFactory.create()).
            build();
    public static Retrofit newsFit = new Retrofit.Builder().
            baseUrl("https://gnews.io/api/v4/").addConverterFactory(GsonConverterFactory.create()).
            build();
}
