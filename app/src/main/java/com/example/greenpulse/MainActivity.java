package com.example.greenpulse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.adapters.WeatherAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView weatherRecyclerView;
    private WeatherAdapter weatherAdapter;
    private List<Weather> weatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and set layout manager
        weatherRecyclerView = findViewById(R.id.weather_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        weatherRecyclerView.setLayoutManager(layoutManager);

        // Sample weather data
        weatherList = new ArrayList<>();
        weatherList.add(new Weather(R.drawable.cloud, "25°C", "60%", "15mm", "10 km/h", "5", "Raleigh, NC"));
        weatherList.add(new Weather(R.drawable.sunny_svgrepo_com, "22°C", "55%", "5mm", "8 km/h", "3", "Raleigh, NC"));
        weatherList.add(new Weather(R.drawable.rain, "18°C", "80%", "20mm", "12 km/h", "8", "Raleigh, NC"));
        ;

        // Set Adapter
        weatherAdapter = new WeatherAdapter(weatherList);
        weatherRecyclerView.setAdapter(weatherAdapter);
    }
}
