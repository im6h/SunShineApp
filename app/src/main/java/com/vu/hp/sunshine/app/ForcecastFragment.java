package com.vu.hp.sunshine.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vu.hp.sunshine.app.Adapter.WeatherForceCastAdapter;
import com.vu.hp.sunshine.app.Common.Common;
import com.vu.hp.sunshine.app.Model.WeatherForceCastResult;
import com.vu.hp.sunshine.app.Retrofit.IOpenWeatherMap;
import com.vu.hp.sunshine.app.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForcecastFragment extends Fragment {

    static ForcecastFragment instances;
    LinearLayout main_info;
    TextView tv_fc_city, tv_fc_coord;
    RecyclerView rcv_fc;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap iOpenWeatherMap;


    public static ForcecastFragment getInstances() {
        if (instances == null) {
            instances = new ForcecastFragment();
        }
        return instances;
    }

    public ForcecastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        iOpenWeatherMap = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forcecast, container, false);
        main_info = (LinearLayout) view.findViewById(R.id.main_info);
        tv_fc_city = (TextView) view.findViewById(R.id.tv_fc_city);
        tv_fc_coord = (TextView) view.findViewById(R.id.tv_fc_coord);
        rcv_fc = (RecyclerView) view.findViewById(R.id.rcv_fc);
        rcv_fc.setHasFixedSize(true);
        rcv_fc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getInformation();
        return view;
    }

    private void getInformation() {
        compositeDisposable.add(iOpenWeatherMap.getForceCastWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()), String.valueOf(Common.current_location.getLongitude()), Common.APP_API_KEY, "matric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForceCastResult>() {
                    @Override
                    public void accept(WeatherForceCastResult weatherForceCastResult) throws Exception {
                        tv_fc_city.setText(new StringBuilder(weatherForceCastResult.city.getName()));
                        tv_fc_coord.setText(new StringBuilder(weatherForceCastResult.city.getCoord().toString()));
                        WeatherForceCastAdapter adapter = new WeatherForceCastAdapter(getContext(), weatherForceCastResult);
                        rcv_fc.setAdapter(adapter);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "Force Cast" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR",""+throwable.getMessage());

                    }
                }));
    }


    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
