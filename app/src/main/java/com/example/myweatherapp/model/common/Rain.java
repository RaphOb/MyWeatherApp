package com.example.myweatherapp.model.common;

import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("rain.1h")
    private double rain1;

    @SerializedName("rain.3h")
    private double rain3;

    public double getRain1() {
        return rain1;
    }

    public void setRain1(double rain1) {
        this.rain1 = rain1;
    }

    public double getRain3() {
        return rain3;
    }

    public void setRain3(double rain3) {
        this.rain3 = rain3;
    }
}
