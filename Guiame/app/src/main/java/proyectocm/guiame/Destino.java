package proyectocm.guiame;

import java.io.Serializable;

public class Destino implements Serializable {
    String fragment;
    String nombre=null;
    double latitud;
    double longitud;

    public Destino(String fragment, String nombre, double latitud, double longitud) {
        this.fragment = fragment;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Destino( String fragment, double latitud,double longitud) {
        this.latitud = latitud;
        this.fragment = fragment;
        this.longitud = longitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

}
