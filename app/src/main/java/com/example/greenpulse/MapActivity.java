package com.example.greenpulse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.greenpulse.databinding.ActivityMapBinding;
import com.example.greenpulse.models.Field;
import com.example.greenpulse.models.Task;
import com.example.greenpulse.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private List<LatLng> polygonPoints = new ArrayList<>();
    private Polygon polygon;
    private List<com.google.android.gms.maps.model.Circle> circles = new ArrayList<>(); // List to hold circles

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Dialog dialog;
    ActivityMapBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize

        db=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //retrieveFieldData();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPolygons();
            }
        });
        binding.drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawPolygon();
                setTheDialog();
            }
        });
    }

    private void setTheDialog() {
        // Initialize the dialog with the current activity context
        dialog = new Dialog(MapActivity.this);

        // Set the dialog's content view (replace `R.layout.dialog_layout` with your custom layout)
        dialog.setContentView(R.layout.field_prompt_layout);

        // Set width and height
        Window window = dialog.getWindow();
        if (window != null) {
            // Set the dialog width and height (MATCH_PARENT makes it fullscreen horizontally)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // Set optional animations for showing and dismissing the dialog
            window.setWindowAnimations(android.R.style.Animation_Dialog);
        }

        // Optional: Set the dialog to be cancelable or non-cancelable
        dialog.setCancelable(true);

        // Show the dialog
        dialog.show();

        Field field = new Field();

        EditText soilET = dialog.findViewById(R.id.field_soil_TV);
        EditText weatherET = dialog.findViewById(R.id.field_weather_TV);
        EditText descET = dialog.findViewById(R.id.field_des_ET);
        AppCompatButton button = dialog.findViewById(R.id.dialog_done);
        EditText addTV = dialog.findViewById(R.id.location_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // Calculate the center point of the polygon
                LatLng centerPoint = calculateCenterPoint(polygonPoints);

                // Use Geocoder to get the address from the center point
                String address = getAddressFromLatLng(centerPoint);

                field.setSoil(soilET.getText().toString());
                field.setWeather(weatherET.getText().toString());
                field.setAddress(address);
                field.setSoil(soilET.getText().toString());
                field.setPolygon(polygon.getPoints());
                field.setDescription(descET.getText().toString());

                String childPath = mAuth.getCurrentUser().getEmail().replace('.','_');
                String fieldPath = address.replace(',','_');
                db.getReference().child("users").child(childPath).child("fields")
                        .child(fieldPath)
                        .setValue(field);
            }
        });


    }
    private LatLng calculateCenterPoint(List<LatLng> points) {
        if (points.isEmpty()) return null;

        double latSum = 0;
        double lngSum = 0;

        for (LatLng point : points) {
            latSum += point.latitude;
            lngSum += point.longitude;
        }

        int totalPoints = points.size();
        return new LatLng(latSum / totalPoints, lngSum / totalPoints);
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;
        String address = "Address not found"; // Default value

        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && !addressList.isEmpty()) {
                address = addressList.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("GetAddress", "Geocoder service not available");
        }

        return address;
    }


    private void clearPolygons() {
        // Remove the polygon if it exists
        if (polygon != null) {
            polygon.remove();
            polygon = null; // Clear the reference
        }
        // Remove all circles
        for (com.google.android.gms.maps.model.Circle circle : circles) {
            circle.remove();
        }
        circles.clear(); // Clear the list of circles
    }

    private void drawPolygon() {
        // Clear any existing polygons
        if (polygon != null) {
            polygon.remove();
        }
        // Create a polygon on the map using the points
        PolygonOptions polygonOptions = new PolygonOptions()
                .addAll(polygonPoints) // Add the points to the polygon
                .strokeColor(Color.RED) // Set the outline color
                .fillColor(0x220FF000) // Set the fill color with transparency
                .strokeWidth(5); // Set the stroke width
        // Add the polygon to the map
        polygon = mMap.addPolygon(polygonOptions);
    }
    private void retrieveFieldData() {
        String childPath = mAuth.getCurrentUser().getEmail().replace('.','_');
        DatabaseReference newFieldRef = db.getReference().child("users").child(childPath).
                child("fields").child("1650 Amphitheatre Pkwy_ Mountain View_ CA 94043_ USA");
        newFieldRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Field field = dataSnapshot.getValue(Field.class); // Assuming Field is your model class

                    // Do something with the retrieved field data
                    if (field != null) {
                        drawRetrievedPolygon(field.getPolygon());
                    }
                } else {
                    Log.d("FieldData", "No such field exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FieldData", "Database error: " + databaseError.getMessage());
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set map type to HYBRID for a more agriculture-friendly view (shows satellite imagery with labels)
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // Change here for a relevant view in agriculture

        checkLocationPermission();
        mMap.setOnMapClickListener(latLng -> {
            // Add the point to the polygon points list
            polygonPoints.add(latLng);
            // Draw a circle at the touched position
            com.google.android.gms.maps.model.Circle circle = mMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(5) // radius in meters
                    .strokeColor(Color.BLUE) // border color
                    .fillColor(Color.argb(128, 0, 0, 255))); // fill color with transparency
            circles.add(circle); // Add the circle to the list
        });
    }
    // Check if location permission is granted
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, get the location
            getCurrentLocation();
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                // Permission denied, show a message to the user
            }
        }
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // You can also request permissions here
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Create a LatLng object for the current location
                        LatLng userLocation = new LatLng(latitude, longitude);

                        // Move the camera to the user's location with a closer zoom
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17)); // Adjusted zoom level to 17

                        // Add a marker at the user's location
                        mMap.addMarker(new MarkerOptions().position(userLocation).title("My Location"));
                    } else {
                        // Handle case where location is null
                        Log.d("Location", "Location is null");
                    }
                });
    }

    // Method to search for a location
    private void searchLocation(String location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(location, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                // Move the camera to the new location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                // Add a marker at the new location
                mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
                mMap.addCircle(new CircleOptions()
                        .center(latLng) // Set the center to the searched location
                        .radius(100) // Radius in meters (adjust as needed)
                        .strokeColor(0xFF0000FF) // Blue outline
                        .fillColor(0x220000FF) // Light blue fill
                        .strokeWidth(5)); // Width of the circle outline
            } else {
                Log.d("SearchLocation", "Location not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SearchLocation", "Geocoder service not available");
        }
    }

    private void drawRetrievedPolygon(List<LatLng> points) {
        if (points != null && !points.isEmpty()) {
            PolygonOptions polygonOptions = new PolygonOptions()
                    .addAll(points)
                    .strokeColor(Color.RED) // Set polygon stroke color
                    .fillColor(0x220FF000) // Set polygon fill color
                    .strokeWidth(5); // Set stroke width

            // Add polygon to the map
            mMap.addPolygon(polygonOptions);
        }
    }




}