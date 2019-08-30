package com.example.myweatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweatherapp.model.common.CityList;
import com.example.myweatherapp.model.common.Coord;
import com.example.myweatherapp.model.common.ListCommon;
import com.example.myweatherapp.model.common.Main;
import com.example.myweatherapp.model.common.Weather;
import com.example.myweatherapp.model.common.Wind;
import com.example.myweatherapp.model.searchData.SearchWeatherData;
import com.example.myweatherapp.others.Constants;
import com.example.myweatherapp.others.ListForecastAdapter;
import com.example.myweatherapp.service.LocationGPS;
import com.example.myweatherapp.service.RetrofitConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocGPSActivity extends AppCompatActivity {


    private Coord myCoord;
    private List<ListCommon> mForecastList;
    private ListView mListView;
    private ListForecastAdapter mAdapter;

    private ImageView currentWeatherView;
    private TextView currentCityDescr;
    private String mCity = "coucou";
    private String mCountry = "kikou";

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);


        //Init the ImageView and it's weather description
        currentWeatherView = findViewById(R.id.weather_image);
        //currentCityDescr = findViewById(R.id.location);

        //Set GPS
        LocationGPS locationGPS = new LocationGPS(this);
        locationGPS.refreshLocation();

        this.myCoord = new Coord();
        myCoord.setLat(locationGPS.getLatitude());
        myCoord.setLon(locationGPS.getLongitude());
        Log.d("LOC", String.valueOf(myCoord.getLat()));
        Log.d("LOC", String.valueOf(myCoord.getLon()));

        //Init the list for datas
        mForecastList = new ArrayList<>();
        getCityForecastFromCoord();

    }

    /**
     * Api Call
     */
    public void getCityForecastFromCoord() {
        retrofitConfig.getApiWeather().getForecast(
                null,
                null,
                myCoord.getLat(),
                myCoord.getLon(),
                Constants.LANG,
                Constants.UNITS,
                Constants.APPID,
                null
        ).enqueue(new Callback<SearchWeatherData>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<SearchWeatherData> call, Response<SearchWeatherData> response) {
                SearchWeatherData s = response.body();
                List<ListCommon> tempList = response.body().getList();

                if (s == null) {
                    Log.d("FAILED", "Response from API call return NULL");
                    Toast.makeText(getApplicationContext(), "An error occured while getting weather data...", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAGGGGGGG", s.getList().get(0).getDtTxt());
                    mCity = s.getList().get(0).getName();
                    mCountry = s.getList().get(0).getSys().getCountry();
                    Intent intent  = new Intent(LocGPSActivity.this, ForecastActivity.class);
                    intent.putExtra("lat", myCoord.getLat());
                    intent.putExtra("lon", myCoord.getLon());
                    intent.putExtra("City", "Paris");

                    intent.putExtra("Country", mCountry);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<SearchWeatherData> call, Throwable t) {
                Log.d(">>>>>>ERREUR !!!!!! ", "message : " + t + call);
            }
        });
    }

    //Set image according to most recent forecast Weather
    public  void manageImageFromWeather(String mainWeather) {
        Intent myIntent = getIntent();

//        if (mainWeather.equals("Clouds")) {
//            currentWeatherView.setImageResource(R.drawable.couvert);
//            currentCityDescr.setText(myIntent.getStringExtra("City") + " (" + myIntent.getStringExtra("Country") + ")\nActuellement: Couvert");
//        }
//        if (mainWeather.equals("Rain")) {
//            currentWeatherView.setImageResource(R.drawable.rain);
//            currentCityDescr.setText(myIntent.getStringExtra("City") + " (" + myIntent.getStringExtra("Country") + ")\nActuellement: Pluie");
//        }
//        if (mainWeather.equals("Clear")) {
//            currentWeatherView.setImageResource(R.drawable.sun);
//            currentCityDescr.setText(myIntent.getStringExtra("City") + " (" + myIntent.getStringExtra("Country") + ")\nActuellement: Dégagé");
//        }
        //TODO
       /* if (mainWeather.equals("Snow"))
        {
            currentWeatherView.setImageResource(R.drawable.couvert);
        }
        if (mainWeather.equals("Mist"))
        {
            currentWeatherView.setImageResource(R.drawable.couvert);
        }*/
    }

}
