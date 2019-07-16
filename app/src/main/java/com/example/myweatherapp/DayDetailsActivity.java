package com.example.myweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweatherapp.others.ListForecastAdapter;
import com.example.myweatherapp.service.RetrofitConfig;

public class DayDetailsActivity extends AppCompatActivity {

    private ImageView iconView;
    private TextView mPlace;
    private TextView mTemp;
    private TextView mTempInterval;
    private TextView mWindSpeed;
    private TextView mWindOrientation;
    private TextView mPressure;
    private TextView mHumidity;
    private TextView mRain;
    private TextView mDescription;
    private TextView mIcon;
    private ListForecastAdapter mAdapter;

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);

        //Set visual elements
        iconView = findViewById(R.id.state);
        mPlace = findViewById(R.id.place);
        mDescription = findViewById(R.id.description);
        mTemp = findViewById(R.id.day_temperature);
        mTempInterval = findViewById(R.id.day_interval_temperature);
        mWindOrientation = findViewById(R.id.day_wind_direction);
        mWindSpeed = findViewById(R.id.day_wind_speed);
        mPressure = findViewById(R.id.day_pression);
        mHumidity = findViewById(R.id.day_humidity);

        Intent myIntent = getIntent();
        mPlace.setText(myIntent.getStringExtra("City") + " " + myIntent.getStringExtra("Country"));

    }


}
