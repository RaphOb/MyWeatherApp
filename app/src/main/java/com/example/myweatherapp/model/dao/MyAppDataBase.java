package com.example.myweatherapp.model.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myweatherapp.model.common.CityFav;

@Database(entities = {CityFav.class}, version = 1)
public abstract class MyAppDataBase extends RoomDatabase {

    public  abstract DataAccess dataAccess();
}
