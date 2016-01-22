package proyectocm.guiame;

/**
 * Created by Usuario on 17/01/2016.
 */
public class Punto {
    int idPunto;
    double latitud;
    double longitud;

    public Punto(int idPunto, double latitud, double longitud) {
        this.idPunto = idPunto;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdPunto() {
        return idPunto;
    }

    public void setIdPunto(int idPunto) {
        this.idPunto = idPunto;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
