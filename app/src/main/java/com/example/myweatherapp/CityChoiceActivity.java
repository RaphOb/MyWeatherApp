package com.example.myweatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myweatherapp.model.common.CityList;
import com.example.myweatherapp.model.currentWeather.CurrentWeatherData;
import com.example.myweatherapp.others.CityAdaptateur;
import com.example.myweatherapp.others.Constants;
import com.example.myweatherapp.service.LocationGPS;
import com.example.myweatherapp.service.RetrofitConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityChoiceActivity extends AppCompatActivity {

    //Visual Elements
    private TextView mGreetingText;
    private AutoCompleteTextView mLocatedCity;
    private TextView mConfirm;
    private Button mSearchButton;
    private BottomNavigationView mNavigationView;

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

    //Converted Data
    private int mId;
    private String mCity;
    private String mCountry;
    private CityList mCityObj;
    public static List<CityList> mCityLists;
    public String mQuery;

    //LOC GPS


    /*----------Activity Usage--------*/

    public List<CityList> getCityList()
    {
        return this.mCityLists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choice);
        //Get all visual elements
        mGreetingText = findViewById(R.id.activity_city_choice_greeting_txt);
        mSearchButton = findViewById(R.id.activity_city_choice_search_btn);
        mConfirm = findViewById(R.id.activity_city_choice_confirm_txt);
        mLocatedCity = findViewById(R.id.autoCompleteTextView);
        mNavigationView = findViewById(R.id.bottom_navigation_view);
        mNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSearchButton.setEnabled(false);
        //Deserialize the list of city file
        try {
            if (mCityLists == null)
                City(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Start autoCompletion at 4 char
        mLocatedCity.setThreshold(4);
        Collections.sort(mCityLists);
        //Load Adapter to set Autocompletion View
        final CityAdaptateur adapter = new CityAdaptateur(this, R.layout.activity_city_choice, android.R.layout.simple_list_item_1, mCityLists);
        mLocatedCity.setAdapter(adapter);
        //Set listener for choice of City and get Country
        mLocatedCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCityObj = (CityList) adapterView.getItemAtPosition(i);
                mCity = mCityObj.getName();
                mCountry = mCityObj.getCountry();
                mId = mCityObj.getId();
                mQuery = mCity + "," + mCountry;
            }
        });

        //Monitor changing on auto-completion text
        mLocatedCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /* Set clickable for search button according to content*/
                mSearchButton.setEnabled(charSequence.toString().length() != 0);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /* Set confirm text empty */
                mConfirm.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchButton.setText("Search !");
            }
        });

        /* Monitor click events */
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The user just clicked
                if (mSearchButton.getText() == "OK") {
                    /* Launch forecasts */
                    Intent intent = new Intent(CityChoiceActivity.this, ForecastActivity.class);
                    /* Add City and Country choosed to forecast */
                    intent.putExtra("Query", mQuery);
                    intent.putExtra("City", mCity);
                    intent.putExtra("Country", mCountry);
                    intent.putExtra("id", mId);
                    startActivity(intent);
                } else {
                    getWeather();
                    mConfirm.setText(mCity + " (" + mCountry + ")." + " Is it correct ?");
                }
            }
        });
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


    /*-------API Usage-------*/

    //Call to Weath API
    public void getWeather() {
        retrofitConfig.getApiWeather().getWeather(mCity, Constants.LANG, Constants.UNITS, Constants.APPID).enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                CurrentWeatherData api_result = response.body();
                if (api_result == null) {
                    Log.d("FAILED", "Reponse from API call return NULL");
                    Toast.makeText(getApplicationContext(), "It seems the city you entered is not known from us...", Toast.LENGTH_SHORT).show();
                } else {
                    mCountry = api_result.getSys().getCountry();
                    mSearchButton.setText("OK");
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable t) {
                Log.d("FAILED", "message : " + t + call);
            }
        });
    }

    // Deserialize json with all world's city for auto-completion
    public static void City(Context context) throws IOException {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        TypeReference<List<CityList>> typeReferenceCity = new TypeReference<List<CityList>>() {
        };
        AssetManager manager = context.getAssets();
        InputStream inputStreamCity = manager.open("city.list.json");
        try {
            mCityLists = mapper.readValue(inputStreamCity, typeReferenceCity);
            Log.d("SUCCESS", "City Saved");
        } catch (Exception e) {
            Log.d("ERREUR", "Impossible de charger le fichier");
        }
    }


    //Define interface and its method to listen navbar
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_gps:
                    Log.d("INFO", "End");
                    Intent intent = new Intent(CityChoiceActivity.this, LocGPSActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_search:
                    intent = new Intent(CityChoiceActivity.this, CityChoiceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_fav:
                    /*intent = new Intent(CityChoiceActivity.this, FavouriteActivity.class);
                    startActivity(intent);*/
                    break;
            }
            return true;
        }
    };

}