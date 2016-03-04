package com.hbsis.treinamento.matheus.nienow.weatherapp.model.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.WeekForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by matheus.nienow on 24/11/2015.
 */
public class WeekForecastDAO {
    public static final String FORECAST_FOLDER_NAMES = "forecastFolderNames";
    public static final String TAG_ERRO = "TAG_ERRO";
    private static ArrayList<String> folderNames = new ArrayList<>();

    public static void salvaWeekForecast(WeekForecast weekForecast, Context context) {
        String folderName = String.valueOf(weekForecast.getCity().getId());
        String filename = String.valueOf(weekForecast.getCity().getId());
        folderNames.add(folderName);

        try {
            File file = new File(context.getFilesDir() + "/" + folderName + "/", filename);
            file.getParentFile().mkdirs();

            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(weekForecast);

            gravaFolderNames(context);
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            Log.e(TAG_ERRO, e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deletaFiles(DailyForecast daily, Context context) {
        String dailyFileName = String.valueOf(DailyForecastDAO.PREFIXO_NOME_ARQUIVO + daily.getId());
        File file = new File(context.getFilesDir(), dailyFileName);

        if (file.exists())
            if (!Util.deleteDirectoryOrFile(file))
                Log.e(TAG_ERRO, "Arquivo " + dailyFileName + " não foi deletado!");
    }

    public static WeekForecast recuperaForecast(Context context, DailyForecast daily) {
        WeekForecast weekForecast = null;
        recuperaFolderNames(context);

        //recupera forecast
        try {
            String fileName = String.valueOf(daily.getId());
            File file = new File(context.getFilesDir() + "/" + daily.getId() + "/", fileName);
            if (file.exists()) {
                FileInputStream inputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                weekForecast = (WeekForecast) objectInputStream.readObject();

                objectInputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            e.printStackTrace();
        }

        return weekForecast;
    }

    private static void gravaFolderNames(Context context) {
        try {
            FileOutputStream outputStream = context.openFileOutput(FORECAST_FOLDER_NAMES, Context.MODE_PRIVATE);//new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(folderNames);

            outputStream.close();
            objectOutputStream.close();
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            e.printStackTrace();
        }
    }

    private static void recuperaFolderNames(Context context) {
        FileInputStream inputStream;
        ObjectInputStream objectInputStream;
        File file = new File(context.getFilesDir(), FORECAST_FOLDER_NAMES);

        //recupera lista com nome dos arquivos
        if (file.exists()) {
            try {
                inputStream = context.openFileInput(FORECAST_FOLDER_NAMES);
                objectInputStream = new ObjectInputStream(inputStream);
                folderNames = (ArrayList<String>) objectInputStream.readObject();
                objectInputStream.close();
                inputStream.close();
            } catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static Bitmap recuperaBitmap(WeekForecast weekForecast, int position, Context context) {
        Bitmap img = null;
        try {
            File file = new File(context.getFilesDir() + "/" + weekForecast.getCity().getId() + "/", DailyForecastDAO.SUFIXO_NOME_ARQUIVO_IMG + position);
            img = BitmapFactory.decodeFile(file.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            e.printStackTrace();
        }
        return img;
    }

    public static void salvaBitmap(Bitmap bitmap, WeekForecast weekForecast, int position, Context context) {
        if (bitmap != null) {
            try {
                String filename = String.valueOf(DailyForecastDAO.SUFIXO_NOME_ARQUIVO_IMG + position);
                File file = new File(context.getFilesDir() + "/" + weekForecast.getCity().getId() + "/", filename);
                file.getParentFile().mkdirs();

                FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                Log.e(TAG_ERRO, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
