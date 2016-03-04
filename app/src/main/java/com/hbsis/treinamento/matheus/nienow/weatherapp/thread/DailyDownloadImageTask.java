package com.hbsis.treinamento.matheus.nienow.weatherapp.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.dao.DailyForecastDAO;

import java.io.InputStream;

public class DailyDownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    private int position;
    private Context context;

    public DailyDownloadImageTask(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        this.context = context;
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
        bmImage.setImageBitmap(result);
        DailyForecastDAO.salvaBitmap(result, position, context);
    }
}