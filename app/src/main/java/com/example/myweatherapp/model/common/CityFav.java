package com.example.myweatherapp.model.common;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "citifav")
public class CityFav {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="city_name")
    private String name;

    @ColumnInfo(name = "city_id")
    private Integer idTown;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdTown() {
        return idTown;
    }

    public void setIdTown(Integer idTown) {
        this.idTown = idTown;
    }
}
