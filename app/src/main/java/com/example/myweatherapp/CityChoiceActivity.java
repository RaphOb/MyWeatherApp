package com.example.myweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myweatherapp.model.currentWeather.CurrentWeatherData;
import com.example.myweatherapp.model.searchData.SearchWeatherData;
import com.example.myweatherapp.service.ApiWeather;
import com.example.myweatherapp.service.RetrofitConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityChoiceActivity extends AppCompatActivity {

    //Visual Elements
    private TextView mGreetingText;
    private TextView mCountryFound;
    private EditText mCityInput;
    private Button mSearchButton;
    private TextView mDescription;
    private TextView mTempActuelle;
    private TextView mTempMax;
    private TextView mTempMin;
    private TextView mPressure;
    private TextView mHumidity;
    private TextView mWindSpeed;

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

    //Converted Data
    private String mCity;

    //Query parameters
    private String lang = "Fr";
    private String units = "metric";


    /*----------Activity Usage--------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choice);
        //Get all visual elements
        mGreetingText = findViewById(R.id.activity_city_choice_greeting_txt);
        mCityInput = findViewById(R.id.activity_city_choice_name_input);
        mCountryFound = findViewById(R.id.activity_city_choice_city_result_txt);
        mSearchButton = findViewById(R.id.activity_city_choice_search_btn);

        mSearchButton.setEnabled(false);

        //Monitor changing on inputText
        mCityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSearchButton.setEnabled(charSequence.toString().length() != 0);
                mSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // The user just clicked
                        if (mSearchButton.getText() == "OK") {
                            //HERE WE START NEW ACTIVITY
                            mCountryFound.setText("NEW ACTIVITY TO START");

                            //Intent mainActivity = new Intent(CityChoiceActivity.this, MainActivity.class);
                            //startActivity(mainActivity);
                        } else {
                            mCity = mCityInput.getText().toString().trim();
                            getWeather();
                            mCityInput.getText().clear();
                        }
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchButton.setText("Search !");
            }
        });
        //Disable button while text is not entered
        mSearchButton.setClickable(false);
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


    //Configure Retrofit to call API
//    public void configureRetrofit() {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        retrofit = new Retrofit.Builder().baseUrl("https://community-open-weather-map.p.rapidapi.com/")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        apiWeather = retrofit.create(ApiWeather.class);
//    }

    public void getWeather() {
        retrofitConfig.getApiWeather().getWeather(mCity, lang, units).enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                CurrentWeatherData w = response.body();
                if (w == null) {
                    Log.d("FAILED", "Reponse from API call return NULL");
                    mCountryFound.setText("");
                    Toast.makeText(getApplicationContext(), "It seems the city you entered is not known from us...", Toast.LENGTH_SHORT).show();
                } else {
                    mCountryFound.setText("We found you in " + w.getSys().getCountry() + ". Is it correct?");
//                    mDescription.setText(w.getWeather().get(0).getDescription());
//                    mTempActuelle.setText(String.valueOf(w.getMain().getTemp()));
//                    mTempMax.setText(String.valueOf(w.getMain().getTemp_max()));
//                    mTempMin.setText(String.valueOf(w.getMain().getTemp_min()));
//                    mPressure.setText(String.valueOf(w.getMain().getPressure()));
//                    mHumidity.setText(String.valueOf(w.getMain().getHumidity()));
//                    mWindSpeed.setText(String.valueOf(w.getWind().getSpeed()));
                    Log.d("SUCCESS", " Country found according to the City");
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable t) {
                Log.d(">>>>>>ERREUR !!!!!! ", "message : " + t + call);
            }
        });
    }

   /* private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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
    };*/

}
