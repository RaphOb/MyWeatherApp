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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ListCommon> getList() {
        return list;
    }

    public void setList(List<ListCommon> list) {
        this.list = list;
    }




}
