package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Weather;

import java.io.Serializable;

public class WeekDaily implements Serializable{
    public static String TAG_DT = "dt";
    public static String TAG_PRESSURE = "pressure";
    public static String TAG_HUMIDITY = "humidity";
    public static String TAG_SPEED = "speed";
    public static String TAG_CLOUDS = "clouds";
    public static String TAG_DEG = "deg";
    public static String TAG_TEMP = "temp";

    private int dt;
    private Weather weather;
    private double pressure;
    private double humidity;
    private Temp temp;
    private double speed;
    private double clouds;
    private double deg;

    public WeekDaily() {}

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public double getClouds() {
        return clouds;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }
}