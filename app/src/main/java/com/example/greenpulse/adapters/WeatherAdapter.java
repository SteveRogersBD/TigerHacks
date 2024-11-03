package com.example.greenpulse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.responses.WeatherResponse;
import com.squareup.picasso.Picasso;

import java.util.List;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{

    Context context;
    List<WeatherResponse.Forecastday>days;

    public WeatherAdapter(Context context, List<WeatherResponse.Forecastday> days) {
        this.context = context;
        this.days = days;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherResponse.Forecastday a = days.get(position);


        holder.temp.setText(a.day.avgtemp_c + "°C"); // Replace with your desired temperature format
        holder.desc.setText(a.day.condition.text); // Description of the weather condition

        // Set button text or other properties if needed
        holder.humi.setText(a.day.avghumidity + "%"); // Example for humidity
        holder.rain.setText(a.day.daily_chance_of_rain + ""); // Example for rain chance
        holder.moist.setText(a.day.maxwind_kph+""); // Or another appropriate value
        String url = a.day.condition.icon.substring(2);
        Picasso.get().load(url).into(holder.sunny);
    }


    @Override
    public int getItemCount() {
        return days.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sunny;
        AppCompatButton rain,humi,moist;
        TextView temp,desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sunny = itemView.findViewById(R.id.weather_poster);
            rain = itemView.findViewById(R.id.rain_btn);
            humi = itemView.findViewById(R.id.humidity_btn);
            moist = itemView.findViewById(R.id.moisture_btn);
            temp = itemView.findViewById(R.id.temp_tv);
            desc = itemView.findViewById(R.id.description_tv);

        }
    }
}
