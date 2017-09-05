package com.hbsis.treinamento.matheus.nienow.weatherapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class Util {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String makeFirstLetterUpperCase(String text){
        return (text == null ? null : text.substring(0,1).toUpperCase()+text.substring(1));
    }

    public static String getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == 1)
            return "Domingo";
        else if (dayOfWeek == 2)
            return "Segunda-feira";
        else if (dayOfWeek == 3)
            return "Terça-feira";
        else if (dayOfWeek == 4)
            return "Quarta-feira";
        else if (dayOfWeek == 5)
            return "Quinta-feira";
        else if (dayOfWeek == 6)
            return "Sexta-feira";
        else if (dayOfWeek == 7)
            return "Sábado";

        return null;
    }

    public static  boolean deleteDirectoryOrFile(File path) {
        if (path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory())
                    deleteDirectoryOrFile(files[i]);
                else
                    files[i].delete();
            }
        }

        return (path.delete());
    }
}
