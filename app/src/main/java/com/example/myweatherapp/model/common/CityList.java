package com.example.myweatherapp.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CityList implements Comparable<CityList>{

    private Double id;

    private String name;

    private String country;

    @JsonIgnore
    private Coord coord;

    @Override
    public int compareTo(CityList cl) {
        return this.name.compareTo(cl.name);
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }
}
