package proyectocm.guiame;

/**
 * Created by Usuario on 11/01/2016.
 */
public class PuntosMasCercanos {

    int id;
    double distancia;

    public PuntosMasCercanos(int id, double distancia) {
        this.id = id;
        this.distancia = distancia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
