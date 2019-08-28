package com.example.myweatherapp.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myweatherapp.model.common.CityFav;

import java.util.List;

@Dao
public interface DataAccess {

    @Insert
     void addTown(CityFav cityFav);

    @Query("select * from citifav")
     LiveData<List<CityFav>> getCity();

    @Delete
     void delete(CityFav cityFav);

}
