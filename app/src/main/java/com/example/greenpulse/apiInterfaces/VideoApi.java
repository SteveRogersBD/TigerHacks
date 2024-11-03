package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.responses.ArticleResponse;
import com.example.greenpulse.responses.ImageResponse;
import com.example.greenpulse.responses.NewsResponse;
import com.example.greenpulse.responses.YoutubeVideo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoApi {

    // Define the endpoint and query parameters
    @GET("search.json")
    Call<YoutubeVideo> getVideos(
            @Query("engine") String engine,
            @Query("search_query") String query,
            @Query("api_key") String apiKey,
            @Query("gl") String gl,
            @Query("hl") String hl
    );

    @GET("search.json")
    Call<ImageResponse> searchImages(
            @Query("engine") String engine,
            @Query("q") String query,
            @Query("location") String location,
            @Query("api_key") String apiKey // Include your API key as a parameter
    );

    @GET("search.json")
    Call<ArticleResponse> getNews(
            @Query("engine") String engine,
            @Query("q") String query,
            @Query("gl") String country,
            @Query("hl") String language,
            @Query("api_key") String apiKey // Include your API key here
    );
}
