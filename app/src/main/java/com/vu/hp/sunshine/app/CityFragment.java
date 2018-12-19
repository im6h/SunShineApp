package com.vu.hp.sunshine.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.vu.hp.sunshine.app.Common.Common;
import com.vu.hp.sunshine.app.Model.WeatherResult;
import com.vu.hp.sunshine.app.Retrofit.IOpenWeatherMap;
import com.vu.hp.sunshine.app.Retrofit.RetrofitClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {

    static CityFragment instances;
    private List<String> lstCities;

    MaterialSearchBar materialSearchBar;
    ImageView img_icon;
    ProgressBar loading;
    LinearLayout weather_panel;
    TextView tv_city, tv_description, tv_temprature, tv_wind, tv_datetime,
            tv_coord, tv_pressure, tv_humidity, tv_sunshine, tv_sunset;
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    public static CityFragment getInstances() {
        if (instances == null){
            instances = new CityFragment();
        }
        return instances;
    }

    public CityFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_city, container, false);

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
        materialSearchBar = (MaterialSearchBar)itemView.findViewById(R.id.searchBar);
        materialSearchBar.setEnabled(true);

        new loadCities().execute(); // asynctask

        return itemView;
    }
    private class loadCities extends SimpleAsyncTask<List<String>>{

        @Override
        protected List<String> doInBackgroundSimple() {
            lstCities = new ArrayList<>();
            try {
                StringBuilder builder = new StringBuilder();
                InputStream is = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(is);

                InputStreamReader isr = new InputStreamReader(gzipInputStream);
                BufferedReader br = new BufferedReader(isr);

                String readed;
                while ((readed = br.readLine())!= null){
                    builder.append(readed);
                }
            lstCities = new Gson().fromJson(builder.toString(),new TypeToken<List<String>>(){}.getType());
            }catch (IOException io){
                io.printStackTrace();
            }
            return lstCities;
        }

        @Override
        protected void onSuccess(final List<String> listCity) {
            super.onSuccess(listCity);
            materialSearchBar.setEnabled(true);
            materialSearchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest = new ArrayList<>();
                    for(String search : listCity){
                        if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                            suggest.add(search);
                        }
                        materialSearchBar.setLastSuggestions(suggest);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {
                    getWeatherInformation(text.toString());
                    materialSearchBar.setLastSuggestions(listCity);
                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });
            materialSearchBar.setLastSuggestions(listCity);

            loading.setVisibility(View.GONE);
        }
    }

    private void getWeatherInformation(String cityName) {
        compositeDisposable.add(mService.getWeatherByCityName(cityName, Common.APP_API_KEY, "matric")
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
                        tv_temprature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°F").toString());
                        tv_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" Hpa").toString());
                        tv_wind.setText(new StringBuilder(String.valueOf(weatherResult.getWind().getSpeed())).append("km/h-").append(String.valueOf(weatherResult.getWind().getDeg())).append("").toString());
                        tv_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                        tv_sunset.setText(Common.convertUnixToSunSeet(weatherResult.getSys().getSunset()));
                        tv_sunshine.setText(Common.convertUnixToSunSeet(weatherResult.getSys().getSunrise()));
                        tv_datetime.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        tv_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().getLat()).append("-").append(weatherResult.getCoord().getLon()).append("]").toString());
                        loading.setVisibility(View.GONE);
                        weather_panel.setVisibility(View.VISIBLE);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR",""+throwable.getMessage());
                    }
                }));
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        compositeDisposable.clear();
        super.onDestroyView();
    }
}
