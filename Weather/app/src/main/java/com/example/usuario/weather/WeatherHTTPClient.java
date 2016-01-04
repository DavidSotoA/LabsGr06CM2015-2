package com.example.usuario.weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Usuario on 22/11/2015.
 */
public class WeatherHTTPClient{
    private static String BASE_URL_TODAY="http://api.openweathermap.org/data/2.5/weather?APPID=";
    private static String BASE_URL_FORECAST="http://api.openweathermap.org/data/2.5/forecast/daily?APPID=";
    private static String OPEN_WEATHER_MAP_API_KEY="641e20404ab63afef87f517905d3c8d9";


    public String getWeatherDataToday(String location,boolean today){
        HttpURLConnection conn=null;
        InputStream is=null;
        URL url=null;
        try{
            if(today)
                url=new URL(BASE_URL_TODAY+OPEN_WEATHER_MAP_API_KEY+"&q="+location);
            else
                url=new URL(BASE_URL_FORECAST+OPEN_WEATHER_MAP_API_KEY+"&q="+location);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        try{
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            StringBuffer buffer=new StringBuffer();
            is=conn.getInputStream();
            BufferedReader br= new BufferedReader(new InputStreamReader(is));
            String line=null;
            while((line=br.readLine())!=null){
                buffer.append(line+"\r\n");
            }
            is.close();
            conn.disconnect();
            return buffer.toString();
        }catch(Throwable t){
            t.printStackTrace();
        }
        finally {
            try {
                is.close();}
            catch (Throwable t){}
            try{
                conn.disconnect();
                }catch (Throwable t){}

            }
        return null;
        }
    }

