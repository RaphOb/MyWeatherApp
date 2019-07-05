package com.example.myweatherapp.service;

import com.example.myweatherapp.model.currentWeather.CurrentWeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiWeather {

    @Headers({
            "X-RapidAPI-Host : community-open-weather-map.p.rapidapi.com",
            "X-RapidAPI-Key : 76280a0c29msh5c2b710390f7e46p1592a4jsncba170a55638"
    })
    @GET("/weather?callback=test&id=2172797&units=%22metric%22+or+%22imperial%22&mode=xml%2C+html&q=London%2Cuk")
    Call<CurrentWeatherData> getWeather();
}
