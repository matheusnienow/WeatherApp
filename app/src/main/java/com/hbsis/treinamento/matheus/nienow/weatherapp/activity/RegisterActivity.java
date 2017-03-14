package com.hbsis.treinamento.matheus.nienow.weatherapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbsis.treinamento.matheus.nienow.weatherapp.R;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.parser.JSONParser;
import com.hbsis.treinamento.matheus.nienow.weatherapp.util.Util;

import org.json.JSONException;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG_RESULT = "result";
    public static final String ERROR_NOT_FOUND_CITY = "Error: Not found city";
    private EditText etCity;
    private ProgressDialog progress;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        layout = (RelativeLayout) findViewById(R.id.content_cadastro);
        etCity = (EditText) findViewById(R.id.et_cidade);
        findViewById(R.id.bt_cadastrar).setOnClickListener(this);

        TextView help = (TextView) findViewById(R.id.descricao_help);
        help.setText(Html.fromHtml(getString(R.string.guia_cadastro_1)+" <a href=\"https://www.iso.org/obp/ui/#search\" >ISO 3166</a>"+getString(R.string.guia_cadastro_2)));
        help.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void hideKeyboard(){
        if (this.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        register();
    }

    private void register() {
        if (!Util.isOnline(this)) {
            Snackbar.make(layout, getString(R.string.erro_estabelecer_conexao), Snackbar.LENGTH_LONG).show();
            return;
        }

        if (etCity.getText() != null && !etCity.getText().toString().equals("")) {
            progress = ProgressDialog.show(this, getString(R.string.cadastrando_cidade),
                    getString(R.string.buscando_informacoes), true);
            searchCity();
        } else
            etCity.setError(getString(R.string.erro_campo_vazio));
    }

    private void searchCity() {
        String nomeCidade = etCity.getText().toString();

        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + nomeCidade.trim().replace(" ", "") + "&appid=37663b8a1a81f6163bfd6d89919e6b51&units=metric&lang=pt";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                if (!response.contains(ERROR_NOT_FOUND_CITY))
                                    registerCity(JSONParser.getCidade(response));
                                else {
                                    Snackbar.make(layout, getString(R.string.cidade_nao_encontrada), Snackbar.LENGTH_SHORT).show();
                                    progress.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progress.dismiss();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Snackbar.make(layout, getString(R.string.nao_foi_possivel_conectar_servidor), Snackbar.LENGTH_LONG).show();
                progress.dismiss();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        queue.add(stringRequest);
        queue.start();
    }

    private void registerCity(DailyForecast dailyForecast) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(TAG_RESULT, dailyForecast);
        setResult(Activity.RESULT_OK, returnIntent);
        progress.dismiss();
        finish();
    }

}
