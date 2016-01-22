package proyectocm.guiame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.maps.android.kml.KmlLayer;
import java.util.ArrayList;
import java.util.List;


public class GpsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener {

    private LocationManager locManager;
    private LocationListener locListener;
    Button button_mi_posicion;
    Button button_destino;
    LatLng destino;
    TextView distancia;
    LatLng miPosicion;
    PolylineOptions camino;
    GoogleMap googleMap;
    Dijkstra r;
    Marker posicionActual;


    private Location obtenerUltimaPosicion() {
        locManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locManager.getProviders(true);
        SQLRutas rutas = new SQLRutas(this, "Rutas", null, 1);
        SQLiteDatabase bd = rutas.getWritableDatabase();
        Cursor c = bd.rawQuery("SELECT longitud FROM punto WHERE idPunto=1", null);
        c.moveToFirst();
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void mostrarPosicion(Location location) {
        if (location != null) {
            miPosicion = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.clear();
            addMarkers();
            if(camino!=null) {
                Polyline l = googleMap.addPolyline(camino);
                l.setColor(Color.YELLOW);
            }
            googleMap.addMarker(new MarkerOptions().position(destino).title(getIntent().getStringExtra("lugar")));
            googleMap.addMarker(new MarkerOptions().position(miPosicion).title("Estas aqui!").icon(BitmapDescriptorFactory.fromResource(R.drawable.walking)));
        }
      //  agregarKML();
    }


    private void comenzarLocalizacion() {
        Location location = obtenerUltimaPosicion();
        mostrarPosicion(location);
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mostrarPosicion(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
    }
/*
    public void agregarKML() {
        try {
            KmlLayer kmlLayer = new KmlLayer(googleMap, R.raw.mapa_udea, getApplicationContext());
            kmlLayer.addLayerToMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
*/
    public void agregarLogo() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    public void animacionDeCamara(LatLng mover_camara) {
        if (mover_camara != null) {
            CameraPosition camPos = new CameraPosition.Builder().target(mover_camara).zoom(19).bearing(45).tilt(70).build();
            CameraUpdate cam = CameraUpdateFactory.newCameraPosition(camPos);
            googleMap.animateCamera(cam);
        } else
            Toast.makeText(this, "Buscando posición actual, espere un momento", Toast.LENGTH_LONG).show();
    }

    public void addMarkers() {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26919, -75.5667)).title("1"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26897, -75.56668)).title(("2")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26865, -75.56675)).title(("3")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26854, -75.56689)).title(("4")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26827, -75.56693)).title(("5")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26755, -75.56713)).title(("6")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26757, -75.56779)).title("origen")).showInfoWindow();
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.2676, -75.56792)).title(("8")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26774, -75.56794)).title(("9")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26792, -75.56842)).title(("10")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26839, -75.56838)).title(("11")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26865, -75.56835)).title(("12")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26855, -75.56754)).title(("13")));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(6.26827, -75.56759)).title(("14")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.gps);
        button_destino = (Button) findViewById(R.id.destino);
       // button_mi_posicion = (Button) findViewById(R.id.mi_posicion);
        TextView lugar = (TextView) findViewById(R.id.lugar);
        String nombre = getIntent().getStringExtra("lugar");
   //     distancia=(TextView) findViewById(R.id.distancia);
        lugar.setText(nombre);
       // googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        agregarLogo();
        button_destino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animacionDeCamara(destino);
            }
        });
        button_mi_posicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animacionDeCamara(miPosicion);
            }
        });

        addMarkers();
    //    agregarKML();
        googleMap.setOnMarkerClickListener(this);
        FavoritosSQL usuarioBD = new FavoritosSQL(this, "Usuario", null, 1);
        SQLiteDatabase bd = usuarioBD.getWritableDatabase();
        Cursor c= bd.rawQuery("SELECT longitud,latitud FROM lugares WHERE nombre='" + getIntent().getStringExtra("lugar") + "'", null);
        c.moveToFirst();
        destino=new LatLng(c.getDouble(1),c.getDouble(0));

        if(c.getDouble(0)!=0) {
            googleMap.addMarker(new MarkerOptions().position(destino).title(getIntent().getStringExtra("lugar")));
            animacionDeCamara(destino);
            comenzarLocalizacion();
        }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        googleMap.clear();
        int d;
        addMarkers();
     //   agregarKML();
        googleMap.addMarker(new MarkerOptions().position(destino).title(getIntent().getStringExtra("lugar")));
        posicionActual=googleMap.addMarker(new MarkerOptions().position(miPosicion).title("Estas aqui!").icon(BitmapDescriptorFactory.fromResource(R.drawable.walking)));
        String dest=marker.getTitle();
        if(!dest.equals("origen")  && !dest.equals("Estas aqui!") && !dest.equals(getIntent().getStringExtra("lugar"))) {
            SQLRutas rutas_bd = new SQLRutas(this, "Rutas", null, 1);
            SQLiteDatabase bdf = rutas_bd.getWritableDatabase();

            camino = new PolylineOptions();
            ArrayList<Integer> ruta = new ArrayList<>();
            ruta = r.consultarRuta(7, Integer.parseInt(dest), ruta);
            d=r.costoMinimo(7, Integer.parseInt(dest));
            distancia.setText("Distancia: "+d+"m");
            String query;
            for (int i = ruta.size() - 1; i >= 0; i--) {
                 query = "SELECT longitud,latitud FROM punto WHERE idPunto="+ ruta.get(i) ;
                 Cursor f = bdf.rawQuery(query, null);
                 if (f.moveToFirst()) {
                     camino.add(new LatLng(f.getDouble(1), f.getDouble(0)));
                }
                /*
                System.out.println(ruta.get(i) + ", ");
                if (i != 0)
                    query = query + ruta.get(i) + " or idPunto=";
                else
                    query = query + ruta.get(i);
                    */
            }/*
            Cursor f = bdf.rawQuery(query, null);
            if (f.moveToFirst()) {
                do {
                    System.out.println(f.getDouble(1)+","+ f.getDouble(0));
                    linea.add(new LatLng(f.getDouble(1), f.getDouble(0)));
                } while (f.moveToNext());
            }*/
            Polyline l = googleMap.addPolyline(camino);
            l.setColor(Color.YELLOW);
            bdf.close();
            rutas_bd.close();
        }
        return false;

    }
}
