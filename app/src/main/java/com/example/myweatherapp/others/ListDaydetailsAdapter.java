package com.example.myweatherapp.others;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myweatherapp.R;
import com.example.myweatherapp.model.common.ListCommon;

import java.util.List;

public class ListDaydetailsAdapter extends BaseAdapter {

    private Context context;
    private List<ListCommon> dayDetails;
    private String mHours;

    public ListDaydetailsAdapter(Context context, List<ListCommon> dayDetails) {
        this.context = context;
        this.dayDetails = dayDetails;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<ListCommon> getDayDetails() {
        return dayDetails;
    }

    public void setDayDetails(List<ListCommon> dayDetails) {
        this.dayDetails = dayDetails;
    }

    public String getmHours() {
        return mHours;
    }

    public void setmHours(String mHours) {
        this.mHours = mHours;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if( view == null) {
            Log.d("INFO", "IN");
            view = LayoutInflater.from(context).inflate(R.layout.day_row_details, null);
        }
        Log.d("INFO", "OUT");

        if (position == 0) {
            view.setBackgroundColor(Color.parseColor("#87CEFA"));
        }

        ListCommon day_now = dayDetails.get(position);

        TextView temperature = view.findViewById(R.id.day_row_details_temperature);
        //TextView description = view.findViewById(R.id.day_row_description);
        TextView wind_speed = view.findViewById(R.id.day_row_details_wind);
        TextView humidity = view.findViewById(R.id.day_row_details_humidity);
        ImageView imageView =  view.findViewById(R.id.imageView);

        //SET VISUAL ELEMENTS
        temperature.setText(String.valueOf((int)day_now.getMain().getTemp()) + "°C");
        //description.setText(String.valueOf(day.getWeathers().get(0).getDescription()));
        wind_speed.setText(String.valueOf((int)day_now.getWind().getSpeed()) + "km/h");
        humidity.setText(String.valueOf(day_now.getMain().getHumidity()) + "%");
        String url = Constants.URL_ICON + day_now.getWeathers().get(0).getIcon();
        new DownloadImageTask(imageView).execute(url);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 110));
        view.setLayoutParams(params);
        return view;
    }
}
