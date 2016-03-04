package com.hbsis.treinamento.matheus.nienow.weatherapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbsis.treinamento.matheus.nienow.weatherapp.R;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.dao.WeekForecastDAO;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.WeekDaily;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.WeekForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.thread.WeekDownloadImageAdapter;
import com.hbsis.treinamento.matheus.nienow.weatherapp.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by matheus.nienow on 19/11/2015.
 */
public class WeekForecastAdapter extends BaseAdapter implements Serializable {
    private Context context;
    private WeekForecast weekForecast;
    private ViewHolder viewHolder;
    private static ArrayList<String> fileNames;

    //Construtor
    public WeekForecastAdapter(Context context) {
        this.context = context;

        if (weekForecast == null)
            weekForecast = new WeekForecast();

        if (fileNames == null)
            fileNames = new ArrayList<>();
    }

    //ViewHolder
    static class ViewHolder implements Serializable {
        TextView dia;
        TextView tempMax;
        TextView tempMin;
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
    public void setWeekForecast(WeekForecast weekForecast) {
        this.weekForecast = weekForecast;
    }

    //Override adapter methods
    @Override
    public int getCount() {
        if (weekForecast != null && weekForecast.getWeekDailies() != null)
            return weekForecast.getWeekDailies().size();
        return 0;
    }

    @Override
    public WeekDaily getItem(int position) {
        return weekForecast.getWeekDailies().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.forecast_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.dia = (TextView) convertView.findViewById(R.id.dia_semana_forecast);
            viewHolder.tempMax = (TextView) convertView.findViewById(R.id.temp_max_forecast);
            viewHolder.tempMin = (TextView) convertView.findViewById(R.id.temp_min_forecast);
            viewHolder.tempo = (TextView) convertView.findViewById(R.id.tempo_cidade_forecast);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_cidade_forecast);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        final WeekDaily weekDaily = getItem(position);
        Bitmap img = WeekForecastDAO.recuperaBitmap(weekForecast, position, context);

        if (img != null)
            viewHolder.img.setImageBitmap(img);
        else {
            String url[] = new String[]{"http://openweathermap.org/img/w/" + weekDaily.getWeather().getIcon() + ".png"};
            new WeekDownloadImageAdapter(weekForecast, position, context, viewHolder.img, url).execute();
        }

        Date time = new java.util.Date((long) weekDaily.getDt() * 1000);

        viewHolder.dia.setText(Util.getDiaSemana(time));
        viewHolder.tempMax.setText((int) weekDaily.getTemp().getMax() + " º");
        viewHolder.tempMin.setText((int) weekDaily.getTemp().getMin() + " º");
        viewHolder.tempo.setText(Util.primeiraLetraMaiuscula(weekDaily.getWeather().getDescription()));

        return convertView;
    }

    //WeekForecast adapter methods
    public WeekForecast recuperaWeekForecast(DailyForecast daily) {
        return WeekForecastDAO.recuperaForecast(context, daily);
    }

    public WeekForecast atualizaForecast(DailyForecast daily) {
        weekForecast = recuperaWeekForecast(daily);
        notifyDataSetChanged();

        return weekForecast;
    }
}