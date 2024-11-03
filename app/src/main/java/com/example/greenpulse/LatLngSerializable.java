package com.example.greenpulse;

import com.google.android.gms.maps.model.LatLng;

public class LatLngSerializable {
    private double latitude;
    private double longitude;

    // No-argument constructor required for Firebase
    public LatLngSerializable() {
    }

    public LatLngSerializable(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }
}
