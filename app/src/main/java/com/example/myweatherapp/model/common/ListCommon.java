package com.example.myweatherapp.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCommon {

    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("sys")
    @Expose
    private Sys sys;

    @SerializedName("weathers")
    @Expose
    private List<Weather> weathers;

    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("rain")
    @Expose
    private Rain rain;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

}
