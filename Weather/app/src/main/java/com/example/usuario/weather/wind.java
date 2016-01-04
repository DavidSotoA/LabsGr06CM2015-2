package com.example.usuario.weather;

/**
 * Created by Usuario on 23/11/2015.
 */
public class wind {

    double speed;
    double deg;

    public wind(double speed, double deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }
}
