package proyectocm.guiame;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Usuario on 15/01/2016.
 */
public class Arista {
    int idPuntoA;
    int idPuntoB;
    LatLng coordenadasA;
    LatLng coordenadasB;
    int costo;

    public Arista(int idPuntoA, int idPuntoB, int costo) {
        this.idPuntoA = idPuntoA;
        this.idPuntoB = idPuntoB;
        this.costo = costo;
    }

    public Arista(int idPuntoB, int idPuntoA,int costo, LatLng coordenadasA, LatLng coordenadasB) {
        this.idPuntoB = idPuntoB;
        this.idPuntoA = idPuntoA;
        this.coordenadasA = coordenadasA;
        this.costo = costo;
        this.coordenadasB = coordenadasB;
    }

    public LatLng getCoordenadasA() {
        return coordenadasA;
    }

    public void setCoordenadasA(LatLng coordenadasA) {
        this.coordenadasA = coordenadasA;
    }

    public LatLng getCoordenadasB() {
        return coordenadasB;
    }

    public void setCoordenadasB(LatLng coordenadasB) {
        this.coordenadasB = coordenadasB;
    }

    public int getIdPuntoB() {
        return idPuntoB;
    }

    public void setIdPuntoB(int idPuntoB) {
        this.idPuntoB = idPuntoB;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public int getIdPuntoA() {
        return idPuntoA;
    }

    public void setIdPuntoA(int idPuntoA) {
        this.idPuntoA = idPuntoA;
    }
}
