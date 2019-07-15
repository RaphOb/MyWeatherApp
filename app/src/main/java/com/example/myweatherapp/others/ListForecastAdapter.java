package com.example.myweatherapp.others;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import com.example.myweatherapp.R;
import com.example.myweatherapp.model.common.ListCommon;

import java.util.List;


public class ListForecastAdapter extends BaseAdapter {


    private Context context;
    private List<ListCommon> forecast;

    public ListForecastAdapter(Context context, List<ListCommon> forecast)
    {
        this.context = context;
        this.forecast = forecast;

    }

    public List<ListCommon> getForeCast5Days() {
        return forecast;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return forecast.size();
    }

    @Override
    public Object getItem(int pos) {
        return forecast.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }


    /* Création de la vue à partir des données */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        /**
         * Recyclage de la vue si déja existente
         */
        if (view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.day_row, null);
        }
        if (position == 0)
            view.setBackgroundColor(Color.parseColor("#87CEFA"));
        //Get current day
        ListCommon day = forecast.get(position);
        //Set visual elements
        TextView temperature = view.findViewById(R.id.day_row_temperature);
        //TextView description = view.findViewById(R.id.day_row_description);
        TextView current_day = view.findViewById(R.id.day_row_current_day);
        TextView wind_speed = view.findViewById(R.id.day_row_wind);
        //TextView humidity = view.findViewById(R.id.day_row_humidity);
        ImageView imageView =  view.findViewById(R.id.imageView);
        //Downloads icon
        String url = Constants.URL_ICON + day.getWeathers().get(0).getIcon();
        new DownloadImageTask(imageView).execute(url);

        //Set visual elemnts
        temperature.setText(String.valueOf((int)day.getMain().getTemp()) + "°C");

        //Set current Day
        //If February
        if (getCurrentMonth() == 1)
        {
            current_day.setText(convertIntToDay((getCurrentDay() + position) % 8) + " " + String.valueOf((getCurrentMonthDay() + position) % 28));
        }
        else if(getCurrentMonth() % 2 == 0) {
            current_day.setText(convertIntToDay((getCurrentDay() + position) % 8) + " " + String.valueOf((getCurrentMonthDay() + position) % 31));
        }
        else{
            current_day.setText(convertIntToDay((getCurrentDay() + position)%8) + " " + String.valueOf((getCurrentMonthDay() + position)%30));
        }

        //description.setText(String.valueOf(day.getWeathers().get(0).getDescription()));
        wind_speed.setText(String.valueOf((int)day.getWind().getSpeed()) + "km/h");
        //humidity.setText(String.valueOf(day.getMain().getHumidity()) + "%");
        return view;
    }

    public int getCurrentDay()
    {
        Calendar calendar = Calendar.getInstance();
        //Set the current day
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getCurrentMonthDay()
    {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getCurrentMonth()
    {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public String convertIntToDay(int currentDay)
    {
        switch (currentDay) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }
        return "undefined";
    }
}

