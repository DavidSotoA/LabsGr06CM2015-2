package proyectocm.guiame;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GpsElegirDestino extends Fragment implements View.OnClickListener {

    private static GoogleMap googleMap;
    Button aceptar;
    Button cancelar;
    Button udea;
    MapView mapView;
    Marker destino=null;
    LatLng[] limitesUdeA=Utilidades.limitesUdeA();



    public interface onSomeEventListener {
         void cancelarDestino();
         void aceptarDestino(LatLng destino);
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
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_gps_elegir_destino, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview_destino);
        mapView.onCreate(savedInstanceState);
        googleMap = mapView.getMap();

        MapsInitializer.initialize(this.getActivity());
        aceptar=(Button) v.findViewById(R.id.destino);
        cancelar=(Button) v.findViewById(R.id.cancelar_destino);
        udea=(Button) v.findViewById(R.id.udea);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(Utilidades.COORDENADAS_UDEA, 17);
        googleMap.moveCamera(camUpd1);
        udea.setOnClickListener(this);
        aceptar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
        PolylineOptions p= new PolylineOptions();
        for(int i=0;i<limitesUdeA.length;i++){
            p.add(limitesUdeA[i]);
        }
        Polyline l= googleMap.addPolyline(p);
        l.setColor(Color.BLACK);


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if(!Recta.contains(limitesUdeA,point)){
                    Toast.makeText(getActivity(),"el destino debe de estar dentro de la UdeA",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(destino!=null){
                        destino.remove();
                    }
                    destino = googleMap.addMarker(new MarkerOptions().position(point));
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        googleMap.clear();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.udea) {
            Utilidades.animacionDeCamara(googleMap, Utilidades.COORDENADAS_UDEA, 17, 0);
        }else if (id == R.id.cancelar_destino) {
            someEventListener.cancelarDestino();
        }else if (id == R.id.destino) {
            if(destino!=null) {
                someEventListener.aceptarDestino(destino.getPosition());
            }else{
                Toast.makeText(getActivity(), "Debes de elegir un destino", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
