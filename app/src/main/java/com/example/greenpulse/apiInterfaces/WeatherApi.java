package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.responses.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("current")
    Call<WeatherResponse> getDailyWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("key") String apiKey,
            @Query("include") String include
    );
}
