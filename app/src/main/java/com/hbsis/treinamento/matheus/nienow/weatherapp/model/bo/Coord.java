package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo;

import java.io.Serializable;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class Coord implements Serializable {
    public static String TAG_COORD = "coord";
    public static String TAG_LON = "lon";
    public static String TAG_LAT = "lat";
    private double lon;
    private double lat;

    public Coord() {
    }

    public Coord(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
