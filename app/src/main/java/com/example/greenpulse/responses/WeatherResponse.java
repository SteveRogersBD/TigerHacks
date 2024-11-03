package com.example.greenpulse.responses;

import java.util.ArrayList;
import java.util.List;

public class WeatherResponse {


    public Location location;
    public Current current;
    public Forecast forecast;
    public class Astro{
        public String sunrise;
        public String sunset;
        public String moonrise;
        public String moonset;
        public String moon_phase;
        public int moon_illumination;
        public int is_moon_up;
        public int is_sun_up;
    }

    public class Condition{
        public String text;
        public String icon;
        public int code;
    }

    public class Current{
        public double temp_c;
        public int is_day;
        public Condition condition;
        public double wind_kph;
        public int wind_degree;
        public double pressure_mb;
        public double precip_mm;
        public int humidity;
        public int cloud;
        public double feelslike_c;
        public double windchill_c;
        public double heatindex_c;
        public double dewpoint_c;
        public double vis_km;
        public double uv;
        public double gust_kph;
    }

    public class Day{
        public double maxtemp_c;
        public double mintemp_c;
        public double avgtemp_c;
        public double maxwind_kph;
        public double totalprecip_mm;
        public double totalsnow_cm;
        public double avgvis_km;
        public int avghumidity;
        public int daily_will_it_rain;
        public int daily_chance_of_rain;
        public int daily_will_it_snow;
        public int daily_chance_of_snow;
        public Condition condition;
        public double uv;
    }

    public class Forecast{
        public ArrayList<Forecastday> forecastday;
    }

    public class Forecastday{
        public String date;
        public Day day;
        public Astro astro;
        public ArrayList<Hour> hour;
    }

    public class Hour{
        public int time_epoch;
        public String time;
        public double temp_c;
        public double temp_f;
        public int is_day;
        public Condition condition;
        public double wind_mph;
        public double wind_kph;
        public int wind_degree;
        public String wind_dir;
        public double pressure_mb;
        public double pressure_in;
        public double precip_mm;
        public double precip_in;
        public double snow_cm;
        public int humidity;
        public int cloud;
        public double feelslike_c;
        public double feelslike_f;
        public double windchill_c;
        public double windchill_f;
        public double heatindex_c;
        public double heatindex_f;
        public double dewpoint_c;
        public double dewpoint_f;
        public int will_it_rain;
        public int chance_of_rain;
        public int will_it_snow;
        public int chance_of_snow;
        public double vis_km;
        public double vis_miles;
        public double gust_mph;
        public double gust_kph;
        public double uv;
    }

    public class Location{
        public String name;
        public String region;
        public String country;
        public double lat;
        public double lon;
        public String tz_id;
        public int localtime_epoch;
        public String localtime;
    }
}


