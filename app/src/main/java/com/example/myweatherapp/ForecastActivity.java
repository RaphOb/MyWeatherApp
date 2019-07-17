package com.example.myweatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.myweatherapp.model.common.ListCommon;
import com.example.myweatherapp.model.common.Wind;
import com.example.myweatherapp.others.ListForecastAdapter;
import com.example.myweatherapp.model.common.Main;
import com.example.myweatherapp.model.common.Weather;
import com.example.myweatherapp.model.searchData.SearchWeatherData;
import com.example.myweatherapp.others.Constants;
import com.example.myweatherapp.service.RetrofitConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ForecastActivity extends AppCompatActivity {
    //Visual Elements
    private ImageView currentWeatherView;
    private TextView currentWeatherDescr;
    private List<ListCommon> mForecastList;
    private ListView mListView;
    private ListForecastAdapter mAdapter;

    private TextView mTextMessage;

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        //Init the ImageView and it's weather description
        currentWeatherView = findViewById(R.id.state);
        currentWeatherDescr = findViewById(R.id.description);
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

    /**
     * Uses a ConcurrentHashMap instance to find out if there is any existing key with same value-
     * where key is obtained from a function reference.
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Api Call
     */
    public void getForecast() {
        retrofitConfig.getApiWeather().getForecast(
                getIntent().getStringExtra("City"),
                Constants.LANG,
                Constants.UNITS,
                Constants.APPID
        ).enqueue(new Callback<SearchWeatherData>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<SearchWeatherData> call, Response<SearchWeatherData> response) {
                SearchWeatherData s = response.body();
                List<ListCommon> tempList = response.body().getList()
                        .stream()
                        .filter(distinctByKey(b -> b.getDate(b.getDtTxt())))
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

    //Create a list used to show datas
    public ListCommon createOwnList(ListCommon lw, int done) {
        //Get data from getted List
        double temperature = lw.getMain().getTemp();
        String icon = lw.getWeathers().get(0).getIcon();
        String description = lw.getWeathers().get(0).getDescription();
        String mainWeather = lw.getWeathers().get(0).getMain();
        Double windSpeed = (double) Math.round(lw.getWind().getSpeed() * 3.6);
        Double windOrientation = lw.getWind().getDeg();
        Double humidity = lw.getMain().getHumidity();

        //Set image from mainWeather only for the most recent forecast
        if (done == 0)
            manageImageFromWeather(mainWeather);

        //Insert these data in a new list
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

    //Set image according to most recent forecast Weather
    public void manageImageFromWeather(String mainWeather) {
        Intent myIntent = getIntent();
        if (mainWeather.equals("Clouds")) {
            currentWeatherView.setImageResource(R.drawable.couvert);
            currentWeatherDescr.setText(myIntent.getStringExtra("City") + " (" + myIntent.getStringExtra("Country") + ")\nActuellement: Couvert");
        }
        if (mainWeather.equals("Rain")) {
            currentWeatherView.setImageResource(R.drawable.rain);
            currentWeatherDescr.setText(myIntent.getStringExtra("City") + " (" + myIntent.getStringExtra("Country") + ")\nActuellement: Pluie");
        }
        if (mainWeather.equals("Clear")) {
            currentWeatherView.setImageResource(R.drawable.sun);
            currentWeatherDescr.setText(myIntent.getStringExtra("City") + " (" + myIntent.getStringExtra("Country") + ")\nActuellement: Dégagé");
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

    public Integer GetWindOrientation(double value) {
        RangeMap<Integer, Integer> WindDirection = TreeRangeMap.create();
        WindDirection.put(Range.closed(340, 360), R.drawable.nwind);
        WindDirection.put(Range.closed(0, 15), R.drawable.nwind);
        WindDirection.put(Range.closed(16, 80), R.drawable.newind);
        WindDirection.put(Range.closed(81, 110), R.drawable.ewind);
        WindDirection.put(Range.closed(111, 160), R.drawable.sewind);
        WindDirection.put(Range.closed(161, 200), R.drawable.swind);
        WindDirection.put(Range.closed(201, 250), R.drawable.sowind);
        WindDirection.put(Range.closed(251, 300), R.drawable.owind);
        WindDirection.put(Range.closed(301, 339), R.drawable.nowind);
        Integer deg = (int) value;

        return WindDirection.get((deg));
    }

    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_gps:
                    mTextMessage.setText(R.string.title_gps);
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_fav:
                    mTextMessage.setText(R.string.title_fav);
            }
            return false;
        }
    };*/

}