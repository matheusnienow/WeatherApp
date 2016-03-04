package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current;

import java.io.Serializable;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class Sys implements Serializable {
    public static final String TAG_SYS= "sys";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_COUNTRY = "country";
    public static final String TAG_SUNRISE = "sunrise";
    public static final String TAG_SUNSET = "sunset";
    private String message;
    private String country;
    private double sunrise;
    private double sunset;

    public Sys() {}

    public String getCountry() {
        return country;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getSunrise() {
        return sunrise;
    }

    public void setSunrise(double sunrise) {
        this.sunrise = sunrise;
    }

    public double getSunset() {
        return sunset;
    }

    public void setSunset(double sunset) {
        this.sunset = sunset;
    }
}
