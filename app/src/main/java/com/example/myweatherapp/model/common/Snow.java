package com.example.myweatherapp.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snow {

    @SerializedName("snow.1h")
    @Expose
    private double snow1;

    @SerializedName("snow.3h")
    @Expose
    private double snow3;

    public double getSnow1() {
        return snow1;
    }

    public void setSnow1(double snow1) {
        this.snow1 = snow1;
    }

    public double getSnow3() {
        return snow3;
    }

    public void setSnow3(double snow3) {
        this.snow3 = snow3;
    }
}
