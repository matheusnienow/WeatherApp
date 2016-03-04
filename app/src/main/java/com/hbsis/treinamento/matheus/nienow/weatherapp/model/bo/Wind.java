package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo;

import java.io.Serializable;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class Wind implements Serializable{
    public static String TAG_WIND = "wind";
    public static String TAG_SPEED = "speed";
    public static String TAG_DEG = "deg";
    private double speed;
    private double deg;

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
