package com.hbsis.treinamento.matheus.nienow.weatherapp.parser;

import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.DailyForecast;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Clouds;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Coord;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.MainDaily;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.current.Sys;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Weather;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.Wind;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.City;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.Temp;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.WeekDaily;
import com.hbsis.treinamento.matheus.nienow.weatherapp.model.bo.forecast.WeekForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matheus.nienow on 20/11/2015.
 */
public class JSONParser {

    public static DailyForecast parseCurrentWeather(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);

        Clouds clouds = new Clouds();
        JSONObject cloudsObjt = obj.getJSONObject(Clouds.TAG_CLOUDS);
        clouds.setAll(cloudsObjt.getString(Clouds.TAG_ALL));

        Coord coord = new Coord();
        JSONObject coordObj = obj.getJSONObject(Coord.TAG_COORD);
        coord.setLon(coordObj.getDouble(Coord.TAG_LON));
        coord.setLat(coordObj.getDouble(Coord.TAG_LAT));

        MainDaily mainDaily = new MainDaily();
        JSONObject mainObj = obj.getJSONObject(MainDaily.TAG_MAIN);
        mainDaily.setHumidity(mainObj.getDouble(MainDaily.TAG_HUMIDITY));
        mainDaily.setPressure(mainObj.getDouble(MainDaily.TAG_PRESSURE));
        mainDaily.setTempMin(mainObj.getDouble(MainDaily.TAG_TEMP_MIN));
        mainDaily.setTempMax(mainObj.getDouble(MainDaily.TAG_TEMP_MAX));
        mainDaily.setTemp(mainObj.getDouble(MainDaily.TAG_TEMP));

        Sys sys = new Sys();
        JSONObject sysObj = obj.getJSONObject(Sys.TAG_SYS);
        sys.setCountry(sysObj.getString(Sys.TAG_COUNTRY));
        sys.setMessage(sysObj.getString(Sys.TAG_MESSAGE));
        sys.setSunrise(sysObj.getDouble(Sys.TAG_SUNRISE));
        sys.setSunset(sysObj.getDouble(Sys.TAG_SUNSET));

        JSONArray array = obj.getJSONArray(Weather.TAG_WEATHER);
        JSONObject weather = array.getJSONObject(0);
        Weather weatherObj = new Weather();
        weatherObj.setId(weather.getInt(Weather.TAG_ID));
        weatherObj.setMain(weather.getString(Weather.TAG_MAIN));
        weatherObj.setDescription(weather.getString(Weather.TAG_DESCRIPTION));
        weatherObj.setIcon(weather.getString(Weather.TAG_ICON));

        Wind wind = new Wind();
        JSONObject windobObject = obj.getJSONObject(Wind.TAG_WIND);
        wind.setSpeed(windobObject.getDouble(Wind.TAG_SPEED));

        try {
            wind.setDeg(windobObject.getDouble(Wind.TAG_DEG));
        } catch(JSONException e){
            e.printStackTrace();
        }


        DailyForecast dailyForecast = new DailyForecast(obj.getInt(DailyForecast.TAG_ID), obj.getString(DailyForecast.TAG_NAME), obj.getInt(DailyForecast.TAG_COD),
                obj.getInt(DailyForecast.TAG_DT), clouds, coord, mainDaily, sys, weatherObj, wind);
        return dailyForecast;
    }

    public static WeekForecast parseWeekForecast(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);

        City city = new City();
        JSONObject cityObj = obj.getJSONObject(City.TAG_CITY);
        JSONObject coordObj = cityObj.getJSONObject(City.TAG_COORD);
        city.setCoord(new Coord(coordObj.getDouble(Coord.TAG_LAT), coordObj.getDouble(Coord.TAG_LON)));
        city.setCountry(cityObj.getString(City.TAG_COUNTRY));
        city.setId(cityObj.getInt(City.TAG_ID));
        city.setNome(cityObj.getString(City.TAG_NAME));

        double code = obj.getDouble(WeekForecast.TAG_CODE);
        String message = obj.getString(WeekForecast.TAG_MESSAGE);
        int cnt = obj.getInt(WeekForecast.TAG_CNT);

        JSONArray array = obj.getJSONArray(WeekForecast.TAG_LIST);
        ArrayList<WeekDaily> weekDailies = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject objArray = array.getJSONObject(i);
            JSONArray arrayWeather = objArray.getJSONArray(Weather.TAG_WEATHER);
            JSONObject objWeather = arrayWeather.getJSONObject(0);

            WeekDaily weekDaily = new WeekDaily();
            weekDaily.setDt(objArray.getInt(WeekDaily.TAG_DT));

            JSONObject objTemp = objArray.getJSONObject(Temp.TAG_TEMP);
            weekDaily.setTemp(new Temp(objTemp.getDouble(Temp.TAG_DAY), objTemp.getDouble(Temp.TAG_MIN), objTemp.getDouble(Temp.TAG_MAX), objTemp.getDouble(Temp.TAG_NIGHT), objTemp.getDouble(Temp.TAG_EVE),
                    objTemp.getDouble(Temp.TAG_MORN)));
            weekDaily.setPressure(objArray.getDouble(WeekDaily.TAG_PRESSURE));
            weekDaily.setHumidity(objArray.getDouble(WeekDaily.TAG_HUMIDITY));
            weekDaily.setWeather(new Weather(objWeather.getInt(Weather.TAG_ID), objWeather.getString(Weather.TAG_MAIN), objWeather.getString(Weather.TAG_DESCRIPTION), objWeather.getString(Weather.TAG_ICON)));
            weekDaily.setSpeed(objArray.getDouble(WeekDaily.TAG_SPEED));
            weekDaily.setDeg(objArray.getDouble(WeekDaily.TAG_DEG));
            weekDaily.setClouds(objArray.getDouble(WeekDaily.TAG_CLOUDS));
            weekDailies.add(weekDaily);
        }

        WeekForecast weekForecast = new WeekForecast(code, message, city, cnt, weekDailies);
        return weekForecast;
    }

    public static WeekDaily parseTodayForecast(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        JSONArray array = obj.getJSONArray(WeekForecast.TAG_LIST);

        WeekDaily weekDaily = new WeekDaily();
        JSONObject objArray = array.getJSONObject(0);
        JSONArray arrayWeather = objArray.getJSONArray(Weather.TAG_WEATHER);
        JSONObject objWeather = arrayWeather.getJSONObject(0);

        weekDaily.setDt(objArray.getInt(WeekDaily.TAG_DT));

        JSONObject objTemp = objArray.getJSONObject(WeekDaily.TAG_TEMP);
        weekDaily.setTemp(new Temp(objTemp.getDouble(Temp.TAG_DAY), objTemp.getDouble(Temp.TAG_MIN), objTemp.getDouble(Temp.TAG_MAX), objTemp.getDouble(Temp.TAG_NIGHT), objTemp.getDouble(Temp.TAG_EVE),
                objTemp.getDouble(Temp.TAG_MORN)));
        weekDaily.setPressure(objArray.getDouble(WeekDaily.TAG_PRESSURE));
        weekDaily.setHumidity(objArray.getDouble(WeekDaily.TAG_HUMIDITY));
        weekDaily.setWeather(new Weather(objWeather.getInt(Weather.TAG_ID), objWeather.getString(Weather.TAG_MAIN), objWeather.getString(Weather.TAG_DESCRIPTION), objWeather.getString(Weather.TAG_ICON)));
        weekDaily.setSpeed(objArray.getDouble(WeekDaily.TAG_SPEED));
        weekDaily.setDeg(objArray.getDouble(WeekDaily.TAG_DEG));
        weekDaily.setClouds(objArray.getDouble(WeekDaily.TAG_CLOUDS));
        return weekDaily;
    }

    public static DailyForecast getCidade(String json) throws JSONException {
        return JSONParser.parseCurrentWeather(json);
    }
}
