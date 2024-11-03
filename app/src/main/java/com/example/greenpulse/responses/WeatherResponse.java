package com.example.greenpulse.responses;

import java.util.ArrayList;
import java.util.List;

public class WeatherResponse {

    public int count;
    public ArrayList<Datum> data;
    public class Datum{
        public double app_temp;
        public int aqi;
        public String city_name;
        public int clouds;
        public String country_code;
        public String datetime;
        public int dewpt;
        public int dhi;
        public int dni;
        public double elev_angle;
        public int ghi;
        public int gust;
        public int h_angle;
        public double lat;
        public double lon;
        public String ob_time;
        public String pod;
        public int precip;
        public double pres;
        public int rh;
        public double slp;
        public int snow;
        public int solar_rad;
        public ArrayList<String> sources;
        public String state_code;
        public String station;
        public String sunrise;
        public String sunset;
        public double temp;
        public String timezone;
        public int ts;
        public int uv;
        public int vis;
        public Weather weather;
        public String wind_cdir;
        public String wind_cdir_full;
        public int wind_dir;
        public int wind_spd;
    }

    public class Weather{
        public int code;
        public String icon;
        public String description;
    }
}


