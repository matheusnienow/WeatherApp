package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current;

import java.io.Serializable;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class MainDaily implements Serializable {
    public static String TAG_MAIN = "main";
    public static String TAG_TEMP = "temp";
    public static String TAG_HUMIDITY = "humidity";
    public static String TAG_PRESSURE = "pressure";
    public static String TAG_TEMP_MIN = "temp_min";
    public static String TAG_TEMP_MAX = "temp_max";
    private double temp;
    private double humidity;
    private double pressure;
    private double tempMin;
    private double tempMax;

    public MainDaily() {}

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }
}
