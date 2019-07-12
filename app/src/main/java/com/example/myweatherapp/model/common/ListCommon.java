package com.example.myweatherapp.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCommon {

    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("sys")
    @Expose
    private Sys sys;

    @SerializedName("weather")
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

    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;

    @SerializedName("dt")
    @Expose
    private int dt;

    private String DatesubString;

    public String getDate(String dtTxt) {
        return dtTxt.substring(0, Math.min(dtTxt.length(),10));
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }
}
