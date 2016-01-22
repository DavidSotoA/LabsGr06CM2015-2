package proyectocm.guiame;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Usuario on 15/01/2016.
 */
public class Recta {


    public static double distanciaEntreDosPuntos(double lat1, double lon1, double lat2, double lon2){
        double R = 6378.137;
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000;
    }

    public static double pendienteDeLaRecta(double lat1, double lon1, double lat2, double lon2){
        double m=(lon2-lon1)/(lat2-lat1);
        return m;
    }

    public static double constanteDeLaRecta(double lat1, double lon1, double m){
        double b=lon1-(m*lat1);
        return b;
    }


    public static  boolean contains(LatLng[] points,LatLng test){
        double b;
        double m;
        double xi,yi,x1,x2,y1,y2;
        int cont=0;
        for(int i=0;i<points.length-1;i++){
            System.out.println("contains-i="+i);
            x1=points[i].latitude;
            y1=points[i].longitude;
            x2=points[i + 1].latitude;
            y2=points[i + 1].longitude;
            if(x2<x1){
                x1=points[i + 1].latitude;
                x2=points[i].latitude;
            }
            if(y2<y1){
                y2=points[i].longitude;
                y1=points[i + 1].longitude;
            }
            if((x2-x1)!=0) {
                m = Recta.pendienteDeLaRecta(x1, y1, x2, y2);
                b = Recta.constanteDeLaRecta(x1, y1, m);
                yi = test.longitude;
                xi = (yi-b)/m;
                if (xi > test.latitude) {
                    if (x1 < xi && xi < x2) {
                        if (y1 < xi && yi < y2) {
                            cont++;
                        }
                    }
                }
            }
        }
        if (cont%2==0)
            return false;
        return true;
    }


    public static boolean esPuntoValido(double lat1 ,double lat2, double lon1, double lon2, LatLng punto){
        double x1,x2,y1,y2;
        x1=lat1;
        y1=lon1;
        x2=lat2;
        y2=lon2;
        if(x2<x1){
            x1=lat2;
            x2=lat1;
        }
        if(y2<y1){
            y2=lon2;
            y1=lon1;
        }
        if (x1 <= punto.latitude && punto.latitude <= x2) {
          //  if (y1 < punto.longitude && punto.longitude < y2) {
                return true;
            }
        //}
        return false;
    }


    public static double[] puntosDeLaRecta(double m, double b, double long1, double aumento, int numDePuntos){
        double[] puntos=new double[numDePuntos];
        for(int i=0;i<numDePuntos;i++){
            puntos[i]=(m*long1)+b;
            long1=long1+aumento;
        }
        return puntos;
    }

    public static double pendienteDeRectaPerpendicular(double m){
        double mPerpendicuar=-(1/m);
        return  mPerpendicuar;
    }

    public static LatLng interseccionEntreDosRectas(double m1, double m2, double b1, double b2){
        double x=(b2-b1)/(m1-m2);
        double y=(m1*x)+b1;
        return new LatLng(x,y);
    }

    public static double angleFromCoordinate(double x1, double y1, double x2, double y2) {
        double m=(y2-y1)/(x2-x1);
        return Math.atan(m);
    }
}
