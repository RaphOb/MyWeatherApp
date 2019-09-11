package com.example.myweatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProviders;

import com.example.myweatherapp.model.common.CityFav;
import com.example.myweatherapp.model.common.ListCommon;
import com.example.myweatherapp.model.common.ListFavViewModel;
import com.example.myweatherapp.model.common.Main;
import com.example.myweatherapp.model.common.Weather;
import com.example.myweatherapp.model.common.Wind;
import com.example.myweatherapp.model.searchData.SearchWeatherData;
import com.example.myweatherapp.others.Constants;
import com.example.myweatherapp.others.ListForecastAdapter;
import com.example.myweatherapp.service.RetrofitConfig;
import com.example.myweatherapp.service.ToolService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastActivity extends AppCompatActivity {
    //Visual Elements
    private ImageView currentWeatherView;
    private TextView currentCityDescr;
    private List<ListCommon> mForecastList;
    private ListView mListView;
    private ListForecastAdapter mAdapter;
    private BottomNavigationView mNavigationView;
    private FloatingActionButton addButt;
    private ListFavViewModel listFavViewModel;

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        //db instance
        listFavViewModel = ViewModelProviders.of(this).get(ListFavViewModel.class);

        //Init the ImageView and it's weather description
        currentWeatherView = findViewById(R.id.state);
        currentCityDescr = findViewById(R.id.description);
        //Init the listView
        mListView = findViewById(R.id.activity_forecasts);

        mNavigationView = findViewById(R.id.bottom_navigation_view);
        mNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Init the list for datas
        mForecastList = new ArrayList<>();
        //Build mForecastList with weather Data
        getForecast();

        addButt = findViewById(R.id.fab);
        addButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer city_id = getIntent().getIntExtra("id", 0);
                String city_name = getIntent().getStringExtra("City");
                String country_name = getIntent().getStringExtra("Country");
                CityFav cityFav = new CityFav();
                cityFav.setIdTown(city_id);
                cityFav.setName(city_name);
                cityFav.setCountry(country_name);


//                myAppDataBase.dataAccess().addTown(cityFav);
                listFavViewModel.insert(cityFav);
                Toast.makeText(getApplicationContext(), "Success Full add to list", Toast.LENGTH_SHORT).show();
            }
        });
        //Build Adapter to convert datas to view List
        mAdapter = new ListForecastAdapter(getApplicationContext(), mForecastList);
        mListView.setAdapter(mAdapter);

        //Set possibility to click on a list element
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long ids) {
                //Day to define with more precisions
                ListCommon day1 = mForecastList.get(position);
                //Intent to get City and Country
                Intent myIntent = getIntent();
                //Start new activity
                Intent intent = new Intent(ForecastActivity.this, DayDetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("City", myIntent.getStringExtra("City"));
                intent.putExtra("Country", myIntent.getStringExtra("Country"));
                intent.putExtra("id", myIntent.getIntExtra("id", 0));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    /**
     * Api Call
     */
    public void getForecast() {
        retrofitConfig.getApiWeather().getForecast(
                getIntent().getStringExtra("City"),
                null,
                null,
                null,
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
                    Log.d("SUCCESS", "Data from 5 next days");
                }
            }

            @Override
            public void onFailure(Call<SearchWeatherData> call, Throwable t) {
                Log.d(">>>>>>ERREUR !!!!!! ", "message : " + t + call);
            }
        });
    }

    //Create a list used to show datas
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

    //Set image according to most recent forecast Weather
    public void manageImageFromWeather(String mainWeather) {
        Intent myIntent = getIntent();
        if (mainWeather.equals("Clouds")) {
            currentWeatherView.setImageResource(R.drawable.couvert);
            currentCityDescr.setText(myIntent.getStringExtra("City") + " (" + myIntent.getStringExtra("Country") + ")\nActuellement: Couvert");
        }
        if (mainWeather.equals("Rain")) {
            currentWeatherView.setImageResource(R.drawable.rain);
            currentCityDescr.setText(myIntent.getStringExtra("City") + " (" + myIntent.getStringExtra("Country") + ")\nActuellement: Pluie");
        }
        if (mainWeather.equals("Clear")) {
            currentWeatherView.setImageResource(R.drawable.sun);
            currentCityDescr.setText(myIntent.getStringExtra("City") + " (" + myIntent.getStringExtra("Country") + ")\nActuellement: Dégagé");
        }
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

    //Define interface and its method to listen navbar
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_gps:
                    Intent intent = new Intent(ForecastActivity.this, LocGPSActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_search:
                    intent = new Intent(ForecastActivity.this, CityChoiceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_fav:
                    intent = new Intent(ForecastActivity.this, FavouriteActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };

}