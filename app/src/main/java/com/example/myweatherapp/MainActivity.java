package com.example.myweatherapp;

import android.os.Bundle;

import com.example.myweatherapp.model.currentWeather.CurrentWeatherData;
import com.example.myweatherapp.service.ApiWeather;
import com.example.myweatherapp.service.LocationGPS;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    LocationGPS lh;
    private TextView name;
    private Retrofit retrofit;
    private ApiWeather apiWeather;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        TextView longitude = null;
        TextView latitude = null;

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       this.lh = new LocationGPS(longitude, latitude, this);
       lh.refreshLocation();
       name = findViewById(R.id.textTest);
       this.configureRetrofit();
       getWeather();


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void configureRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder().baseUrl("https://community-open-weather-map.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiWeather = retrofit.create(ApiWeather.class);
    }

    public void getWeather() {
        apiWeather.getWeather().enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                CurrentWeatherData w = response.body();
                if (w != null) {
                    name.setText(w.getName());
                    Log.d("SUCCESS", " ça a marchééé");
                } else {
                    Log.d(">>>>>>TAG!!!!!", "empty reponse");
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable t) {
            Log.d(">>>>>>ERREUR !!!!!! ", "message : " + t + call);
            }
        });
    }
}
