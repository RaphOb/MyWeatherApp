package com.example.myweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweatherapp.model.currentWeather.CurrentWeatherData;
import com.example.myweatherapp.others.Constants;
import com.example.myweatherapp.service.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityChoiceActivity extends AppCompatActivity {

    //Visual Elements
    private TextView mGreetingText;
    private TextView mCountryFound;
    private EditText mCityInput;
    private Button mSearchButton;

    //Retrofit instance
    RetrofitConfig retrofitConfig = new RetrofitConfig();

    //Converted Data
    private String mCity;

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

        //Monitor changing on inputText
        mCityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /* Set clickable for search button */
                mSearchButton.setEnabled(charSequence.toString().length() != 0);
                /* Monitor click events */
                mSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // The user just clicked
                        if (mSearchButton.getText() == "OK") {
                            /* Launch 5 next days results */
                            Intent intent = new Intent(CityChoiceActivity.this, ForecastActivity.class);
                            intent.putExtra("City", mCity);
                            startActivity(intent);
                        } else {
                            mCity = mCityInput.getText().toString().trim();
                            getWeather();
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchButton.setText("Search !");
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

    public void getWeather() {
        retrofitConfig.getApiWeather().getWeather(mCity, Constants.LANG, Constants.UNITS, Constants.APPID).enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                CurrentWeatherData w = response.body();
                if (w == null) {
                    Log.d("FAILED", "Reponse from API call return NULL");
                    mCountryFound.setText("");
                    Toast.makeText(getApplicationContext(), "It seems the city you entered is not known from us...", Toast.LENGTH_SHORT).show();
                } else {
                    mCountryFound.setText("We found you in " + w.getSys().getCountry() + ". Is it correct?");
                    mSearchButton.setText("OK");
                    Log.d("SUCCESS", " Country found according to the City");
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable t) {
                Log.d(">>>>>>ERREUR !!!!!! ", "message : " + t + call);
            }
        });
    }
}
