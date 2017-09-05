package com.hbsis.treinamento.matheus.nienow.weatherapp.cityListing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.hbsis.treinamento.matheus.nienow.weatherapp.R;
import com.hbsis.treinamento.matheus.nienow.weatherapp.activity.RegisterActivity;
import com.hbsis.treinamento.matheus.nienow.weatherapp.adapter.DailyAdapter;
import com.hbsis.treinamento.matheus.nienow.weatherapp.components.FabListView;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.util.Util;

import java.util.ArrayList;

public class CitiesActivity extends AppCompatActivity implements MVP.CitiesView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private DailyAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MVP.CitiesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPresenter();
        initComponents();
    }

    private void initPresenter() {
        if (presenter == null)
            presenter = new CitiesPresenterImpl(this);
    }

    private void initComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        FabListView cityList = (FabListView) findViewById(R.id.lv_cidade);
        cityList.attachFabForScroll(fab);
        View emptyView = findViewById(R.id.empty_layout);
        cityList.setEmptyView(emptyView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (adapter == null)
            adapter = new DailyAdapter(this, swipeRefreshLayout);

        if (cityList.getAdapter() == null)
            cityList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                DailyForecast dailyForecast = (DailyForecast) data.getExtras().getSerializable(RegisterActivity.TAG_RESULT);
                presenter.onCityRegistered(dailyForecast);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.setContext(this);
            presenter.recoverForecasts();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            presenter.onActionSort();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivityForResult(new Intent(this, RegisterActivity.class), 1);
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Util.isOnline(getApplicationContext())) {
                    swipeRefreshLayout.setRefreshing(true);
                    adapter.atualizaForecasts(swipeRefreshLayout);
                } else {
                    showSnack(getString(R.string.erro_estabelecer_conexao), Snackbar.LENGTH_LONG);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 5000);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showSnack(String txt, int duration) {
        Snackbar.make(swipeRefreshLayout, txt, duration).show();
    }

    @Override
    public void setForecasts(ArrayList<DailyForecast> dailyForecasts) {
        adapter.setDailyForecasts(dailyForecasts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addCity(DailyForecast dailyForecast) {
        adapter.addDailyForecast(dailyForecast, null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.ordenar_listagem_por))
                .setItems(getResources().getStringArray(R.array.ordenar_opcoes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.sortCities(which);
                    }
                }).show();
    }
}
