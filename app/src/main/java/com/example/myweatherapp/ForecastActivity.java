package com.example.myweatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.myweatherapp.model.common.ListCommon;
import com.example.myweatherapp.model.searchData.SearchWeatherData;
import com.example.myweatherapp.service.ApiWeather;
import com.example.myweatherapp.service.RetrofitConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
    private List<ListCommon> mForecastList;
    private TextView[] mTemp;
    private TextView[] mTempMax;
    private TextView[] mTempMin;
    private TextView[] mWindSpeed;
    private TextView[] mWindOrientation;
    private TextView[] mRain1;
    private TextView[] mRain3;
    private TextView[] mDescription;
    private TextView[] mIcon;
    private ImageView[] iconView;

    //Query parameters
    private String lang = "Fr";
    private String units = "metric";

    //URL
    private static String URL_ICON = "http://api.openweathermap.org/img/w/";

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
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<SearchWeatherData> call, Response<SearchWeatherData> response) {
                SearchWeatherData s = response.body();
                if (s == null) {
                    Log.d("FAILED", "Reponse from API call return NULL");
                    mCountryFound.setText("");
                    Toast.makeText(getApplicationContext(), "It seems the city you entered is not known from us...", Toast.LENGTH_SHORT).show();
                } else {
                    mForecastList = s.getList();
                    int day = 0;
                    for (ListCommon x : mForecastList) {
                        mCountryFound.setText("We found you in " + x.getSys().getCountry() + ". Is it correct?");
                        mTemp[day].setText(String.valueOf(x.getMain().getTemp()));
                        mTempMax[day].setText(String.valueOf(x.getMain().getTemp_max()));
                        mTempMin[day].setText(String.valueOf(x.getMain().getTemp_min()));
                        mWindSpeed[day].setText(String.valueOf(x.getWind().getSpeed()));
                        mWindOrientation[day].setText(String.valueOf(x.getWind().getSpeed()));
                        mRain1[day].setText(String.valueOf(x.getRain().getRain1()));
                        mRain3[day].setText(String.valueOf(x.getRain().getRain3()));
                        mDescription[day].setText(x.getWeathers().get(0).getDescription());
                        mIcon[day].setText(x.getWeathers().get(0).getIcon());
                        Picasso.get().load(URL_ICON + x.getWeathers().get(0).getIcon() + "@2x.png").into(iconView[day]);

                        mSearchButton.setText("OK");
                        Log.d("SUCCESS", " Country found according to the City");
                        ++day;
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchWeatherData> call, Throwable t) {
                Log.d(">>>>>>ERREUR !!!!!! ", "message : " + t + call);
            }
        });
    }


}
