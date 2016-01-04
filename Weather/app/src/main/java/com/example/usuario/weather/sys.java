package com.example.usuario.weather;

/**
 * Created by Usuario on 23/11/2015.
 */
public class sys {
    double message;
    String country;
    double sunrise;
    double sunset;

    public sys(String country, double sunrise, double message, double sunset) {
        this.country = country;
        this.sunrise = sunrise;
        this.message = message;
        this.sunset = sunset;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getSunrise() {
        return sunrise;
    }

    public void setSunrise(double sunrise) {
        this.sunrise = sunrise;
    }

    public double getSunset() {
        return sunset;
    }

    public void setSunset(double sunset) {
        this.sunset = sunset;
    }
}
