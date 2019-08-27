package com.example.myweatherapp.model.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myweatherapp.model.common.CityFav;

@Database(entities = {CityFav.class}, version = 1)
public abstract class MyAppDataBase extends RoomDatabase {

    public  abstract DataAccess dataAccess();

    private static volatile MyAppDataBase myAppDataBaseInstance;

    public static MyAppDataBase getDatabase(final Context context) {
        if( myAppDataBaseInstance == null) {
            synchronized (MyAppDataBase.class) {
                if(myAppDataBaseInstance == null) {
                    myAppDataBaseInstance = Room.databaseBuilder(context.getApplicationContext(), MyAppDataBase.class, "citydb").allowMainThreadQueries().build();
                }
            }
        }
        return myAppDataBaseInstance;
    }
}
