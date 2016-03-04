package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by matheus.nienow on 23/11/2015.
 */
public class WeekForecast implements Serializable {
    public static String TAG_CODE = "cod";
    public static String TAG_MESSAGE = "message";
    public static String TAG_CNT = "cnt";
    public static String TAG_CITY = "city";
    public static String TAG_LIST = "list";
    private double code;
    private String message;
    private City city;
    private int cnt;
    private ArrayList<WeekDaily> weekDailies;

    public WeekForecast() {
    }

    public WeekForecast(double code, String message, City city, int cnt, ArrayList<WeekDaily> weekDailies) {
        this.code = code;
        this.message = message;
        this.city = city;
        this.cnt = cnt;
        this.weekDailies = weekDailies;
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public ArrayList<WeekDaily> getWeekDailies() {
        return weekDailies;
    }

    public void setWeekDailies(ArrayList<WeekDaily> weekDailies) {
        this.weekDailies = weekDailies;
    }
}



