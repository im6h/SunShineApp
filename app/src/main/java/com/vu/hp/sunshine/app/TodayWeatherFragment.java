package com.vu.hp.sunshine.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vu.hp.sunshine.app.Common.Common;
import com.vu.hp.sunshine.app.Model.WeatherResult;
import com.vu.hp.sunshine.app.Retrofit.IOpenWeatherMap;
import com.vu.hp.sunshine.app.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {

    static TodayWeatherFragment instances;

    ImageView img_icon;
    ProgressBar loading;
    LinearLayout weather_panel;
    TextView tv_city, tv_description, tv_temprature, tv_wind, tv_datetime, tv_cloud,
            tv_coord, tv_pressure, tv_humidity, tv_sunshine, tv_sunset;
    CompositeDisposable compositeDisposable;
    Disposable disposable;
    IOpenWeatherMap mService;

    public static TodayWeatherFragment getInstances() {
        if (instances == null) {
            instances = new TodayWeatherFragment();
        }
        return instances;
    }

    public TodayWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        loading = (ProgressBar) itemView.findViewById(R.id.prgBar);
        img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
        weather_panel = (LinearLayout) itemView.findViewById(R.id.weather_panel);
        tv_city = (TextView) itemView.findViewById(R.id.tv_city);
        tv_pressure = (TextView) itemView.findViewById(R.id.tv_pressure);
        tv_wind = (TextView) itemView.findViewById(R.id.tv_wind);
        tv_sunset = (TextView) itemView.findViewById(R.id.tv_sunset);
        tv_sunshine = (TextView) itemView.findViewById(R.id.tv_sunrise);
        tv_datetime = (TextView) itemView.findViewById(R.id.tv_dateTime);
        tv_description = (TextView) itemView.findViewById(R.id.tv_description);
        tv_temprature = (TextView) itemView.findViewById(R.id.tv_temparture);
        tv_coord = (TextView) itemView.findViewById(R.id.tv_coord);
        tv_humidity = (TextView) itemView.findViewById(R.id.tv_humidity);

        getWeatherInformation();
        return itemView;
    }

    public void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()), String.valueOf(Common.current_location.getLongitude()), Common.APP_API_KEY, "matric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        // load image
                        Picasso.get().load(new StringBuilder(Common.HOME_ICON).append(weatherResult.getWeather().get(0).getIcon()).append(".png").toString()).into(img_icon);
                        // load data
                        tv_city.setText(weatherResult.getName());
                        tv_description.setText(new StringBuilder("Weather in ").append(weatherResult.getName()).toString());
                        tv_temprature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append(" C").toString());
                        tv_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append("Hpa").toString());
                        tv_wind.setText(new StringBuilder(String.valueOf(weatherResult.getWind().getSpeed())).append("km/h-").append(String.valueOf(weatherResult.getWind().getDeg())).append("").toString());
                        tv_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                        tv_sunset.setText(Common.convertUnixToSunSeet(weatherResult.getSys().getSunset()));
                        tv_sunshine.setText(Common.convertUnixToSunSeet(weatherResult.getSys().getSunrise()));
                        tv_datetime.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        tv_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().getLat()).append("-").append(weatherResult.getCoord().getLon()).append("]").toString());

                        // display
                        displayPanel();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }


    private void displayPanel() {
        weather_panel.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }
}
