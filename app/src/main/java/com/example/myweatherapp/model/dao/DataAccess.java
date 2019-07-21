package com.example.myweatherapp.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myweatherapp.model.common.CityFav;

import java.util.List;

@Dao
public interface DataAccess {

    @Insert
    public void addTown(CityFav cityFav);

    @Query("select * from citifav")
    public List<CityFav> getCity();

    @Delete
    public void delCity(CityFav cityFav);
}
