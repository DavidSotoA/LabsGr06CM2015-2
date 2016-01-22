package proyectocm.guiame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class GpsRuta extends Fragment implements GoogleMap.OnMyLocationChangeListener{

    private static GoogleMap googleMap;
    MapView mapView;
    Button button_udea;
    Button button_destino;
    Button button_cancelar;
    TextView lugar;
    LocationManager locManager;
    LatLng miPosicion;
    LatLng destino;
    Destino lugar_de_destino;
    LatLng punto1=null,punto2=null;
    double limit1,limit2,limit3,limit4;
    LatLng limites[]=new LatLng[5];
    SQLRutas rutas_bd;
    SQLiteDatabase rutas_bdWritableDatabase;
    ArrayList<Punto> puntos;
    ArrayList<Arista> aristas;
    double ERROR=.00004;
    ArrayList<LatLng> camino;
    int n,tam,cont=0;
    boolean salir=false;
    int distanciaRectaDestino;
    int distanciaPuntoDestino;
    LatLng puntoDestino ;
    int idPuntoDestino;
    Arista[] puntoMasCercanoARectaDestino = new Arista[2];
    double menordist;
    Polyline trazar_ruta;
    Marker marker;
    LatLng[] limitesUdeA=Utilidades.limitesUdeA();


    public static GpsRuta newInstance(Destino describable) {
        GpsRuta fragment = new GpsRuta();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Utilidades.DESCRIBABLE_KEY, describable);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface onSomeEventListener {
        void cancelarDestinoRuta();
        void rutaTerminada();
    }

    onSomeEventListener someEventListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = null;
        if (context instanceof Activity){
            activity=(Activity) context;
        }
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            String fragment;
            String nombre_de_lugar_de_destino;
            super.onCreate(savedInstanceState);
            View v= inflater.inflate(R.layout.fragment_gps_ruta, container, false);
            mapView = (MapView) v.findViewById(R.id.mapview_ruta);
            mapView.onCreate(savedInstanceState);
            locManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            googleMap = mapView.getMap();
            System.out.println("234234");
            MapsInitializer.initialize(this.getActivity());
            button_udea= (Button) v.findViewById(R.id.udea_ruta);
            button_destino=(Button)v.findViewById(R.id.destino_ruta);
            button_cancelar=(Button) v.findViewById(R.id.cancelar_gps_ruta);
            lugar=(TextView) v.findViewById(R.id.lugar);
            rutas_bd = new SQLRutas(getActivity(), "Rutas", null, 1);
            rutas_bdWritableDatabase = rutas_bd.getWritableDatabase();
            lugar_de_destino = (Destino) getArguments().getSerializable(Utilidades.DESCRIBABLE_KEY);
            googleMap.setBuildingsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationChangeListener(this);
            orm();
            fragment=lugar_de_destino.getFragment();
            destino=new LatLng(lugar_de_destino.getLatitud(),lugar_de_destino.getLongitud());
            googleMap.addMarker(new MarkerOptions().position(destino).title("Destino"));
            if(fragment.equalsIgnoreCase("gpsElegirDestino")){
                distanciaRectaDestino=-1;
                puntoDestino = null;
                n=tam+1;
                System.out.println("n: "+n);
                Arista[] puntoMasCercanoAPuntoDestino = puntoMasCercanoAPunto(destino);
                puntoMasCercanoARectaDestino = puntoMasCercanoARecta(destino);
                distanciaPuntoDestino = puntoMasCercanoAPuntoDestino[0].getCosto();
                if (puntoMasCercanoARectaDestino[2] != null) {
                    distanciaRectaDestino = puntoMasCercanoARectaDestino[2].getCosto();
                }
                if (distanciaRectaDestino != -1 && distanciaRectaDestino < distanciaPuntoDestino) {
                    System.out.println("distanciaRectaEntreDestino");
                    idPuntoDestino = puntoMasCercanoARectaDestino[1].getIdPuntoB();
                    puntoDestino = puntoMasCercanoARectaDestino[2].getCoordenadasA();
                    menordist=distanciaRectaDestino;
                    cont++;
                } else {
                    System.out.println("distanciaPuntoEntreDestino");
                    idPuntoDestino = puntoMasCercanoAPuntoDestino[0].getIdPuntoA();
                    menordist=distanciaPuntoDestino;
                    puntoMasCercanoARectaDestino=null;
                    n--;
                }
                Utilidades.animacionDeCamara(googleMap, destino, 17, 0);

            }else if(fragment.equalsIgnoreCase("gpsDefault") || fragment.equalsIgnoreCase("lista")) {
                nombre_de_lugar_de_destino = lugar_de_destino.getNombre();
                lugar.setText(nombre_de_lugar_de_destino);

                distanciaRectaDestino=-1;
                puntoDestino = null;
                n=tam+1;
                System.out.println("n: "+n);
                Arista[] puntoMasCercanoAPuntoDestino = puntoMasCercanoAPunto(destino);
                puntoMasCercanoARectaDestino = puntoMasCercanoARecta(destino);
                distanciaPuntoDestino = puntoMasCercanoAPuntoDestino[0].getCosto();
                if (puntoMasCercanoARectaDestino[2] != null) {
                    distanciaRectaDestino = puntoMasCercanoARectaDestino[2].getCosto();
                }
                if (distanciaRectaDestino != -1 && distanciaRectaDestino < distanciaPuntoDestino) {
                    System.out.println("distanciaRectaEntreDestino");
                    idPuntoDestino = puntoMasCercanoARectaDestino[1].getIdPuntoB();
                    puntoDestino = puntoMasCercanoARectaDestino[2].getCoordenadasA();
                    menordist=distanciaRectaDestino;
                    cont++;
                } else {
                    System.out.println("distanciaPuntoEntreDestino");
                    idPuntoDestino = puntoMasCercanoAPuntoDestino[0].getIdPuntoA();
                    menordist=distanciaPuntoDestino;
                    puntoMasCercanoARectaDestino=null;
                    n--;
                }

                Utilidades.animacionDeCamara(googleMap, destino, 17, 0);
            }

            button_destino.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidades.animacionDeCamara(googleMap, destino,19, 0);
                }
            });


            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Utilidades.displayPromptForEnablingGPS(getActivity());
                    } else {
                        Utilidades.animacionDeCamara(googleMap, miPosicion, 19, 0);
                    }
                    return true;
                }
            });

            button_udea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidades.animacionDeCamara(googleMap, Utilidades.COORDENADAS_UDEA, 17, 0);
                }
            });

            button_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    someEventListener.cancelarDestinoRuta();
                }
            });
            return v;
        }

    public void orm(){
        puntos=new ArrayList<>();
        aristas= new ArrayList<>();
        Punto punto;
        Arista arista;
        Cursor c;
        rutas_bd = new SQLRutas(getActivity(), "Rutas", null, 1);
        rutas_bdWritableDatabase = rutas_bd.getWritableDatabase();
        //numero de puntos en la bd
        c = rutas_bdWritableDatabase.rawQuery("SELECT MAX(idPunto) FROM punto", null);
        c.moveToFirst();
        tam = c.getInt(0);
        //puntos en la bd
        c=rutas_bdWritableDatabase.rawQuery("SELECT idPunto,latitud,longitud FROM Punto", null);
        if(c.moveToFirst()){
            do{
                punto=new Punto(c.getInt(0), c.getDouble(1), c.getDouble(2));
                puntos.add(punto);
            }while (c.moveToNext());
        }
        // aristas en la bd
        c=rutas_bdWritableDatabase.rawQuery("SELECT puntoA,puntoB,distancia FROM Arista", null);
        if(c.moveToFirst()){
            do{
                arista=new Arista(c.getInt(0), c.getInt(1),(int) c.getDouble(2));
                aristas.add(arista);
            }while (c.moveToNext());
        }
        rutas_bd.close();
        rutas_bdWritableDatabase.close();
    }


    @Override
    public void onMyLocationChange(Location location) {
        double m,mp;
        double b,bp;
        if(salir==false) {
            miPosicion = new LatLng(location.getLatitude(), location.getLongitude());
            PolylineOptions puntosCamino = new PolylineOptions();
            double distancia;
            AlertDialog.Builder alerta;
            if (marker != null) {
                marker.remove();
            }
            distancia = Recta.distanciaEntreDosPuntos(destino.latitude, destino.longitude, miPosicion.latitude, miPosicion.longitude);
            if (distancia < 3) {
                alerta = new AlertDialog.Builder(getActivity());
                alerta.setTitle("Notificación");
                alerta.setMessage("Has llegado a tu destino");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        someEventListener.rutaTerminada();
                    }
                });
                salir=true;
                alerta.show();
            }

            distancia = Recta.distanciaEntreDosPuntos(destino.latitude, destino.longitude, miPosicion.latitude, miPosicion.longitude);
            if(distancia>menordist+1){
                if (punto1 == null || !Recta.contains(limites,miPosicion)) {
                    System.out.println("puntolllll1= "+ punto1);
                    System.out.println("######################################################################");
                 //   System.out.println("distanciaEntreDosPuntos= "+Recta.contains(limites, miPosicion));
                    System.out.println("######################################################################");
            //        marker = googleMap.addMarker(new MarkerOptions().position(miPosicion).icon(BitmapDescriptorFactory.fromResource(R.drawable.walking)));
                    camino = encontrarRuta();
                    limitarCamino();
                    for (int i = 0; i < camino.size(); i++) {
                        puntosCamino.add(camino.get(i));
                    }
                } else {
                    m=Recta.pendienteDeLaRecta(punto1.latitude, punto1.longitude, punto2.latitude, punto2.longitude);
                    b=Recta.constanteDeLaRecta(punto1.latitude, punto1.longitude, m);
                    mp=Recta.pendienteDeRectaPerpendicular(m);
                    bp=Recta.constanteDeLaRecta(miPosicion.latitude, miPosicion.longitude, mp);
                    LatLng interseccion=Recta.interseccionEntreDosRectas(mp,m,bp,b);
               //     marker=googleMap.addMarker(new MarkerOptions().position(interseccion).icon(BitmapDescriptorFactory.fromResource(R.drawable.walking)));
                    puntosCamino.add(interseccion);
                    for (int i = 1; i < camino.size(); i++) {
                        puntosCamino.add(camino.get(i));
                    }
                }
                if (trazar_ruta != null) {
                    trazar_ruta.remove();
                }
                trazar_ruta = googleMap.addPolyline(puntosCamino);
                trazar_ruta.setColor(Color.YELLOW);
            }else {
             //   marker=googleMap.addMarker(new MarkerOptions().position(miPosicion).icon(BitmapDescriptorFactory.fromResource(R.drawable.walking)));
                if (trazar_ruta != null) {
                    trazar_ruta.remove();
                }
            }
        }
    }

    public void limitarCamino(){
        double x1=punto1.latitude;
        double y1=punto1.longitude;
        double x2=punto2.latitude;
        double y2=punto2.longitude;
        double angulo;
        double xa;
        double ya;
        LatLng inicio;

      //  googleMap.addMarker(new MarkerOptions().position((punto1)).title("punto1"));
       // googleMap.addMarker(new MarkerOptions().position((punto2)).title("punto2"));

        angulo=Recta.angleFromCoordinate(x1,y1,x2,y2);
        xa=x1 + ERROR * Math.sin(angulo);
        ya=y1-ERROR*Math.cos(angulo);
       // googleMap.addMarker(new MarkerOptions().position(new LatLng(xa, ya)).title("punto1-A"));
        limit1=xa;
        limit2=ya;
        inicio=limites[0]=new LatLng(xa,ya);


        xa=x1 - ERROR * Math.sin(angulo);
        ya=y1+ERROR*Math.cos(angulo);
      //  googleMap.addMarker(new MarkerOptions().position(new LatLng(xa, ya)).title("punto1-B"));
        limites[1]=new LatLng(xa,ya);


        xa=x2 - ERROR * Math.sin(angulo);
        ya=y2+ERROR*Math.cos(angulo);
       // googleMap.addMarker(new MarkerOptions().position(new LatLng(xa, ya)).title("punto2-B"));
        limit3=xa;
        limit4 =ya;
        limites[2]=new LatLng(xa,ya);

        xa=x2 + ERROR * Math.sin(angulo);
        ya=y2-ERROR*Math.cos(angulo);
      //  googleMap.addMarker(new MarkerOptions().position(new LatLng(xa, ya)).title("punto2-A"));
        limites[3]=new LatLng(xa,ya);
        limites[4]=inicio;

    }

    public ArrayList<LatLng> encontrarRuta() {
        int distanciaRectaOrigen = -1;
        int distanciaPuntoOrigen;
        ArrayList<Integer> ruta;
        ArrayList<Arista> nuevasAristas;
        int idPuntoOrigen;
        LatLng puntoOrigen = null;
        Dijkstra dijkstra;
        n=tam+cont+1;
        ruta = new ArrayList<>();
        nuevasAristas = new ArrayList<>();
        int cont2 = cont;
        Arista[] puntoMasCercanoARectaOrigen = puntoMasCercanoARecta(miPosicion);
        Arista[] puntoMasCercanoAPuntoOrigen = puntoMasCercanoAPunto(miPosicion);
        distanciaPuntoOrigen = puntoMasCercanoAPuntoOrigen[0].getCosto();
        if (puntoMasCercanoARectaOrigen[2] != null) {
            distanciaRectaOrigen = puntoMasCercanoARectaOrigen[2].getCosto();
        }
        if(puntoMasCercanoARectaDestino!=null) {
            for (int i = 0; i < puntoMasCercanoARectaDestino.length - 1; i++) {
                nuevasAristas.add(puntoMasCercanoARectaDestino[i]);
            }
        }
        if (distanciaRectaOrigen != -1 && distanciaRectaOrigen < distanciaPuntoOrigen) {
            System.out.println("distanciaRectaEntreOrigen");
            idPuntoOrigen = puntoMasCercanoARectaOrigen[1].getIdPuntoB();
            puntoOrigen = puntoMasCercanoARectaOrigen[2].getCoordenadasA();
            nuevasAristas.add(puntoMasCercanoARectaOrigen[0]);
            nuevasAristas.add(puntoMasCercanoARectaOrigen[1]);
            cont2++;
        } else {
            System.out.println("distanciaPuntoEntreOrigen");
            idPuntoOrigen = puntoMasCercanoAPuntoOrigen[0].getIdPuntoA();
            n--;
        }
        System.out.println( );

        Arista[] aristasTotales = new Arista[nuevasAristas.size()];
        aristasTotales = nuevasAristas.toArray(aristasTotales);
        dijkstra = new Dijkstra(aristas,tam, aristasTotales, cont2);
        ruta = dijkstra.consultarRuta(idPuntoOrigen, idPuntoDestino, ruta);
        System.out.print("camino:");
        ArrayList<LatLng> camino = new ArrayList<>();

        if (distanciaRectaOrigen != -1 && distanciaRectaOrigen < distanciaPuntoOrigen ) {
            punto1 = puntoOrigen;
            camino.add(puntoOrigen);
        }

            for (int i = ruta.size() - 1; i >= 0; i--) {
                System.out.print(ruta.get(i) + ",");
                for (int j = 0; j < puntos.size(); j++) {
                    if (ruta.get(i) == puntos.get(j).getIdPunto()) {
                        if (i == ruta.size() - 1) {
                            punto1 = new LatLng(puntos.get(j).getLatitud(), puntos.get(j).getLongitud());
                        }
                        if (i == ruta.size() - 2) {
                            punto2 = new LatLng(puntos.get(j).getLatitud(), puntos.get(j).getLongitud());
                        }
                        camino.add(new LatLng(puntos.get(j).getLatitud(), puntos.get(j).getLongitud()));
                    }
                }
            }

        if (distanciaRectaDestino != -1 && distanciaRectaDestino < distanciaPuntoDestino) {
            if (ruta.size() == 2) {
                punto2 = puntoDestino;
            }
            camino.add(puntoDestino);
        }
        if (distanciaRectaDestino != -1 && distanciaRectaDestino < distanciaPuntoDestino) {
            if (distanciaRectaOrigen != -1 && distanciaRectaOrigen < distanciaPuntoOrigen ) {
                System.out.println("puntoMasCercanoARectaOrigen[0].getIdPuntoA()"+puntoMasCercanoARectaOrigen[0].getIdPuntoA());
                System.out.println("puntoMasCercanoARectaOrigen[1].getIdPuntoA()"+puntoMasCercanoARectaOrigen[1].getIdPuntoA());
                System.out.println("puntoMasCercanoARectaDestino[0].getIdPuntoA()"+puntoMasCercanoARectaDestino[0].getIdPuntoA());
                System.out.println("puntoMasCercanoARectaDestino[1].getIdPuntoA()"+puntoMasCercanoARectaDestino[1].getIdPuntoA());
                if(puntoMasCercanoARectaOrigen[0].getIdPuntoA()==puntoMasCercanoARectaDestino[0].getIdPuntoA()
                        || puntoMasCercanoARectaOrigen[0].getIdPuntoA()==puntoMasCercanoARectaDestino[1].getIdPuntoA()){
                    System.out.println("entre2");
                    if(puntoMasCercanoARectaOrigen[1].getIdPuntoA()==puntoMasCercanoARectaDestino[0].getIdPuntoA()
                            || puntoMasCercanoARectaOrigen[1].getIdPuntoA()==puntoMasCercanoARectaDestino[1].getIdPuntoA()){
                        System.out.println("entre3");
                        camino.clear();
                        punto1 = puntoOrigen;
                        punto2 = puntoDestino;
                        camino.add(punto1);
                        camino.add(punto2);
                    }
                }
            }
        }
        return camino;
    }

    public Arista[] puntoMasCercanoAPunto (LatLng posicion) {
            double distancia = -1, distancia_parcial;
            int puntoMasCercano = 0;
            Arista[] aristas = new Arista[1];
            for(int i=0;i<puntos.size();i++){
                distancia_parcial = Recta.distanciaEntreDosPuntos(puntos.get(i).getLatitud(), puntos.get(i).getLongitud(), posicion.latitude, posicion.longitude);
                if (distancia == -1 || distancia_parcial < distancia) {
                    distancia = distancia_parcial;
                    puntoMasCercano = puntos.get(i).getIdPunto();
                }
            }
            aristas[0] = new Arista(puntoMasCercano, 9, (int) distancia);
            return aristas;
    }

    public Arista[] puntoMasCercanoARecta(LatLng posicion){
        LatLng p1 = null,p2 = null,interseccion;
        double m1,b1,m2,b2;
        double distancia_parcial,distanciaA = 0,distanciaB = 0;
        double distancia=-1;
        LatLng puntoMasCercano=null;
        int puntoA = 0, puntoB=0,pA = 0,pB = 0;
        Arista[] aristas_punto_cercano=new Arista[3];
        for(int i=0;i<aristas.size();i++){
            for (int j = 0; j < puntos.size(); j++) {
                if(aristas.get(i).getIdPuntoA()==puntos.get(j).getIdPunto()){
                    p1 = new LatLng(puntos.get(j).getLatitud(),puntos.get(j).getLongitud());
                    pA=puntos.get(j).getIdPunto();
                }
                if(aristas.get(i).getIdPuntoB()==puntos.get(j).getIdPunto()){
                    p2 = new LatLng(puntos.get(j).getLatitud(),puntos.get(j).getLongitud());
                    pB=puntos.get(j).getIdPunto();
                }
            }
            m1 = Recta.pendienteDeLaRecta(p1.latitude, p1.longitude, p2.latitude, p2.longitude);
            b1 = Recta.constanteDeLaRecta(p1.latitude, p1.longitude, m1);
            m2=Recta.pendienteDeRectaPerpendicular(m1);
            b2 = Recta.constanteDeLaRecta(posicion.latitude, posicion.longitude, m2);
            interseccion=Recta.interseccionEntreDosRectas(m1, m2, b1, b2);
            //googleMap.addMarker(new MarkerOptions().position(interseccion).title("interseccion "+pA+"-"+pB));
            if(Recta.esPuntoValido(p1.latitude, p2.latitude, p1.longitude, p2.longitude, interseccion)){
                System.out.println("p1="+pA);
                System.out.println("p2="+pB);
                System.out.println("esPuntoValido="+Recta.esPuntoValido(p1.latitude, p2.latitude, p1.longitude, p2.longitude, interseccion));
                distancia_parcial=Recta.distanciaEntreDosPuntos(posicion.latitude,posicion.longitude, interseccion.latitude,interseccion.longitude);
                if(distancia==-1 || distancia_parcial<distancia){
                    distancia=distancia_parcial;
                    puntoMasCercano=interseccion;
                    distanciaA=Recta.distanciaEntreDosPuntos(p1.latitude,p1.longitude, interseccion.latitude,interseccion.longitude);
                    distanciaB=Recta.distanciaEntreDosPuntos(p2.latitude,p2.longitude, interseccion.latitude,interseccion.longitude);
                    puntoA=pA;
                    puntoB=pB;
                }
            }
        }
        if(distancia!=-1) {
            aristas_punto_cercano[0] = new Arista(puntoA, n, (int) distanciaA);
            aristas_punto_cercano[1] = new Arista(puntoB, n, (int) distanciaB);
            aristas_punto_cercano[2] = new Arista(n, (n+1), (int) distancia, puntoMasCercano, posicion);
        }
        n++;
        return aristas_punto_cercano;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}