package com.vu.hp.sunshine.app.Retrofit;


import com.vu.hp.sunshine.app.Model.WeatherForceCastResult;
import com.vu.hp.sunshine.app.Model.WeatherResult;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {

    /**
     * https://api.openweathermap.org/data/2.5/weather?lat=35&lon=139
     * @GET "weather"                        = weather(in 2.5/weather)
     *
     *
     * @param lat  Query "lat" = lat (lat =35)
     * @param lng  Query "lon" = lon (lon = 139)
     * @param appid
     * @param unit
     * @return
     */

    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);

    @GET("forecast")
    Observable<WeatherForceCastResult> getForceCastWeatherByLatLng(@Query("lat") String lat,
                                                                   @Query("lon") String lng,
                                                                   @Query("appid") String appid,
                                                                   @Query("units") String unit);

    @GET("weather")
    Observable<WeatherResult> getWeatherByCityName(@Query("q") String cityName,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);

}
