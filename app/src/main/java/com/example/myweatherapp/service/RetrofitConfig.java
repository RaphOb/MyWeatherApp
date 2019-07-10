package com.example.myweatherapp.service;

import com.example.myweatherapp.others.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private Retrofit retrofit;
    private ApiWeather apiWeather;


    public RetrofitConfig() {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            apiWeather = retrofit.create(ApiWeather.class);

    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public ApiWeather getApiWeather() {
        return apiWeather;
    }

    public void setApiWeather(ApiWeather apiWeather) {
        this.apiWeather = apiWeather;
    }
}
