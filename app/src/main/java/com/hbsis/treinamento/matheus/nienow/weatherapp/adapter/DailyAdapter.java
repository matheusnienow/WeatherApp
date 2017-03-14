package com.hbsis.treinamento.matheus.nienow.weatherapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbsis.treinamento.matheus.nienow.weatherapp.R;
import com.hbsis.treinamento.matheus.nienow.weatherapp.activity.ForecastActivity;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.dao.DailyForecastDAO;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.parser.JSONParser;
import com.hbsis.treinamento.matheus.nienow.weatherapp.thread.DailyDownloadImageTask;
import com.hbsis.treinamento.matheus.nienow.weatherapp.util.Util;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class DailyAdapter extends BaseAdapter implements Serializable {
    public static final String TAG_ERRO = "TAG_ERRO";
    public static final String TAG_DAILY_FORECAST = "dailyForecast";
    private Context context;
    private ArrayList<DailyForecast> dailyForecasts;
    private ViewHolder viewHolder;
    private static ArrayList<String> fileNames;
    private int contRequest;
    private int contRequestErrors;
    private int ordem;
    private View snackBarView;

    //Construtor
    public DailyAdapter(Context context, View snackBarView) {
        this.context = context;

        if (dailyForecasts == null)
            dailyForecasts = new ArrayList<>();

        if (fileNames == null)
            fileNames = new ArrayList<>();
        this.snackBarView = snackBarView;

        contRequest = 0;
        contRequestErrors = 0;
        ordem = 0;
    }

    //ViewHoler
    private static class ViewHolder implements Serializable {
        TextView nome;
        TextView tempMin;
        TextView tempMax;
        TextView tempo;
        ImageView img;
    }

    //Getter's e setter's
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<DailyForecast> getDailyForecasts() {
        return dailyForecasts;
    }

    public void setDailyForecasts(ArrayList<DailyForecast> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    //Override adapter methods
    @Override
    public int getCount() {
        return dailyForecasts.size();
    }

    @Override
    public DailyForecast getItem(int position) {
        return dailyForecasts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.daily_list_item, null);

            viewHolder = new ViewHolder();

            viewHolder.nome = (TextView) convertView.findViewById(R.id.nome_cidade);
            viewHolder.tempMin = (TextView) convertView.findViewById(R.id.temp_min_cidade);
            viewHolder.tempMax = (TextView) convertView.findViewById(R.id.temp_max_cidade);
            viewHolder.tempo = (TextView) convertView.findViewById(R.id.tempo_cidade);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_cidade);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        final DailyForecast dailyForecast;

        try {
            dailyForecast = getItem(position);
        } catch (Exception e) {
            viewHolder.img.setImageBitmap(null);
            viewHolder.tempo.setText("");
            viewHolder.nome.setText("");
            viewHolder.tempMin.setText("");
            viewHolder.tempMax.setText("");
            convertView.setEnabled(false);
            convertView.setClickable(false);
            return convertView;
        }

        Bitmap img = null;
        if (dailyForecast != null)
            img = dailyForecast.getImg();

        if (img != null)
            viewHolder.img.setImageBitmap(img);
        else {
            String[] url = new String[]{"http://openweathermap.org/img/w/" + dailyForecast.getWeather().getIcon() + ".png", String.valueOf(position)};
            new DailyDownloadImageTask(viewHolder.img, context).execute(url);
        }

        viewHolder.nome.setText(dailyForecast.getNome() + ", " + dailyForecast.getSys().getCountry());
        viewHolder.tempMin.setText((int) dailyForecast.getMainDaily().getTempMin() + " ºC");
        viewHolder.tempMax.setText((int) dailyForecast.getMainDaily().getTempMax() + " ºC");

        String description = Util.makeFirstLetterUpperCase(dailyForecast.getWeather().getDescription());
        viewHolder.tempo.setText(description);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ForecastActivity.class);
                DailyForecast daily = dailyForecast;
                BitmapDrawable bmDrawable = (BitmapDrawable) viewHolder.img.getDrawable();
                if (bmDrawable != null)
                    daily.setImg(bmDrawable.getBitmap());

                i.putExtra(TAG_DAILY_FORECAST, daily);
                //i.putExtra(TAG_BITMAP_IMAGE, ((BitmapDrawable) viewHolder.img.getDrawable()).getBitmap());
                context.startActivity(i);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!((Activity) context).isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setItems(new String[]{context.getString(R.string.deletar)}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteDaily(position, null);
                            recuperaForecasts();
                        }
                    }).show();
                }

                return true;
            }
        });

        return convertView;
    }

    //Daily adapter methods
    public void addDailyForecast(DailyForecast dailyForecast, ArrayList<DailyForecast> dailyForecastsCopia) {
        if (dailyForecast != null && dailyForecasts != null) {
            if (dailyForecastsCopia == null) {
                dailyForecasts.add(dailyForecast);
                DailyForecastDAO.salvaDailyForecast(dailyForecast, context);
                DailyForecastDAO.salvaFileNames(dailyForecasts, context);
            } else
                dailyForecastsCopia.add(dailyForecast);
        }
    }

    public void recuperaForecasts() {
        dailyForecasts = DailyForecastDAO.recuperaForecasts(context);
        notifyDataSetChanged();
    }

    public void atualizaForecasts(final SwipeRefreshLayout swipe) {
        contRequest = 0;
        contRequestErrors = 0;
        final ArrayList<DailyForecast> dailyForecastsCopia = DailyForecastDAO.recuperaForecasts(context);

        if (!dailyForecastsCopia.isEmpty()) {
            final RequestQueue queue = Volley.newRequestQueue(context);

            for (int i = 0; i < dailyForecastsCopia.size(); i++) {
                DailyForecast daily = dailyForecastsCopia.get(i);
                deleteDaily(daily, dailyForecastsCopia);
                --i;
                queue.add(getDailyForecastRequest(daily, dailyForecastsCopia));
                contRequest++;
            }

            queue.start();
            queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
                @Override
                public void onRequestFinished(Request<String> request) {
                    contRequest--;
                    if (contRequest == 0 && contRequestErrors == 0) {
                        setDailyForecasts(dailyForecastsCopia);
                        DailyForecastDAO.deletaFiles(dailyForecasts, context);
                        DailyForecastDAO.salvaFileNames(dailyForecasts, context);
                        DailyForecastDAO.salvaForecasts(dailyForecasts, context);
                        recuperaForecasts();
                        sortCities(ordem);
                        notifyDataSetChanged();

                        if (swipe != null)
                            swipe.setRefreshing(false);

                        Snackbar.make(snackBarView, context.getString(R.string.informacoes_atualizadas), Snackbar.LENGTH_SHORT).show();
                    } else if (contRequestErrors > 0) {
                        Snackbar.make(snackBarView, context.getString(R.string.erro_atualizacao_tente_novamente), Snackbar.LENGTH_LONG).show();
                        if (swipe != null) {
                            swipe.setRefreshing(false);
                        }
                    }
                }
            });
        } else {
            Snackbar.make(snackBarView, "Não há informações para atualizar", Snackbar.LENGTH_LONG).show();
            if (swipe != null)
                swipe.setRefreshing(false);
        }
    }

    private StringRequest getDailyForecastRequest(DailyForecast daily, final ArrayList<DailyForecast> dailyForecastsCopia) {
        String idCidade = String.valueOf(daily.getId());
        String url = "http://api.openweathermap.org/data/2.5/weather?id=" + idCidade + "&appid=37663b8a1a81f6163bfd6d89919e6b51&units=metric&lang=pt";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if (response != null) {
                            try {
                                if (dailyForecastsCopia == null)
                                    addDailyForecast(JSONParser.getCidade(response), getDailyForecasts());
                                else
                                    addDailyForecast(JSONParser.getCidade(response), dailyForecastsCopia);

                                notifyDataSetChanged();
                            } catch (JSONException e) {
                                contRequestErrors++;
                                e.printStackTrace();
                                Log.e(TAG_ERRO, e.getMessage());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                contRequestErrors++;
                error.printStackTrace();
                Snackbar.make(snackBarView, context.getString(R.string.erro_demora_resposta), Snackbar.LENGTH_LONG).show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        return stringRequest;
    }

    private void deleteDaily(int position, ArrayList<DailyForecast> dailyForecastsCopia) {
        if (dailyForecastsCopia != null)
            dailyForecastsCopia.remove(position);
        else {
            DailyForecastDAO.deletaFile(dailyForecasts.get(position), context);
            dailyForecasts.remove(position);
            DailyForecastDAO.salvaFileNames(dailyForecasts, context);
        }
    }

    private void deleteDaily(DailyForecast daily, ArrayList<DailyForecast> dailyForecastsCopia) {
        if (dailyForecastsCopia != null)
            dailyForecastsCopia.remove(daily);
        else {
            DailyForecastDAO.deletaFile(daily, context);
            dailyForecasts.remove(daily);
            DailyForecastDAO.salvaFileNames(dailyForecasts, context);
        }
    }

    public void sortCities(int i) {
        ordem = i;

        if (i == 0) {
            Collections.sort(dailyForecasts, new Comparator<DailyForecast>() {

                @Override
                public int compare(DailyForecast lhs, DailyForecast rhs) {
                    return (lhs).getNome().compareTo(rhs.getNome());
                }
            });
        } else if (i == 1) {
            Collections.sort(dailyForecasts, new Comparator<DailyForecast>() {

                @Override
                public int compare(DailyForecast lhs, DailyForecast rhs) {
                    return (int) ((rhs).getMainDaily().getTempMax() - lhs.getMainDaily().getTempMax());
                }
            });
        } else if (i == 2) {
            Collections.sort(dailyForecasts, new Comparator<DailyForecast>() {

                @Override
                public int compare(DailyForecast lhs, DailyForecast rhs) {
                    return (int) ((lhs).getMainDaily().getTempMin() - rhs.getMainDaily().getTempMin());
                }
            });
        }

        DailyForecastDAO.salvaFileNames(dailyForecasts, context);
        recuperaForecasts();
    }
}