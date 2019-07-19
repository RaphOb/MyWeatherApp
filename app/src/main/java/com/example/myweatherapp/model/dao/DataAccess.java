package com.example.myweatherapp.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.myweatherapp.model.common.CityFav;

@Dao
public interface DataAccess {

    @Insert
    public void addTown(CityFav cityFav);
}
