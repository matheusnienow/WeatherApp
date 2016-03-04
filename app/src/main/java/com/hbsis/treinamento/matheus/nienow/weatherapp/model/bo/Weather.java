package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo;

import java.io.Serializable;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class Weather implements Serializable {
    public static String TAG_WEATHER = "weather";
    public static String TAG_DESCRIPTION = "description";
    public static String TAG_ID = "id";
    public static String TAG_ICON = "icon";
    public static String TAG_MAIN = "main";
    private int id;
    private String main;
    private String description;
    private String icon;

    public Weather() {}
    public Weather(int id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
