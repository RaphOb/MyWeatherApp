package com.example.myweatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.myweatherapp.model.common.ListCommon;
import com.example.myweatherapp.model.common.ListForecastAdapter;
import com.example.myweatherapp.model.common.Main;
import com.example.myweatherapp.model.common.Weather;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastActivity extends AppCompatActivity {
    //Visual Elements
    private List<ListCommon> mForecastList;
    private ListView mListView;
    private ListForecastAdapter mAdapter;

    private TextView[] mTemp;
    private TextView[] mTempMax;
    private TextView[] mTempMin;
    private TextView[] mWindSpeed;
    private TextView[] mWindOrientation;
    private TextView[] mTempActuelle;
    private TextView[] mPressure;
    private TextView[] mHumidity;
    private TextView[] mRain;
    private TextView[] mDescription;
    private TextView[] mIcon;
    private ImageView[] iconView;

    //Query parameters
    private String lang = "Fr";
    private String units = "metric";

    //URL
    private static String URL_ICON = "http://api.openweathermap.org/img/w/";

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        //Init the listView
        mListView = findViewById(R.id.activity_forecasts);
        //Init the list for datas
        mForecastList = new ArrayList<>();
        //Build mForecastList with weather Data
        getForecast();

        //Build Adapter to convert datas to view List
        mAdapter = new ListForecastAdapter(getApplicationContext(), mForecastList);
        mListView.setAdapter(mAdapter);

        //Set possibility to click on a list element
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ListCommon day1 = mForecastList.get(position);
            }
        });
    }


    /**
     * Api Call
     */
    public void getForecast() {
        retrofitConfig.getApiWeather().getForecast(
                getIntent().getStringExtra("City"),
                lang,
                units
        ).enqueue(new Callback<SearchWeatherData>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<SearchWeatherData> call, Response<SearchWeatherData> response) {
                SearchWeatherData s = response.body();
                if (s == null) {
                    Log.d("FAILED", "Response from API call return NULL");
                    Toast.makeText(getApplicationContext(), "An error occured while getting weather data...", Toast.LENGTH_SHORT).show();
                } else {
                    for (ListCommon lw : s.getList()) {
                        ListCommon l = new ListCommon();
                        Main m = new Main();
                        m.setTemp(lw.getMain().getTemp());
                        l.setMain(m);
                        Weather w = new Weather();
                        w.setDescription(lw.getWeathers().get(0).getDescription());
                        List<Weather> ll = new ArrayList<>();
                        ll.add(w);
                        l.setWeathers(ll);

                        mForecastList.add(l);
                        mAdapter.notifyDataSetChanged();
                    }


//                    int day = 0;
//                    Iterator<ListCommon> iterator = mForecastList.iterator();
//                    while (iterator.hasNext()) {
//                        ListCommon x = iterator.next();
//                        Log.d("INFO", x.getSys().getCountry());
//                        Log.d("INFO", String.valueOf(x.getCoord().getLat()));
//                        Log.d("INFO", String.valueOf(x.getCoord().getLon()));
//                        Log.d("INFO", x.getName());

//                        mTemp[day].setText(String.valueOf(x.getMain().getTemp()));
//                        mTempMax[day].setText(String.valueOf(x.getMain().getTemp_max()));
//                        mTempMin[day].setText(String.valueOf(x.getMain().getTemp_min()));
//                        mWindSpeed[day].setText(String.valueOf(x.getWind().getSpeed()));
//                        mWindOrientation[day].setText(String.valueOf(x.getWind().getSpeed()));
//                        mRain1[day].setText(String.valueOf(x.getRain().getRain1()));
//                        mRain3[day].setText(String.valueOf(x.getRain().getRain3()));
//                        mDescription[day].setText(x.getWeathers().get(0).getDescription());
//                        mIcon[day].setText(x.getWeathers().get(0).getIcon());
//                        Picasso.get().load(URL_ICON + x.getWeathers().get(0).getIcon() + "@2x.png").into(iconView[day]);


                    Log.d("SUCCESS", "Data from 5 next days");
//                        ++day;
//                    }
                }
            }

            @Override
            public void onFailure(Call<SearchWeatherData> call, Throwable t) {
                Log.d(">>>>>>ERREUR !!!!!! ", "message : " + t + call);
            }
        });
    }
}


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