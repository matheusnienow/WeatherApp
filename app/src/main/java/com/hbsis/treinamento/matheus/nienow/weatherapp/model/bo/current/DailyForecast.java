package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current;

import android.graphics.Bitmap;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Clouds;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Coord;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Weather;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Wind;

import java.io.Serializable;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class DailyForecast implements Serializable{
    public static String TAG_ID = "id";
    public static String TAG_NAME= "name";
    public static String TAG_COD = "cod";
    public static String TAG_DT = "dt";

    private int id;
    private String nome;
    private int cod;
    private int dt;
    private Clouds clouds;
    private Coord coord;
    private MainDaily mainDaily;
    private Sys sys;
    private Weather weather;
    private Wind wind;
    private transient Bitmap img;

    public DailyForecast(int id, String nome, int cod, int dt, Clouds clouds, Coord coord, MainDaily mainDaily, Sys sys, Weather weather, Wind wind) {
        this.id = id;
        this.nome = nome;
        this.cod = cod;
        this.dt = dt;
        this.clouds = clouds;
        this.coord = coord;
        this.mainDaily = mainDaily;
        this.sys = sys;
        this.weather = weather;
        this.wind = wind;
    }
    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public int getDt() {
        return dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getDy() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public MainDaily getMainDaily() {
        return mainDaily;
    }

    public void setMainDaily(MainDaily mainDaily) {
        this.mainDaily = mainDaily;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
