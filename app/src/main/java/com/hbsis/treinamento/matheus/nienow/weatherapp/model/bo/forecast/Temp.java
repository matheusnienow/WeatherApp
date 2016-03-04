package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast;

import java.io.Serializable;

/**
 * Created by matheus.nienow on 23/11/2015.
 */
public class Temp implements Serializable {
    public static String TAG_TEMP = "temp";
    public static String TAG_DAY = "day";
    public static String TAG_MIN = "min";
    public static String TAG_MAX = "max";
    public static String TAG_NIGHT = "night";
    public static String TAG_EVE = "eve";
    public static String TAG_MORN = "morn";

    private double day;
    private double min;
    private double max;
    private double night;
    private double eve;
    private double morn;

    public Temp(double day, double min, double max, double night, double eve, double morn) {
        this.day = day;
        this.min = min;
        this.max = max;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getEve() {
        return eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }
}
