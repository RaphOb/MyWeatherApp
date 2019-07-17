package com.example.myweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweatherapp.model.common.ListCommon;
import com.example.myweatherapp.model.common.Main;
import com.example.myweatherapp.model.common.Weather;
import com.example.myweatherapp.model.common.Wind;
import com.example.myweatherapp.model.searchData.SearchWeatherData;
import com.example.myweatherapp.others.Constants;
import com.example.myweatherapp.others.DownloadImageTask;
import com.example.myweatherapp.others.ListDaydetailsAdapter;
import com.example.myweatherapp.others.ListForecastAdapter;
import com.example.myweatherapp.service.RetrofitConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private List<ListCommon> mDayList;
    private ListDaydetailsAdapter mAdapter;
    private ListView mListView;


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
        mListView = findViewById(R.id.day_3hours_forecast);
        mDayList = new ArrayList<>();
        getDayDetails();

        Intent myIntent = getIntent();
        mPlace.setText(myIntent.getStringExtra("City") + " " + myIntent.getStringExtra("Country"));

        mAdapter = new ListDaydetailsAdapter(getApplicationContext(), mDayList);
        mListView.setAdapter(mAdapter);

    }

    public void getDayDetails() {
        retrofitConfig.getApiWeather().getDayDetails(
                getIntent().getIntExtra("id", 0),
                Constants.LANG,
                Constants.UNITS,
                Constants.APPID

        ).enqueue(new Callback<SearchWeatherData>() {
            @Override
            public void onResponse(Call<SearchWeatherData> call, Response<SearchWeatherData> response) {
                SearchWeatherData s = response.body();

                if (s == null) {
                    Log.d("FAILED", "Response from API call return NULL");
                    Toast.makeText(getApplicationContext(), "An error occured while getting weather data...", Toast.LENGTH_SHORT).show();
                } else {

                    //BUG always getting the same element
                    Intent myIntent = getIntent();
                    int position = myIntent.getIntExtra("position", 0);
                    mDescription.setText(s.getList().get(position).getWeathers().get(0).getDescription());
                    mTemp.setText("Température  : "+ String.valueOf(s.getList().get(position).getMain().getTemp()) + "°C");
                    mTempInterval.setText("Min-Max  : "+ String.valueOf((int)s.getList().get(position).getMain().getTemp_min()) + " - " + String.valueOf((int)s.getList().get(0).getMain().getTemp_max()) + "°C");
//                    mWindOrientation;
                    mWindSpeed.setText( "Vitesse vent  : "+ String.valueOf(s.getList().get(position).getWind().getSpeed()) + "km/h");
                    mPressure.setText( "Pression  : "+ String.valueOf((int)s.getList().get(position).getMain().getPressure()) + " hPa");
                    mHumidity.setText( "Humidité  : "+ String.valueOf(s.getList().get(position).getMain().getHumidity()) + " %");
                    new DownloadImageTask(iconView).execute(Constants.URL_ICON2 + s.getList().get(position).getWeathers().get(0).getIcon() + "@2x.png");

                    for (ListCommon lw : s.getList()) {
                        ListCommon copy = createOwnList(lw);

                        Log.d("INFO", "RES : " + copy.getDtTxt());
                        Log.d("INFO", "RES : " + copy.getWeathers().get(0).getDescription());

                        mDayList.add(copy);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchWeatherData> call, Throwable t) {
                Log.d("ERREUR", "message : " + t + call);
            }
        });
    }

    private ListCommon createOwnList(ListCommon lw) {
        double temperature = lw.getMain().getTemp();
        String icon = lw.getWeathers().get(0).getIcon();
        String description = lw.getWeathers().get(0).getDescription();
        Double windSpeed = (double) Math.round(lw.getWind().getSpeed() * 3.6);
        Double windOrientation = lw.getWind().getDeg();

        ListCommon l = new ListCommon();
        Main m = new Main();
        m.setTemp(temperature);
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
        l.setDtTxt(lw.getDtTxt());
        return l;
    }


}