package com.example.greenpulse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.responses.WeatherResponse;

import java.util.List;

public class TempAdapter extends RecyclerView.Adapter<TempAdapter.ViewHolder>{

    Context context;
    List<WeatherResponse.Datum>weatherList;


    public TempAdapter(Context context, List<WeatherResponse.Datum> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherResponse.Datum data = weatherList.get(position);
        String details = data.city_name+", "+data.state_code+", "+data.datetime;
        holder.location.setText(details);
        // Set the temperature
        holder.temp.setText(String.format("Temperature: %d°C", (int) data.temp));

        // Set the humidity
        holder.humid.setText(String.format("Humidity: %d%%", data.rh));

        // Set the rain amount
        holder.rain.setText(String.format("Rain: %.2f mm", data.precip));

        // Set the wind speed
        holder.wndSpeed.setText(String.format("Wind Speed: %.2f km/h", (int) data.wind_spd));

        // Set the UV index
        holder.uv.setText(String.format("UV Index: %.2f", (int) data.uv));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView temp,humid,rain, wndSpeed,uv,location;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.weather_temp);
            humid = itemView.findViewById(R.id.weather_humidity);
            rain = itemView.findViewById(R.id.weather_rain);
            wndSpeed = itemView.findViewById(R.id.weather_wind_speed);
            uv = itemView.findViewById(R.id.weather_uv_index);
            location = itemView.findViewById(R.id.weather_location);
        }
    }
}
