package com.vu.hp.sunshine.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {

    public static TodayWeatherFragment instances;

    public static TodayWeatherFragment getInstances() {
        if (instances == null){
            instances = new TodayWeatherFragment();
        }
        return instances;
    }

    public TodayWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today_weather, container, false);
    }

}
