package com.hbsis.treinamento.matheus.nienow.weatherapp.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.dao.DailyForecastDAO;

import java.io.InputStream;

public class DailyDownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ProgressBar progressBar;
    private ImageView bmImage;
    private int position;
    private Context context;

    public DailyDownloadImageTask(ImageView bmImage, ProgressBar progressBar, Context context) {
        this.bmImage = bmImage;
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        bmImage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        position = Integer.parseInt(urls[1]);

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
        progressBar.setVisibility(View.GONE);
        bmImage.setVisibility(View.VISIBLE);
        bmImage.setImageBitmap(result);
        DailyForecastDAO.salvaBitmap(result, position, context);
    }
}