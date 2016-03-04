package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Coord;

import java.io.Serializable;

/**
 * Created by matheus.nienow on 23/11/2015.
 */
public class City implements Serializable {
    public static String TAG_CITY = "city";
    public static String TAG_NAME = "name";
    public static String TAG_ID = "id";
    public static String TAG_COORD = "coord";
    public static String TAG_COUNTRY = "country";
    private String nome;
    private int id;
    private Coord coord;
    private String country;

    public City() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
