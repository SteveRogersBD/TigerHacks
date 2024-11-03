package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.responses.GNewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("search")
    Call<GNewsResponse> searchNews(
            @Query("q") String query,
            @Query("apikey") String apiKey,
            @Query("max") int maxResults,
            @Query("lang") String language,
            @Query("country") String country
    );
}
