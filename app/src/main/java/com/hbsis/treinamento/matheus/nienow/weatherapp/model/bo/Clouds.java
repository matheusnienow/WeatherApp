package com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo;

import java.io.Serializable;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class Clouds implements Serializable {
    private String all;
    public static final String TAG_CLOUDS = "clouds";
    public static final String TAG_ALL = "all";

    public Clouds() {
    }

    public Clouds(String all) {
        this.all = all;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }
}
