package com.example.myweatherapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationGPS extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final int REQUEST_PERMISSION_LOCATION_FINE = 0;
    private TextView longitude, latitude;
    private  Context context;

    public void initLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d("TAG", "on" + s);

            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("TAG", "off");

            }
        };

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(context)
                        .setTitle("Permissions GEOLOC")
                        .setMessage("C'est une app Meteo sans geoloc ?: ALLO mais ALLO?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LocationGPS.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION_LOCATION_FINE);
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION_FINE);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_LOCATION_FINE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation();
            } else {
                Log.w("TAG", "permission denied");
            }
        }
    }
}
