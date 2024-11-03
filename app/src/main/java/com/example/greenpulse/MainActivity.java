package com.example.greenpulse;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.greenpulse.adapters.VideoAdapter;
import com.example.greenpulse.adapters.WeatherAdapter;
import com.example.greenpulse.apiInterfaces.NewsApi;
import com.example.greenpulse.apiInterfaces.VideoApi;
import com.example.greenpulse.apiInterfaces.WeatherApi;
import com.example.greenpulse.databinding.ActivityMainBinding;
import com.example.greenpulse.responses.GNewsResponse;
import com.example.greenpulse.responses.WeatherResponse;
import com.example.greenpulse.responses.YoutubeVideo;
import com.example.greenpulse.retrofit.RetrofitInstance;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    NewsAdapter newsAdapter;
    VideoAdapter videoAdapter;
    Location location;
    ActivityMainBinding binding;
    WeatherApi weatherApi;
    VideoApi videoApi;
    NewsApi newsApi;
    WeatherAdapter weatherAdapter;
    List<YoutubeVideo.VideoResult>videoList;
    List<GNewsResponse.Article>articleList;
    List<WeatherResponse.Forecastday>forecastdays;
    String address = "";
    String temp = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkLocationPermission();
        videoApi = RetrofitInstance.seraFit.create(VideoApi.class);
        newsApi = RetrofitInstance.newsFit.create(NewsApi.class);
        weatherApi = RetrofitInstance.weatherFit.create(WeatherApi.class);
        dealWithVideo();
        dealWithNews();
        handleBttomMenu();





    }

    private void dealWithWeather(String location) {
        weatherApi.getWeatherForecast(getString(R.string.weather_key),location,10).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {

                    forecastdays = response.body().forecast.forecastday;
                    String t = response.body().location.name;
                    address = address.concat(t+", "+response.body().location.name);
                    temp = temp.concat(response.body().forecast.forecastday.get(0).day.avgtemp_c+"");
                    weatherAdapter = new WeatherAdapter(MainActivity.this,forecastdays);
                    binding.weatherRecyclerView.setAdapter(weatherAdapter);
                    binding.weatherRecyclerView.setLayoutManager(new LinearLayoutManager(
                            MainActivity.this,LinearLayoutManager.HORIZONTAL,false
                    ));
                }
                else{

                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable throwable) {

            }
        });
    }

    private void dealWithNews() {

        Call<GNewsResponse> newsCall = newsApi.searchNews("crops OR agriculture OR farming OR agribusiness",
                getString(R.string.news_api_key),10,"en","us");
        newsCall.enqueue(new Callback<GNewsResponse>() {
            @Override
            public void onResponse(Call<GNewsResponse> call, Response<GNewsResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    articleList = response.body().articles;
                    newsAdapter = new NewsAdapter(MainActivity.this,articleList);
                    binding.news.setAdapter(newsAdapter);
                    LinearLayoutManager lm = new LinearLayoutManager(MainActivity.this,
                            LinearLayoutManager.HORIZONTAL,false);
                    binding.news.setLayoutManager(lm);
                }
                else{
                    Toast.makeText(MainActivity.this, response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GNewsResponse> call, Throwable throwable) {

            }
        });

    }

    private void dealWithVideo() {
        videoApi.getVideos("youtube","trending agriculture and farming videos",
                getString(R.string.video_api_key),"us","en").enqueue(new Callback<YoutubeVideo>() {
            @Override
            public void onResponse(Call<YoutubeVideo> call, Response<YoutubeVideo> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    videoList = response.body().video_results;
                    videoAdapter = new VideoAdapter(MainActivity.this,videoList);
                    binding.videos.setAdapter(videoAdapter);
                    LinearLayoutManager gm = new LinearLayoutManager(MainActivity.this,
                            LinearLayoutManager.HORIZONTAL,false);
                    binding.videos.setLayoutManager(gm);
                    Toast.makeText(MainActivity.this, response.body().toString(),
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<YoutubeVideo> call, Throwable throwable) {

            }
        });

    }
    

    //________method__________
    //weather logic



    // Check if location permission is granted
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, get the location
            getCurrentLocation();
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                // Permission denied, show a message to the user
            }
        }
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // You can also request permissions here
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        this.location = location;
                        String add = location.getLatitude()+","+location.getLongitude();
                        dealWithWeather(add);


                    } else {
                        Toast.makeText(this, "Location not found!!!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public void handleBttomMenu(){
        binding.bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.home){
                    //do nothing stay there
                }
                else if(id==R.id.disease){
                    Intent intent = new Intent(MainActivity.this,DiseaseAnalyzeActivity.class);
                    intent.putExtra("address",address);
                    intent.putExtra("tempC",temp);
                    startActivity(intent);
                }
                else if(id==R.id.search){
                    //goTo(DiseaseAnalyzeActivity.class);
                }
                else if(id==R.id.map){
                    goTo(MapActivity.class);
                }
                else if(id==R.id.social_media){
                    //do nothing for now
                    goTo(BotActivity.class);
                }
                return false;
            }
        });


    }

    private void goTo(Class<?> destinity) {
        startActivity(new Intent(MainActivity.this,destinity));
    }
}
