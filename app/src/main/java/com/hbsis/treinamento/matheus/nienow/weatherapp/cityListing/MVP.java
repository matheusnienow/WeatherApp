package com.hbsis.treinamento.matheus.nienow.weatherapp.cityListing;

import android.content.Context;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;

import java.util.ArrayList;

class MVP {
    interface CitiesView {
        Context getContext();

        void showSnack(String txt, int duration);

        void setForecasts(ArrayList<DailyForecast> dailyForecasts);

        void addCity(DailyForecast dailyForecast);

        void showSortDialog();
    }

    interface CitiesPresenter{

        void recoverForecasts();

        void onCityRegistered(DailyForecast dailyForecast);

        void onActionSort();
    }
}
