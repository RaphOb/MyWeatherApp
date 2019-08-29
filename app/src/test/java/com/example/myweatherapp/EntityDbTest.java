package com.example.myweatherapp;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.myweatherapp.model.common.CityFav;
import com.example.myweatherapp.model.dao.DataAccess;
import com.example.myweatherapp.model.dao.MyAppDataBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.google.common.truth.Truth;


import java.io.IOException;
import java.util.List;


public class EntityDbTest {
    private CityFav cityFav;
    private MyAppDataBase db;



    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context,MyAppDataBase.class).build();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insert() throws Exception {

        CityFav cityFav = new CityFav();
        cityFav.setName("france");
        db.dataAccess().addTown(cityFav);
        LiveData<List<CityFav>> list = db.dataAccess().getCity();
       Truth.assertThat(list.getValue().get(0).getName()).contains("france");

    }
}
