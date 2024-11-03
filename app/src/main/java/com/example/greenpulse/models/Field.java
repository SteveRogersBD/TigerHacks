package com.example.greenpulse.models;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class Field {
    private String description;
    private List<LatLng> polygon; // List of coordinates for the polygon
    private String address; // Address of the field
    private String weather; // Weather information
    private String soil; // Soil conditions

    // Constructor
    public Field(String description,List<LatLng> polygon, String address, String weather, String soil
                 ) {
        this.description = description;
        this.polygon = polygon;
        this.address = address;
        this.weather = weather;
        this.soil = soil;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters and Setters
    public List<LatLng> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<LatLng> polygon) {
        this.polygon = polygon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }


    public Field() {
    }
}
