package com.example.myweatherapp.service;

import com.example.myweatherapp.R;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;


public abstract class ToolService {

    public static Integer getImageOrientation(double value) {
        RangeMap<Integer, Integer> WindDirection = TreeRangeMap.create();
        WindDirection.put(Range.closed(340, 360), R.drawable.nwind);
        WindDirection.put(Range.closed(0, 15), R.drawable.nwind);
        WindDirection.put(Range.closed(16, 80), R.drawable.newind);
        WindDirection.put(Range.closed(81, 110), R.drawable.ewind);
        WindDirection.put(Range.closed(111, 160), R.drawable.sewind);
        WindDirection.put(Range.closed(161, 200), R.drawable.swind);
        WindDirection.put(Range.closed(201, 250), R.drawable.sowind);
        WindDirection.put(Range.closed(251, 300), R.drawable.owind);
        WindDirection.put(Range.closed(301, 339), R.drawable.nowind);
        Integer deg = (int) value;

        return WindDirection.get((deg));
    }


}
