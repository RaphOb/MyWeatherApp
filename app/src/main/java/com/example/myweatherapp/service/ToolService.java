package com.example.myweatherapp.service;

import com.example.myweatherapp.R;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class ToolService {

    /**
     *
     * @param value
     * @return
     */
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

    /**
     * Uses a ConcurrentHashMap instance to find out if there is any existing key with same value-
     * where key is obtained from a function reference.
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


}
