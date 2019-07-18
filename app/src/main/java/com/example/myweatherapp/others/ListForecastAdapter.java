package com.example.myweatherapp.others;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import com.example.myweatherapp.R;
import com.example.myweatherapp.model.common.ListCommon;

import java.util.List;


public class ListForecastAdapter extends BaseAdapter {


    private Context context;
    private List<ListCommon> forecast;
    private String mDay;

    public ListForecastAdapter(Context context, List<ListCommon> forecast)
    {
        this.context = context;
        this.forecast = forecast;

    }

    public String getDate()
    {
        return mDay;
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

        //Get current day
        ListCommon day = forecast.get(position);

        //DEFINE VISUAL ELEMENTS
        TextView temperature = view.findViewById(R.id.day_row_temperature);
        TextView current_day = view.findViewById(R.id.day_row_current_day);
        ImageView imageView =  view.findViewById(R.id.imageView);

        //SET VISUAL ELEMENTS
        temperature.setText(String.valueOf((int)day.getMain().getTemp()) + "°C");
        //Set backgroundColor and font style for the currentDay
        if (position == 0) {
            view.setBackgroundColor(Color.parseColor("#87CEFA"));
            current_day.setTypeface(null, Typeface.BOLD);
        }

        mDay = getDayForList(position);
        //Set the date
        current_day.setText(mDay + "\n" + day.getDtTxt());
        //Downloads icon
        String url = Constants.URL_ICON + day.getWeathers().get(0).getIcon();
        new DownloadImageTask(imageView).execute(url);

        //Set height for a row
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 110));
        view.setLayoutParams(params);
        return view;
    }

    public int getCurrentDay()
    {
        Calendar calendar = Calendar.getInstance();
        //Set the current day
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public int getCurrentMonth()
    {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public String convertIntToDay(int currentDay)
    {
        Log.d("INFO", "CONVERSION DE " + currentDay);
        switch (currentDay) {
            case 1:
                return "Lundi";
            case 2:
                return "Mardi";
            case 3:
                return "Mercredi";
            case 4:
                return "Jeudi";
            case 5:
                return "Vendredi";
            case 6:
                return "Samedi";
            case 7:
                return "Dimanche";
            default:
                return "undefined";
        }
    }

    //Give the right day according to position in ListView
    public String getDayForList(int position)
    {
        //Set days
        int numDay = (getCurrentDay() + position) % 7;
        Log.d("INFO", "NUMERO DE JOUR " + getCurrentDay());
        Log.d("INFO", "POSITION COURANTE" + position);
        Log.d("INFO", "Calcul : " + getCurrentDay() + " " + position);

        //0 isn't a valid number for a day (start at 1)
        if (numDay == 0)
            numDay = 7;
            position++;

        Log.d("INFO", "Jour calculé " + numDay);
        return convertIntToDay(numDay);
    }
}

