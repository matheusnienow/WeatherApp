package com.hbsis.treinamento.matheus.nienow.weatherapp.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.dao.WeekForecastDAO;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.WeekForecast;

import java.io.InputStream;

/**
 * Created by matheus.nienow on 27/11/2015.
 */
public class WeekDownloadImageAdapter {
    private WeekForecast weekForecast;
    private int position;
    private String[] params;
    private Context context;
    private ImageView img;

    public WeekDownloadImageAdapter(WeekForecast weekForecast, int position, Context context, ImageView img, String[] params) {
        this.weekForecast = weekForecast;
        this.position = position;
        this.params = params;
        this.context = context;
        this.img = img;
    }

    public void execute(){
        new DownloadImageTask(img, context).execute(params);
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView bmImage;
        private Context context;

        public DownloadImageTask(ImageView bmImage, Context context) {
            this.bmImage = bmImage;
            this.context = context;
        }

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
            bmImage.setImageBitmap(result);
            WeekForecastDAO.salvaBitmap(result, weekForecast, position, context);
            WeekForecastDAO.salvaWeekForecast(weekForecast, context);
        }
    }
}
