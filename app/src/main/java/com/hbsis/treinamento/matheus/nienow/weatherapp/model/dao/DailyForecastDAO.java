package com.hbsis.treinamento.matheus.nienow.weatherapp.model.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;

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
public class DailyForecastDAO {
    private static final String TAG_ERRO = "TAG_ERRO";
    public static final String PREFIXO_NOME_ARQUIVO = "daily";
    public static final String SUFIXO_NOME_ARQUIVO_IMG = "img";
    public static final String FILE_NAMES = "fileNames";
    private static ArrayList<String> fileNames = new ArrayList<>();

    public static void salvaForecasts(ArrayList<DailyForecast> array, Context context) {
        for (DailyForecast dailyForecast : array)
            salvaDailyForecast(dailyForecast, context);

        salvaFileNames(array, context);
    }

    public static void salvaDailyForecast(DailyForecast dailyForecast, Context context) {
        String filename = String.valueOf(PREFIXO_NOME_ARQUIVO + dailyForecast.getId());

        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(dailyForecast);
        } catch (IOException e) {
            Log.e(TAG_ERRO, e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deletaFiles(ArrayList<DailyForecast> dailyForecasts, Context context) {
        for (String fileName : fileNames) {
            boolean existe = false;
            File file = null;

            for (DailyForecast daily : dailyForecasts) {
                String dailyFileName = String.valueOf(PREFIXO_NOME_ARQUIVO + daily.getId());
                file = new File(context.getFilesDir(), dailyFileName);

                if (file.exists() && fileName.contains(dailyFileName)) {
                    existe = true;
                    break;
                } else
                    WeekForecastDAO.deletaFiles(daily, context);
            }

            if (!existe && file != null) {
                File imgFile = new File(context.getFilesDir(), file.getAbsolutePath() + SUFIXO_NOME_ARQUIVO_IMG);
                file.delete();

                if (imgFile.exists())
                    imgFile.delete();
            }
        }
    }

    public static void deletaFile(DailyForecast dailyForecast, Context context) {
        for (String fileName : fileNames) {
            boolean existe = false;
            String dailyFileName = String.valueOf(PREFIXO_NOME_ARQUIVO + dailyForecast.getId());
            File file = new File(context.getFilesDir(), dailyFileName);

            if (file.exists() && fileName.contains(dailyFileName))
                existe = true;
            else
                WeekForecastDAO.deletaFiles(dailyForecast, context);

            if (!existe && file != null) {
                File imgFile = new File(context.getFilesDir(), file.getAbsolutePath() + SUFIXO_NOME_ARQUIVO_IMG);
                file.delete();

                if (imgFile.exists())
                    imgFile.delete();
            }
        }
    }

    public static void salvaFileNames(ArrayList<DailyForecast> dailyForecasts, Context context) {
        fileNames = new ArrayList<>();

        //Salva o nome dos itens do arry dailyForecasts
        for (DailyForecast daily : dailyForecasts) {
            String filename = String.valueOf(PREFIXO_NOME_ARQUIVO + daily.getId());
            File file = new File(context.getFilesDir(), filename);
            fileNames.add(file.getAbsolutePath());
        }

        //Salva o arquivo contentdo o nome dos arquivos
        try {
            FileOutputStream outputStream = context.openFileOutput(FILE_NAMES, Context.MODE_PRIVATE);//new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(fileNames);
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            Log.e(TAG_ERRO, e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<DailyForecast> recuperaForecasts(Context context) {
        ArrayList<DailyForecast> dailyForecasts = new ArrayList<>();

        FileInputStream inputStream;
        ObjectInputStream objectInputStream;
        File file = new File(context.getFilesDir(), FILE_NAMES);

        //Recupera o nome dos arquivos
        if (file.exists()) {
            try {
                inputStream = context.openFileInput(FILE_NAMES);
                objectInputStream = new ObjectInputStream(inputStream);
                fileNames = (ArrayList<String>) objectInputStream.readObject();
                objectInputStream.close();
                inputStream.close();
            } catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
                e.printStackTrace();
            }
        }

        //Utiliza o nome dos arquivos para recuperar os forecasts
        for (int i = 0; i < fileNames.size(); i++) {
            try {
                String fileName = fileNames.get(i);
                inputStream = new FileInputStream(fileName);
                objectInputStream = new ObjectInputStream(inputStream);

                DailyForecast daily = (DailyForecast) objectInputStream.readObject();
                daily.setImg(recuperaBitmap(i, fileNames));

                dailyForecasts.add(daily);
                objectInputStream.close();
                inputStream.close();
            } catch (Exception e) {
                Log.e(TAG_ERRO, e.getMessage());
                e.printStackTrace();
            }
        }

        return dailyForecasts;
    }

    public static void salvaBitmap(Bitmap bitmap, int position, Context context) {
        if (bitmap != null) {
            try {
                String filename = String.valueOf(fileNames.get(position) + SUFIXO_NOME_ARQUIVO_IMG);
                FileOutputStream outputStream = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);

                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                Log.e(TAG_ERRO, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static Bitmap recuperaBitmap(int position, ArrayList<String> fileNames) {
        Bitmap img = null;

        try {
            File file = new File(fileNames.get(position) + SUFIXO_NOME_ARQUIVO_IMG);
            if (file.exists())
                img = BitmapFactory.decodeFile(fileNames.get(position) + SUFIXO_NOME_ARQUIVO_IMG);
        } catch (Exception e) {
            Log.e(TAG_ERRO, e.getMessage());
            e.printStackTrace();
        }
        return img;
    }
}
