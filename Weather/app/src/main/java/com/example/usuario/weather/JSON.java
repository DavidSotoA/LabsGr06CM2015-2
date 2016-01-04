package com.example.usuario.weather;

import java.util.ArrayList;

/**
 * Created by Usuario on 23/11/2015.
 */
public class JSON {
    coord coord;
    ArrayList<weather> weather;
    String base;
    main main;
    wind wind;
    clouds clouds;
    String dt;
    sys sys;
    int id;
    String name;
    int cod;

    public JSON(com.example.usuario.weather.coord coord, ArrayList<com.example.usuario.weather.weather> weather, String base, com.example.usuario.weather.wind wind, com.example.usuario.weather.clouds clouds, String dt, String name, int cod, int id, com.example.usuario.weather.sys sys, com.example.usuario.weather.main main) {
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.wind = wind;
        this.clouds = clouds;
        this.dt = dt;
        this.name = name;
        this.cod = cod;
        this.id = id;
        this.sys = sys;
        this.main = main;
    }

    public com.example.usuario.weather.coord getCoord() {
        return coord;
    }

    public void setCoord(com.example.usuario.weather.coord coord) {
        this.coord = coord;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public ArrayList<com.example.usuario.weather.weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<com.example.usuario.weather.weather> weather) {
        this.weather = weather;
    }

    public com.example.usuario.weather.main getMain() {
        return main;
    }

    public void setMain(com.example.usuario.weather.main main) {
        this.main = main;
    }

    public com.example.usuario.weather.wind getWind() {
        return wind;
    }

    public void setWind(com.example.usuario.weather.wind wind) {
        this.wind = wind;
    }

    public com.example.usuario.weather.sys getSys() {
        return sys;
    }

    public void setSys(com.example.usuario.weather.sys sys) {
        this.sys = sys;
    }

    public com.example.usuario.weather.clouds getClouds() {
        return clouds;
    }

    public void setClouds(com.example.usuario.weather.clouds clouds) {
        this.clouds = clouds;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}
