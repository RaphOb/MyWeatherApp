package com.example.myweatherapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweatherapp.model.common.CityList;
import com.example.myweatherapp.model.common.Coord;
import com.example.myweatherapp.service.LocationGPS;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LocGPSActivity extends AppCompatActivity {


    private Coord myCoord;
    private double bestDistance;

    public static List<CityList> mCityLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //Set GPS
        LocationGPS locationGPS = new LocationGPS(this);
        locationGPS.refreshLocation();

        this.myCoord = new Coord();
        myCoord.setLat(locationGPS.getLatitude());
        myCoord.setLon(locationGPS.getLongitude());
        Log.d("LOC", String.valueOf(myCoord.getLat()));
        Log.d("LOC", String.valueOf(myCoord.getLon()));

        Coord c = findMostNearByCity();
    }

    private Coord findMostNearByCity()
    {
        Coord bestCoordFound = new Coord();
        bestCoordFound.setLat(-10000);
        bestCoordFound.setLon(-10000);
        try
        {
            //Get list of all city
            if (mCityLists == null)
                CityChoiceActivity.City(getApplicationContext());

            mCityLists = CityChoiceActivity.mCityLists;

            //Use this variable to stock best distance found between 2 Coord
            this.bestDistance = 1000000;
            //For each city
            for(CityList c: mCityLists)
            {
                Log.d("INFO", "nom : " + c.getName());

                //Ne marche pas parce que le forecast ne fournit pas les coordonnées
                //Il faut requeter chaque ville une par une pour récupérer les coordonnées
                //avec l'endpoint CurrentWeatherData et comparer chaque coordonnées récupérés

                //Search mostNearByLocation comparing Coord of user
                //bestCoordFound = getMostNearByCoord(c, bestCoordFound);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return bestCoordFound;
    }

    private Coord getMostNearByCoord(CityList c, Coord bestCoordFound)
    {
        //Toujours null avec le endpoint forecast :(
        if (c.getCoord() == null)
        {
            Log.d("INFO", "Found a city without Coord");
            return null;
        }
        //Get Coord of city to compare
        Coord coord = c.getCoord();
        //Get Lon and Lat of city to compare
        double currentLat = coord.getLat();
        double currentLon = coord.getLon();
        //Get Lon and Lat where user is
        double myLat = myCoord.getLat();
        double myLon = myCoord.getLon();

        //Math Formula to calcul distance between 2 points
        double dist_calculated = Math.sqrt( Math.pow(myLat - currentLat, 2) + Math.pow(myLon - currentLon, 2));

        if(dist_calculated < bestDistance)
        {
            //Stock distanceFound
            bestDistance = dist_calculated;
            //Stock Coord found
            bestCoordFound.setLon(currentLat);
            bestCoordFound.setLat(currentLon);

            Log.d("MEILLEUR DISTANCE", "DIST : " + String.valueOf(bestDistance));
            Log.d("MEILLEUR LON", "LON : " + String.valueOf(bestCoordFound.getLon()));
            Log.d("MEILLEUR LAT", "LAT : " + String.valueOf(bestCoordFound.getLat()));
        }
        return bestCoordFound;
    }


}
