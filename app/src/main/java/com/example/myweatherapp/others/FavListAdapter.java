package com.example.myweatherapp.others;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherapp.FavouriteActivity;
import com.example.myweatherapp.ForecastActivity;
import com.example.myweatherapp.R;
import com.example.myweatherapp.model.common.CityFav;

import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.FavViewHolder> {

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(CityFav mCityFav);
    }


    private final LayoutInflater layoutInflater;
    private  Context mContext;
    private List<CityFav> mCityFav;
    private OnDeleteClickListener onDeleteClickListener;

    public FavListAdapter(Context context, OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        onDeleteClickListener = listener;

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
            holder.setListeners();
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
        private ImageView imgDelete;
        private TextView txtCountry;
        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            favItemView = itemView.findViewById(R.id.txvNote);
            imgDelete = itemView.findViewById(R.id.ivRowDelete);
            txtCountry = itemView.findViewById(R.id.txvNote);
        }

        public void setData(String cityFav, int position) {
            favItemView.setText(cityFav);
            mPosition = position;
        }

        public void setListeners() {
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onDeleteClickListener !=null) {
                        onDeleteClickListener.OnDeleteClickListener(mCityFav.get(mPosition));
                    }
                }
            });

            txtCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ForecastActivity.class);
                    /* Add City and Country choosed to forecast */
//                    intent.putExtra("Query", mQuery);
                    intent.putExtra("City", mCityFav.get(mPosition).getName());
                    intent.putExtra("Country", mCityFav.get(mPosition).getCountry());
                    intent.putExtra("id", mCityFav.get(mPosition).getIdTown());
                    ((Activity)mContext).startActivityForResult(intent,1);
                }
            });
        }
    }
}
