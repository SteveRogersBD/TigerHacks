package com.example.greenpulse.models;

public class DiseasedCrop {
    public String name;
    public String type;
    public String weather;
    public String location;
    public String affectedArea;
    public String growthStage;

    public DiseasedCrop(String name, String type, String weather, String location,
                        String affectedArea, String growthStage) {
        this.name = name;
        this.type = type;
        this.weather = weather;
        this.location = location;
        this.affectedArea = affectedArea;
        this.growthStage = growthStage;
    }

    public DiseasedCrop() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAffectedArea() {
        return affectedArea;
    }

    public void setAffectedArea(String affectedArea) {
        this.affectedArea = affectedArea;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }
}
