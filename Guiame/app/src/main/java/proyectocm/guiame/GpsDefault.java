package proyectocm.guiame;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class GpsDefault extends Fragment implements GoogleMap.OnMyLocationChangeListener, View.OnClickListener {

    private static GoogleMap googleMap;
    LatLng miPosicion;
    FloatingActionButton reconocimiento_de_voz;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    LocationManager locManager;
    MapView mapView;
    Button udea;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_gps_default, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview_default);
        mapView.onCreate(savedInstanceState);
        googleMap = mapView.getMap();
        MapsInitializer.initialize(this.getActivity());
        reconocimiento_de_voz=(FloatingActionButton) v.findViewById(R.id.reconocimiento_de_voz);
        locManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        udea=(Button) v.findViewById(R.id.udea_default);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(this);
        udea.setOnClickListener(this);
        Utilidades.animacionDeCamara(googleMap, Utilidades.COORDENADAS_UDEA, 17, 0);

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Utilidades.displayPromptForEnablingGPS(getActivity());
                } else {
                    Utilidades.animacionDeCamara(googleMap, miPosicion, 18, 0);
                }
                return true;
            }
        });

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            reconocimiento_de_voz.setOnClickListener(this);
        }
        return v;
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
        getActivity().startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onMyLocationChange(Location location) {
        miPosicion=new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
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
        int id=v.getId();
        if(id==R.id.udea_default){
            Utilidades.animacionDeCamara(googleMap, Utilidades.COORDENADAS_UDEA, 17, 0);
        }else if(id==R.id.reconocimiento_de_voz){
            if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Utilidades.displayPromptForEnablingGPS(getActivity());
            } else{
                startVoiceRecognitionActivity();
            }
        }
    }
}
