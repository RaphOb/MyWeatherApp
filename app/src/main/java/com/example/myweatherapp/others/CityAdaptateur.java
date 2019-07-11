package com.example.myweatherapp.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.myweatherapp.R;
import com.example.myweatherapp.model.common.CityList;


public class CityAdaptateur extends ArrayAdapter {

    Context context;
    int resource, textViewResourceId;
    List<CityList> citys, tempCities, suggestions;

    public CityAdaptateur(Context context, int resource, int textViewResourceId, List<CityList> citys) {
        super(context, resource, textViewResourceId, citys);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.citys = citys;
        tempCities = new ArrayList<CityList>(citys);
        suggestions = new ArrayList<CityList>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        CityList cityList = citys.get(position);
        if (cityList != null) {
            TextView lblName = (TextView) view.findViewById(android.R.id.text1);
            if (lblName != null)
                lblName.setText(cityList.getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((CityList) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CityList c : tempCities) {
                    if (c.getName().toLowerCase().contains(constraint.toString().toLowerCase()) &&
                        c.getCountry() != null &&
                        c.getCoord() != null) {
                        suggestions.add(c);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<CityList> filterList = (ArrayList<CityList>) results.values;
            List<CityList> copy = new ArrayList<>();
            if (results != null && results.count > 0) {
                clear();
                for (CityList c : filterList) {
                    copy.add(c);
                }
                addAll(copy);
                notifyDataSetChanged();
            }
        }
    };

}
