package com.example.greenpulse.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.Weather;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<Weather> weatherList;

    public WeatherAdapter(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.weatherIcon.setImageResource(weather.getIcon());
        holder.weatherTemp.setText("Temperature: " + weather.getTemperature());
        holder.weatherHumidity.setText("Humidity: " + weather.getHumidity());
        holder.weatherRain.setText("Rain: " + weather.getRain());
        holder.weatherWindSpeed.setText("Wind Speed: " + weather.getWindSpeed());
        holder.weatherUvIndex.setText("UV Index: " + weather.getUvIndex());
        holder.weatherLocation.setText("Location: " + weather.getLocation());

        // Set icons for each attribute
        holder.iconTemperature.setImageResource(R.drawable.baseline_device_thermostat_24); // Temperature icon
        holder.iconHumidity.setImageResource(R.drawable.humidity); // Humidity icon
        holder.iconRain.setImageResource(R.drawable.rain); // Rain icon
        holder.iconWindSpeed.setImageResource(R.drawable.wind); // Wind speed icon
        holder.iconUvIndex.setImageResource(R.drawable.uv_index); // UV index icon
        holder.iconLocation.setImageResource(R.drawable.map); // Location icon
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        ImageView weatherIcon, iconTemperature, iconHumidity, iconRain, iconWindSpeed, iconUvIndex, iconLocation;
        TextView weatherTemp, weatherHumidity, weatherRain, weatherWindSpeed, weatherUvIndex, weatherLocation;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherIcon = itemView.findViewById(R.id.weather_icon);
            weatherTemp = itemView.findViewById(R.id.weather_temp);
            weatherHumidity = itemView.findViewById(R.id.weather_humidity);
            weatherRain = itemView.findViewById(R.id.weather_rain);
            weatherWindSpeed = itemView.findViewById(R.id.weather_wind_speed);
            weatherUvIndex = itemView.findViewById(R.id.weather_uv_index);
            weatherLocation = itemView.findViewById(R.id.weather_location);

            // Initialize icon references
            iconTemperature = itemView.findViewById(R.id.icon_temperature);
            iconHumidity = itemView.findViewById(R.id.icon_humidity);
            iconRain = itemView.findViewById(R.id.icon_rain);
            iconWindSpeed = itemView.findViewById(R.id.icon_wind_speed);
            iconUvIndex = itemView.findViewById(R.id.icon_uv_index);
            iconLocation = itemView.findViewById(R.id.icon_location);
        }
    }
}
