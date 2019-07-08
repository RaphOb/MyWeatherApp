package com.example.myweatherapp;

import android.os.Bundle;

import com.example.myweatherapp.model.searchData.SearchWeatherData;
import com.example.myweatherapp.service.ApiWeather;
import com.example.myweatherapp.service.RetrofitConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastActivity extends AppCompatActivity {
    //Visual Elements
    private TextView mTextMessage;
    private TextView mCountryFound;
    private EditText mCityInput;
    private Button mSearchButton;

    //Query parameters
    private String lang = "Fr";
    private String units = "metric";

    //Converted Data
    private String mCity;

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        mTextMessage = findViewById(R.id.message);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    /**
     * Api Call
     */
    public void getForecast() {
        retrofitConfig.getApiWeather().getForecast(mCity, lang, units).enqueue(new Callback<SearchWeatherData>() {
            @Override
            public void onResponse(Call<SearchWeatherData> call, Response<SearchWeatherData> response) {
                SearchWeatherData s = response.body();
                if (s == null) {
                    Log.d("FAILED", "Reponse from API call return NULL");
                    mCountryFound.setText("");
                    Toast.makeText(getApplicationContext(), "It seems the city you entered is not known from us...", Toast.LENGTH_SHORT).show();
                } else {
                    mCountryFound.setText("We found you in " + s.getList().get(0).getSys().getCountry() + ". Is it correct?");
                    mSearchButton.setText("OK");
                    Log.d("SUCCESS", " Country found according to the City");
                }
            }

            @Override
            public void onFailure(Call<SearchWeatherData> call, Throwable t) {
                Log.d(">>>>>>ERREUR !!!!!! ", "message : " + t + call);
            }
        });
    }


}
