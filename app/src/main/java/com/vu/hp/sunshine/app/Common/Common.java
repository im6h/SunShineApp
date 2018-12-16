package com.vu.hp.sunshine.app.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_API_KEY = "eeb265c7d11bcecc66076b68e0a94e8e";
    public static final String HOME_PAGE = "https://api.openweathermap.org/data/2.5/";
    public static final String HOME_ICON = "https://openweathermap.org/img/w/";

    public static Location current_location;

    public static String convertUnixToDate(int dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm EEE MM dd yyyy");
        String formatDate = simpleDateFormat.format(date);
        return formatDate;
    }
    public static String convertUnixToSunSeet(int dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm ");
        String formatDate = simpleDateFormat.format(date);
        return formatDate;
    }
}
