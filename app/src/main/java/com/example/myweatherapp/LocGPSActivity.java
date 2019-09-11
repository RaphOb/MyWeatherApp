package com.example.myweatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


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
import com.example.myweatherapp.service.ToolService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocGPSActivity extends AppCompatActivity {


    private Coord myCoord;
    private List<ListCommon> mForecastList;
    private ListView mListView;
    private ListForecastAdapter mAdapter;
    private BottomNavigationView mNavigationView;

    private ImageView currentWeatherView;

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);


        //Init the ImageView and it's weather description
        currentWeatherView = findViewById(R.id.weather_image);
        //currentCityDescr = findViewById(R.id.location);
        mListView = findViewById(R.id.activity_forecasts);
        //Set GPS
        LocationGPS locationGPS = new LocationGPS(this);
        locationGPS.refreshLocation();

        mNavigationView = findViewById(R.id.bottom_navigation_view);
        mNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        this.myCoord = new Coord();
        myCoord.setLat(locationGPS.getLatitude());
        myCoord.setLon(locationGPS.getLongitude());
        Log.d("LOC", String.valueOf(myCoord.getLat()));
        Log.d("LOC", String.valueOf(myCoord.getLon()));

        //Init the list for datas
        mForecastList = new ArrayList<>();
        getCityForecastFromCoord();

        mAdapter = new ListForecastAdapter(getApplicationContext(), mForecastList);
        mListView.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                List<ListCommon> tempList = response.body().getList()
                        .stream()
                        .filter(ToolService.distinctByKey(b -> b.getDate(b.getDtTxt())))
                        .collect(Collectors.toList());

                if (s == null) {
                    Log.d("FAILED", "Response from API call return NULL");
                    Toast.makeText(getApplicationContext(), "An error occured while getting weather data...", Toast.LENGTH_SHORT).show();
                } else {
                    int done = 0;
                    for (ListCommon lw : tempList) {
                        ListCommon copy = createOwnList(lw, done);
                        done = 1;
                        mForecastList.add(copy);

                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchWeatherData> call, Throwable t) {
                Log.d(">>>>>>ERREUR !!!!!! ", "message : " + t + call);
            }
        });
    }

    public ListCommon createOwnList(ListCommon lw, int done) {
        //Get data from getted List
        double temperature_min = lw.getMain().getTemp_min();
        double temperature_max = lw.getMain().getTemp_max();

        String icon = lw.getWeathers().get(0).getIcon();
        String description = lw.getWeathers().get(0).getDescription();
        String mainWeather = lw.getWeathers().get(0).getMain();
        Double windSpeed = (double) Math.round(lw.getWind().getSpeed() * 3.6);
        Double windOrientation = lw.getWind().getDeg();

        //Set image from mainWeather only for the most recent forecast
        if (done == 0)
            manageImageFromWeather(mainWeather);

        //Insert these data in a new list
        ListCommon l = new ListCommon();
        Main m = new Main();
        m.setTemp_min(temperature_min);
        m.setTemp_max(temperature_max);
        l.setMain(m);
        Weather w = new Weather();
        w.setIcon(icon);
        w.setDescription(description);
        Wind v = new Wind();
        v.setDeg(windOrientation);
        v.setSpeed(windSpeed);
        l.setWind(v);

        List<Weather> ll = new ArrayList<>();
        ll.add(w);
        l.setWeathers(ll);
        l.setDtTxt(lw.getDtTxt().substring(0, 10));
        return l;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_gps:
                    Intent intent = new Intent(LocGPSActivity.this, LocGPSActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_search:
                    intent = new Intent(LocGPSActivity.this, CityChoiceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_fav:
                    intent = new Intent(LocGPSActivity.this, FavouriteActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };


    //Set image according to most recent forecast Weather
    public void manageImageFromWeather(String mainWeather) {
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
