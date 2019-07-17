package com.example.myweatherapp.service;

import com.example.myweatherapp.model.currentWeather.CurrentWeatherData;
import com.example.myweatherapp.model.searchData.SearchWeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiWeather {


    @GET("weather")
    Call<CurrentWeatherData> getWeather(@Query("q") String q, @Query("lang") String lang, @Query("units") String units, @Query("APPID") String appid);


    @GET("forecast")
    Call<SearchWeatherData> getForecast(@Query("q") String q, @Query("lang") String lang, @Query("units") String units, @Query("APPID") String appid);

    @GET("forecast")
    Call<SearchWeatherData> getDayDetails(@Query("id") int id, @Query("lang") String lang, @Query("units") String units, @Query("APPID") String appid);
}
