package com.example.myweatherapp.model.searchData;

import com.example.myweatherapp.model.common.ListCommon;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchWeatherData {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("cod")
    @Expose
    private Integer cod;

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("list")
    @Expose
    private List<ListCommon> list;



}
