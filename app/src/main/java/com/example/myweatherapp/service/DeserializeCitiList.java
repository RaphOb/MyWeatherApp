package com.example.myweatherapp.service;

import android.content.Context;
import android.util.Log;

import com.example.myweatherapp.model.common.CityList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DeserializeCitiList  {
    public static List<CityList> cityLists;

    public DeserializeCitiList(Context context) throws IOException {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        TypeReference<List<CityList>> typeReferenceCity = new TypeReference<List<CityList>>() {
        };
        InputStream inputStreamCity = context.getAssets().open("city.list.json");
        try {
            cityLists = mapper.readValue(inputStreamCity, typeReferenceCity);
        } catch (Exception e) {
            Log.d("ERREUR", "Impossible de charger le fichier");
        }
    }

}
