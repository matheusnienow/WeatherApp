package com.hbsis.treinamento.matheus.nienow.weatherapp.activity;

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
import com.hbsis.treinamento.matheus.nienow.weatherapp.adapter.DailyAdapter;
import com.hbsis.treinamento.matheus.nienow.weatherapp.components.MyListView;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.util.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private DailyAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        MyListView listCidades = (MyListView) findViewById(R.id.lv_cidade);
        listCidades.attachFabForScroll(fab);

        if (adapter == null)
            adapter = new DailyAdapter(this, swipeRefreshLayout);

        if (listCidades.getAdapter() == null)
            listCidades.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                DailyForecast dailyForecast = (DailyForecast) data.getExtras().getSerializable(CadastroActivity.TAG_RESULT);
                adapter.addDailyForecast(dailyForecast, null);
                adapter.notifyDataSetChanged();
                makeSnack(getString(R.string.cidade_cadastrada), Snackbar.LENGTH_LONG);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.setContext(this);
            adapter.recuperaForecasts();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.ordenar_listagem_por))
                    .setItems(getResources().getStringArray(R.array.ordenar_opcoes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.sortCities(which);
                        }
                    }).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(this, CadastroActivity.class), 1);
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
                    makeSnack(getString(R.string.erro_estabelecer_conexao), Snackbar.LENGTH_LONG);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 5000);
    }

    private void makeSnack(String txt, int duration) {
        Snackbar.make(swipeRefreshLayout, txt, duration).show();
    }
}
