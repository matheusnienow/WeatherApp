package com.hbsis.treinamento.matheus.nienow.weatherapp.cityListing;

import android.support.design.widget.Snackbar;

import com.hbsis.treinamento.matheus.nienow.weatherapp.R;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.dao.DailyForecastDAO;

import java.util.ArrayList;

class CitiesPresenterImpl implements MVP.CitiesPresenter {

    private MVP.CitiesView view;

    CitiesPresenterImpl(MVP.CitiesView view) {
        this.view = view;
    }

    @Override
    public void recoverForecasts() {
        ArrayList<DailyForecast> dailyForecasts = DailyForecastDAO.recuperaForecasts(view.getContext());
        view.setForecasts(dailyForecasts);
    }

    @Override
    public void onCityRegistered(DailyForecast dailyForecast) {
        view.addCity(dailyForecast);
        view.showSnack(view.getContext().getString(R.string.cidade_cadastrada), Snackbar.LENGTH_LONG);
    }

    @Override
    public void onActionSort() {
        view.showSortDialog();
    }
}
