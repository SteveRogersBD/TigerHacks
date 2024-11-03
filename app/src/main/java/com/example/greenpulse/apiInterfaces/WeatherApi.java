package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.responses.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("forecast.json")
    Call<WeatherResponse> getWeatherForecast(
            @Query("key") String apiKey,
            @Query("q") String location,
            @Query("days") int days
    );
}
