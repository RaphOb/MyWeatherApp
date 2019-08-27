package com.example.myweatherapp.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherapp.FavouriteActivity;
import com.example.myweatherapp.R;
import com.example.myweatherapp.model.common.CityFav;

import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.FavViewHolder> {

    private final LayoutInflater layoutInflater;
    private  Context mContext;
    private List<CityFav> mCityFav;

    public FavListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;

    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item,parent, false);
        FavViewHolder favViewHolder = new FavViewHolder(itemView);
        return favViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {

        if (mCityFav != null) {
            CityFav cityFav = mCityFav.get(position);
            holder.setData(cityFav.getName(),position);
        } else {
            holder.favItemView.setText("Aucun favoris enregistré");

        }
    }


    @Override
    public int getItemCount() {
        if(mCityFav != null) {
            return mCityFav.size();
        } else
        return 0 ;
    }

    public void setFav(List<CityFav> cityFavs) {
        mCityFav = cityFavs;
        notifyDataSetChanged();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {

        private TextView favItemView;
        private int mPosition;
        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            favItemView = itemView.findViewById(R.id.txvNote);
        }

        public void setData(String cityFav, int position) {
            favItemView.setText(cityFav);
            mPosition = position;
        }
    }
}
