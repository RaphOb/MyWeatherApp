package com.example.myweatherapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherapp.model.common.CityFav;
import com.example.myweatherapp.model.common.ListFavViewModel;
import com.example.myweatherapp.others.FavListAdapter;

import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private FavListAdapter favListAdapter;
    private ListFavViewModel listFavViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_fav);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        favListAdapter = new FavListAdapter(this);
        recyclerView.setAdapter(favListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listFavViewModel = ViewModelProviders.of(this).get(ListFavViewModel.class);

        listFavViewModel.getAllFavs().observe(this, cityFavs -> favListAdapter.setFav(cityFavs));
    }
}
