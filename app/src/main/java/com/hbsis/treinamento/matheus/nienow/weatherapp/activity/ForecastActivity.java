package com.hbsis.treinamento.matheus.nienow.weatherapp.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbsis.treinamento.matheus.nienow.weatherapp.R;
import com.hbsis.treinamento.matheus.nienow.weatherapp.adapter.DailyAdapter;
import com.hbsis.treinamento.matheus.nienow.weatherapp.adapter.WeekForecastAdapter;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.dao.WeekForecastDAO;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.WeekDaily;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.WeekForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.parser.JSONParser;
import com.hbsis.treinamento.matheus.nienow.weatherapp.util.Util;

import org.json.JSONException;

import java.io.InputStream;

/**
 * Created by matheus.nienow on 20/11/2015.
 */
public class ForecastActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private DailyForecast dailyForecast;
    private WeekForecastAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_header);
        progressBar.setVisibility(View.VISIBLE);

        adapter = new WeekForecastAdapter(this);
        ListView listForecast = (ListView) findViewById(R.id.list_forecast);
        listForecast.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_detalhe);
        swipeRefreshLayout.setOnRefreshListener(this);

        dailyForecast = (DailyForecast) getIntent().getExtras().getSerializable(DailyAdapter.TAG_DAILY_FORECAST);
        getForecast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.setContext(this);
            adapter.recoverWeekForecast(dailyForecast);
        }
    }

    private void cadastraForecast(WeekForecast weekForecast) {
        adapter.setWeekForecast(weekForecast);
        WeekForecastDAO.salvaWeekForecast(weekForecast, this);
        adapter.notifyDataSetChanged();
    }

    private void getForecast() {
        WeekForecast forecast = adapter.updateForecast(dailyForecast);

        if (forecast != null) {
            WeekDaily current = forecast.getWeekDailies().get(0);
            setHeader(current, forecast);
        } else
            atualizaForecast();
    }

    private void atualizaForecast() {
        if (Util.isOnline(this))
            getForecastFromRequest();
        else
            Snackbar.make(swipeRefreshLayout, getString(R.string.erro_estabelecer_conexao), Snackbar.LENGTH_LONG).show();
    }

    private void getForecastFromRequest() {
        final Context context = this;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://api.openweathermap.org/data/2.5/forecast/daily?id=" + dailyForecast.getId() + "&cnt=5&appid=37663b8a1a81f6163bfd6d89919e6b51&units=metric&lang=pt";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null) {
                                    try {
                                        WeekForecast weekForecast = JSONParser.parseWeekForecast(response);
                                        cadastraForecast(weekForecast);
                                        setHeader(JSONParser.parseTodayForecast(response), weekForecast);
                                        swipeRefreshLayout.setRefreshing(false);
                                        Snackbar.make(swipeRefreshLayout, context.getString(R.string.informacoes_atualizadas), Snackbar.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        finish();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
                queue.add(stringRequest);
                queue.start();
                return null;
            }
        }.execute();
    }

    private void setHeader(WeekDaily daily, WeekForecast weekForecast) {
        TextView nomeCidade = (TextView) findViewById(R.id.nome_cidade_forecast_header);
        TextView tempoCidade = (TextView) findViewById(R.id.tempo_cidade_forecast_header);
        TextView tempCidade = (TextView) findViewById(R.id.temp_cidade_forecast_header);
        final ImageView imgCidade = (ImageView) findViewById(R.id.img_cidade_forecast_header);

        progressBar.setVisibility(View.INVISIBLE);
        nomeCidade.setText(dailyForecast.getNome());
        tempoCidade.setText(Util.makeFirstLetterUpperCase(dailyForecast.getWeather().getDescription()));
        tempCidade.setText((int) dailyForecast.getMainDaily().getTemp() + " º");

        Bitmap img = WeekForecastDAO.recuperaBitmap(weekForecast, 0, this);
        if (img == null && Util.isOnline(this)) {
            String params[] = new String[]{"http://openweathermap.org/img/w/" + daily.getWeather().getIcon() + ".png"};
            new AsyncTask<String, Void, Bitmap>() {
                protected Bitmap doInBackground(String... urls) {
                    String urldisplay = urls[0];

                    Bitmap mIcon11 = null;
                    try {
                        InputStream in = new java.net.URL(urldisplay).openStream();
                        mIcon11 = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return mIcon11;
                }

                protected void onPostExecute(Bitmap result) {
                    imgCidade.setImageBitmap(result);
                }
            }.execute(params);
        } else {
            imgCidade.setImageBitmap(img);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                atualizaForecast();
            }
        }, 5000);
    }
}
