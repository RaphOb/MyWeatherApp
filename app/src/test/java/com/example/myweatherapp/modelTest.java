package com.example.myweatherapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import com.example.myweatherapp.model.common.CityList;
import com.example.myweatherapp.model.common.Weather;
import com.example.myweatherapp.service.ToolService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class modelTest {
    @Test
    public void createModel() {
        Weather weather = new Weather();
        weather.setDescription("une description");
        assertEquals(weather.getDescription(), "une description");
    }


    @Test
    public void serviceTest() {
        assertEquals(2131165312, (long) ToolService.getImageOrientation(12));
    }

////PROB de CONTEXT
//    @Test
//    public void testCity() throws IOException {
//        List<CityList> mCityLists = new ArrayList<>();
//        Context context = ApplicationProvider.getApplicationContext();
//        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//        TypeReference<List<CityList>> typeReferenceCity = new TypeReference<List<CityList>>() {
//        };
//        AssetManager manager = context.getAssets();
//        InputStream inputStreamCity = manager.open("city.list.json");
//        try {
//            mCityLists = mapper.readValue(inputStreamCity, typeReferenceCity);
//            Log.d("SUCCESS", "City Saved");
//        } catch (Exception e) {
//            Log.d("ERREUR", "Impossible de charger le fichier");
//        }
//        assertNotNull(mCityLists);
//    }


}
