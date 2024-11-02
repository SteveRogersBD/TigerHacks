package com.example.greenpulse;

public class Weather {
    private final int icon;
    private final String temperature;
    private final String humidity;
    private final String rain;
    private final String windSpeed;
    private final String uvIndex;  // New field
    private final String location;  // New field

    public Weather(int icon, String temperature, String humidity, String rain, String windSpeed, String uvIndex, String location) {
        this.icon = icon;
        this.temperature = temperature;
        this.humidity = humidity;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.uvIndex = uvIndex;  // Initialize new field
        this.location = location;  // Initialize new field
    }

    public int getIcon() {
        return icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getRain() {
        return rain;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getUvIndex() {  // Getter for UV index
        return uvIndex;
    }

    public String getLocation() {  // Getter for location
        return location;
    }
}
