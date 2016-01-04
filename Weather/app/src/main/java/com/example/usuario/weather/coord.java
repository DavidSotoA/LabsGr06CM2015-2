package com.example.usuario.weather;

/**
 * Created by Usuario on 23/11/2015.
 */
public class coord {

    double lon;
    double lat;

    public coord(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
