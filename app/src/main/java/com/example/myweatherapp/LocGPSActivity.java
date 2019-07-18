package com.example.myweatherapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweatherapp.service.LocationGPS;

public class LocGPSActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //Set GPS
        LocationGPS locationGPS = new LocationGPS(this);
        locationGPS.refreshLocation();
        Log.d("LOC", String.valueOf(locationGPS.getLatitude()));
        Log.d("LOC", String.valueOf(locationGPS.getLongitude()));
    }
}
